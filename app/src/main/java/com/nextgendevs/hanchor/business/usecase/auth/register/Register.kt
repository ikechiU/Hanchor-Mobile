package com.nextgendevs.hanchor.business.usecase.auth.register

import com.android.sampleschoolandroid.business.domain.util.*
import com.nextgendevs.hanchor.business.datasource.network.auth.AuthApiInterface
import com.nextgendevs.hanchor.business.datasource.network.request.UserRequest
import com.nextgendevs.hanchor.business.datasource.network.response.UserRest
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

class Register (private val service: AuthApiInterface) {

    fun execute(fullName: String, email: String, password: String): Flow<DataState<UserRest>> =
        flow {

            if (fullName.trim().isEmpty() && email.trim().isEmpty() && password.trim().isEmpty()) {
                throw Exception(ERROR_ALL_VALIDATION_FIELDS_REQUIRED)
            }

            if (!isEmailValid(email)) {
                throw Exception(ERROR_INVALID_EMAIL)
            }

            emit(DataState.loading<UserRest>())

            val userRequest =
                UserRequest(fullName, fullName, fullName, email, password)

            val registerResponse = service.register(userRequest)

            emit(
                DataState.data(
                    response = Response(
                        "Successful registration",
                        UIComponentType.Toast,
                        MessageType.None
                    ),
                    data = registerResponse
                )
            )

        }.catch { e ->
            emit(handleUseCaseException(e))
        }
}














