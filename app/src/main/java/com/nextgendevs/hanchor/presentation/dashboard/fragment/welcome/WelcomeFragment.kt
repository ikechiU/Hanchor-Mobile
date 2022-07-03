package com.nextgendevs.hanchor.presentation.dashboard.fragment.welcome

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.nextgendevs.hanchor.databinding.FragmentWelcomeBinding
import com.nextgendevs.hanchor.presentation.main.MainActivity
import com.nextgendevs.hanchor.presentation.main.fragments.BaseFragment
import com.nextgendevs.hanchor.presentation.utils.Constants

class WelcomeFragment : BaseWelcomeFragment() {

    private var _binding: FragmentWelcomeBinding? = null
    private val binding: FragmentWelcomeBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWelcomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiCommunicationListener.displayProgressBar(false)

        val username = mySharedPreferences.getStoredString(Constants.USERNAME)
        binding.username.text = username

        binding.letsGo.setOnClickListener {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            activity?.finish()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}