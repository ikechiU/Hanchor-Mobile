package com.nextgendevs.hanchor.presentation.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.nextgendevs.hanchor.R
import com.nextgendevs.hanchor.presentation.main.MainActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


private const val TAG = "MyExtensionFunctions"

fun calculateTimeFormatLong(day: Int, month: Int, year: Int, hour: Int, minute: Int): Long {
    val dateString = "${day}-$month-${year} ${hour}:${minute}:00"
    Log.d(TAG, "start: date string is: $dateString")
    val sdf = SimpleDateFormat("dd-M-yyyy HH:mm:ss")
    val cal = Calendar.getInstance()
    try {
        val date: Date = sdf.parse(dateString)!!
        Log.d(TAG, "start: date is: $date")
        cal.time = date
    } catch (e: ParseException) {
        e.printStackTrace()
    }

    return cal.timeInMillis
}

fun Context.setNotification(
    pendingIntent: PendingIntent,
    title: String,
    body: String
) {
    val notificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(notificationChannel)
    }

//    val ringtoneSound =
//        Uri.parse("android.resource://com.nextgendevs.hanchor/raw/R.raw.aviscovery")
    val defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

    val notificationBuilder: NotificationCompat.Builder =
        NotificationCompat.Builder(applicationContext, Constants.NOTIFICATION_CHANNEL_ID)
            .setContentTitle(title)
            .setContentText(body)
            .setSound(defaultSoundUri)
            .setSmallIcon(android.R.drawable.star_on)
            .setColor(applicationContext.resources.getColor(R.color.primary_color_light))
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)
    notificationManager.notify(System.currentTimeMillis().toInt(), notificationBuilder.build())
}

fun Context.createNotification(
    title: String, text: String, pendingIntent: PendingIntent,
    classname: String
): Notification {
    val notificationManager =
        getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val notificationChannel = NotificationChannel(
            Constants.NOTIFICATION_CHANNEL_ID,
            Constants.NOTIFICATION_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(notificationChannel)
    }

    val flag =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) PendingIntent.FLAG_MUTABLE else PendingIntent.FLAG_UPDATE_CURRENT

    val stopSelf = Intent(this, getKlass(classname))
    stopSelf.action = Constants.STOP_SELF_SERVICE_KEY
    val pStopSelf = PendingIntent.getService(this, 0, stopSelf, flag)

    return NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
        .setContentTitle(title)
        .setContentText(text)
        .setSmallIcon(android.R.drawable.star_on)
        .setColor(applicationContext.resources.getColor(R.color.primary_color_light))
        .setAutoCancel(true)
        .setFullScreenIntent(pendingIntent, true)
        .setContentIntent(pendingIntent)
        .addAction(android.R.drawable.ic_menu_close_clear_cancel, "Stop Alarm", pStopSelf)
        .build()
}

private fun getKlass(classname: String): Class<*> {
    return when (classname) {
        "MorningAlarmForegroundService" -> {
            MainActivity::class.java
        }
//        "SnoozeMorningAlarmForegroundService" -> {
//            SnoozeMorningAlarmForegroundService::class.java
//        }
//        "NightAlarmForegroundService" -> {
//            NightAlarmForegroundService::class.java
//        }
        else -> {
            MainActivity::class.java
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
fun Context.confirmSchedulePermission() {
    MaterialDialog(this)
        .show {
            cornerRadius(10F)
            title(R.string.text_info)
            message(R.string.schedule_exact_alarms)
            negativeButton(R.string.return_to_page) {
                dismiss()
            }
            positiveButton(R.string.settings) {
                val intent = Intent().apply {
                    action = Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM
                }
                startActivity(intent)
            }
            onDismiss {
            }
            cancelable(false)
        }
}

fun nextAlarmTime(time: Long): String {
    var uptime = time - System.currentTimeMillis()

    val days: Long = TimeUnit.MILLISECONDS.toDays(uptime)
    uptime -= TimeUnit.DAYS.toMillis(days)

    val hours: Long = TimeUnit.MILLISECONDS.toHours(uptime)
    uptime -= TimeUnit.HOURS.toMillis(hours)

    val minutes: Long = TimeUnit.MILLISECONDS.toMinutes(uptime)
    uptime -= TimeUnit.MINUTES.toMillis(minutes)

    val seconds: Long = TimeUnit.MILLISECONDS.toSeconds(uptime)

    return "Alarm will come up in $days days, $hours hours, $minutes minutes and $seconds seconds."
}

fun setAlarmTime(hourOfDay: Int, minute: Int): Long {
    val cal = Calendar.getInstance()

    val year = cal.get(Calendar.YEAR)
    val month = cal.get(Calendar.MONTH)
    val dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)

    val newCal = Calendar.getInstance()
    newCal.set(year, month, dayOfMonth, hourOfDay, minute, 0)
    newCal.set(Calendar.MILLISECOND, 0)
    var alarmTime = newCal.timeInMillis

    if (System.currentTimeMillis() > alarmTime) {
        newCal.add(Calendar.DAY_OF_MONTH, +1)

        alarmTime = newCal.timeInMillis
    }

    return alarmTime
}

fun Context.getActivityPendingIntent(alarmIntent: Intent, requestCode: Int): PendingIntent? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getActivity(
            applicationContext,
            requestCode,
            alarmIntent,
            PendingIntent.FLAG_MUTABLE
        )
    } else {
        PendingIntent.getActivity(
            applicationContext,
            requestCode,
            alarmIntent,
            0
        )
    }
}

fun Context.getBroadcastPendingIntent(alarmIntent: Intent, requestCode: Int): PendingIntent? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        PendingIntent.getBroadcast(
            applicationContext,
            requestCode,
            alarmIntent,
            PendingIntent.FLAG_MUTABLE
        )
    } else {
        PendingIntent.getBroadcast(
            applicationContext,
            requestCode,
            alarmIntent,
            0
        )
    }
}

fun setAlarm(alarmManager: AlarmManager, alarmTime: Long, alarmPendingIntent: PendingIntent?) {
    if (Build.VERSION.SDK_INT >= 23) {
        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmTime, alarmPendingIntent)
    } else
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, alarmPendingIntent)
}

