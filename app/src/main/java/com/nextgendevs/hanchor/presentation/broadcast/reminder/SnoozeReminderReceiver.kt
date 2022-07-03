package com.nextgendevs.hanchor.presentation.broadcast.reminder

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.nextgendevs.hanchor.presentation.reminder.ReminderActivity
import com.nextgendevs.hanchor.presentation.utils.Constants
import com.nextgendevs.hanchor.presentation.utils.MySharedPreferences
import com.nextgendevs.hanchor.presentation.utils.getActivityPendingIntent
import com.nextgendevs.hanchor.presentation.utils.setNotification
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private const val TAG = "AppDebug"

@AndroidEntryPoint
class SnoozeReminderReceiver: BroadcastReceiver() {
    @Inject lateinit var mySharedPreferences: MySharedPreferences

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {

            if (intent.hasExtra(Constants.SNOOZE_TODO_REMINDER_TIME)) {
                val id = intent.getLongExtra(Constants.SNOOZE_TODO_REMINDER_ID, 0)
                val task = intent.getStringExtra(Constants.SNOOZE_TODO_REMINDER_TASK)!!
                val nextReminderTime = intent.getLongExtra(Constants.SNOOZE_TODO_REMINDER_TIME, System.currentTimeMillis() + 10000)

                val reminderIntent = Intent(context, ReminderActivity::class.java)
                reminderIntent.action =   "$nextReminderTime + $task:snooze-broadcast"
                reminderIntent.putExtra(Constants.SNOOZE_TODO_REMINDER_ID, id)
                reminderIntent.putExtra(Constants.SNOOZE_TODO_REMINDER_TASK, task)
                reminderIntent.putExtra(Constants.SNOOZE_TODO_REMINDER_TIME, nextReminderTime)

                val pendingIntent: PendingIntent? = context?.getActivityPendingIntent(reminderIntent, Constants.PI_SNOOZE_REMINDER_BROADCAST)

                if (pendingIntent != null) {
                    context.setNotification(pendingIntent, "TODO (Snooze)", task)
                }
            }
        }
    }

}