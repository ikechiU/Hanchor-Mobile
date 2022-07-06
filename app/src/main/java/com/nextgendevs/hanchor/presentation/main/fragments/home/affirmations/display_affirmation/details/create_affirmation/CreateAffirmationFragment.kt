package com.nextgendevs.hanchor.presentation.main.fragments.home.affirmations.display_affirmation.details.create_affirmation

import android.os.Bundle
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
import com.nextgendevs.hanchor.databinding.FragmentCreateAffirmationBinding
import com.nextgendevs.hanchor.presentation.main.fragments.home.affirmations.display_affirmation.details.AffirmationDetailsFragmentArgs
import com.nextgendevs.hanchor.presentation.main.fragments.home.affirmations.display_affirmation.details.AffirmationDetailsFragmentDirections
import com.nextgendevs.hanchor.presentation.main.fragments.home.affirmations.display_affirmation.details.viewmodel.AffirmationViewModel
import com.nextgendevs.hanchor.presentation.utils.displayToast
import com.nextgendevs.hanchor.presentation.utils.processQueue
import com.nextgendevs.hanchor.presentation.utils.safeNavigate
import com.nextgendevs.hanchor.presentation.utils.setStatusBarGradiant

class CreateAffirmationFragment : BaseCreateAffirmationFragment() {

    private var _binding: FragmentCreateAffirmationBinding? = null
    private val binding: FragmentCreateAffirmationBinding get() = _binding!!
    private val viewModel: AffirmationViewModel by viewModels()

    var affirmationTitle = ""
    var affirmationId = 0L
    var affirmationMessage = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateAffirmationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments ?: return
        val args = CreateAffirmationFragmentArgs.fromBundle(bundle)

        affirmationTitle = args.affirmationTitle!!
        affirmationId = args.affirmationId
        affirmationMessage = args.affirmationMessage!!

        binding.affirmation.text = affirmationMessage
        
        binding.navigateUp.setOnClickListener {
            val directions =
                AffirmationDetailsFragmentDirections.actionAffirmationDetailsFragmentToDisplayAffirmationFragment()
            Navigation.findNavController(binding.root).safeNavigate(directions)
        }

        binding.btnSave.setOnClickListener {
            if (binding.affirmation.text.isNotEmpty()) {

                viewModel.insertAffirmation()
                subscribeObservers()
            } else {
                getContext.displayToast("Add an affirmation")
            }
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