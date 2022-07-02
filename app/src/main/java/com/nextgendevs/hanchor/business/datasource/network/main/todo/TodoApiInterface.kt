package com.nextgendevs.hanchor.business.datasource.network.main.todo

import com.nextgendevs.hanchor.business.datasource.network.request.TodoRequest
import com.nextgendevs.hanchor.business.datasource.network.response.OperationResult
import com.nextgendevs.hanchor.business.datasource.network.response.TodoRest
import retrofit2.Response

import retrofit2.http.*

interface TodoApiInterface {

    @POST("/hanchor/v1/users/{userId}/todos")
    suspend fun createTodo(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String,
        @Body todoRequest: TodoRequest
    ): Response<TodoRest>

    @GET("/hanchor/v1/users/{userId}/todos/{todoId}")
    suspend fun getTodo(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String,
        @Path("todoId") todoId: Long
    ): Response<TodoRest>

    @PUT("/hanchor/v1/users/{userId}/todos/{todoId}")
    suspend fun updateTodo(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String,
        @Path("todoId") todoId: Long,
        @Body todoRequest: TodoRequest
    ): Response<TodoRest>

    @GET("/hanchor/v1/users/{userId}/todos")
    suspend fun getTodos(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String,
        @Query("limit") limit: Int,
        @Query("page") page: Int
    ): Response<List<TodoRest>>

    @DELETE("/hanchor/v1/users/{userId}/todos/{todoId}")
    suspend fun deleteTodo(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String,
        @Path("todoId") todoId: Long
    ): Response<OperationResult>

}