package com.nextgendevs.hanchor.presentation.main.fragments.home.affirmations.display_affirmation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nextgendevs.hanchor.databinding.FragmentDisplayAffirmationBinding

class DisplayAffirmationFragment : BaseDisplayAffirmationFragment() {
    private var _binding: FragmentDisplayAffirmationBinding? = null
    private val binding: FragmentDisplayAffirmationBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDisplayAffirmationBinding.inflate(layoutInflater)
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