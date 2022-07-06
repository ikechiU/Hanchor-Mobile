package com.nextgendevs.hanchor.presentation.main.fragments.home.todo_list.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextgendevs.hanchor.business.datasource.network.request.TodoRequest
import com.nextgendevs.hanchor.business.domain.utils.StateMessage
import com.nextgendevs.hanchor.business.domain.utils.UIComponentType
import com.nextgendevs.hanchor.business.domain.utils.doesMessageAlreadyExistInQueue
import com.nextgendevs.hanchor.business.usecase.main.todo.*
import com.nextgendevs.hanchor.presentation.main.fragments.home.affirmations.display_affirmation.details.viewmodel.TodoState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

private const val TAG = "AppDebug"

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val insertTodo: InsertTodo,
    private val updateTodo: UpdateTodo,
    private val fetchTodo: FetchTodo,
    private val fetchTodos: FetchTodos,
    private val deleteTodo: DeleteTodo
): ViewModel(){

    val state: MutableLiveData<TodoState> = MutableLiveData(TodoState())

    fun insertTodo(todoRequest: TodoRequest) {
        state.value?.let { todoState ->

            insertTodo.execute(todoRequest).onEach { dataState ->
                state.value = todoState.copy(isLoading = dataState.isLoading)

                dataState.data?.let { result ->
                    state.value = todoState.copy(isLoading = false)
                    state.value = todoState.copy(insertResult = result)
                }

                dataState.stateMessage?.let { stateMessage ->
                    state.value = todoState.copy(isLoading = false)
                    if (stateMessage.response.message == "Token expired") {
                        state.value = todoState.copy(tokenExpired = true)
                    }
                    onError(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }

   fun updateTodo(todoId: Long, todoRequest: TodoRequest) {
        state.value?.let { todoState ->

            updateTodo.execute(todoId, todoRequest).onEach { dataState ->
                state.value = todoState.copy(isLoading = dataState.isLoading)

                dataState.data?.let { result ->
                    Log.d(TAG, "updateTodo: result is ...$result")
                    state.value = todoState.copy(isLoading = false)
                    state.value = todoState.copy(updateResult = result)
                }

                dataState.stateMessage?.let { stateMessage ->
                    state.value = todoState.copy(isLoading = false)
                    if (stateMessage.response.message == "Token expired") {
                        state.value = todoState.copy(tokenExpired = true)
                    }
                    onError(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }

    fun fetchTodo(todoId: Long) {
        state.value?.let { todoState ->

            fetchTodo.execute(todoId).onEach { dataState ->
                state.value = todoState.copy(isLoading = dataState.isLoading)

                dataState.data?.let { result ->
                    state.value = todoState.copy(isLoading = false)
                    state.value = todoState.copy(todo = result)
                }

                dataState.stateMessage?.let { stateMessage ->
                    state.value = todoState.copy(isLoading = false)
                    if (stateMessage.response.message == "Token expired") {
                        state.value = todoState.copy(tokenExpired = true)
                    }
                    onError(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun clearList() {
        state.value?.let { todoState ->
            this.state.value = todoState.copy(todoList = listOf())
        }
    }

    private fun resetPage() {
        state.value?.let { todoState ->
            state.value = todoState.copy(page = 1)
        }
        updateQueryExhausted(false)
    }

    private fun incrementPageNumber() {
        state.value?.let { todoState ->
            this.state.value = todoState.copy(page = todoState.page + 1)
        }
    }

    private fun updateQueryExhausted(isExhausted: Boolean) {
        state.value = state.value?.copy(isQueryExhausted = isExhausted)
    }

    fun fetchTodos(page: Int = 1, limit: Int = 10) {
        resetPage()
        state.value?.let { todoState ->

            fetchTodos.execute(todoState.page, limit).onEach { dataState ->
                state.value = todoState.copy(isLoading = dataState.isLoading)

                dataState.data?.let { result ->
                    state.value = todoState.copy(isLoading = false)
                    state.value = todoState.copy(todoList = result)
                }

                dataState.stateMessage?.let { stateMessage ->
                    state.value = todoState.copy(isLoading = false)
                    if (stateMessage.response.message == "Token expired") {
                        state.value = todoState.copy(tokenExpired = true)
                    }
                    onError(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }

    fun fetchNextTodos(page: Int = 1, limit: Int = 10) {
        incrementPageNumber()
        state.value?.let { todoState ->

            fetchTodos.execute(todoState.page, limit).onEach { dataState ->
//                state.value = todoState.copy(isLoading = dataState.isLoading)

                dataState.data?.let { result ->
                    if (result.isEmpty()) updateQueryExhausted(true)
                    state.value = todoState.copy(isLoading = false)
                    if (result.isNotEmpty()) state.value = todoState.copy(todoList = result)
                }

                dataState.stateMessage?.let { stateMessage ->
                    state.value = todoState.copy(isLoading = false)
                    if (stateMessage.response.message == "Token expired") {
                        state.value = todoState.copy(tokenExpired = true)
                    }
                    onError(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }

    fun deleteTodo(todoId: Long) {
        state.value?.let { todoState ->

            deleteTodo.execute(todoId).onEach { dataState ->
                state.value = todoState.copy(isLoading = dataState.isLoading)

                dataState.data?.let { result ->
                    state.value = todoState.copy(isLoading = false)
                    state.value = todoState.copy(deleteResult = result)
                }

                dataState.stateMessage?.let { stateMessage ->
                    state.value = todoState.copy(isLoading = false)
                    if (stateMessage.response.message == "Token expired") {
                        state.value = todoState.copy(tokenExpired = true)
                    }
                    onError(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun onError(stateMessage: StateMessage) {
        state.value?.let { todoState ->
            val queue = todoState.queue
            if(!stateMessage.doesMessageAlreadyExistInQueue(queue = queue)){
                if(stateMessage.response.uiComponentType !is UIComponentType.None){
                    queue.add(stateMessage)
                    state.value = todoState.copy(queue = queue)
                }
            }
        }
    }

    fun onRemoveHeadFromQuery() {
        state.value?.let { todoState ->
            try {
                val queue = todoState.queue
                queue.remove() // can throw exception if empty
                state.value = todoState.copy(queue = queue)
            } catch (e: Exception) {
                Log.d(TAG, "removeHeadFromQueue: Nothing to remove from DialogQueue")
            }
        }
    }


}