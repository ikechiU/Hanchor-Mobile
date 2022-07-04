package com.nextgendevs.hanchor.business.datasource.cache.main.affirmation

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.nextgendevs.hanchor.business.domain.models.Affirmation

private const val AFFIRMATION_TABLE = "affirmation_table"
private const val AFFIRMATION_ID = "_id"
private const val AFFIRMATION_TITLE = "affirmation_title"
private const val AFFIRMATION = "affirmation"

@Entity(tableName = AFFIRMATION_TABLE)
data class AffirmationEntity constructor(

    @ColumnInfo(name = AFFIRMATION_ID)
    @PrimaryKey(autoGenerate = false)
    val id: Long,

    @ColumnInfo(name = AFFIRMATION_TITLE)
    val affirmationTitle: String,

    @ColumnInfo(name = AFFIRMATION)
    val affirmation: String,
)

fun AffirmationEntity.toAffirmation(): Affirmation {
    return Affirmation(
        id, affirmationTitle, affirmation
    )
}

fun Affirmation.toAffirmationEntity(): AffirmationEntity {
    return AffirmationEntity(
        id, affirmationTitle, affirmation
    )
}
