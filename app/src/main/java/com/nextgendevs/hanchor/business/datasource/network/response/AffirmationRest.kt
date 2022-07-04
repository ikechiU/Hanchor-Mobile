package com.nextgendevs.hanchor.business.datasource.network.response

import com.nextgendevs.hanchor.business.domain.models.Affirmation

class AffirmationRest (
    val id: Long,
    val title: String,
    val affirmation: String
)

fun AffirmationRest.toAffirmation() : Affirmation {
    return Affirmation(id, title, affirmation)
}
