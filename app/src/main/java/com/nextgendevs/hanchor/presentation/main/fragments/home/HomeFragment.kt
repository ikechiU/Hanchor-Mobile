package com.nextgendevs.hanchor.presentation.main.fragments.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.nextgendevs.hanchor.databinding.FragmentHomeBinding
import com.nextgendevs.hanchor.presentation.utils.Constants
import com.nextgendevs.hanchor.presentation.utils.safeNavigate

class HomeFragment : BaseHomeFragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}