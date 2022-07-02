package com.nextgendevs.hanchor.presentation.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.nextgendevs.hanchor.R
import com.nextgendevs.hanchor.databinding.ActivityOnBoardingBinding
import com.nextgendevs.hanchor.presentation.BaseActivity
import com.nextgendevs.hanchor.presentation.auth.AuthActivity
import com.nextgendevs.hanchor.presentation.utils.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity : BaseActivity() {

    private lateinit var binding: ActivityOnBoardingBinding
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityOnBoardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val isFirstTime = mySharedPreferences.getStoredBooleanDefaultTrue(Constants.FIRST_TIME_LOADING)
        if (!isFirstTime) {
            startActivity(Intent(this, AuthActivity::class.java))
        }

        viewPager = binding.viewPager

        viewPager.adapter = OnBoardingViewPagerAdapter(this, this)
        viewPager.offscreenPageLimit = 1

        val btnBack = binding.btnPreviousStep
        val btnNext = binding.btnNextStep

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (position == 2) {
                    binding.btnGetStarted.visibility = View.VISIBLE
                    binding.btnPreviousStep2.visibility = View.VISIBLE
                    btnNext.visibility = View.GONE
                    btnBack.visibility = View.GONE
                    btnNext.text = getText(R.string.finish)
                } else {
                    binding.btnGetStarted.visibility = View.GONE
                    binding.btnPreviousStep2.visibility = View.GONE
                    btnNext.visibility = View.VISIBLE
                    btnBack.visibility = View.VISIBLE
                    btnNext.text = getText(R.string.next)
                }
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(arg0: Int) {}
        })

        TabLayoutMediator(binding.pageIndicator, viewPager) { _, _ -> }.attach()

        btnNext.setOnClickListener {
            if (getItem() > viewPager.childCount) {
                val intent = Intent(this, AuthActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            } else {
                viewPager.setCurrentItem(getItem() + 1, true)
            }
        }

        binding.btnGetStarted.setOnClickListener{
            val intent = Intent(this, AuthActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }

        binding.btnPreviousStep2.setOnClickListener {
            if (getItem() == 0) {
                finish()
            } else {
                viewPager.setCurrentItem(getItem() - 1, true)
            }
        }

        btnBack.setOnClickListener {
            if (getItem() == 0) {
                finish()
            } else {
                viewPager.setCurrentItem(getItem() - 1, true)
            }
        }
    }

    override fun displayProgressBar(isLoading: Boolean) {

    }

    private fun getItem(): Int {
        return viewPager.currentItem
    }
}