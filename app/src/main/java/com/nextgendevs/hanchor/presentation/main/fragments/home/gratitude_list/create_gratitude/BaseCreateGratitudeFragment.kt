package com.nextgendevs.hanchor.presentation.main.fragments.home.gratitude_list.create_gratitude

import android.content.Context
import android.util.Log
import com.nextgendevs.hanchor.presentation.UICommunicationListener
import com.nextgendevs.hanchor.presentation.main.fragments.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
abstract class BaseCreateGratitudeFragment : BaseFragment(){

    lateinit var uiCommunicationListener: UICommunicationListener
    lateinit var getContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)

        getContext = context

        try{
            uiCommunicationListener = context as UICommunicationListener
        }catch(e: ClassCastException){
            Log.e(TAG, "$context must implement UICommunicationListener" )
        }
    }

}
