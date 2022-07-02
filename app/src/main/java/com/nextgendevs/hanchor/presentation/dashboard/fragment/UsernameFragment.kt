package com.nextgendevs.hanchor.presentation.dashboard.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.nextgendevs.hanchor.business.domain.utils.StateMessageCallback
import com.nextgendevs.hanchor.databinding.FragmentUsernameBinding
import com.nextgendevs.hanchor.presentation.MainViewModel
import com.nextgendevs.hanchor.presentation.utils.processQueue
import com.nextgendevs.hanchor.presentation.utils.safeNavigate
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class UsernameFragment : BaseUsernameFragment() {

    private var _binding: FragmentUsernameBinding? = null
    private val binding: FragmentUsernameBinding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUsernameBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.next.setOnClickListener {
            if (!binding.username.text.isNullOrEmpty()) {
                uiCommunicationListener.hideSoftKeyboard()

                viewModel.updateUsername(binding.username.text.toString())
                subscribeObservers(it)

                lifecycleScope.launch {
                    delay(10000)
                    navigateToWelcome(it)
                }
            }
        }

    }

    private fun navigateToWelcome(view: View) {
        val directions =
            UsernameFragmentDirections.actionUsernameFragmentToWelcomeFragment()
        Navigation.findNavController(view).safeNavigate(directions)
    }

    private fun subscribeObservers(view: View){
        viewModel.state.observe(viewLifecycleOwner) { state ->
            uiCommunicationListener.displayProgressBar(state.isLoading)

            if(state.updateResult != "" || state.errorMessage == "No Internet.") {
                navigateToWelcome(view)

                Log.d(TAG, "subscribeObservers: fragment username updated")
            }

            processQueue(
                context = context,
                queue = state.queue,
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