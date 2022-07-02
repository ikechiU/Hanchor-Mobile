package com.nextgendevs.hanchor.business.datasource.network.response

import com.nextgendevs.hanchor.business.domain.models.Gratitude

class GratitudeRest(
    var id: Long = 0,
    var title: String = "",
    var message: String = "",
    var imageUrl: String = "",
    var imageId: String = "0"
)

fun GratitudeRest.toGratitude() : Gratitude {
    return Gratitude(
        id, title, message, imageUrl, imageId
    )
}
