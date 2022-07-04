package com.nextgendevs.hanchor.presentation.main.fragments.home.gratitude_list.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextgendevs.hanchor.business.datasource.network.request.TodoRequest
import com.nextgendevs.hanchor.business.domain.utils.StateMessage
import com.nextgendevs.hanchor.business.domain.utils.UIComponentType
import com.nextgendevs.hanchor.business.domain.utils.doesMessageAlreadyExistInQueue
import com.nextgendevs.hanchor.business.usecase.auth.forgot_password.ForgotPassword
import com.nextgendevs.hanchor.business.usecase.auth.login.Login
import com.nextgendevs.hanchor.business.usecase.auth.register.Register
import com.nextgendevs.hanchor.business.usecase.main.gratitude.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

private const val TAG = "AppDebug"

@HiltViewModel
class GratitudeViewModel @Inject constructor(
    private val insertGratitude: InsertGratitude,
    private val updateGratitude: UpdateGratitude,
    private val fetchGratitude: FetchGratitude,
    private val fetchGratitudes: FetchGratitudes,
    private val deleteGratitude: DeleteGratitude
) : ViewModel() {

    val state: MutableLiveData<GratitudeState> = MutableLiveData(GratitudeState())

    fun fetchGratitude(todoId: Long) {
        state.value?.let { gratitudeState ->

            fetchGratitude.execute(todoId).onEach { dataState ->
                state.value = gratitudeState.copy(isLoading = dataState.isLoading)

                dataState.data?.let { result ->
                    state.value = gratitudeState.copy(isLoading = false)
                    state.value = gratitudeState.copy(gratitude = result)
                }

                dataState.stateMessage?.let { stateMessage ->
                    state.value = gratitudeState.copy(isLoading = false)
                    if (stateMessage.response.message == "Token expired") {
                        state.value = gratitudeState.copy(tokenExpired = true)
                    }
                    onError(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun clearList() {
        state.value?.let { gratitudeState ->
            this.state.value = gratitudeState.copy(gratitudeList = listOf())
        }
    }

    private fun resetPage() {
        state.value?.let { gratitudeState ->
            state.value = gratitudeState.copy(page = 1)
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

    fun fetchNextGratitudes(page: Int = 1, limit: Int = 10) {
        incrementPageNumber()
        state.value?.let { gratitudeState ->
            Log.d(TAG, "fetchNextGratitudes: page is ${gratitudeState.page}")

            fetchGratitudes.execute(gratitudeState.page, limit).onEach { dataState ->
                state.value = gratitudeState.copy(isLoading = dataState.isLoading)

                dataState.data?.let { result ->
                    if (result.isEmpty()) updateQueryExhausted(true)
                    state.value = gratitudeState.copy(isLoading = false)
                    if (result.isNotEmpty()) state.value = gratitudeState.copy(gratitudeList = result)
                }

                dataState.stateMessage?.let { stateMessage ->
                    state.value = gratitudeState.copy(isLoading = false)
                    if (stateMessage.response.message == "Token expired") {
                        state.value = gratitudeState.copy(tokenExpired = true)
                    }
                    onError(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }

    fun insertGratitude(
        title: RequestBody,
        message: RequestBody,
        file: MultipartBody.Part?
    ) {
        state.value?.let { gratitudeState ->

            insertGratitude.execute(title, message, file).onEach { dataState ->
                state.value = gratitudeState.copy(isLoading = dataState.isLoading)

                dataState.data?.let { result ->
                    state.value = gratitudeState.copy(isLoading = false)
                    state.value = gratitudeState.copy(insertResult = result)
                }

                dataState.stateMessage?.let { stateMessage ->
                    state.value = gratitudeState.copy(isLoading = false)
                    if (stateMessage.response.message == "Token expired") {
                        state.value = gratitudeState.copy(tokenExpired = true)
                    }
                    onError(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }

    fun updateGratitude(
        gratitudeId: Long,
        title: RequestBody,
        message: RequestBody,
        imageId: RequestBody,
        file: MultipartBody.Part?
    ) {
        state.value?.let { gratitudeState ->

            updateGratitude.execute(gratitudeId, title, message, imageId, file).onEach { dataState ->
                state.value = gratitudeState.copy(isLoading = dataState.isLoading)

                dataState.data?.let { result ->
                    state.value = gratitudeState.copy(isLoading = false)
                    state.value = gratitudeState.copy(updateResult = result)
                }

                dataState.stateMessage?.let { stateMessage ->
                    state.value = gratitudeState.copy(isLoading = false)
                    if (stateMessage.response.message == "Token expired") {
                        state.value = gratitudeState.copy(tokenExpired = true)
                    }
                    onError(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }


   fun fetchGratitudes(page: Int = 1, limit: Int = 10) {
       resetPage()
        state.value?.let { gratitudeState ->
            Log.d(TAG, "fetchGratitudes: page is ${gratitudeState.page}")

            fetchGratitudes.execute(gratitudeState.page, limit).onEach { dataState ->
                state.value = gratitudeState.copy(isLoading = dataState.isLoading)

                dataState.data?.let { result ->
                    state.value = gratitudeState.copy(isLoading = false)
                    state.value = gratitudeState.copy(gratitudeList = result)
                    Log.d(TAG, "fetchGratitudes: List is $result")
                }

                dataState.stateMessage?.let { stateMessage ->
                    state.value = gratitudeState.copy(isLoading = false)
                    if (stateMessage.response.message == "Token expired") {
                        state.value = gratitudeState.copy(tokenExpired = true)
                    }
                    onError(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }

    fun deleteGratitude(todoId: Long, imageId: String) {
        state.value?.let { gratitudeState ->

            deleteGratitude.execute(todoId, imageId).onEach { dataState ->
                state.value = gratitudeState.copy(isLoading = dataState.isLoading)

                dataState.data?.let { result ->
                    state.value = gratitudeState.copy(isLoading = false)
                    state.value = gratitudeState.copy(deleteResult = result)
                }

                dataState.stateMessage?.let { stateMessage ->
                    state.value = gratitudeState.copy(isLoading = false)
                    if (stateMessage.response.message == "Token expired") {
                        state.value = gratitudeState.copy(tokenExpired = true)
                    }
                    onError(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }


    private fun onError(stateMessage: StateMessage) {
        state.value?.let { gratitudeState ->
            val queue = gratitudeState.queue
            if (!stateMessage.doesMessageAlreadyExistInQueue(queue = queue)) {
                if (stateMessage.response.uiComponentType !is UIComponentType.None) {
                    queue.add(stateMessage)
                    state.value = gratitudeState.copy(queue = queue)
                }
            }
        }
    }

    fun onRemoveHeadFromQuery() {
        state.value?.let { gratitudeState ->
            try {
                val queue = gratitudeState.queue
                queue.remove() // can throw exception if empty
                state.value = gratitudeState.copy(queue = queue)
            } catch (e: Exception) {
                Log.d(TAG, "removeHeadFromQueue: Nothing to remove from DialogQueue")
            }
        }
    }

}