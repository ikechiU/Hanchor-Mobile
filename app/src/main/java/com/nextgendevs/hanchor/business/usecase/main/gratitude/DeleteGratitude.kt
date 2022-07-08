package com.nextgendevs.hanchor.business.usecase.main.gratitude

import com.nextgendevs.hanchor.business.datasource.cache.datastore.AppDataStore
import com.nextgendevs.hanchor.business.datasource.cache.datastore.DataStoreKeys
import com.nextgendevs.hanchor.business.datasource.cache.main.gratitude.GratitudeDao
import com.nextgendevs.hanchor.business.datasource.network.main.gratitude.GratitudeApiInterface
import com.nextgendevs.hanchor.business.datasource.network.response.handleUseCaseException
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
import okhttp3.RequestBody
import retrofit2.HttpException
import retrofit2.http.Part
import javax.inject.Inject

class DeleteGratitude (
    private val appDataStoreManager: AppDataStore,
    val service: GratitudeApiInterface,
    val cache: GratitudeDao,
    val mySharedPreferences: MySharedPreferences,
    val checkNetwork: CheckNetwork
) {

    fun execute(gratitudeId: Long, imageId: String): Flow<DataState<Int>> = flow {

        emit(DataState.loading<Int>())

        if(!checkNetwork.isInternetAvailable) {
            throw Exception("No Internet.")
        } else {
            var auth = ""
            appDataStoreManager.readValue(DataStoreKeys.USER)?.let { auth = it }
//            val token = mySharedPreferences.getStoredString(Constants.AUTH_TOKEN)
            val userId = mySharedPreferences.getStoredString(Constants.USERID)

            val operationResult = service.deleteGratitude(auth, userId, gratitudeId, imageId)

            if(operationResult.isSuccessful) {
                if(operationResult.body()?.operationResult == "SUCCESS") {
                    val result = cache.deleteGratitudeById(gratitudeId)
                    if (result == 1) {
                        emit(
                            DataState.data(
                                Response("Deleted", UIComponentType.Toast, MessageType.Success),
                                result
                            )
                        )
                    }
                }
            } else {
                if (operationResult.code() == 401) {
                    emit(
                        DataState.error(
                            Response("Token expired", UIComponentType.Toast, MessageType.Success)
                        )
                    )
                }

                if (operationResult.code() != 401) {
                    throw HttpException(operationResult)
                }
            }

        }

    }.catch { e ->
        emit(handleUseCaseException(e))
    }

}