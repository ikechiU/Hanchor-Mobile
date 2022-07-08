package com.nextgendevs.hanchor.presentation.main.fragments.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.nextgendevs.hanchor.R
import com.nextgendevs.hanchor.business.domain.utils.StateMessageCallback
import com.nextgendevs.hanchor.databinding.FragmentHomeBinding
import com.nextgendevs.hanchor.presentation.MainViewModel
import com.nextgendevs.hanchor.presentation.main.MainActivity
import com.nextgendevs.hanchor.presentation.utils.*
import kotlin.text.Typography.quote

class HomeFragment : BaseHomeFragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.setStatusBarGradiant(getContext, R.drawable.ic_white_bkgrd)
        uiCommunicationListener.displayProgressBar(false)

        if (mySharedPreferences.getStoredString(Constants.FCM_QUOTE_OF_THE_DAY) == "") {
            mySharedPreferences.storeStringValue(Constants.FCM_QUOTE_OF_THE_DAY, getString(R.string.default_quote))
        }
        if (mySharedPreferences.getStoredString(Constants.FCM_LIFE_HACK) == "") {
            mySharedPreferences.storeStringValue(Constants.FCM_LIFE_HACK, getString(R.string.default_life_hack))
        }

        Log.d(TAG, "onViewCreated: stored list is ${mySharedPreferences.getStoredString(Constants.LIST_OF_TODOS)}")

        val quote = (requireActivity() as MainActivity).getQuote()
        val lifeHack = (requireActivity() as MainActivity).getLifeHack()

        Log.d(TAG, "onViewCreated: quote 2 $quote")
        Log.d(TAG, "onViewCreated: lifeHack 2 $lifeHack")

        if(quote != null && lifeHack != null) {
            binding.quote.text = quote
            binding.lifeHack.text = lifeHack
        } else {
            binding.quote.text = mySharedPreferences.getStoredString(Constants.FCM_QUOTE_OF_THE_DAY)
            binding.lifeHack.text = mySharedPreferences.getStoredString(Constants.FCM_LIFE_HACK)
        }

        val username = mySharedPreferences.getStoredString(Constants.USERNAME)
        binding.username.text = username

        binding.cardGratitude.setOnClickListener {
            val directions =
                HomeFragmentDirections.actionHomeFragmentToGratitudeFragment()
            Navigation.findNavController(it).safeNavigate(directions)
        }

        binding.cardHappinessIsland.setOnClickListener {
            val directions =
                HomeFragmentDirections.actionHomeFragmentToHappinessIslandFragment()
            Navigation.findNavController(it).safeNavigate(directions)
        }

        binding.cardTodo.setOnClickListener {
            val directions =
                HomeFragmentDirections.actionHomeFragmentToTodoFragment()
            Navigation.findNavController(it).safeNavigate(directions)
        }

        binding.cardAffirmations.setOnClickListener {
            val directions =
                HomeFragmentDirections.actionHomeFragmentToAffirmationsFragment()
            Navigation.findNavController(it).safeNavigate(directions)
        }

        viewModel.getUser()
        subscribeObservers()
    }

    private fun subscribeObservers() {
        viewModel.state.observe(viewLifecycleOwner) { mainState ->
            Log.d(TAG, "subscribeObservers: affirmations ${mainState.affirmations}")
            Log.d(TAG, "subscribeObservers: affirmationMessages ${mainState.affirmationMessages}")

            processQueue(
                context = context,
                queue = mainState.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onRemoveHeadFromQuery()
                    }
                })
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}