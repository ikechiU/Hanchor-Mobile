package com.nextgendevs.hanchor.business.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Gratitude(
    val id: Long,
    val gratitudeTitle: String,
    val gratitudeMessage: String,
    val gratitudeImageUrl: String,
    val gratitudeImageId: String,
) : Parcelable