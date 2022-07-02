package com.nextgendevs.hanchor.business.datasource.network.main

import com.nextgendevs.hanchor.business.datasource.network.request.UpdateUserRequest
import com.nextgendevs.hanchor.business.datasource.network.response.UserRest
import retrofit2.Response
import retrofit2.http.*

interface MainApiInterface {

    @PUT("/hanchor/v1/users/{userId}")
    suspend fun updateUser(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String,
        @Body updateUserRequest: UpdateUserRequest
    ): Response<UserRest>


}