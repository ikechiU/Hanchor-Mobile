package com.nextgendevs.hanchor.business.usecase.auth.forgot_password

import com.android.sampleschoolandroid.business.domain.util.*
import com.nextgendevs.hanchor.business.datasource.network.auth.AuthApiInterface
import com.nextgendevs.hanchor.business.datasource.network.request.PasswordResetRequestModel
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

private const val ERROR_INVALID_EMAIL = "Invalid email address"
private const val ERROR_ALL_VALIDATION_FIELDS_REQUIRED = "All fields required"
private const val TAG = "AppDebug"

class ForgotPassword @Inject constructor(private val service: AuthApiInterface) {

    fun execute(email: String): Flow<DataState<String>> = flow {

        if(email.trim().isEmpty()) {
            throw Exception(ERROR_ALL_VALIDATION_FIELDS_REQUIRED)
        }

        if (!isEmailValid(email)) {
            throw Exception(ERROR_INVALID_EMAIL)
        }

        emit(DataState.loading<String>())

        val passwordResetRequestModel =
            PasswordResetRequestModel(email)

        val response = service.forgotPassword(passwordResetRequestModel)
        val result = response.operationResult

        emit(
            DataState.data(
                response = Response(
                    "Password reset link sent to your email.", UIComponentType.Dialog, MessageType.Info
                ), data = result
            )
        )

    }.catch { e ->
        emit(handleUseCaseException(e))
    }
}














