package com.nextgendevs.hanchor.presentation.main.fragments

import androidx.fragment.app.Fragment
import com.bumptech.glide.RequestManager
import com.nextgendevs.hanchor.presentation.utils.MySharedPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
open class BaseFragment: Fragment() {
    @Inject lateinit var mySharedPreferences: MySharedPreferences

    val TAG = "AppDebug"
}