package com.nextgendevs.hanchor.presentation.main.fragments.home.affirmations.display_affirmation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.nextgendevs.hanchor.databinding.FragmentDisplayAffirmationBinding
import com.nextgendevs.hanchor.presentation.main.fragments.home.todo_list.create_todo.CreateTodoFragmentArgs
import com.nextgendevs.hanchor.presentation.utils.safeNavigate

class DisplayAffirmationFragment : BaseDisplayAffirmationFragment() {
    private var _binding: FragmentDisplayAffirmationBinding? = null
    private val binding: FragmentDisplayAffirmationBinding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDisplayAffirmationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.navigateUp.setOnClickListener {
            val directions =
                DisplayAffirmationFragmentDirections.actionDisplayAffirmationFragmentToAffirmationsFragment()
            Navigation.findNavController(binding.root).safeNavigate(directions)
        }

        val bundle = arguments ?: return
        val args = DisplayAffirmationFragmentArgs.fromBundle(bundle)
        val affirmationTitle = args.affirmationTitle
        val affirmationMessage = args.affirmationMessage

        if (affirmationTitle != null && affirmationMessage != null) {
            binding.placeholderTitle.text = affirmationTitle
            setUpExistingAffirmationMessage(affirmationMessage)
        }


    }

    private fun setUpExistingAffirmationMessage(affirmationMessage: String) {
        binding.affirmation.text = affirmationMessage
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}