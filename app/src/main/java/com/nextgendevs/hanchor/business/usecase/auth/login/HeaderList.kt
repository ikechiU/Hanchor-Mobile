package com.nextgendevs.hanchor.business.usecase.auth.login

import okhttp3.Headers

object HeaderList {

    private const val AUTHORIZATION_HEADER_STRING =  "Authorization"
    private const val USER_ID =  "UserId"
    private const val FIRST_NAME =  "Firstname"
    private const val LAST_NAME =  "Lastname"
    private const val USERNAME =  "Username"
    private const val EMAIL =  "Email"

    fun authorizationHeader(headerList: Headers): String {
        return headerList[AUTHORIZATION_HEADER_STRING]!!
    }

    fun username(headerList: Headers): String {
        return headerList[USERNAME]!!
    }

    fun userId(headerList: Headers): String {
        return headerList[USER_ID]!!
    }

    fun firstName(headerList: Headers): String {
        return headerList[FIRST_NAME]!!
    }

    fun lastName(headerList: Headers): String {
        return headerList[LAST_NAME]!!
    }

    fun email(headerList: Headers): String {
        return headerList[EMAIL]!!
    }
}