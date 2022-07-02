package com.nextgendevs.hanchor.presentation.service.firebase

import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.nextgendevs.hanchor.presentation.utils.MySharedPreferences
import com.nextgendevs.hanchor.presentation.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

private val TAG = MyFirebaseMessagingService::class.simpleName

@AndroidEntryPoint
class MyFirebaseMessagingService : FirebaseMessagingService() {

    private val processLater = false
    @Inject lateinit var mySharedPreferences: MySharedPreferences
    @Inject lateinit var mFCMNotification: FCMNotification
    private val handler = Handler(Looper.getMainLooper())

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "onCreate: MyFirebaseMessagingService $mySharedPreferences")
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
        handler.post {
            mySharedPreferences.storeStringValue(Constants.FCM_TOKEN, token)
        }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "From: ${remoteMessage.from}")

        if (processLater) {/* Check if data needs to be processed by long running job */
            //scheduleJob()
            Log.d(TAG, "executing schedule job")
        } else {// Handle message within 10 seconds
            handleNow(remoteMessage)
        }
    }

    private fun handleNow(remoteMessage: RemoteMessage) {

        val title = remoteMessage.data[Constants.FCM_TITLE_KEY]!!
        val body = remoteMessage.data[Constants.FCM_BODY_KEY]!!
        val link = remoteMessage.data[Constants.FCM_LINK_KEY]!!

        Log.d(TAG, "handleNow: $title")
        Log.d(TAG, "handleNow: $body")
        Log.d(TAG, "handleNow: $link")
        handler.post {
            Toast.makeText(this, body, Toast.LENGTH_SHORT).show()
            mFCMNotification.setFCMNotification(title, body, link)
        }

    }

}