package com.nextgendevs.hanchor.presentation.main

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.FirebaseApp
import com.nextgendevs.hanchor.R
import com.nextgendevs.hanchor.business.domain.utils.StateMessageCallback
import com.nextgendevs.hanchor.databinding.ActivityMainBinding
import com.nextgendevs.hanchor.presentation.BaseActivity
import com.nextgendevs.hanchor.presentation.MainViewModel
import com.nextgendevs.hanchor.presentation.utils.Constants
import com.nextgendevs.hanchor.presentation.utils.processQueue
import dagger.hilt.android.AndroidEntryPoint

private const val TAG = "AppDebug"

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var bottomNavigationView: BottomNavigationView
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        when {
            intent.hasExtra(Constants.FCM_LINK_KEY) -> {
                val intent = this.intent
                if (intent != null) {
                    if (intent.getStringExtra(Constants.FCM_BODY_KEY) != null) {
                        if (intent.getStringExtra(Constants.FCM_LINK_KEY) != null) {
                            if(intent.getStringExtra(Constants.FCM_LINK_KEY) != "") {
                                val data = intent.getStringExtra(Constants.FCM_LINK_KEY)!!
                                val body = intent.getStringExtra(Constants.FCM_BODY_KEY)!!
                                val title = intent.getStringExtra(Constants.FCM_TITLE_KEY)!!

                                Log.d(TAG, "data is: $data")
                                Log.d(TAG, "body is: $body")
                                Log.d(TAG, "title is: $title")

                                Handler(Looper.getMainLooper()).post {
                                    Toast.makeText(this, body, Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                    }
                }
            }
        }

        initFCM()

        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.main_fragments_container) as NavHostFragment
        navController = navHostFragment.navController

        setupBottomNavigationView()

//        lifecycleScope.launch {
//            appDataStoreManager.readValue(DataStoreKeys.USER)?.let { user ->
//                Log.d(TAG, "onCreate: display $user")
//            }
//        }
    }

    override fun displayProgressBar(isLoading: Boolean) {
        if(isLoading){
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