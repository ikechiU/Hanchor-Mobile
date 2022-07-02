package com.nextgendevs.hanchor.business.usecase.main.todo

import com.nextgendevs.hanchor.business.datasource.cache.datastore.AppDataStore
import com.nextgendevs.hanchor.business.datasource.cache.datastore.DataStoreKeys
import com.nextgendevs.hanchor.business.datasource.cache.main.todo.TodoDao
import com.nextgendevs.hanchor.business.datasource.cache.main.todo.toTodoEntity
import com.nextgendevs.hanchor.business.datasource.network.main.todo.TodoApiInterface
import com.nextgendevs.hanchor.business.datasource.network.response.handleUseCaseException
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

class DeleteTodo @Inject constructor(
    private val appDataStoreManager: AppDataStore,
    val service: TodoApiInterface,
    val cache: TodoDao,
    val mySharedPreferences: MySharedPreferences,
    val checkNetwork: CheckNetwork
) {
    fun execute(todoId: Long): Flow<DataState<Int>> = flow {

        emit(DataState.loading<Int>())

        if(!checkNetwork.isInternetAvailable) {
            val id = mySharedPreferences.getStoredLong(Constants.TODO_UPDATE_ID_LOCAL)

            val result = cache.deleteTodoById(id)
            if (result > 0) {
                emit(
                    DataState.data(
                        Response("Deleted locally", UIComponentType.Toast, MessageType.Success),
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

            val operationResult = service.deleteTodo(auth, userId, todoId)

            if(operationResult.isSuccessful) {
                if(operationResult.body()?.operationResult == "SUCCESS") {
                    val result = cache.deleteTodoById(todoId)
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
            }
        }

    }.catch { e ->
        emit(handleUseCaseException(e))
    }

}