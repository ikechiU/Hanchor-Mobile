package com.nextgendevs.hanchor.presentation.service.firebase

import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.localbroadcastmanager.content.LocalBroadcastManager
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
    private var broadcaster: LocalBroadcastManager? = null

    override fun onCreate() {
        super.onCreate()
        broadcaster = LocalBroadcastManager.getInstance(this)
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

        handler.post {
            mySharedPreferences.storeStringValue(Constants.FCM_QUOTE_OF_THE_DAY, body)
            mySharedPreferences.storeStringValue(Constants.FCM_LIFE_HACK, link)

            mFCMNotification.setFCMNotification(title, body, link)

            val intent = Intent(Constants.LOCAL_BROADCAST_INTENT)
            intent.putExtra(Constants.FCM_TITLE_KEY, title)
            intent.putExtra(Constants.FCM_BODY_KEY, body)
            intent.putExtra(Constants.FCM_LINK_KEY, link)
            broadcaster?.sendBroadcast(intent)
        }

    }

}