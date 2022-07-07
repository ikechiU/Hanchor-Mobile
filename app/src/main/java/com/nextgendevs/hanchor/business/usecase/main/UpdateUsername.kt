package com.nextgendevs.hanchor.business.usecase.main

import com.nextgendevs.hanchor.business.datasource.cache.datastore.AppDataStore
import com.nextgendevs.hanchor.business.datasource.cache.datastore.DataStoreKeys
import com.nextgendevs.hanchor.business.datasource.network.main.MainApiInterface
import com.nextgendevs.hanchor.business.datasource.network.request.UpdateUserRequest
import com.nextgendevs.hanchor.business.datasource.network.response.UserRest
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
import javax.inject.Inject

private const val USERNAME_REQUIRED = "Username required"

class UpdateUsername (
    val appDataStoreManager: AppDataStore,
    val service: MainApiInterface,
    val mySharedPreferences: MySharedPreferences,
    val checkNetwork: CheckNetwork
) {

    fun execute(username: String): Flow<DataState<UserRest>> = flow {
        mySharedPreferences.storeStringValue(Constants.USERNAME, username)

        emit(DataState.loading<UserRest>())

        if(!checkNetwork.isInternetAvailable) {
            throw Exception("No Internet.")
        } else {
//            val token = mySharedPreferences.getStoredString(Constants.AUTH_TOKEN)

            var auth = ""
            appDataStoreManager.readValue(DataStoreKeys.USER)?.let {
                auth = it
            }
            val userId = mySharedPreferences.getStoredString(Constants.USERID)
            val firstname = mySharedPreferences.getStoredString(Constants.FIRSTNAME)

            val updateUserRequest = UpdateUserRequest(firstname, firstname, username)

            val response = service.updateUser(auth, userId, updateUserRequest)

            if (response.isSuccessful) {
                val updateResponse = response.body()!!

                updateResponse.email

                DataState.data(
                    response = null,
                    data = updateResponse
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
        }

    }.catch { e ->
        emit(handleUseCaseException(e))
    }

}