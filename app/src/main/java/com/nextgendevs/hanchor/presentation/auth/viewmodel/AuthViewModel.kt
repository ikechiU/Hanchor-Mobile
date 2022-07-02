package com.nextgendevs.hanchor.presentation.auth.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextgendevs.hanchor.business.domain.utils.StateMessage
import com.nextgendevs.hanchor.business.domain.utils.UIComponentType
import com.nextgendevs.hanchor.business.domain.utils.doesMessageAlreadyExistInQueue
import com.nextgendevs.hanchor.business.usecase.auth.forgot_password.ForgotPassword
import com.nextgendevs.hanchor.business.usecase.auth.login.Login
import com.nextgendevs.hanchor.business.usecase.auth.register.Register
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

private const val TAG = "AppDebug"

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val login: Login,
    private val register: Register,
    private val forgotPassword: ForgotPassword,
): ViewModel(){

    val state: MutableLiveData<AuthState> = MutableLiveData(AuthState())

    //Login
    fun login(username: String, password: String) {
        state.value?.let { authState ->
            Log.d(TAG, "authState: is $authState")

            login.execute(username, password).onEach { dataState ->
                state.value = authState.copy(isLoading = dataState.isLoading)

                dataState.data?.let { result ->
                    state.value = authState.copy(isLoading = false)
                    state.value = authState.copy(loginResult = result)
                }

                dataState.stateMessage?.let { stateMessage ->
                    state.value = authState.copy(isLoading = false)
                    onError(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }

    fun register(fullName: String, email: String, password: String) {
        state.value?.let { authState ->

            register.execute(
                fullName, email, password
            ).onEach { dataState ->
                state.value = authState.copy(isLoading = dataState.isLoading)

                dataState.data?.let { result ->
                    state.value = authState.copy(isLoading = false)
                    state.value = authState.copy(userRestResult = result)
                }

                dataState.stateMessage?.let { stateMessage ->
                    state.value = authState.copy(isLoading = false)
                    onError(stateMessage)
                }
            }.launchIn(viewModelScope)
        }

//        login(email, password)
    }

    fun forgotPassword(email: String) {
        state.value?.let { authState ->

            forgotPassword.execute(
                email
            ).onEach { dataState ->

                state.value = authState.copy(isLoading = dataState.isLoading)

                dataState.data?.let { result ->
                    state.value = authState.copy(isLoading = false)
                    state.value = authState.copy(forgotPasswordResult = result)
                }

                dataState.stateMessage?.let { stateMessage ->
                    state.value = authState.copy(isLoading = false)
                    onError(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }


    private fun onError(stateMessage: StateMessage) {
        state.value?.let { authState ->
            val queue = authState.queue
            if(!stateMessage.doesMessageAlreadyExistInQueue(queue = queue)){
                if(stateMessage.response.uiComponentType !is UIComponentType.None){
                    queue.add(stateMessage)
                    state.value = authState.copy(queue = queue)
                }
            }
        }
    }

    fun onRemoveHeadFromQuery() {
        state.value?.let { authState ->
            try {
                val queue = authState.queue
                queue.remove() // can throw exception if empty
                state.value = authState.copy(queue = queue)
            } catch (e: Exception) {
                Log.d(TAG, "removeHeadFromQueue: Nothing to remove from DialogQueue")
            }
        }
    }

}