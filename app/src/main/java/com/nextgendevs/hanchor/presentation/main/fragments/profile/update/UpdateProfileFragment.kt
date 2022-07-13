package com.nextgendevs.hanchor.presentation.main.fragments.profile.update

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.nextgendevs.hanchor.business.domain.utils.StateMessageCallback
import com.nextgendevs.hanchor.databinding.FragmentUpdateProfileBinding
import com.nextgendevs.hanchor.presentation.MainViewModel
import com.nextgendevs.hanchor.presentation.auth.AuthActivity
import com.nextgendevs.hanchor.presentation.main.fragments.home.affirmations.AffirmationsFragmentDirections
import com.nextgendevs.hanchor.presentation.utils.*

class UpdateProfileFragment : BaseUpdateProfileFragment() {
    private var _binding: FragmentUpdateProfileBinding? = null
    private val binding: FragmentUpdateProfileBinding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private var shouldObserveOnce = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentUpdateProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiCommunicationListener.displayProgressBar(false)

        binding.navigateUp.setOnClickListener {
            navigateToProfile()
        }

        val fullname = mySharedPreferences.getStoredString(Constants.FIRSTNAME)
        binding.fullname.setText(fullname)

        val username = mySharedPreferences.getStoredString(Constants.USERNAME)
        binding.username.setText(username)

        val email = mySharedPreferences.getStoredString(Constants.EMAIL)
        binding.email.setText(email)

        val userId = mySharedPreferences.getStoredString(Constants.USERID)
        binding.userid.setText(userId)

        binding.btnSave.setOnClickListener {
            updateProfile()
        }

    }

    private fun updateProfile() {
        val fullname = binding.fullname.text.toString()
        val username = binding.username.text.toString()

        if (fullname.isNotEmpty() && username.isNotEmpty()) {
            shouldObserveOnce =  true
            viewModel.updateUser(fullname, username)
            subscribeObservers(fullname, username)
        } else {
            getContext.displayToast("Cannot update empty field")
        }
    }

    private fun subscribeObservers(fullname: String, username: String) {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            uiCommunicationListener.displayProgressBar(state.isLoading)

            if(state.tokenExpired) {
                activity?.logoutUser(mySharedPreferences)
            }

            if (state.userDetails.isNotEmpty()) {
                if (shouldObserveOnce) {
                    shouldObserveOnce = false

                    mySharedPreferences.storeStringValue(Constants.FIRSTNAME, fullname)
                    mySharedPreferences.storeStringValue(Constants.USERNAME, username)

                    navigateToProfile()
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

    private fun navigateToProfile() {
        val directions =
            UpdateProfileFragmentDirections.actionUpdateProfileFragmentToProfileFragment()
        Navigation.findNavController(binding.root).safeNavigate(directions)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}