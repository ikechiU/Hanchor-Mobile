package com.nextgendevs.hanchor.presentation.reminder

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import com.google.gson.Gson
import com.nextgendevs.hanchor.business.datasource.network.request.TodoRequest
import com.nextgendevs.hanchor.business.domain.utils.StateMessageCallback
import com.nextgendevs.hanchor.databinding.ActivityReminderBinding
import com.nextgendevs.hanchor.presentation.BaseActivity
import com.nextgendevs.hanchor.presentation.broadcast.reminder.scheduleSnoozeReminder
import com.nextgendevs.hanchor.presentation.main.MainActivity
import com.nextgendevs.hanchor.presentation.main.fragments.home.todo_list.viewmodel.TodoViewModel
import com.nextgendevs.hanchor.presentation.utils.Constants
import com.nextgendevs.hanchor.presentation.utils.displayToast
import com.nextgendevs.hanchor.presentation.utils.processQueue
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ReminderActivity : BaseActivity() {

    private lateinit var binding: ActivityReminderBinding
    private var id = 0L
    private var task = ""
    private var time = 0L

    private val viewModel: TodoViewModel by viewModels()
    private var shouldObserveOnce = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        when {
            intent.hasExtra(Constants.FCM_LINK_KEY) -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
            intent.hasExtra(Constants.TODO_REMINDER_TIME) -> {
                id = intent.getLongExtra(Constants.TODO_REMINDER_ID, 0)
                task = intent.getStringExtra(Constants.TODO_REMINDER_TASK)!!
                time = intent.getLongExtra(Constants.TODO_REMINDER_TIME, System.currentTimeMillis())
                binding.reminderTask.text = task
                markTodoAsDone()
            }

            intent.hasExtra(Constants.SNOOZE_TODO_REMINDER_TIME) -> { //New reminder
                task = intent.getStringExtra(Constants.SNOOZE_TODO_REMINDER_TASK)!!
                binding.reminderTask.text = task
            }

            else -> {
                onBackPressed()
            }
        }

        binding.markAsDone.setOnClickListener {
            displayToast("Todo marked as done!")
            onBackPressed()
        }

        binding.fifteenMinutesSnooze.setOnClickListener {
            displayToast("Todo snoozed for 15 minutes.")
            snoozeReminder(Constants.FIFTEEN_MINUTES)
        }

        binding.thirtyMinutesSnooze.setOnClickListener {
            displayToast("Todo snoozed for 30 minutes.")
            snoozeReminder(Constants.THIRTY_MINUTES)
        }
    }

    private fun snoozeReminder(time: Long) {
        displayProgressBar(true)
        val snoozeTime = System.currentTimeMillis() + time
        scheduleSnoozeReminder(snoozeTime, task, id)
        onBackPressed()
    }

    private fun markTodoAsDone() {
        val todoRequest = TodoRequest("TODO", task, time, true)
        viewModel.updateTodo(id, todoRequest)
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.state.observe(this) { todoState ->

            if (todoState.updateResult != 0) {
                if (shouldObserveOnce) {
                    shouldObserveOnce = false

                    mySharedPreferences.storeStringValue(Constants.LIST_OF_TODOS, Gson().toJson(todoState.todoList))
                }
            }

            processQueue(
                context = this,
                queue = todoState.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onRemoveHeadFromQuery()
                    }
                })
        }
    }

    override fun onBackPressed() {
        displayProgressBar(true)
        Handler(Looper.getMainLooper()).postDelayed({
            finishAndRemoveTask()
        }, Constants.FIVE_SECONDS)
    }

    override fun displayProgressBar(isLoading: Boolean) {
        if(isLoading) {
            binding.root.isEnabled = false
            binding.root.alpha = 0.5f
            binding.progressBar.visibility  = View.VISIBLE
        } else {
            binding.progressBar.visibility  = View.GONE
        }
    }
}