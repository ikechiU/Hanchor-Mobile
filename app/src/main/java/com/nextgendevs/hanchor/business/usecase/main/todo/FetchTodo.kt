package com.nextgendevs.hanchor.business.usecase.main.todo

import com.nextgendevs.hanchor.business.datasource.cache.datastore.AppDataStore
import com.nextgendevs.hanchor.business.datasource.cache.datastore.DataStoreKeys
import com.nextgendevs.hanchor.business.datasource.cache.main.todo.TodoDao
import com.nextgendevs.hanchor.business.datasource.cache.main.todo.toTodo
import com.nextgendevs.hanchor.business.datasource.cache.main.todo.toTodoEntity
import com.nextgendevs.hanchor.business.datasource.network.main.todo.TodoApiInterface
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
import javax.inject.Inject

class FetchTodo (
    private val appDataStoreManager: AppDataStore,
    val service: TodoApiInterface,
    val cache: TodoDao,
    val mySharedPreferences: MySharedPreferences,
    val checkNetwork: CheckNetwork
) {
    fun execute(todoId: Long): Flow<DataState<Todo>> = flow {

        emit(DataState.loading<Todo>())

        if(checkNetwork.isInternetAvailable) {
            var auth = ""
            appDataStoreManager.readValue(DataStoreKeys.USER)?.let {
                auth = it
            }

//            val token = mySharedPreferences.getStoredString(Constants.AUTH_TOKEN)
            val userId = mySharedPreferences.getStoredString(Constants.USERID)

            val response = service.getTodo(auth, userId, todoId)

            if(response.isSuccessful) {
                val todo = response.body()?.toTodo()!!

                cache.insertTodo(todo.toTodoEntity())

                val cachedTodo = cache.fetchTodo(todoId).toTodo()
                emit(
                    DataState.data(null, cachedTodo)
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

        } else {
            val cachedTodo = cache.fetchTodo(todoId).toTodo()
            emit(
                DataState.data(null, cachedTodo)
            )
        }

    }.catch { e ->
        emit(handleUseCaseException(e))
    }

}