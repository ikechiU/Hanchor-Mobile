package com.nextgendevs.hanchor.presentation.main.fragments.home.affirmations

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.nextgendevs.hanchor.business.domain.utils.StateMessageCallback
import com.nextgendevs.hanchor.databinding.FragmentAffirmationsBinding
import com.nextgendevs.hanchor.presentation.MainViewModel
import com.nextgendevs.hanchor.presentation.utils.Constants
import com.nextgendevs.hanchor.presentation.utils.processQueue
import com.nextgendevs.hanchor.presentation.utils.safeNavigate

class AffirmationsFragment : BaseAffirmationsFragment() {
    private var _binding: FragmentAffirmationsBinding? = null
    private val binding: FragmentAffirmationsBinding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAffirmationsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navigateUp.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToHomeFragment()
            Navigation.findNavController(binding.root).safeNavigate(directions)
        }

        val username = mySharedPreferences.getStoredString(Constants.USERNAME)
        binding.username.text = username

        viewModel.getAffirmation()
        subscribeObservers()

    }

    private fun subscribeObservers() {
        viewModel.state.observe(viewLifecycleOwner) { mainState ->
            Log.d(TAG, "subscribeObservers: mainState.affirmationMessages ${mainState.affirmationMessages}")

            if (mainState.affirmationMessages.isNotEmpty()) {
                affirmationClick(mainState.affirmationMessages)
            }

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

    private fun affirmationClick(affirmationMessages: List<String>) {
        binding.btnProsperity.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment(
                    binding.btnProsperity.text.toString(), affirmationMessages[0])
            Navigation.findNavController(it).safeNavigate(directions)
        }
        binding.btnLove.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment(
                    binding.btnLove.text.toString(), affirmationMessages[1])
            Navigation.findNavController(it).safeNavigate(directions)
        }
        binding.btnIam.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment(
                    binding.btnIam.text.toString(), affirmationMessages[2])
            Navigation.findNavController(it).safeNavigate(directions)
        }
        binding.btnFamily.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment(
                    binding.btnFamily.text.toString(), affirmationMessages[3])
            Navigation.findNavController(it).safeNavigate(directions)
        }
        binding.btnSelfEsteem.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment(
                    binding.btnSelfEsteem.text.toString(), affirmationMessages[4])
            Navigation.findNavController(it).safeNavigate(directions)
        }
        binding.btnSpiritual.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment(
                    binding.btnSpiritual.text.toString(), affirmationMessages[5])
            Navigation.findNavController(it).safeNavigate(directions)
        }
        binding.btnBeauty.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment(
                    binding.btnBeauty.text.toString(), affirmationMessages[6])
            Navigation.findNavController(it).safeNavigate(directions)
        }
        binding.btnSuccess.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment(
                    binding.btnSuccess.text.toString(), affirmationMessages[7])
            Navigation.findNavController(it).safeNavigate(directions)
        }
        binding.btnRelationship.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment(
                    binding.btnRelationship.text.toString(), affirmationMessages[8])
            Navigation.findNavController(it).safeNavigate(directions)
        }
        binding.btnHealth.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment(
                    binding.btnHealth.text.toString(), affirmationMessages[9])
            Navigation.findNavController(it).safeNavigate(directions)
        }
        binding.btnBusiness.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment(
                    binding.btnBusiness.text.toString(), affirmationMessages[10])
            Navigation.findNavController(it).safeNavigate(directions)
        }
        binding.btnSelfForgiveness.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment(
                    binding.btnSelfForgiveness.text.toString(), affirmationMessages[11])
            Navigation.findNavController(it).safeNavigate(directions)
        }
        binding.btnSelfImprovement.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment(
                    binding.btnSelfImprovement.text.toString(), affirmationMessages[12])
            Navigation.findNavController(it).safeNavigate(directions)
        }
        binding.btnIcan.setOnClickListener {
            val directions =
                AffirmationsFragmentDirections.actionAffirmationsFragmentToDisplayAffirmationFragment(
                    binding.btnIcan.text.toString(), affirmationMessages[13])
            Navigation.findNavController(it).safeNavigate(directions)
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}