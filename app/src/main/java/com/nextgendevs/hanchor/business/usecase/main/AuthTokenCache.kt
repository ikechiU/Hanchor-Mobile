package com.nextgendevs.hanchor.business.usecase.main

import com.nextgendevs.hanchor.business.datasource.cache.auth.AuthTokenDao
import com.nextgendevs.hanchor.business.datasource.network.response.handleUseCaseException
import com.nextgendevs.hanchor.business.domain.utils.DataState
import com.nextgendevs.hanchor.business.domain.utils.MessageType
import com.nextgendevs.hanchor.business.domain.utils.Response
import com.nextgendevs.hanchor.business.domain.utils.UIComponentType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

class AuthTokenCache (
    private val authTokenCache: AuthTokenDao
) {
    fun execute(): Flow<DataState<String>> = flow {

        emit(DataState.loading<String>())

        try {
            val result = authTokenCache.fetchAuthToken("1").tokenId
            emit(DataState.data(response = null, data = result))

        } catch (e: Exception) {
            emit(
                DataState.error(Response("ERROR " + e.message, UIComponentType.Dialog, MessageType.Error))
            )
        }

    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}