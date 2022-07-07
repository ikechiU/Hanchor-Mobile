package com.nextgendevs.hanchor.presentation.main.fragments.home.todo_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.gson.Gson
import com.nextgendevs.hanchor.business.domain.models.Todo
import com.nextgendevs.hanchor.business.domain.utils.StateMessageCallback
import com.nextgendevs.hanchor.databinding.FragmentTodoBinding
import com.nextgendevs.hanchor.presentation.main.fragments.home.todo_list.viewmodel.TodoViewModel
import com.nextgendevs.hanchor.presentation.utils.*

class TodoFragment : BaseTodoFragment(), SwipeRefreshLayout.OnRefreshListener {
    private var _binding: FragmentTodoBinding? = null
    private val binding: FragmentTodoBinding get() = _binding!!
    private val viewModel: TodoViewModel by viewModels()
    private lateinit var todoListAdapter: TodoListAdapter
    private var scrollingPosition  = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentTodoBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            val directions =
                TodoFragmentDirections.actionTodoFragmentToCreateTodoFragment(null)
            Navigation.findNavController(it).safeNavigate(directions)
        }

        binding.navigateUp.setOnClickListener {
            val directions =
                TodoFragmentDirections.actionTodoFragmentToHomeFragment()
            Navigation.findNavController(it).safeNavigate(directions)
        }

        todoListAdapter = TodoListAdapter(getContext.applicationContext)
        binding.swipeRefresh.setOnRefreshListener(this)

        viewModel.fetchTodos()
        subscribeObservers()
        initRecyclerView()
    }

    private fun subscribeObservers() {
        viewModel.state.observe(viewLifecycleOwner) { todoState ->
            uiCommunicationListener.displayProgressBar(todoState.isLoading)
            Log.d(TAG, "subscribeObservers: todoState list is ${todoState.todoList}")

            if(todoState.tokenExpired) {
                activity?.logoutUser(mySharedPreferences)
            }

            if (!todoState.isLoading){
                if (todoState.todoList.isNotEmpty()) {
                    binding.emptyTodo.visibility = View.GONE
                } else {
                    binding.emptyTodo.visibility = View.VISIBLE
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

            if (todoState.todoList.isNotEmpty()) {
                mySharedPreferences.storeStringValue(Constants.LIST_OF_TODOS, Gson().toJson(todoState.todoList))
            }
            todoListAdapter.apply {
                submitList(list  = todoState.todoList)
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(getContext)
            val topSpacingDecorator = TopSpacingItemDecoration(10)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    scrollingPosition = dy
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()

                    if(scrollingPosition > 0) {
                        Log.d(TAG, "onScrolled: Scrolling up.")
                        Log.d(TAG, "onScrollStateChanged: exhausted? ${viewModel.state.value?.isQueryExhausted}")
                        if (
                            lastPosition == todoListAdapter.itemCount.minus(1) &&
                            viewModel.state.value?.isLoading == false && viewModel.state.value?.isQueryExhausted == false
                        ) {
                            Log.d(TAG, "TodoFragment: attempting to load next page...")
                            viewModel.fetchNextTodos()
                        }
                    } else {
                        Log.d(TAG, "onScrolled: Scrolling down.")
                    }

                }
            })
            adapter = todoListAdapter

            todoListAdapter.setTodoClickListener(object : TodoClickListener {
                override fun onItemSelected(todo: Todo, position: Int) {
                    try {
                        Log.d(TAG, "onItemSelected: The todo position is $position")
                        val directions =
                            TodoFragmentDirections.actionTodoFragmentToCreateTodoFragment(todo)
                        Navigation.findNavController(binding.root).navigate(directions)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        getContext.displayToast("Error on click")
                    }
                }
            })
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onRefresh() {
        viewModel.fetchTodos()
        subscribeObservers()
        binding.swipeRefresh.isRefreshing = false
    }
}