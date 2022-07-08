package com.nextgendevs.hanchor.business.usecase.main.gratitude

import com.nextgendevs.hanchor.business.datasource.cache.datastore.AppDataStore
import com.nextgendevs.hanchor.business.datasource.cache.datastore.DataStoreKeys
import com.nextgendevs.hanchor.business.datasource.cache.main.gratitude.GratitudeDao
import com.nextgendevs.hanchor.business.datasource.cache.main.gratitude.toGratitudeEntity
import com.nextgendevs.hanchor.business.datasource.network.main.gratitude.GratitudeApiInterface
import com.nextgendevs.hanchor.business.datasource.network.response.handleUseCaseException
import com.nextgendevs.hanchor.business.datasource.network.response.toGratitude
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
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException
import javax.inject.Inject

class UpdateGratitude (
    private val appDataStoreManager: AppDataStore,
    val service: GratitudeApiInterface,
    val cache: GratitudeDao,
    val mySharedPreferences: MySharedPreferences,
    val checkNetwork: CheckNetwork
) {
    fun execute(
        gratitudeId: Long,
        title: RequestBody,
        message: RequestBody,
        imageId: RequestBody,
        file: MultipartBody.Part?
    ): Flow<DataState<Int>> = flow {

        emit(DataState.loading<Int>())

        if(!checkNetwork.isInternetAvailable) {
            throw Exception("No Internet.")
        } else {
            var auth = ""
            appDataStoreManager.readValue(DataStoreKeys.USER)?.let {
                auth = it
            }

//            val token = mySharedPreferences.getStoredString(Constants.AUTH_TOKEN)
            val userId = mySharedPreferences.getStoredString(Constants.USERID)

            val response = service.updateGratitude(auth, userId, gratitudeId, title, message, imageId, file)

            if(response.isSuccessful) {
                val gratitude = response.body()?.toGratitude()!!

                val result = cache.updateGratitude(gratitude.toGratitudeEntity())

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
                if (response.code() != 401) {
                    throw HttpException(response)
                }
            }
        }

    }.catch { e ->
        emit(handleUseCaseException(e))
    }

}