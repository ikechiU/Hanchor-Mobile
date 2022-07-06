package com.nextgendevs.hanchor.presentation

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextgendevs.hanchor.business.domain.utils.StateMessage
import com.nextgendevs.hanchor.business.domain.utils.UIComponentType
import com.nextgendevs.hanchor.business.domain.utils.doesMessageAlreadyExistInQueue
import com.nextgendevs.hanchor.business.usecase.main.AuthTokenCache
import com.nextgendevs.hanchor.business.usecase.main.affirmation.GetAffirmation
import com.nextgendevs.hanchor.business.usecase.main.GetUser
import com.nextgendevs.hanchor.business.usecase.main.UpdateUsername
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

private const val TAG = "AppDebug"

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getAffirmation: GetAffirmation,
    private val getUser: GetUser,
    private val updateUsername: UpdateUsername,
    private val authTokenCache: AuthTokenCache
): ViewModel(){

    val state: MutableLiveData<MainState> = MutableLiveData(MainState())


    fun authToken() {
        state.value?.let { mainState ->

            authTokenCache.execute().onEach { dataState ->
                state.value = mainState.copy(isLoading = dataState.isLoading)

                dataState.data?.let { result ->
                    state.value = mainState.copy(authToken = result)
                }

                dataState.stateMessage?.let { stateMessage ->
                    if (stateMessage.response.message == "Token expired") {
                        state.value = mainState.copy(tokenExpired = true)
                    }
                    if (stateMessage.response.message == "No Internet.") {
                        state.value = mainState.copy(errorMessage = "No Internet.")
                    }
                    onError(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }


    fun updateUsername(username: String) {
        state.value?.let { mainState ->

            updateUsername.execute(username).onEach { dataState ->
                state.value = mainState.copy(isLoading = dataState.isLoading)

                dataState.data?.let { result ->
                    state.value = mainState.copy(updateResult = result.username!!)
                }

                dataState.stateMessage?.let { stateMessage ->
                    if (stateMessage.response.message == "Token expired") {
                        state.value = mainState.copy(tokenExpired = true)
                    }
                    if (stateMessage.response.message == "No Internet.") {
                        state.value = mainState.copy(errorMessage = "No Internet.")
                    }
                    onError(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }

    fun getUser() {
        state.value?.let { mainState ->
            state.value = mainState.copy(affirmations = emptyList())

            getUser.execute().onEach { dataState ->
                state.value = mainState.copy(isLoading = dataState.isLoading)

                dataState.data?.let { result ->
                    state.value = mainState.copy(affirmations = result)
                }

                dataState.stateMessage?.let { stateMessage ->
                    if (stateMessage.response.message == "Token expired") {
                        state.value = mainState.copy(tokenExpired = true)
                    }
                    if (stateMessage.response.message == "No Internet.") {
                        state.value = mainState.copy(errorMessage = "No Internet.")
                    }
                    onError(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }

    fun getAffirmation() {
        state.value?.let { mainState ->
            state.value = mainState.copy(affirmationMessages = emptyList())

            getAffirmation.execute().onEach { dataState ->
                state.value = mainState.copy(isLoading = dataState.isLoading)

                dataState.data?.let { result ->
                    Log.d(TAG, "getAffirmation: result is $result")
                    state.value = mainState.copy(affirmationMessages = result)
                }

                dataState.stateMessage?.let { stateMessage ->
                    if (stateMessage.response.message == "Token expired") {
                        state.value = mainState.copy(tokenExpired = true)
                    }
                    if (stateMessage.response.message == "No Internet.") {
                        state.value = mainState.copy(errorMessage = "No Internet.")
                    }
                    onError(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }


    fun getAffirmations(query: String) {
        state.value?.let { mainState ->
            state.value = mainState.copy(affirmations = emptyList())

            getAffirmation.execute(query).onEach { dataState ->
                state.value = mainState.copy(isLoading = dataState.isLoading)

                dataState.data?.let { result ->
                    Log.d(TAG, "getAffirmation: result is $result")
                    state.value = mainState.copy(affirmations = result)
                }

                dataState.stateMessage?.let { stateMessage ->
                    if (stateMessage.response.message == "Token expired") {
                        state.value = mainState.copy(tokenExpired = true)
                    }
                    if (stateMessage.response.message == "No Internet.") {
                        state.value = mainState.copy(errorMessage = "No Internet.")
                    }
                    onError(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }


    private fun onError(stateMessage: StateMessage) {
        state.value?.let { mainState ->
            val queue = mainState.queue
            if (!stateMessage.doesMessageAlreadyExistInQueue(queue = queue)) {
                if (stateMessage.response.uiComponentType !is UIComponentType.None) {
                    queue.add(stateMessage)
                    state.value = mainState.copy(queue = queue)
                }
            }
        }
    }

    fun onRemoveHeadFromQuery() {
        state.value?.let { mainState ->
            try {
                val queue = mainState.queue
                queue.remove() // can throw exception if empty
                state.value = mainState.copy(queue = queue)
            } catch (e: Exception) {
                Log.d(TAG, "removeHeadFromQueue: Nothing to remove from DialogQueue")
            }
        }
    }

}