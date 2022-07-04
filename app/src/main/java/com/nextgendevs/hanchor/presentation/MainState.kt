package com.nextgendevs.hanchor.presentation

import com.nextgendevs.hanchor.business.domain.models.Affirmation
import com.nextgendevs.hanchor.business.domain.utils.Queue
import com.nextgendevs.hanchor.business.domain.utils.StateMessage

data class MainState (
    val isLoading: Boolean = false,
    val updateResult: String = "",
    val deleteResult: Int = 0,
    val affirmationMessages: List<String> = emptyList(),
    val affirmations: List<Affirmation> = emptyList(),
    val authToken: String = "",
    val errorMessage: String = "",
    val tokenExpired: Boolean = false,
    val queue: Queue<StateMessage> = Queue(mutableListOf())
)