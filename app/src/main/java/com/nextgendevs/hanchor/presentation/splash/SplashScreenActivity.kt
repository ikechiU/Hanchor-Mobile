package com.nextgendevs.hanchor.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.nextgendevs.hanchor.R
import com.nextgendevs.hanchor.databinding.ActivitySplashScreenBinding
import com.nextgendevs.hanchor.presentation.BaseActivity
import com.nextgendevs.hanchor.presentation.auth.AuthActivity
import com.nextgendevs.hanchor.presentation.main.MainActivity
import com.nextgendevs.hanchor.presentation.onboarding.OnBoardingActivity
import com.nextgendevs.hanchor.presentation.reminder.ReminderActivity
import com.nextgendevs.hanchor.presentation.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashScreenActivity : BaseActivity() {

    private lateinit var binding : ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.SplashTheme)
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        when {
            intent.hasExtra(Constants.FCM_LINK_KEY) -> {
                startActivity(Intent(this, MainActivity::class.java))
            }
            intent.hasExtra(Constants.TODO_REMINDER_TIME) -> {
                startActivity(Intent(this, ReminderActivity::class.java))
            }
            intent.hasExtra(Constants.SNOOZE_TODO_REMINDER_TIME) -> {
                startActivity(Intent(this, ReminderActivity::class.java))
            }
        }

        val isFirstTime = mySharedPreferences.getStoredBooleanDefaultTrue(Constants.FIRST_TIME_LOADING)
        val authToken = mySharedPreferences.getStoredString(Constants.AUTH_TOKEN)

        Handler(Looper.getMainLooper()).postDelayed({
            if (!isFirstTime) {
                if (authToken == "") startActivity(Intent(this, AuthActivity::class.java))
                else startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, OnBoardingActivity::class.java))
            }
            finish()
        }, 1500)
    }

    override fun displayProgressBar(isLoading: Boolean) {

    }
}