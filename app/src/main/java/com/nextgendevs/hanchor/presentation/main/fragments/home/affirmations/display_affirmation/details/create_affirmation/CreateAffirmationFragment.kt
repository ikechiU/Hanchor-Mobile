package com.nextgendevs.hanchor.presentation.main.fragments.home.affirmations.display_affirmation.details.create_affirmation

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.nextgendevs.hanchor.R
import com.nextgendevs.hanchor.business.datasource.network.request.AffirmationRequest
import com.nextgendevs.hanchor.business.domain.utils.AreYouSureCallback
import com.nextgendevs.hanchor.business.domain.utils.StateMessageCallback
import com.nextgendevs.hanchor.databinding.FragmentCreateAffirmationBinding
import com.nextgendevs.hanchor.presentation.broadcast.reminder.deleteReminder
import com.nextgendevs.hanchor.presentation.broadcast.reminder.scheduleReminder
import com.nextgendevs.hanchor.presentation.main.fragments.home.affirmations.display_affirmation.details.AffirmationDetailsFragmentDirections
import com.nextgendevs.hanchor.presentation.main.fragments.home.affirmations.display_affirmation.details.viewmodel.AffirmationViewModel
import com.nextgendevs.hanchor.presentation.main.fragments.home.todo_list.create_todo.CreateTodoFragmentDirections
import com.nextgendevs.hanchor.presentation.utils.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class CreateAffirmationFragment : BaseCreateAffirmationFragment() {

    private var _binding: FragmentCreateAffirmationBinding? = null
    private val binding: FragmentCreateAffirmationBinding get() = _binding!!
    private val viewModel: AffirmationViewModel by viewModels()

    private var affirmationTitle = ""
    private var affirmationId = 0L
    private var affirmationMessage = ""
    private var size = 0
    private var position = 0

    private var shouldObserveOnce = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.onBackPressedDispatcher?.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                areYouSure()
            }
        })
    }

    private fun OnBackPressedCallback.areYouSure() {
        val fragment: String = if(affirmationId == 0L) "Display Affirmations" else "Affirmation Details"

        activity?.areYouSureDialog("Navigate to $fragment", object : AreYouSureCallback {
            override fun proceed() {
                this@areYouSure.isEnabled = false
                activity?.onBackPressed()
            }

            override fun cancel() {
                Log.d(TAG, "cancel: pressed.")
            }
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCreateAffirmationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.setStatusBarGradiant(getContext, R.drawable.ic_white_bkgrd)

        val bundle = arguments ?: return
        val args = CreateAffirmationFragmentArgs.fromBundle(bundle)

        affirmationTitle = args.affirmationTitle!!
        affirmationId = args.affirmationId
        affirmationMessage = args.affirmationMessage!!
        size = args.size
        position = args.position

        if (affirmationId == 0L) {
            binding.deleteAffirmation.visibility = View.INVISIBLE
            binding.appBarTitle.text = getContext.getString(R.string.create_affirmation)
        }
        else {
            binding.deleteAffirmation.visibility = View.VISIBLE
            binding.appBarTitle.text = getContext.getString(R.string.update_affirmation)
        }

        binding.affirmation.setText(affirmationMessage)

        binding.navigateUp.setOnClickListener {
            val directions =
                AffirmationDetailsFragmentDirections.actionAffirmationDetailsFragmentToDisplayAffirmationFragment(affirmationTitle)
            Navigation.findNavController(binding.root).safeNavigate(directions)
        }

        binding.btnSave.setOnClickListener {
            uiCommunicationListener.hideSoftKeyboard()

            if (binding.affirmation.text.isNotEmpty()) {
                affirmationMessage = binding.affirmation.text.toString()
                val affirmation = AffirmationRequest(affirmationTitle, affirmationMessage)

                if (affirmationId == 0L) viewModel.insertAffirmation(affirmation)
                else viewModel.updateAffirmation(affirmationId, affirmation)

                subscribeObservers()
            } else {
                getContext.displayToast("Add an affirmation")
            }
        }

        binding.navigateUp.setOnClickListener {
            if (affirmationId == 0L) {
                navigateToDisplayAffirmationFragment()
            } else {
                navigateToAffirmationDetailsFragment()
            }
        }

        binding.deleteAffirmation.setOnClickListener {
            getContext.areYouSureDialog("Delete affirmation", object : AreYouSureCallback {
                override fun proceed() {
                    viewModel.deleteAffirmation(affirmationId)
                    subscribeObservers()
                }

                override fun cancel() {
                    Log.d(TAG, "cancel: pressed.")
                }
            })
        }

    }

    private fun subscribeObservers() {
        viewModel.state.observe(viewLifecycleOwner) { affirmationState ->
            uiCommunicationListener.displayProgressBar(affirmationState.isLoading)

            if (affirmationState.tokenExpired) {
                activity?.logoutUser(mySharedPreferences)
            }

            if (affirmationState.insertResult != 0L) {
                if (shouldObserveOnce) {
                    navigateToDisplayAffirmationFragment()
                    shouldObserveOnce = false
                }
            }

            if (affirmationState.updateResult != 0) {
                if (shouldObserveOnce) {
                    navigateToAffirmationDetailsFragment()
                    shouldObserveOnce = false
                }
            }

            if (affirmationState.deleteResult != 0) {
                if (shouldObserveOnce) {
                    navigateToDisplayAffirmationFragment()
                    shouldObserveOnce = false
                }
            }

            processQueue(
                context = context,
                queue = affirmationState.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onRemoveHeadFromQuery()
                    }
                })

        }
    }

    private fun navigateToDisplayAffirmationFragment() {
        val directions =
            CreateAffirmationFragmentDirections.actionCreateAffirmationFragmentToDisplayAffirmationFragment(affirmationTitle)
        Navigation.findNavController(binding.root).safeNavigate(directions)
    }

    private fun navigateToAffirmationDetailsFragment() {
        val directions =
            CreateAffirmationFragmentDirections.actionCreateAffirmationFragmentToAffirmationDetailsFragment(
                affirmationId, affirmationTitle, affirmationMessage, size, position
            )
        Navigation.findNavController(binding.root).safeNavigate(directions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}
