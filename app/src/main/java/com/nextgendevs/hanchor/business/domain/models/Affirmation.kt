package com.nextgendevs.hanchor.business.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Affirmation(
    val id: Long,
    val affirmationTitle: String,
    val affirmation: String
) : Parcelable