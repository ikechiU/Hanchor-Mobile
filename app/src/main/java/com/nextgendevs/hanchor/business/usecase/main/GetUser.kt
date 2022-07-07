package com.nextgendevs.hanchor.business.usecase.main

import android.util.Log
import com.nextgendevs.hanchor.business.datasource.cache.datastore.AppDataStore
import com.nextgendevs.hanchor.business.datasource.cache.datastore.DataStoreKeys
import com.nextgendevs.hanchor.business.datasource.cache.main.affirmation.AffirmationDao
import com.nextgendevs.hanchor.business.datasource.cache.main.affirmation.toAffirmation
import com.nextgendevs.hanchor.business.datasource.cache.main.affirmation.toAffirmationEntity
import com.nextgendevs.hanchor.business.datasource.network.main.MainApiInterface
import com.nextgendevs.hanchor.business.datasource.network.request.UpdateUserRequest
import com.nextgendevs.hanchor.business.datasource.network.response.UserRest
import com.nextgendevs.hanchor.business.datasource.network.response.handleUseCaseException
import com.nextgendevs.hanchor.business.datasource.network.response.toAffirmation
import com.nextgendevs.hanchor.business.domain.models.Affirmation
import com.nextgendevs.hanchor.business.domain.models.Todo
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

private const val USERNAME_REQUIRED = "Username required"

class GetUser (
    val cache: AffirmationDao,
    val appDataStoreManager: AppDataStore,
    val service: MainApiInterface,
    val mySharedPreferences: MySharedPreferences,
    val checkNetwork: CheckNetwork
) {

    fun execute(): Flow<DataState<List<Affirmation>>> = flow {

        emit(DataState.loading<List<Affirmation>>())

        if(!checkNetwork.isInternetAvailable) {
            throw Exception("No Internet.")
        } else {

            var auth = ""
            appDataStoreManager.readValue(DataStoreKeys.USER)?.let {
                auth = it
            }
            val userId = mySharedPreferences.getStoredString(Constants.USERID)

            val response = service.getUser(auth, userId)

            if (response.isSuccessful) {
                val affirmationRest = response.body()!!.affirmationRests!!

                val affirmations = affirmationRest.map { it.toAffirmation() }

                if(affirmationRest.isNotEmpty()) {
                    for(affirmation in affirmations){
                        cache.insertAffirmation(affirmation.toAffirmationEntity())
                    }
                }

                val cachedAffirmations=  cache.fetchAffirmations(1, 20).map { it.toAffirmation() }

                emit(
                    DataState.data(response = null, data = cachedAffirmations)
                )

            } else {
                if (response.code() == 401) {
                    emit(
                        DataState.error(
                            Response("Token expired", UIComponentType.Toast, MessageType.Error)
                        )
                    )
                }
            }
        }

    }.catch { e ->
        emit(handleUseCaseException(e))
    }

}