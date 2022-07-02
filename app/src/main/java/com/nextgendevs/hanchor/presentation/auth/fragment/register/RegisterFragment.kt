package com.nextgendevs.hanchor.presentation.auth.fragment.register

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.nextgendevs.hanchor.R
import com.nextgendevs.hanchor.business.domain.utils.StateMessageCallback
import com.nextgendevs.hanchor.databinding.FragmentRegisterBinding
import com.nextgendevs.hanchor.presentation.auth.AuthActivity
import com.nextgendevs.hanchor.presentation.auth.viewmodel.AuthViewModel
import com.nextgendevs.hanchor.presentation.dashboard.DashBoardActivity
import com.nextgendevs.hanchor.presentation.main.MainActivity
import com.nextgendevs.hanchor.presentation.utils.displayToast
import com.nextgendevs.hanchor.presentation.utils.processQueue
import com.nextgendevs.hanchor.presentation.utils.safeNavigate

class RegisterFragment : BaseRegisterFragment() {
    private var _binding: FragmentRegisterBinding? = null
    private val binding: FragmentRegisterBinding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()
    private var shouldObserveOnce: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegisterBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiCommunicationListener.hideSoftKeyboard()

        binding.btnSignup.setOnClickListener {
            viewModel.register(
                binding.fullname.text.toString(),
                binding.email.text.toString(),
                binding.password.text.toString()
            )
            subscribeObservers(binding.email.text.toString(), binding.password.text.toString(), it)
            uiCommunicationListener.hideSoftKeyboard()
        }

        binding.login.setOnClickListener {
            val directions =
                RegisterFragmentDirections.actionRegisterFragmentToLoginFragment("", "");
            Navigation.findNavController(it).safeNavigate(directions)
            uiCommunicationListener.hideSoftKeyboard()
        }
    }

    private fun subscribeObservers(email: String, password: String, view: View) {
        viewModel.state.observe(viewLifecycleOwner) { state ->

            if (state.userRestResult.userId != "") {
                if (shouldObserveOnce) {
                    shouldObserveOnce = false
                    val directions =
                        RegisterFragmentDirections.actionRegisterFragmentToLoginFragment(email, password);
                    Navigation.findNavController(view).safeNavigate(directions)
                }
            }


            processQueue(
                context = context,
                queue = state.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onRemoveHeadFromQuery()
                    }
                })

            uiCommunicationListener.displayProgressBar(state.isLoading)
            Log.d(TAG, "subscribeObservers: isLoading is ${state.isLoading}")
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}