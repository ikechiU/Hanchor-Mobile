package com.nextgendevs.hanchor.business.usecase.main.todo

import android.util.Log
import com.nextgendevs.hanchor.business.datasource.cache.auth.AuthTokenDao
import com.nextgendevs.hanchor.business.datasource.cache.datastore.AppDataStore
import com.nextgendevs.hanchor.business.datasource.cache.datastore.DataStoreKeys
import com.nextgendevs.hanchor.business.datasource.cache.main.todo.TodoDao
import com.nextgendevs.hanchor.business.datasource.cache.main.todo.toTodoEntity
import com.nextgendevs.hanchor.business.datasource.network.main.todo.TodoApiInterface
import com.nextgendevs.hanchor.business.datasource.network.request.TodoRequest
import com.nextgendevs.hanchor.business.datasource.network.response.handleUseCaseException
import com.nextgendevs.hanchor.business.datasource.network.response.toTodo
import com.nextgendevs.hanchor.business.domain.models.Todo
import com.nextgendevs.hanchor.business.domain.utils.DataState
import com.nextgendevs.hanchor.business.domain.utils.MessageType
import com.nextgendevs.hanchor.business.domain.utils.Response
import com.nextgendevs.hanchor.business.domain.utils.UIComponentType
import com.nextgendevs.hanchor.business.usecase.CheckNetwork
import com.nextgendevs.hanchor.presentation.utils.Constants
import com.nextgendevs.hanchor.presentation.utils.MySharedPreferences
import com.nextgendevs.hanchor.presentation.utils.setDefaultTodoId
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import javax.inject.Inject

private const val TAG = "AppDebug"

class InsertTodo @Inject constructor(
    private val appDataStoreManager: AppDataStore,
    private val authTokenCache: AuthTokenDao,
    val service: TodoApiInterface,
    val cache: TodoDao,
    val mySharedPreferences: MySharedPreferences,
    val checkNetwork: CheckNetwork
) {

    fun execute(todoRequest: TodoRequest): Flow<DataState<Long>> = flow {

        emit(DataState.loading<Long>())

        if(!checkNetwork.isInternetAvailable) {
            val id = mySharedPreferences.setDefaultTodoId()
            val todo = Todo(id, todoRequest.title, todoRequest.task, todoRequest.date, todoRequest.completed)

            val result = cache.insertTodo(todo.toTodoEntity())
            if (result > 0) {
                emit(
                    DataState.data(
                        Response("Inserted locally", UIComponentType.Toast, MessageType.Success),
                        result
                    )
                )
            } else {
                emit(
                    DataState.error(
                        Response(result.toString(), UIComponentType.Toast, MessageType.Success)
                    )
                )
            }
        } else {

            try{
                val token = authTokenCache.fetchAuthToken("1").tokenId
                Log.d(TAG, "execute: token is $token")
                Log.d(TAG, "execute: token is $token")

                var auth = ""
                 appDataStoreManager.readValue(DataStoreKeys.USER)?.let {
                    Log.d(TAG, "onCreate: display $auth")
                     auth = it
                }

                val userId = mySharedPreferences.getStoredString(Constants.USERID)
                val response = service.createTodo(auth, userId, todoRequest)

                if(response.isSuccessful) {
                    val todo = response.body()?.toTodo()!!

                    val result = cache.insertTodo(todo.toTodoEntity())
                    if (result > 0) {
                        emit(
                            DataState.data(
                                Response("Inserted", UIComponentType.Toast, MessageType.Success),
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
                    } else {
                        emit(
                            DataState.error(
                                Response("ERROR", UIComponentType.Toast, MessageType.Success)
                            )
                        )
                    }
                }
            } catch (e: Exception) {
                emit(
                    DataState.error(Response(e.message, UIComponentType.Dialog, MessageType.Error))
                )
            }
        }

    }.catch { e ->
        emit(handleUseCaseException(e))
    }

}