package com.nextgendevs.hanchor.business.datasource.network.response

import android.util.Log
import com.google.gson.JsonParser
import com.nextgendevs.hanchor.business.domain.utils.DataState
import com.nextgendevs.hanchor.business.domain.utils.MessageType
import com.nextgendevs.hanchor.business.domain.utils.Response
import com.nextgendevs.hanchor.business.domain.utils.UIComponentType
import retrofit2.HttpException

private const val TAG = "AppDebug"

fun <T> handleUseCaseException(e: Throwable): DataState<T> {
    e.printStackTrace()
    when (e) {
        is HttpException -> { // Retrofit exception
            val errorResponse = convertErrorBody(e)
            return DataState.error<T>(
                response = Response(
                    message = errorResponse,
                    uiComponentType = UIComponentType.Toast,
                    messageType = MessageType.Error
                )
            )
        }
        else -> {
            if(e.message == "No Internet.") {
                return DataState.error<T>(
                    response = Response(
                        message = e.message,
                        uiComponentType = UIComponentType.Toast,
                        messageType = MessageType.Error
                    )
                )
            } else {
                return DataState.error<T>(
                    response = Response(
                        message = e.message,
                        uiComponentType = UIComponentType.Toast,
                        messageType = MessageType.Error
                    )
                )
            }
        }
    }
}

private fun convertErrorBody(throwable: HttpException): String? {
    return try {
        val msg = throwable.response()?.errorBody()?.string()
        var errorBody = ""

        if (msg.isNullOrEmpty()) {
            errorBody = throwable.response()?.message().toString()
        } else {
            try {
                errorBody = JsonParser().parse(msg).asJsonObject["message"].asString
            } catch (e: Exception) {
                errorBody = e.message.toString()
                Log.e("AppDebug", "errorMessage: ${e.localizedMessage}")
            }
        }
        Log.d(TAG, "convertErrorBody: $errorBody")
        errorBody
    } catch (exception: Exception) {
       "UNKNOWN_ERROR"
    }
}