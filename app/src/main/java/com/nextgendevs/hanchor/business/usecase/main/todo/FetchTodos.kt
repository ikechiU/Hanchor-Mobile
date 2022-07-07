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

class FetchTodos (
    private val appDataStoreManager: AppDataStore,
    val service: TodoApiInterface,
    val cache: TodoDao,
    val mySharedPreferences: MySharedPreferences,
    val checkNetwork: CheckNetwork
) {

    fun execute(page: Int, limit: Int): Flow<DataState<List<Todo>>> = flow {

        emit(DataState.loading<List<Todo>>())

        if(checkNetwork.isInternetAvailable) {
            var auth = ""
            appDataStoreManager.readValue(DataStoreKeys.USER)?.let {
                auth = it
            }

//            val token = mySharedPreferences.getStoredString(Constants.AUTH_TOKEN)
            val userId = mySharedPreferences.getStoredString(Constants.USERID)

            val response = service.getTodos(auth, userId, page, limit)

            if(response.isSuccessful) {
                val listOfTodo = response.body()?.map { it.toTodo() }!!
                var cachedTodos: List<Todo> = emptyList()

                if(listOfTodo.isNotEmpty()) {
                    for (todo in listOfTodo) {
                        cache.insertTodo(todo.toTodoEntity())
                    }
                }

                cachedTodos = cache.fetchTodos(page, limit).map { it.toTodo() }

                emit(
                    DataState.data(null, cachedTodos)
                )

            } else {
                if (response.code() == 401) {
                    emit(
                        DataState.error(
                            Response("Token expired", UIComponentType.Toast, MessageType.Success)
                        )
                    )
                } else {

                    emit(
                        DataState.data(null, cache.fetchTodos(page, limit).map { it.toTodo() })
                    )

                    emit(
                        DataState.error(
                            Response(response.errorBody().toString(), UIComponentType.Toast, MessageType.Error)
                        )
                    )
                }
            }

        } else {
            val cachedTodos = cache.fetchTodos(page, limit).map { it.toTodo() }
            emit(
                DataState.data(null, cachedTodos)
            )
        }

    }.catch { e ->
        emit(handleUseCaseException(e))
    }

}