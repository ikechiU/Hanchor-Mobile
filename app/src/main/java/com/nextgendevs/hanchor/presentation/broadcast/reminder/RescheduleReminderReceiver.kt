package com.nextgendevs.hanchor.presentation.broadcast.reminder

import android.app.AlarmManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.nextgendevs.hanchor.presentation.service.RescheduleReminderService
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "AppDebug"

@AndroidEntryPoint
class RescheduleReminderReceiver : BroadcastReceiver() {

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            if (intent.action == "android.intent.action.BOOT_COMPLETED" || intent.action == AlarmManager.ACTION_SCHEDULE_EXACT_ALARM_PERMISSION_STATE_CHANGED) {
                val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager

                if (Build.VERSION.SDK_INT >= 31) {
                    if (alarmManager.canScheduleExactAlarms()) {
                        Log.d(TAG, "onReceive: RescheduleReminderReceiver")
                        startRescheduleReminderService(context)
                    }
                } else {
                    Log.d(TAG, "onReceive: RescheduleReminderReceiver")
                    startRescheduleReminderService(context)
                }
            }
        }
    }

    private fun startRescheduleReminderService(context: Context) {
        val intentService = Intent(context, RescheduleReminderService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(context, intentService)
        } else {
            context.startService(intentService)
        }
    }
}