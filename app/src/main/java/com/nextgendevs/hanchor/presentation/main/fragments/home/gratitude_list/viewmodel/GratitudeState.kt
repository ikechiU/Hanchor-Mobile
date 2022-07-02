package com.nextgendevs.hanchor.presentation.main.fragments.home.gratitude_list.viewmodel

import com.nextgendevs.hanchor.business.domain.models.Gratitude
import com.nextgendevs.hanchor.business.domain.utils.Queue
import com.nextgendevs.hanchor.business.domain.utils.StateMessage

data class GratitudeState (
    val isLoading: Boolean = false,
    val insertResult: Long = 0,
    val updateResult: Int = 0,
    val deleteResult: Int = 0,
    val page: Int = 1,
    val isQueryExhausted: Boolean = false,
    val tokenExpired: Boolean = false,
    val gratitude: Gratitude = Gratitude(-1L, "", "", "", "1"),
    val gratitudeList: List<Gratitude> = emptyList(),
    val queue: Queue<StateMessage> = Queue(mutableListOf())
)
