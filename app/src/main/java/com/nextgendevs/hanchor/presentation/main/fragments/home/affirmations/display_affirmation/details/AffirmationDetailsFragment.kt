package com.nextgendevs.hanchor.presentation.main.fragments.home.affirmations.display_affirmation.details

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.nextgendevs.hanchor.R
import com.nextgendevs.hanchor.business.domain.utils.StateMessageCallback
import com.nextgendevs.hanchor.databinding.FragmentAffirmationDetailsBinding
import com.nextgendevs.hanchor.presentation.MainViewModel
import com.nextgendevs.hanchor.presentation.main.fragments.home.affirmations.AffirmationsFragmentDirections
import com.nextgendevs.hanchor.presentation.main.fragments.home.affirmations.display_affirmation.DisplayAffirmationFragmentArgs
import com.nextgendevs.hanchor.presentation.main.fragments.home.affirmations.display_affirmation.DisplayAffirmationFragmentDirections
import com.nextgendevs.hanchor.presentation.utils.changeStatusBarColor
import com.nextgendevs.hanchor.presentation.utils.processQueue
import com.nextgendevs.hanchor.presentation.utils.safeNavigate
import com.nextgendevs.hanchor.presentation.utils.setStatusBarGradiant

class AffirmationDetailsFragment : BaseAffirmationDetailsFragment() {
    private var _binding: FragmentAffirmationDetailsBinding? = null
    private val binding: FragmentAffirmationDetailsBinding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private var affirmationTitle = ""
    private var affirmations: List<String> = emptyList()
    private var ids: List<Long> = emptyList()

    private var id = 0L
    private var size = 0
    private var position = 0
    private var affirmationMessage: String? = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAffirmationDetailsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiCommunicationListener.hideSoftKeyboard()

        val bundle = arguments ?: return
        val args = AffirmationDetailsFragmentArgs.fromBundle(bundle)
        affirmationMessage = args.affirmationMessage
        size = args.size
        position = args.position
        affirmationTitle = args.affirmationTitle!!
        id = args.affirmationId

        if (affirmationMessage != null) {
            binding.affirmation.text = affirmationMessage
            setStatusBarAndBackgroundColor()
            setPreviousNextButton()
            viewModel.getAffirmations(affirmationTitle)
            subscribeObservers()

        } else {
            binding.affirmation.text = ""
        }

        binding.close.setOnClickListener {
            val directions =
                AffirmationDetailsFragmentDirections.actionAffirmationDetailsFragmentToDisplayAffirmationFragment(affirmationTitle)
            Navigation.findNavController(binding.root).safeNavigate(directions)
        }

        binding.right.setOnClickListener {
            position++
            updateVariable(position)
        }

        binding.left.setOnClickListener {
            position--
            updateVariable(position)
        }

        binding.swinge.setOnClickListener {
            val directions =
                AffirmationDetailsFragmentDirections.actionAffirmationDetailsFragmentToCreateAffirmationFragment(
                    id, affirmationTitle, affirmationMessage, size, position
                )
            Navigation.findNavController(binding.root).safeNavigate(directions)
        }

    }

    private fun updateVariable(position: Int) {
        id = ids[position]
        affirmationMessage = affirmations[position]
        binding.affirmation.text = affirmationMessage
        setStatusBarAndBackgroundColor()
        setPreviousNextButton()
    }

    private fun setStatusBarAndBackgroundColor() {
        when ((position + 1) % 6) {
            0 -> {
                activity?.setStatusBarGradiant(getContext, R.drawable.ic_affirm_one)
                binding.root.background =
                    AppCompatResources.getDrawable(getContext, R.drawable.ic_affirm_one)
            }
            1 -> {
                activity?.setStatusBarGradiant(getContext, R.drawable.ic_affirm_two)
                binding.root.background =
                    AppCompatResources.getDrawable(getContext, R.drawable.ic_affirm_two)
            }
            2 -> {
                activity?.setStatusBarGradiant(getContext, R.drawable.ic_affirm_three)
                binding.root.background =
                    AppCompatResources.getDrawable(getContext, R.drawable.ic_affirm_three)
            }
            3 -> {
                activity?.setStatusBarGradiant(getContext, R.drawable.ic_affirm_four)
                binding.root.background =
                    AppCompatResources.getDrawable(getContext, R.drawable.ic_affirm_four)
            }
            4 -> {
                activity?.setStatusBarGradiant(getContext, R.drawable.ic_affirm_five)
                binding.root.background =
                    AppCompatResources.getDrawable(getContext, R.drawable.ic_affirm_five)
            }
            5 -> {
                activity?.setStatusBarGradiant(getContext, R.drawable.ic_affirm_six)
                binding.root.background =
                    AppCompatResources.getDrawable(getContext, R.drawable.ic_affirm_six)
            }
        }

    }

    private fun setPreviousNextButton() {

        if (position == 0 && size == 1) {
            binding.left.visibility = View.INVISIBLE
            binding.right.visibility = View.INVISIBLE
        }

        if (position > 0 && (size - 1) > position) {
            binding.left.visibility = View.VISIBLE
            binding.right.visibility = View.VISIBLE
        }

        if (position > 0 && (size - 1) == position) {
            binding.left.visibility = View.VISIBLE
            binding.right.visibility = View.INVISIBLE
        }

        if (position == 0 && (size - 1) > position) {
            binding.left.visibility = View.INVISIBLE
            binding.right.visibility = View.VISIBLE
        }
    }

    private fun subscribeObservers() {
        viewModel.state.observe(viewLifecycleOwner) { mainState ->
            if (mainState.affirmations.isNotEmpty()) {
                ids = mainState.affirmations.map { it.id }
                affirmations = mainState.affirmations.map { it.affirmation }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}