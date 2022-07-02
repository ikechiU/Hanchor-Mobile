package com.nextgendevs.hanchor.presentation.main.fragments.refer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nextgendevs.hanchor.databinding.FragmentReferBinding

class ReferFragment : BaseReferFragment() {
    private var _binding: FragmentReferBinding? = null
    private val binding: FragmentReferBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentReferBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiCommunicationListener.displayProgressBar(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}