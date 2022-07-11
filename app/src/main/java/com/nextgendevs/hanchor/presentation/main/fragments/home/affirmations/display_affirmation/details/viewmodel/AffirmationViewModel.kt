package com.nextgendevs.hanchor.presentation.main.fragments.home.affirmations.display_affirmation.details.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nextgendevs.hanchor.business.datasource.network.request.AffirmationRequest
import com.nextgendevs.hanchor.business.domain.utils.*
import com.nextgendevs.hanchor.business.usecase.main.affirmation.DeleteAffirmation
import com.nextgendevs.hanchor.business.usecase.main.affirmation.InsertAffirmation
import com.nextgendevs.hanchor.business.usecase.main.affirmation.UpdateAffirmation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

private const val TAG = "AppDebug"

@HiltViewModel
class AffirmationViewModel @Inject constructor(
    private val insertAffirmation: InsertAffirmation,
    private val updateAffirmation: UpdateAffirmation,
    private val deleteAffirmation: DeleteAffirmation
): ViewModel(){

    val state: MutableLiveData<AffirmationState> = MutableLiveData(AffirmationState())

    fun insertAffirmation(affirmationRequest: AffirmationRequest) {
        state.value?.let { affirmationState ->

            insertAffirmation.execute(affirmationRequest).onEach { dataState ->
                state.value = affirmationState.copy(isLoading = dataState.isLoading)

                dataState.data?.let { result ->
                    state.value = affirmationState.copy(isLoading = false)
                    state.value = affirmationState.copy(insertResult = result)
                }

                dataState.stateMessage?.let { stateMessage ->
                    state.value = affirmationState.copy(isLoading = false)
                    if (stateMessage.response.message == "Token expired") {
                        state.value = affirmationState.copy(tokenExpired = true)
                    }
                    onError(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }

   fun updateAffirmation(affirmationId: Long, affirmationRequest: AffirmationRequest) {
        state.value?.let { affirmationState ->

            updateAffirmation.execute(affirmationId, affirmationRequest).onEach { dataState ->
                state.value = affirmationState.copy(isLoading = dataState.isLoading)

                dataState.data?.let { result ->
                    Log.d(TAG, "updateAffirmation: result is ...$result")
                    state.value = affirmationState.copy(isLoading = false)
                    state.value = affirmationState.copy(updateResult = result)
                }

                dataState.stateMessage?.let { stateMessage ->
                    state.value = affirmationState.copy(isLoading = false)
                    if (stateMessage.response.message == "Token expired") {
                        state.value = affirmationState.copy(tokenExpired = true)
                    }
                    onError(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }

    fun deleteAffirmation(affirmationId: Long) {
        state.value?.let { affirmationState ->
            state.value = affirmationState.copy(deleteResult = 0)

            deleteAffirmation.execute(affirmationId).onEach { dataState ->
                state.value = affirmationState.copy(isLoading = dataState.isLoading)

                dataState.data?.let { result ->
                    Log.d(TAG, "deleteAffirmation: result is $result")
                    state.value = affirmationState.copy(isLoading = false)
                    state.value = affirmationState.copy(deleteResult = result)
                }

                dataState.stateMessage?.let { stateMessage ->
                    state.value = affirmationState.copy(isLoading = false)
                    if (stateMessage.response.message == "Token expired") {
                        state.value = affirmationState.copy(tokenExpired = true)
                    }
                    onError(stateMessage)
                }
            }.launchIn(viewModelScope)
        }
    }

    private fun onError(stateMessage: StateMessage) {
        state.value?.let { affirmationState ->
            val queue = affirmationState.queue
            if(!stateMessage.doesMessageAlreadyExistInQueue(queue = queue)){
                if(stateMessage.response.uiComponentType !is UIComponentType.None){
                    queue.add(stateMessage)
                    state.value = affirmationState.copy(queue = queue)
                }
            }
        }
    }

    fun onRemoveHeadFromQuery() {
        state.value?.let { affirmationState ->
            try {
                val queue = affirmationState.queue
                queue.remove() // can throw exception if empty
                state.value = affirmationState.copy(queue = queue)
            } catch (e: Exception) {
                Log.d(TAG, "removeHeadFromQueue: Nothing to remove from DialogQueue")
            }
        }
    }


}