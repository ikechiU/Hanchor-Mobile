package com.nextgendevs.hanchor.business.datasource.network.response

import com.nextgendevs.hanchor.business.domain.models.Gratitude

class GratitudeRest(
    val id: Long,
    val title: String,
    val message: String,
    val imageUrl: String,
    val imageId: String
)

fun GratitudeRest.toGratitude() : Gratitude {
    return Gratitude(
        id, title, message, imageUrl, imageId
    )
}
