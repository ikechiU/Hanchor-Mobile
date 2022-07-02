package com.nextgendevs.hanchor.presentation.main.fragments.home.affirmations

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.nextgendevs.hanchor.databinding.FragmentAffirmationsBinding
import com.nextgendevs.hanchor.presentation.utils.safeNavigate

class AffirmationsFragment : BaseAffirmationsFragment() {
    private var _binding: FragmentAffirmationsBinding? = null
    private val binding: FragmentAffirmationsBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAffirmationsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnProsperity.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment()
            Navigation.findNavController(it).safeNavigate(directions)
        }
        binding.btnLove.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment()
            Navigation.findNavController(it).safeNavigate(directions)
        }
        binding.btnIam.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment()
            Navigation.findNavController(it).safeNavigate(directions)
        }
        binding.btnFamily.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment()
            Navigation.findNavController(it).safeNavigate(directions)
        }
        binding.btnSelfEsteem.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment()
            Navigation.findNavController(it).safeNavigate(directions)
        }
        binding.btnSpiritual.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment()
            Navigation.findNavController(it).safeNavigate(directions)
        }
        binding.btnBeauty.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment()
            Navigation.findNavController(it).safeNavigate(directions)
        }
        binding.btnSuccess.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment()
            Navigation.findNavController(it).safeNavigate(directions)
        }
        binding.btnRelationship.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment()
            Navigation.findNavController(it).safeNavigate(directions)
        }
        binding.btnHealth.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment()
            Navigation.findNavController(it).safeNavigate(directions)
        }
        binding.btnBusiness.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment()
            Navigation.findNavController(it).safeNavigate(directions)
        }
        binding.btnSelfForgiveness.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment()
            Navigation.findNavController(it).safeNavigate(directions)
        }
        binding.btnSelfImprovement.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment()
            Navigation.findNavController(it).safeNavigate(directions)
        }
        binding.btnIcan.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment()
            Navigation.findNavController(it).safeNavigate(directions)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}