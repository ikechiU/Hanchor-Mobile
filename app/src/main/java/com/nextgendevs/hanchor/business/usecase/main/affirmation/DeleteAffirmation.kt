package com.nextgendevs.hanchor.business.usecase.main.affirmation

import com.nextgendevs.hanchor.business.datasource.cache.datastore.AppDataStore
import com.nextgendevs.hanchor.business.datasource.cache.datastore.DataStoreKeys
import com.nextgendevs.hanchor.business.datasource.cache.main.affirmation.AffirmationDao
import com.nextgendevs.hanchor.business.datasource.network.main.affirmation.AffirmationApiInterface
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
import retrofit2.HttpException
import javax.inject.Inject

class DeleteAffirmation (
    private val appDataStoreManager: AppDataStore,
    val service: AffirmationApiInterface,
    val cache: AffirmationDao,
    val mySharedPreferences: MySharedPreferences,
    val checkNetwork: CheckNetwork
) {
    fun execute(affirmationId: Long): Flow<DataState<Int>> = flow {

        emit(DataState.loading<Int>())

        if(checkNetwork.isInternetAvailable) {
            var auth = ""
            appDataStoreManager.readValue(DataStoreKeys.USER)?.let {
                auth = it
            }
            val userId = mySharedPreferences.getStoredString(Constants.USERID)

            val operationResult = service.deleteAffirmation(auth, userId, affirmationId)

            if(operationResult.isSuccessful) {
                if(operationResult.body()?.operationResult == "SUCCESS") {
                    val result = cache.deleteAffirmationById(affirmationId)
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