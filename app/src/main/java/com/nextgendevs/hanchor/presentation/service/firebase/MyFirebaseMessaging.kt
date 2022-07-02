package com.nextgendevs.hanchor.presentation.service.firebase

import android.os.Handler
import android.os.Looper
import android.util.Log
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.nextgendevs.hanchor.presentation.utils.Constants
import com.nextgendevs.hanchor.presentation.utils.MySharedPreferences
import javax.inject.Inject

private val TAG = MyFirebaseMessaging::class.simpleName
class MyFirebaseMessaging @Inject constructor(private val firebaseMessaging: FirebaseMessaging) {
    @Inject lateinit var mySharedPreferences: MySharedPreferences
    private val handler = Handler(Looper.getMainLooper())

    fun subscribe() {
        firebaseMessaging.subscribeToTopic(Constants.SUBSCRIBE_TO_HANCHOR)
            .addOnCompleteListener { task ->
                var msg = "Subscribed"
                if (!task.isSuccessful) {
                    msg = "Subscription Failed"
                }
                Log.d(TAG, msg)
            }
    }

    fun firebaseTokenListener() {
        firebaseMessaging.token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }
            if (task.isSuccessful){
                handler.post {
                    mySharedPreferences.storeStringValue(Constants.FCM_TOKEN, task.result!!)
                }
            }
            // Get new FCM registration token  = task.result!!
            Log.d(TAG, "This is your token: ${task.result!!}")
        })
    }
}