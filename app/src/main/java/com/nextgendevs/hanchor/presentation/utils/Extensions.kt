package com.nextgendevs.hanchor.presentation.utils

import android.app.*
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.provider.Settings
import android.util.Log
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.NotificationCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.afollestad.materialdialogs.MaterialDialog
import com.afollestad.materialdialogs.callbacks.onDismiss
import com.nextgendevs.hanchor.R
import com.nextgendevs.hanchor.presentation.auth.AuthActivity
import com.nextgendevs.hanchor.presentation.main.MainActivity
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


private const val TAG = "MyExtensionFunctions"

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

fun Activity.logoutUser(mySharedPreferences: MySharedPreferences) {
    mySharedPreferences.storeStringValue(Constants.AUTH_TOKEN, "")

    val intent = Intent(this, AuthActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
    finish()
}

fun Activity.changeStatusBarColor(color: Int, isLight: Boolean) {
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = color

    WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = isLight
}

fun Activity.setStatusBarGradiant(context: Context, drawable: Int) {
    val window: Window = window
    val background = AppCompatResources.getDrawable(context, drawable)
    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    window.statusBarColor = resources.getColor(android.R.color.transparent)
    window.navigationBarColor = resources.getColor(android.R.color.transparent)
    window.setBackgroundDrawable(background)
}

