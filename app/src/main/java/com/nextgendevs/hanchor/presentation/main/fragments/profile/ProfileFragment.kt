package com.nextgendevs.hanchor.presentation.main.fragments.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.nextgendevs.hanchor.R
import com.nextgendevs.hanchor.databinding.FragmentProfileBinding
import com.nextgendevs.hanchor.presentation.utils.Constants
import com.nextgendevs.hanchor.presentation.utils.safeNavigate
import com.nextgendevs.hanchor.presentation.utils.setStatusBarGradiant

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
        activity?.setStatusBarGradiant(getContext, R.drawable.ic_white_bkgrd)
        uiCommunicationListener.displayProgressBar(false)

        val fullname = mySharedPreferences.getStoredString(Constants.FIRSTNAME)
        binding.fullname.text = fullname

        val username = mySharedPreferences.getStoredString(Constants.USERNAME)
        binding.username.text = username

        val email = mySharedPreferences.getStoredString(Constants.EMAIL)
        binding.email.text = email

        val userId = mySharedPreferences.getStoredString(Constants.USERID)
        binding.userid.text = userId

        binding.swinge.setOnClickListener {
            val directions =
                ProfileFragmentDirections.actionProfileFragmentToUpdateProfileFragment()
            Navigation.findNavController(binding.root).safeNavigate(directions)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}