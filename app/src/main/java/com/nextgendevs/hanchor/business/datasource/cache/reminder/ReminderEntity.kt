//package com.nextgendevs.hanchor.business.datasource.cache.reminder
//
//import androidx.room.ColumnInfo
//import androidx.room.Entity
//import androidx.room.PrimaryKey
//import com.nextgendevs.hanchor.business.domain.models.Reminder
//
//@Entity(tableName = Constants.REMINDER_TABLE)
//data class ReminderEntity @JvmOverloads constructor(
//
//    @ColumnInfo(name = Constants.REMINDER_ID)
//    @PrimaryKey(autoGenerate = true)
//    var id: Int = 0,
//
//    @ColumnInfo(name = Constants.REMINDER_TITLE)
//    var title: String = "",
//
//    @ColumnInfo(name = Constants.REMINDER_TIME)
//    var reminderTime: Long = 0L,
//
//    @ColumnInfo(name = Constants.REMINDER_DATE_CALENDAR)
//    var dateCalendar: String = "",
//
//    @ColumnInfo(name = Constants.REMINDER_TIME_CALENDAR)
//    var timeCalendar: String = "",
//
//    @ColumnInfo(name = Constants.REMINDER_HAS_TIME_PAST)
//    var hasTimePast: Boolean = false,
//
//    @ColumnInfo(name = Constants.REMINDER_IS_ALARM_DONE)
//    var isReminderDone: Boolean = false
//)
//
//fun ReminderEntity.toReminder(): Reminder {
//    return Reminder(
//        id, title, reminderTime, dateCalendar, timeCalendar, hasTimePast, isReminderDone
//    )
//}
//
//fun Reminder.toReminderEntity(): ReminderEntity {
//    return ReminderEntity(
//        id, title, reminderTime, dateCalendar, timeCalendar, hasTimePast, isReminderDone
//    )
//}