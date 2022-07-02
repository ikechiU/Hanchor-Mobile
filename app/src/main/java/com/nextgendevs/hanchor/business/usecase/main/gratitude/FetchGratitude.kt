package com.nextgendevs.hanchor.business.usecase.main.gratitude

import com.nextgendevs.hanchor.business.datasource.cache.datastore.AppDataStore
import com.nextgendevs.hanchor.business.datasource.cache.datastore.DataStoreKeys
import com.nextgendevs.hanchor.business.datasource.cache.main.gratitude.GratitudeDao
import com.nextgendevs.hanchor.business.datasource.cache.main.gratitude.toGratitude
import com.nextgendevs.hanchor.business.datasource.cache.main.gratitude.toGratitudeEntity
import com.nextgendevs.hanchor.business.datasource.network.main.gratitude.GratitudeApiInterface
import com.nextgendevs.hanchor.business.datasource.network.response.handleUseCaseException
import com.nextgendevs.hanchor.business.datasource.network.response.toGratitude
import com.nextgendevs.hanchor.business.domain.models.Gratitude
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

class FetchGratitude @Inject constructor(
    private val appDataStoreManager: AppDataStore,
    val service: GratitudeApiInterface,
    val cache: GratitudeDao,
    val mySharedPreferences: MySharedPreferences,
    val checkNetwork: CheckNetwork
) {

    fun execute(gratitudeId: Long): Flow<DataState<Gratitude>> = flow {

        emit(DataState.loading<Gratitude>())

        if(checkNetwork.isInternetAvailable) {
//            val token = mySharedPreferences.getStoredString(Constants.AUTH_TOKEN)
            val userId = mySharedPreferences.getStoredString(Constants.USERID)

            var auth = ""
            appDataStoreManager.readValue(DataStoreKeys.USER)?.let {
                auth = it
            }
            val response = service.getGratitude(auth, userId, gratitudeId)

            if(response.isSuccessful) {
                val gratitude = response.body()?.toGratitude()!!

                cache.insertGratitude(gratitude.toGratitudeEntity())

                val cachedGratitude = cache.fetchGratitude(gratitudeId).toGratitude()
                emit(
                    DataState.data(null, cachedGratitude)
                )
            } else {
                if (response.code() == 401) {
                    emit(
                        DataState.error(
                            Response("Token expired", UIComponentType.Toast, MessageType.Success)
                        )
                    )
                }
            }

        } else {
            val cachedGratitude = cache.fetchGratitude(gratitudeId).toGratitude()
            emit(
                DataState.data(null, cachedGratitude)
            )
        }

    }.catch { e ->
        emit(handleUseCaseException(e))
    }

}