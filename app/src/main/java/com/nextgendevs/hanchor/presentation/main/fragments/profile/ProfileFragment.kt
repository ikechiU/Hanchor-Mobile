package com.nextgendevs.hanchor.presentation.main.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nextgendevs.hanchor.databinding.FragmentProfileBinding
import com.nextgendevs.hanchor.presentation.utils.Constants

class ProfileFragment : BaseProfileFragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding: FragmentProfileBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiCommunicationListener.displayProgressBar(false)

        val fullname = mySharedPreferences.getStoredString(Constants.FIRSTNAME)
        binding.fullname.text = fullname

        val username = mySharedPreferences.getStoredString(Constants.USERNAME)
        binding.username.text = username

        val email = mySharedPreferences.getStoredString(Constants.EMAIL)
        binding.email.text = email

        val userId = mySharedPreferences.getStoredString(Constants.USERID)
        binding.userid.text = userId

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}