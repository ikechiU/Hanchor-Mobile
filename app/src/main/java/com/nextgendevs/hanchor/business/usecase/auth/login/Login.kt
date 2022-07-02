package com.nextgendevs.hanchor.business.usecase.auth.login

import com.android.sampleschoolandroid.business.domain.util.isEmailValid
import com.nextgendevs.hanchor.business.datasource.cache.auth.AuthTokenDao
import com.nextgendevs.hanchor.business.datasource.cache.auth.AuthTokenEntity
import com.nextgendevs.hanchor.business.datasource.cache.datastore.AppDataStore
import com.nextgendevs.hanchor.business.datasource.cache.datastore.DataStoreKeys
import com.nextgendevs.hanchor.business.datasource.network.auth.AuthApiInterface
import com.nextgendevs.hanchor.business.datasource.network.response.handleUseCaseException
import com.nextgendevs.hanchor.business.domain.utils.DataState
import com.nextgendevs.hanchor.business.domain.utils.MessageType
import com.nextgendevs.hanchor.business.domain.utils.Response
import com.nextgendevs.hanchor.business.domain.utils.UIComponentType
import com.nextgendevs.hanchor.presentation.utils.Constants
import com.nextgendevs.hanchor.presentation.utils.MySharedPreferences
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import okhttp3.Headers
import java.lang.Exception
import javax.inject.Inject

private const val TAG = "AppDebug"
private const val ERROR_ALL_VALIDATION_FIELDS_REQUIRED = "All fields required"
private const val LOGIN_ERROR_403 = "Incorrect credential."
private const val ERROR_INVALID_EMAIL = "Invalid email address"

class Login @Inject constructor(
    private val appDataStoreManager: AppDataStore,
    private val authTokenCache: AuthTokenDao,
    private val service: AuthApiInterface,
    private val mySharedPreferences: MySharedPreferences,
){
    fun execute(email: String, password: String): Flow<DataState<String>> = flow {

        if(email.trim().isEmpty() && password.trim().isEmpty()) {
            throw Exception(ERROR_ALL_VALIDATION_FIELDS_REQUIRED)
        }

        if (email.contains('@')) {
            if (!isEmailValid(email))
                throw Exception(ERROR_INVALID_EMAIL)
        }

        emit(DataState.loading<String>())

        val loginResponse = service.login(Credential.signInCredentials(email, password))

        if(loginResponse.isSuccessful){
            val headerList: Headers = loginResponse.headers()

            val token = headerList["Authorization"]!!
            appDataStoreManager.setValue(DataStoreKeys.USER, headerList["Authorization"]!!)

            try {
                val result = authTokenCache.insertAuthToken(AuthTokenEntity("1", token))

                if (result > 0L) {
                    val userId = HeaderList.userId(headerList)
                    val username = HeaderList.username(headerList)
                    val firstname = HeaderList.firstName(headerList)
                    val lastname = HeaderList.lastName(headerList)
                    val saveEmail = HeaderList.email(headerList)

                    mySharedPreferences.storeStringValue(Constants.AUTH_TOKEN, token)
                    mySharedPreferences.storeStringValue(Constants.USERID, userId)
                    mySharedPreferences.storeStringValue(Constants.USERNAME, username)
                    mySharedPreferences.storeStringValue(Constants.FIRSTNAME, firstname)
                    mySharedPreferences.storeStringValue(Constants.LASTNAME, lastname)
                    mySharedPreferences.storeStringValue(Constants.EMAIL, saveEmail)

                    emit(DataState.data(response = null, data = result.toString()))
                }
            } catch (e: Exception) {
                emit(
                    DataState.error(Response(e.message, UIComponentType.Dialog, MessageType.Error))
                )
            }

        } else {
            if(loginResponse.code() == 403) {
                throw Exception(LOGIN_ERROR_403)
            }
        }

    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}














