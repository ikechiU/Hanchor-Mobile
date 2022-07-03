package com.nextgendevs.hanchor.presentation.main.fragments.home.todo_list.create_todo

import android.app.AlarmManager
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.google.gson.Gson
import com.nextgendevs.hanchor.business.datasource.network.request.TodoRequest
import com.nextgendevs.hanchor.business.domain.models.Todo
import com.nextgendevs.hanchor.business.domain.utils.AreYouSureCallback
import com.nextgendevs.hanchor.business.domain.utils.StateMessageCallback
import com.nextgendevs.hanchor.databinding.FragmentCreateTodoBinding
import com.nextgendevs.hanchor.presentation.broadcast.reminder.deleteReminder
import com.nextgendevs.hanchor.presentation.broadcast.reminder.scheduleReminder
import com.nextgendevs.hanchor.presentation.main.fragments.home.todo_list.viewmodel.TodoViewModel
import com.nextgendevs.hanchor.presentation.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CreateTodoFragment : BaseCreateTodoFragment() {
    private var _binding: FragmentCreateTodoBinding? = null
    private val binding: FragmentCreateTodoBinding get() = _binding!!
    private val viewModel: TodoViewModel by viewModels()

    private val currentDateTime = Calendar.getInstance()
    private var startYear = currentDateTime.get(Calendar.YEAR)
    private var startMonth = currentDateTime.get(Calendar.MONTH)
    private var startDay = currentDateTime.get(Calendar.DAY_OF_MONTH)
    private var startHour = currentDateTime.get(Calendar.HOUR_OF_DAY)
    private var startMinute = currentDateTime.get(Calendar.MINUTE)

    private var _id = 0L
    private var year = 0
    private var month = 0
    private var day = 0
    private var hour = 0
    private var minute = 0

    private var todo: Todo? = null

    private var shouldObserveOnce = true

    private lateinit var alarmManager: AlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                areYouSure()
            }
        })
    }

    private fun OnBackPressedCallback.areYouSure() {
        activity?.areYouSureDialog("Navigate to Planner", object : AreYouSureCallback {
            override fun proceed() {
                this@areYouSure.isEnabled = false
                activity?.onBackPressed()
            }

            override fun cancel() {
                Log.d(TAG, "cancel: pressed.")
            }
        })
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateTodoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        alarmManager = getContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        Log.d(
            TAG,
            "onViewCreated: AuthToken: ${mySharedPreferences.getStoredString(Constants.AUTH_TOKEN)}"
        )

        val bundle = arguments ?: return
        val args = CreateTodoFragmentArgs.fromBundle(bundle)
        todo = args.todo

        if (todo != null)
            setUpExistingTodo(todo!!)

        binding.btnSave.setOnClickListener {
            saveTodo()
        }

        binding.deleteTodo.setOnClickListener {
            deleteTodo()
        }

        binding.navigateUp.setOnClickListener {
            navigateToTodoFragment()
        }

        binding.calendar.setOnClickListener {
            if (Build.VERSION.SDK_INT >= 31) {
                if (alarmManager.canScheduleExactAlarms()) {
                    pickDateTime()
                } else {
                    getContext.confirmSchedulePermission()
                }
            } else {
                pickDateTime()
            }
        }
    }

    private fun deleteTodo() {
        Log.d(TAG, "deleteTodo: delete")
        mySharedPreferences.storeLongValue(Constants.TODO_UPDATE_ID_LOCAL, _id)
        viewModel.deleteTodo(_id)
        subscribeObservers()
    }

    private fun saveTodo() {
        if (binding.task.text.isNotEmpty() && binding.time.text.isNotEmpty()) {
            if (_id == 0L) {
                Log.d(TAG, "saveTodo: insert")
                val todoRequest =
                    TodoRequest("Todo", binding.task.text.toString(), getDateLong(), false)

                viewModel.insertTodo(todoRequest)
                subscribeObservers()
            } else {
                if (isSame()) {
                    getContext.displayToast("No change made.")
                    navigateToTodoFragment()
                } else {
                    Log.d(TAG, "saveTodo: update")
                    mySharedPreferences.storeLongValue(Constants.TODO_UPDATE_ID_LOCAL, _id)
                    val todoRequest = TodoRequest(
                        "Todo",
                        binding.task.text.toString(),
                        getDateLong(),
                        true
                    )

                    Log.d(TAG, "saveTodo: IS CHECKED is ${binding.isCompleted.isChecked}")
                    viewModel.updateTodo(_id, todoRequest)
                    subscribeObservers()
                }
            }
        } else {
            getContext.toastMessage("All fields required")
        }
    }

    private fun subscribeObservers() {
        viewModel.state.observe(viewLifecycleOwner) { todoState ->
            uiCommunicationListener.displayProgressBar(todoState.isLoading)

            if(todoState.tokenExpired) {
                activity?.logoutUser(mySharedPreferences)
            }

            if (todoState.insertResult != 0L) {
                if (shouldObserveOnce) {
                    shouldObserveOnce = false
                    val id = todoState.insertResult

                    //insert PI
                    getContext.scheduleReminder(id, binding.task.text.toString(), getDateLong(), false)
                    mySharedPreferences.storeStringValue(Constants.LIST_OF_TODOS, Gson().toJson(todoState.todoList))

                    lifecycleScope.launch {
                        delay(200)
                        navigateToTodoFragment()
                    }
                }
            }

            if (todoState.updateResult != 0) {
                if (shouldObserveOnce) {
                    shouldObserveOnce = false

                    //Delete existing PI
                    getContext.deleteReminder(todo?.id!!, todo?.todoTask!!, todo?.todoDate!!, todo?.todoIsCompleted!!)
                    //Update PI
                    getContext.scheduleReminder(_id, binding.task.text.toString(), getDateLong(), binding.isCompleted.isChecked)
                    mySharedPreferences.storeStringValue(Constants.LIST_OF_TODOS, Gson().toJson(todoState.todoList))

                    lifecycleScope.launch {
                        delay(200)
                        navigateToTodoFragment()
                    }
                }
            }

            if (todoState.deleteResult != 0) {
                if (shouldObserveOnce) {
                    shouldObserveOnce = false
                    //delete PI
                    getContext.deleteReminder(todo?.id!!, todo?.todoTask!!, todo?.todoDate!!, todo?.todoIsCompleted!!)
                    mySharedPreferences.storeStringValue(Constants.LIST_OF_TODOS, Gson().toJson(todoState.todoList))

                    lifecycleScope.launch {
                        delay(200)
                        navigateToTodoFragment()
                    }
                }
            }

            processQueue(
                context = context,
                queue = todoState.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onRemoveHeadFromQuery()
                    }
                })
        }
    }

    private fun navigateToTodoFragment() {
        val directions =
            CreateTodoFragmentDirections.actionCreateTodoFragmentToTodoFragment()
        Navigation.findNavController(binding.root).safeNavigate(directions)
    }

    private fun getDateLong(): Long {
        val sdf = SimpleDateFormat("dd-M-yyyy hh:mm:ss")

        val dateString = "$day-${month + 1}-$year $hour:$minute:00"

        val cal = Calendar.getInstance()
        try {
            val date: Date = sdf.parse(dateString)!!
            cal.time = date
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return cal.timeInMillis
    }

    private fun setUpExistingTodo(todo: Todo) {
        binding.deleteTodo.visibility = View.VISIBLE
        binding.isCompleted.visibility = View.VISIBLE
        _id = todo.id

        Log.d(TAG, "setUpExistingTodo: IS CHECKED ${todo.todoIsCompleted}")
        Log.d(TAG, "setUpExistingTodo: IS CHECKED ID ${todo.id}")

        binding.isCompleted.isChecked = todo.todoIsCompleted
        binding.task.setText(todo.todoTask)
        setTime(todo.todoDate)
        setDateTime()
    }

    private fun isSame(): Boolean {
        return binding.task.text.toString() == todo?.todoTask &&
                binding.isCompleted.isChecked == todo?.todoIsCompleted &&
                getDateLong() == todo?.todoDate
    }

    private fun pickDateTime() {
        DatePickerDialog(getContext, DatePickerDialog.OnDateSetListener { _, year, month, day ->
            this.year = year
            this.month = month
            this.day = day
            TimePickerDialog(getContext, TimePickerDialog.OnTimeSetListener { _, hour, minute ->
                this.hour = hour
                this.minute = minute
                Log.d(TAG, "pickDateTime: $hour")

                setDateTime()
            }, startHour, startMinute, false).show()
        }, startYear, startMonth, startDay).show()
    }

    private fun setDateTime() {
        val time = "$day/${(month + 1)}/$year" + "  " + setAmPm("$hour:$minute")
        binding.time.text = time
    }

    private fun setTime(currentTime: Long) {
        val currentDate = Date(currentTime)
        val cal = Calendar.getInstance()
        cal.time = currentDate

        year = cal.get(Calendar.YEAR)
        month = cal.get(Calendar.MONTH)
        day = cal.get(Calendar.DAY_OF_MONTH)
        hour = cal.get(Calendar.HOUR_OF_DAY)
        minute = cal.get(Calendar.MINUTE)
    }

    private fun setAmPm(time: String): String {
        val hourMinuteFormat = SimpleDateFormat("HH:mm")
        val am_pmFormat = SimpleDateFormat("hh:mm aa")

        var date: Date? = null
        try {
            date = hourMinuteFormat.parse(time)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return am_pmFormat.format(date!!)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}