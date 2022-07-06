package com.nextgendevs.hanchor.business.datasource.network.main.affirmation

import com.nextgendevs.hanchor.business.datasource.network.request.AffirmationRequest
import com.nextgendevs.hanchor.business.datasource.network.request.UpdateUserRequest
import com.nextgendevs.hanchor.business.datasource.network.response.AffirmationRest
import com.nextgendevs.hanchor.business.datasource.network.response.OperationResult
import com.nextgendevs.hanchor.business.datasource.network.response.UserRest
import retrofit2.Response
import retrofit2.http.*

interface AffirmationApiInterface {

    @POST("/hanchor/v1/users/{userId}/affirmations")
    suspend fun createAffirmation(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String,
        @Body affirmationRequest: AffirmationRequest
    ): Response<AffirmationRest>

    @PUT("/hanchor/v1/users/{userId}/affirmations/{affirmationId}")
    suspend fun updateAffirmation(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String,
        @Path("affirmationId") affirmationId: Long,
        @Body affirmationRequest: AffirmationRequest
    ): Response<AffirmationRest>

    @GET("/hanchor/v1/users/{userId}/affirmations/{affirmationId}")
    suspend fun getAffirmation(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String,
        @Path("affirmationId") affirmationId: Long
    ): Response<AffirmationRest>

    @GET("/hanchor/v1/users/{userId}/affirmations")
    suspend fun getAffirmations(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String
    ): Response<List<AffirmationRest>>

    @DELETE("/hanchor/v1/users/{userId}/affirmations/{affirmationId}")
    suspend fun deleteAffirmation(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String,
        @Path("affirmationId") affirmationId: Long
    ): Response<OperationResult>
}