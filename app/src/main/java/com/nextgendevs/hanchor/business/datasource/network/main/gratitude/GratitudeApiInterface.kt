package com.nextgendevs.hanchor.business.datasource.network.main.gratitude

import com.nextgendevs.hanchor.business.datasource.network.response.GratitudeRest
import com.nextgendevs.hanchor.business.datasource.network.response.OperationResult
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.*

interface GratitudeApiInterface {

    @Multipart
    @POST("/hanchor/v1/users/{userId}/gratitude")
    suspend fun createGratitude(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String,
        @Part("title") title: RequestBody,
        @Part("message") message: RequestBody,
        @Part file: MultipartBody.Part?
    ): Response<GratitudeRest>

    @GET("/hanchor/v1/users/{userId}/todos/{gratitudeId}")
    suspend fun getGratitude(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String,
        @Path("gratitudeId") gratitudeId: Long
    ): Response<GratitudeRest>

    @Multipart
    @PUT("/hanchor/v1/users/{userId}/gratitude/{gratitudeId}")
    suspend fun updateGratitude(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String,
        @Path("gratitudeId") gratitudeId: Long,
        @Part("title") title: RequestBody,
        @Part("message") message: RequestBody,
        @Part("imageId") imageId: RequestBody,
        @Part file: MultipartBody.Part?
    ): Response<GratitudeRest>

    ///hanchor/v1/users/{userId}/gratitude
    @GET("/hanchor/v1/users/{userId}/gratitude")
    suspend fun getGratitudes(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String,
        @Query("page") page: Int,
        @Query("limit") limit: Int
    ): Response<List<GratitudeRest>>

    //@DELETE("/hanchor/v1/users/{userId}/gratitude/{gratitudeId}")
    @FormUrlEncoded
    @HTTP(method = "DELETE", path = "/hanchor/v1/users/{userId}/gratitude/{gratitudeId}", hasBody = true)
    suspend fun deleteGratitude(
        @Header("Authorization") authorization: String,
        @Path("userId") userId: String,
        @Path("gratitudeId") gratitudeId: Long,
        @Field("imageId") imageId: String,
    ): Response<OperationResult>

}