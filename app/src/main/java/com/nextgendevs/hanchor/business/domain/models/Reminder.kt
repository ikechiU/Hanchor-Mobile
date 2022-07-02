package com.nextgendevs.hanchor.business.domain.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Reminder(
    var id: Int,
    var title: String,
    var reminderTime: Long,
    var dateCalendar: String,
    var timeCalendar: String,
    var hasTimePast: Boolean,
    var isReminderDone: Boolean
) : Parcelable
