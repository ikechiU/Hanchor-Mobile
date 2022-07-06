package com.nextgendevs.hanchor.presentation.main.fragments.home.affirmations.display_affirmation.details.viewmodel

import com.nextgendevs.hanchor.business.domain.models.Affirmation
import com.nextgendevs.hanchor.business.domain.utils.Queue
import com.nextgendevs.hanchor.business.domain.utils.StateMessage

data class AffirmationState (
    val isLoading: Boolean = false,
    val insertResult: Long = 0,
    val updateResult: Int = 0,
    val deleteResult: Int = 0,
    val page: Int = 1,
    val isQueryExhausted: Boolean = false,
    val tokenExpired: Boolean = false,
    val affirmation: Affirmation = Affirmation(-1L, "", ""),
    val affirmationList: List<Affirmation> = emptyList(),
    val queue: Queue<StateMessage> = Queue(mutableListOf())
)
