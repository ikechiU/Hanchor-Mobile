package com.nextgendevs.hanchor.presentation.main.fragments.home.happiness_island

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nextgendevs.hanchor.databinding.FragmentHappinessIslandBinding

class HappinessIslandFragment : BaseHappinessIslandFragment() {
    private var _binding: FragmentHappinessIslandBinding? = null
    private val binding: FragmentHappinessIslandBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHappinessIslandBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}