package com.nextgendevs.hanchor.business.datasource.cache.main.gratitude

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nextgendevs.hanchor.business.domain.models.Gratitude

private const val GRATITUDE_TABLE = "gratitude_table"
private const val GRATITUDE_ID = "_id"
private const val GRATITUDE_TITLE = "gratitude_title"
private const val GRATITUDE_MESSAGE = "gratitude_message"
private const val GRATITUDE_IMAGE_URL = "gratitude_image_url"
private const val GRATITUDE_IMAGE_ID = "gratitude_image_id"

@Entity(tableName = GRATITUDE_TABLE)
data class GratitudeEntity constructor(

    @ColumnInfo(name = GRATITUDE_ID)
    @PrimaryKey(autoGenerate = false)
    var id: Long = 0,

    @ColumnInfo(name = GRATITUDE_TITLE)
    var gratitudeTitle: String = "",

    @ColumnInfo(name = GRATITUDE_MESSAGE)
    var gratitudeMessage: String = "",

    @ColumnInfo(name = GRATITUDE_IMAGE_URL)
    var gratitudeImageUrl: String = "",

    @ColumnInfo(name = GRATITUDE_IMAGE_ID)
    var gratitudeImageId: String = "0"
)

fun GratitudeEntity.toGratitude(): Gratitude {
    return Gratitude(
        id, gratitudeTitle, gratitudeMessage, gratitudeImageUrl, gratitudeImageId
    )
}

fun Gratitude.toGratitudeEntity(): GratitudeEntity {
    return GratitudeEntity(
        id, gratitudeTitle, gratitudeMessage, gratitudeImageUrl, gratitudeImageId
    )
}
