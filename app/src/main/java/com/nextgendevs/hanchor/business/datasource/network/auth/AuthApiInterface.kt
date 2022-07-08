package com.nextgendevs.hanchor.business.datasource.network.auth

import com.nextgendevs.hanchor.business.datasource.network.request.LoginRequestModel
import com.nextgendevs.hanchor.business.datasource.network.request.PasswordResetRequestModel
import com.nextgendevs.hanchor.business.datasource.network.request.UserRequest
import com.nextgendevs.hanchor.business.datasource.network.response.OperationResult
import com.nextgendevs.hanchor.business.datasource.network.response.UserRest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiInterface {

    @POST("/hanchor/login")
    suspend fun login(@Body loginRequestModel: LoginRequestModel): Response<Void>

    @POST("/hanchor/v1/users")
    suspend fun register(@Body userRequest: UserRequest): Response<UserRest>

    @POST("/hanchor/password-reset-request")
    suspend fun forgotPassword(@Body passwordResetRequestModel: PasswordResetRequestModel): Response<OperationResult>

}