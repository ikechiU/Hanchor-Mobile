package com.nextgendevs.hanchor.business.usecase.main.todo

import android.util.Log
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import javax.inject.Inject

class UpdateTodo (
    private val appDataStoreManager: AppDataStore,
    val service: TodoApiInterface,
    val cache: TodoDao,
    val mySharedPreferences: MySharedPreferences,
    val checkNetwork: CheckNetwork
) {

    fun execute(todoId: Long, todoRequest: TodoRequest): Flow<DataState<Int>> = flow {

        emit(DataState.loading<Int>())

        if(!checkNetwork.isInternetAvailable) {
            val todo = Todo(todoId, todoRequest.title, todoRequest.task, todoRequest.date, todoRequest.completed)

            val result = cache.updateTodo(todo.toTodoEntity())
            if (result > 0) {
                emit(
                    DataState.data(
                        Response("Updated locally", UIComponentType.Toast, MessageType.Success),
                        result
                    )
                )
            }
        } else {
//            val token = mySharedPreferences.getStoredString(Constants.AUTH_TOKEN)

            var auth = ""
            appDataStoreManager.readValue(DataStoreKeys.USER)?.let {
                auth = it
            }
            val userId = mySharedPreferences.getStoredString(Constants.USERID)

            val response = service.updateTodo(auth, userId, todoId, todoRequest)

            if (response.isSuccessful) {
                val todo = response.body()?.toTodo()!!
                Log.d("TODO", "execute: $todo")

                val result = cache.updateTodo(todo.toTodoEntity())
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