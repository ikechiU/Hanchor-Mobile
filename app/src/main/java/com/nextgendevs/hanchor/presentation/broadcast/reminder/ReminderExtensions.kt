package com.nextgendevs.hanchor.presentation.broadcast.reminder

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.util.Log
import com.nextgendevs.hanchor.presentation.utils.Constants
import com.nextgendevs.hanchor.presentation.utils.getBroadcastPendingIntent


private const val TAG = "ScheduleReminder"

fun Context.scheduleReminder(ids: Array<String?>, tasks: Array<String?>, times: Array<String?>, isCompleted: Array<String?>) {
    for (i in ids.indices) {
        if (!(isCompleted[i]!!.toBoolean())) {
            createPendingIntent(ids[i]!!.toLong(), tasks[i]!!, times[i]!!.toLong(), isCompleted[i]!!.toBoolean())
        }
    }
}

fun Context.scheduleReminder(id: Long, task: String, time: Long, isCompleted: Boolean) {
    createPendingIntent(id, task, time, isCompleted)
    Log.d(TAG, "start: createPendingIntent called")
}

private fun Context.createPendingIntent(id: Long, task: String, time: Long, isCompleted: Boolean) {
    val reminderIntent = Intent(this, ReminderReceiver::class.java)
    reminderIntent.action =  "$time $task"
    reminderIntent.putExtra(Constants.TODO_REMINDER_ID, id)
    reminderIntent.putExtra(Constants.TODO_REMINDER_TASK, task)
    reminderIntent.putExtra(Constants.TODO_REMINDER_TIME, time)
    reminderIntent.putExtra(Constants.TODO_REMINDER_IS_COMPLETED, isCompleted)

    val pendingIntent: PendingIntent? = getBroadcastPendingIntent(reminderIntent, Constants.PI_TODO_REMINDER)

    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    setAlarm(alarmManager, time, pendingIntent)
}

fun Context.deleteReminder(id: Long, task: String, time: Long, isCompleted: Boolean) {
    val reminderIntent = Intent(this, ReminderReceiver::class.java)
    reminderIntent.action =  "$time $task"
    reminderIntent.putExtra(Constants.TODO_REMINDER_ID, id)
    reminderIntent.putExtra(Constants.TODO_REMINDER_TASK, task)
    reminderIntent.putExtra(Constants.TODO_REMINDER_TIME, time)
    reminderIntent.putExtra(Constants.TODO_REMINDER_IS_COMPLETED, isCompleted)

    val pendingIntent: PendingIntent? = getBroadcastPendingIntent(reminderIntent, Constants.PI_TODO_REMINDER)

    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    pendingIntent?.cancel()
    alarmManager.cancel(pendingIntent)
}


fun Context.scheduleSnoozeReminder(nextReminderTime: Long, task: String, id: Long) {
    val reminderIntent = Intent(this, SnoozeReminderReceiver::class.java)
    reminderIntent.action =   "$nextReminderTime + $task:snooze-worker"
    reminderIntent.putExtra(Constants.SNOOZE_TODO_REMINDER_ID, id)
    reminderIntent.putExtra(Constants.SNOOZE_TODO_REMINDER_TASK, task)
    reminderIntent.putExtra(Constants.SNOOZE_TODO_REMINDER_TIME, nextReminderTime)

    val pendingIntent: PendingIntent? = getBroadcastPendingIntent(reminderIntent, Constants.PI_TODO_REMINDER_SNOOZE)

    val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
    setAlarm(alarmManager, nextReminderTime, pendingIntent)
    Log.d(TAG, "createPendingIntent: $alarmManager")
}

private fun setAlarm(alarmManager: AlarmManager, alarmTime: Long, alarmPendingIntent: PendingIntent?) {
    alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, alarmPendingIntent)
}
