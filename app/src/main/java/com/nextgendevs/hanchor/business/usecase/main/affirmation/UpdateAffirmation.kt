package com.nextgendevs.hanchor.business.usecase.main.affirmation

import android.util.Log
import com.nextgendevs.hanchor.business.datasource.cache.datastore.AppDataStore
import com.nextgendevs.hanchor.business.datasource.cache.datastore.DataStoreKeys
import com.nextgendevs.hanchor.business.datasource.cache.main.affirmation.AffirmationDao
import com.nextgendevs.hanchor.business.datasource.cache.main.affirmation.toAffirmationEntity
import com.nextgendevs.hanchor.business.datasource.network.main.affirmation.AffirmationApiInterface
import com.nextgendevs.hanchor.business.datasource.network.request.AffirmationRequest
import com.nextgendevs.hanchor.business.datasource.network.response.handleUseCaseException
import com.nextgendevs.hanchor.business.datasource.network.response.toAffirmation
import com.nextgendevs.hanchor.business.domain.utils.DataState
import com.nextgendevs.hanchor.business.domain.utils.MessageType
import com.nextgendevs.hanchor.business.domain.utils.Response
import com.nextgendevs.hanchor.business.domain.utils.UIComponentType
import com.nextgendevs.hanchor.business.usecase.CheckNetwork
import com.nextgendevs.hanchor.presentation.utils.Constants
import com.nextgendevs.hanchor.presentation.utils.MySharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class UpdateAffirmation (
    private val appDataStoreManager: AppDataStore,
    val service: AffirmationApiInterface,
    val cache: AffirmationDao,
    val mySharedPreferences: MySharedPreferences,
    val checkNetwork: CheckNetwork
) {

    fun execute(affirmationId: Long, affirmationRequest: AffirmationRequest): Flow<DataState<Int>> = flow {

        emit(DataState.loading<Int>())

        if(checkNetwork.isInternetAvailable) {
            var auth = ""
            appDataStoreManager.readValue(DataStoreKeys.USER)?.let {
                auth = it
            }

            val userId = mySharedPreferences.getStoredString(Constants.USERID)

            val response = service.updateAffirmation(auth, userId, affirmationId, affirmationRequest)

            if (response.isSuccessful) {
                val affirmation = response.body()?.toAffirmation()!!
                Log.d("Affirmation", "execute: $affirmation")

                val result = cache.updateAffirmation(affirmation.toAffirmationEntity())
                if (result == 1) {
                    emit(
                        DataState.data(
                            Response("Updated", UIComponentType.Toast, MessageType.Success),
                            result
                        )
                    )
                }
            } else {
                if (response.code() == 401) {
                    emit(
                        DataState.error(
                            Response("Token expired", UIComponentType.Toast, MessageType.Success)
                        )
                    )
                }
            }
        }

    }.catch { e ->
        emit(handleUseCaseException(e))
    }

}