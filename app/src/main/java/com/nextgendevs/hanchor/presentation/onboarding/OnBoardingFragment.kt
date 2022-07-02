package com.nextgendevs.hanchor.presentation.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import com.nextgendevs.hanchor.databinding.FragmentOnBoardingBinding

class OnBoardingFragment : Fragment() {
    private var _binding: FragmentOnBoardingBinding? = null
    private val binding: FragmentOnBoardingBinding get() = _binding!!


    private lateinit var title: String
    private lateinit var description: String
    private var imageResource = 0

        private lateinit var tvTitle: AppCompatTextView
        private lateinit var tvDescription: AppCompatTextView
        private lateinit var image: ImageView

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            if (arguments != null) {
                title = requireArguments().getString(ARG_PARAM1)!!
                description = requireArguments().getString(ARG_PARAM2)!!
                imageResource = requireArguments().getInt(ARG_PARAM3)
            }
        }
        companion object {
            // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
            private const val ARG_PARAM1 = "param1"
            private const val ARG_PARAM2 = "param2"
            private const val ARG_PARAM3 = "param3"
            fun newInstance(
                title: String?,
                description: String?,
                imageResource: Int
            ): OnBoardingFragment {
                val fragment = OnBoardingFragment()
                val args = Bundle()
                args.putString(ARG_PARAM1, title)
                args.putString(ARG_PARAM2, description)
                args.putInt(ARG_PARAM3, imageResource)
                fragment.arguments = args
                return fragment
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnBoardingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tvTitle = binding.textOnboardingTitle
        tvDescription = binding.textOnboardingDescription
        image = binding.imageOnboarding

        tvTitle.text = title
        tvDescription.text = description
        image.setImageResource(imageResource)
//        image.setAnimation(imageResource)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}