package com.nextgendevs.hanchor.presentation.auth.viewmodel

import com.nextgendevs.hanchor.business.datasource.network.response.UserRest
import com.nextgendevs.hanchor.business.domain.utils.Queue
import com.nextgendevs.hanchor.business.domain.utils.StateMessage

data class AuthState (
    val isLoading: Boolean = false,
    val loginResult: String = "",
    val forgotPasswordResult: String = "",
    val userRestResult: UserRest = UserRest(
        userId = "",
        firstname = "",
        lastname = "",
        username = "",
        email = "",
        emailVerificationStatus = true,
        gratitudeRests = ArrayList(),
        quoteRests = ArrayList(),
        lifeHackRests = ArrayList(),
        todoRests = ArrayList(),
        timestamp = null,
        affirmationRests = ArrayList()
    ),
    val queue: Queue<StateMessage> = Queue(mutableListOf())
)
