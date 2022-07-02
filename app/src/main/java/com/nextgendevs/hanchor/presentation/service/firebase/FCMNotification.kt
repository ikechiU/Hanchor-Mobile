package com.nextgendevs.hanchor.presentation.service.firebase

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import com.nextgendevs.hanchor.presentation.main.MainActivity
import com.nextgendevs.hanchor.presentation.utils.Constants
import com.nextgendevs.hanchor.presentation.utils.MySharedPreferences
import com.nextgendevs.hanchor.presentation.utils.setNotification
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject


class FCMNotification @Inject constructor(@ApplicationContext val context: Context) {

    fun setFCMNotification(title: String, body: String, link: String) {
        val notificationIntent = Intent(context, MainActivity::class.java)
        notificationIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        notificationIntent.putExtra(Constants.FCM_TITLE_KEY, title)
        notificationIntent.putExtra(Constants.FCM_BODY_KEY, body)
        notificationIntent.putExtra(Constants.FCM_LINK_KEY, link)

        val pendingIntent: PendingIntent? = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(context, Constants.FCM_NOTIFICATION, notificationIntent, PendingIntent.FLAG_MUTABLE)
        } else {
            PendingIntent.getActivity(context, Constants.FCM_NOTIFICATION, notificationIntent, 0)
        }
        if (pendingIntent != null) {
            context.setNotification(pendingIntent, title, body)
        }
    }
}