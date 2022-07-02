package com.nextgendevs.hanchor.business.usecase.auth.login

import com.nextgendevs.hanchor.business.datasource.network.request.LoginRequestModel

object Credential {

    fun signInCredentials(username: String, password: String): LoginRequestModel {
        val loginRequest =  LoginRequestModel()

        loginRequest.password = password

        if (username.contains('@')) {
            loginRequest.email = username
        } else {
            loginRequest.id = username
        }

        return loginRequest
    }


}