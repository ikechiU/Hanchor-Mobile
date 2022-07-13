package com.nextgendevs.hanchor.presentation.auth.fragment.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.Navigation
import com.nextgendevs.hanchor.business.domain.utils.StateMessageCallback
import com.nextgendevs.hanchor.databinding.FragmentLoginBinding
import com.nextgendevs.hanchor.presentation.auth.AuthActivity
import com.nextgendevs.hanchor.presentation.auth.viewmodel.AuthViewModel
import com.nextgendevs.hanchor.presentation.dashboard.DashBoardActivity
import com.nextgendevs.hanchor.presentation.main.MainActivity
import com.nextgendevs.hanchor.presentation.utils.processQueue
import com.nextgendevs.hanchor.presentation.utils.safeNavigate
import kotlinx.coroutines.launch

class LoginFragment : BaseLoginFragment() {
    private var _binding: FragmentLoginBinding? = null
    private val binding: FragmentLoginBinding get() = _binding!!
    private val viewModel: AuthViewModel by viewModels()
    private var shouldObserveOnce: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bundle = arguments
        if (bundle == null) {
            Log.d(TAG, "onViewCreated: LoginFragment did not receive any data")
            return
        }

        val args = LoginFragmentArgs.fromBundle(bundle)
        val email = args.email
        val password = args.password

        binding.email.setText(email)
        binding.password.setText(password)

        if (email.isNotEmpty() && password.isNotEmpty()) {
            shouldObserveOnce =  true
            Log.d(TAG, "onViewCreated: email is $email")
            Log.d(TAG, "onViewCreated: password is $password")
            viewModel.login(email, password)
            subscribeObservers(1)
            uiCommunicationListener.hideSoftKeyboard()
        }

        binding.btnLogin.setOnClickListener {
            shouldObserveOnce =  true
            lifecycleScope.launch {
                viewModel.login(binding.email.text.toString(), binding.password.text.toString())
                subscribeObservers(2)
                uiCommunicationListener.hideSoftKeyboard()
            }
        }

        binding.passwordReset.setOnClickListener {
            val directions =
                LoginFragmentDirections.actionLoginFragmentToForgotPasswordFragment()
            Navigation.findNavController(it).safeNavigate(directions)
        }
    }

    private fun subscribeObservers(locationCode: Int){
        viewModel.state.observe(viewLifecycleOwner) { state ->
            uiCommunicationListener.displayProgressBar(state.isLoading)

            Log.d(TAG, "subscribeObservers: token is ${state.loginResult}")

            if (state.loginResult != "") {
                if (shouldObserveOnce) {

                    shouldObserveOnce = false
                    var intent: Intent? = null

                    if (locationCode == 1) {
                        intent = Intent(getContext, DashBoardActivity::class.java)
                    }
                    if (locationCode == 2) {
                        intent = Intent(getContext, MainActivity::class.java)
                    }
                    intent?.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    activity?.finish()

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

    override fun onResume() {
        super.onResume()
        uiCommunicationListener.hideSoftKeyboard()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}