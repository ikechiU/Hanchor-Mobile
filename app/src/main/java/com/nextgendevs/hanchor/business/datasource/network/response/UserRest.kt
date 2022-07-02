package com.nextgendevs.hanchor.business.datasource.network.response

import java.sql.Timestamp

class UserRest(
    var userId: String? = null,
    var firstname: String? = null,
    var lastname: String? = null,
    var username: String? = null,
    var email: String? = null,
    var emailVerificationStatus: Boolean = false,
    var gratitudeRests: List<GratitudeRest>? = null,
    var quoteRests: List<QuoteRest>? = null,
    var lifeHackRests: List<LifeHackRest>? = null,
    var affirmationRests: List<AffirmationRest>? = null,
    var todoRests: List<TodoRest>? = null,
    var timestamp: Timestamp? = null
)
