package com.nextgendevs.hanchor.business.datasource.network.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class OperationResult(

    @Expose
    @SerializedName("operationResult")
    val operationResult: String,

    @Expose
    @SerializedName("operationName")
    val operationName: String

)