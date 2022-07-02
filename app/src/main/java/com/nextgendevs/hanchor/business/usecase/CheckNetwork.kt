package com.nextgendevs.hanchor.business.usecase

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager

class CheckNetwork(val context: Application) {
    private val conMgr =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val isInternetAvailable = conMgr.activeNetworkInfo != null &&
            conMgr.activeNetworkInfo!!.isAvailable && conMgr.activeNetworkInfo!!.isConnected
}