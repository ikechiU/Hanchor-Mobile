package com.nextgendevs.hanchor.presentation

import android.content.Context
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.AppCompatActivity
import com.nextgendevs.hanchor.business.datasource.cache.datastore.AppDataStoreManager
import com.nextgendevs.hanchor.presentation.service.firebase.MyFirebaseMessaging
import com.nextgendevs.hanchor.presentation.utils.MySharedPreferences
import javax.inject.Inject

abstract class BaseActivity: AppCompatActivity(), UICommunicationListener {
    @Inject lateinit var mySharedPreferences: MySharedPreferences
    @Inject lateinit var myFirebaseMessaging: MyFirebaseMessaging
    @Inject lateinit var appDataStoreManager: AppDataStoreManager

    val TAG = "AppDebug"

    override fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(
                Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager
                .hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

}