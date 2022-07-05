package com.nextgendevs.hanchor.presentation.main

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.nextgendevs.hanchor.R
import com.nextgendevs.hanchor.databinding.ActivityMainBinding
import com.nextgendevs.hanchor.presentation.BaseActivity
import com.nextgendevs.hanchor.presentation.MainViewModel
import com.nextgendevs.hanchor.presentation.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "AppDebug"

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bottomNavigationView: BottomNavigationView
    private val viewModel: MainViewModel by viewModels()
    private var quote: String? = null
    private var lifeHack: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initFCM()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragments_container) as NavHostFragment
        navController = navHostFragment.navController
        setupBottomNavigationView()
    }

    private val messageReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent) {

            if (intent.hasExtra(Constants.FCM_LINK_KEY)) {
                quote = intent.getStringExtra(Constants.FCM_BODY_KEY)!!
                lifeHack = intent.getStringExtra(Constants.FCM_LINK_KEY)!!
                navController.navigate(R.id.nav_home)
            }

        }
    }

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(messageReceiver, IntentFilter(Constants.LOCAL_BROADCAST_INTENT))
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver)
    }

    fun getQuote() = quote
    fun getLifeHack() = lifeHack

    override fun displayProgressBar(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun initFCM() {
        FirebaseApp.initializeApp(this)
        subscribeUserToHanchor()
        getFirebaseUserToken()

        val token = mySharedPreferences.getStoredString(Constants.FCM_TOKEN)
        Log.d(TAG, "init: Token is: $token")
    }

    private fun subscribeUserToHanchor() {
        myFirebaseMessaging.subscribe()
    }

    private fun getFirebaseUserToken() {
        myFirebaseMessaging.firebaseTokenListener()
    }

    private fun setupBottomNavigationView() {
        bottomNavigationView = findViewById(R.id.bottom_navigation_view)
        bottomNavigationView.setupWithNavController(navController)
    }
}