package com.nextgendevs.hanchor.business.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Todo(
    val id: Long,
    val todoTitle: String,
    val todoTask: String,
    val todoDate: Long,
    val todoIsCompleted: Boolean = false
) : Parcelable