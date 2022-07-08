package com.nextgendevs.hanchor.business.usecase.main.gratitude

import android.util.Log
import com.nextgendevs.hanchor.business.datasource.cache.datastore.AppDataStore
import com.nextgendevs.hanchor.business.datasource.cache.datastore.DataStoreKeys
import com.nextgendevs.hanchor.business.datasource.cache.main.gratitude.GratitudeDao
import com.nextgendevs.hanchor.business.datasource.cache.main.gratitude.toGratitude
import com.nextgendevs.hanchor.business.datasource.cache.main.gratitude.toGratitudeEntity
import com.nextgendevs.hanchor.business.datasource.network.main.gratitude.GratitudeApiInterface
import com.nextgendevs.hanchor.business.datasource.network.response.handleUseCaseException
import com.nextgendevs.hanchor.business.datasource.network.response.toGratitude
import com.nextgendevs.hanchor.business.domain.models.Gratitude
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
import retrofit2.HttpException
import javax.inject.Inject

class FetchGratitudes (
    private val appDataStoreManager: AppDataStore,
    val service: GratitudeApiInterface,
    val cache: GratitudeDao,
    val mySharedPreferences: MySharedPreferences,
    val checkNetwork: CheckNetwork
) {

    fun execute(page: Int, limit: Int): Flow<DataState<List<Gratitude>>> = flow {

        emit(DataState.loading<List<Gratitude>>())

        if(checkNetwork.isInternetAvailable) {
            var auth = ""
            appDataStoreManager.readValue(DataStoreKeys.USER)?.let { auth = it }
//            val token = mySharedPreferences.getStoredString(Constants.AUTH_TOKEN)
            val userId = mySharedPreferences.getStoredString(Constants.USERID)

            val response = service.getGratitudes(auth, userId, page, limit)
            Log.d("TAG", "execute: response body ${response.body()}")

            if(response.isSuccessful) {
                val listOfGratitude = response.body()?.map { it.toGratitude() }!!
                Log.d("TAG", "execute: List $listOfGratitude")

                if(listOfGratitude.isNotEmpty()) {
                    for (gratitude in listOfGratitude) {
                        cache.insertGratitude(gratitude.toGratitudeEntity())
                    }
                }
                for (gratitude in listOfGratitude) {
                    cache.insertGratitude(gratitude.toGratitudeEntity())
                }


                val cachedGratitudes: List<Gratitude> = cache.fetchGratitudes(page, limit).map { it.toGratitude() }
                emit(
                    DataState.data(null, cachedGratitudes)
                )
            } else {
                if (response.code() == 401) {
                    emit(
                        DataState.error(
                            Response("Token expired", UIComponentType.Toast, MessageType.Error)
                        )
                    )
                }
                if (response.code() != 401) {
                    throw HttpException(response)
                }
            }
        } else {
            val cachedGratitudes = cache.fetchGratitudes(page, limit).map { it.toGratitude() }
            emit(
                DataState.data(null, cachedGratitudes)
            )
        }

    }.catch { e ->
        emit(handleUseCaseException(e))
    }

}