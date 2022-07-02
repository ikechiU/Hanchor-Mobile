package com.nextgendevs.hanchor.presentation.auth.fragment.forgot_password

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.nextgendevs.hanchor.business.domain.utils.StateMessageCallback
import com.nextgendevs.hanchor.databinding.FragmentForgotPasswordBinding
import com.nextgendevs.hanchor.presentation.auth.viewmodel.AuthViewModel
import com.nextgendevs.hanchor.presentation.dashboard.DashBoardActivity
import com.nextgendevs.hanchor.presentation.utils.displayToast
import com.nextgendevs.hanchor.presentation.utils.processQueue
import com.nextgendevs.hanchor.presentation.utils.safeNavigate

class ForgotPasswordFragment : BaseForgotPasswordFragment() {
    private var _binding: FragmentForgotPasswordBinding? = null
    private val binding: FragmentForgotPasswordBinding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()
    private var shouldObserveOnce: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentForgotPasswordBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiCommunicationListener.hideSoftKeyboard()

        binding.btnResetPassword.setOnClickListener{
            viewModel.forgotPassword(binding.email.text.toString())
            subscribeObservers()
            uiCommunicationListener.hideSoftKeyboard()
        }

        binding.login.setOnClickListener {
            val directions =
                ForgotPasswordFragmentDirections.actionForgotPasswordFragmentToLoginFragment()
            Navigation.findNavController(it).safeNavigate(directions)
        }
    }

    private fun subscribeObservers(){
        viewModel.state.observe(viewLifecycleOwner) { state ->
            uiCommunicationListener.displayProgressBar(state.isLoading)


            if (state.forgotPasswordResult != "") {
                if (shouldObserveOnce) {
                    shouldObserveOnce = false
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
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}