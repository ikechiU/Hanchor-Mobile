package com.nextgendevs.hanchor.presentation.onboarding

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.nextgendevs.hanchor.R

class OnBoardingViewPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val context: Context
) :
    FragmentStateAdapter(fragmentActivity) {

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> OnBoardingFragment.newInstance(
                context.resources.getString(R.string.first_onboarding_ttle),
                context.resources.getString(R.string.first_onboarding_description),
                R.drawable.first_onboarding_image
            )
            1 -> OnBoardingFragment.newInstance(
                context.resources.getString(R.string.second_onboarding_ttle),
                context.resources.getString(R.string.second_onboarding_description),
                R.drawable.second_onboarding_image
            )
            else -> OnBoardingFragment.newInstance(
                context.resources.getString(R.string.third_onboarding_ttle),
                context.resources.getString(R.string.third_onboarding_description),
                R.drawable.third_onboarding_image
            )
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}