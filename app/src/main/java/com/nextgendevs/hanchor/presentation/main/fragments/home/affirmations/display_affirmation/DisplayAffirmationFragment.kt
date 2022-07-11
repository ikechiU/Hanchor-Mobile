package com.nextgendevs.hanchor.presentation.main.fragments.home.affirmations.display_affirmation

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nextgendevs.hanchor.R
import com.nextgendevs.hanchor.business.domain.models.Affirmation
import com.nextgendevs.hanchor.business.domain.utils.StateMessageCallback
import com.nextgendevs.hanchor.databinding.FragmentDisplayAffirmationBinding
import com.nextgendevs.hanchor.presentation.MainViewModel
import com.nextgendevs.hanchor.presentation.utils.*

class DisplayAffirmationFragment : BaseDisplayAffirmationFragment(),
    SwipeRefreshLayout.OnRefreshListener {
    private var _binding: FragmentDisplayAffirmationBinding? = null
    private val binding: FragmentDisplayAffirmationBinding get() = _binding!!
    private val viewModel: MainViewModel by viewModels()
    private lateinit var affirmationListAdapter: AffirmationListAdapter
    private var scrollingPosition = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDisplayAffirmationBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        uiCommunicationListener.hideSoftKeyboard()
        uiCommunicationListener.displayProgressBar(false)
        activity?.setStatusBarGradiant(getContext, R.drawable.ic_white_bkgrd)
        activity?.changeStatusBarColor(Color.parseColor("#3AB0FF"), true)

        affirmationListAdapter = AffirmationListAdapter(getContext.applicationContext)
        binding.swipeRefresh.setOnRefreshListener(this)
        initRecyclerView()

        binding.navigateUp.setOnClickListener {
            val directions =
                DisplayAffirmationFragmentDirections.actionDisplayAffirmationFragmentToAffirmationsFragment()
            Navigation.findNavController(binding.root).safeNavigate(directions)
        }

        val bundle = arguments ?: return
        val args = DisplayAffirmationFragmentArgs.fromBundle(bundle)
        val affirmationTitle = args.affirmationTitle

        if (affirmationTitle != null) {
            binding.titleAffirmation.text = affirmationTitle
            viewModel.getAffirmations(binding.titleAffirmation.text.toString())
            subscribeObservers()
        }

        if (binding.titleAffirmation.text.isNotEmpty()) {
            viewModel.getAffirmations(binding.titleAffirmation.text.toString())
            subscribeObservers()
        }

        binding.fab.setOnClickListener {
            val directions =
                DisplayAffirmationFragmentDirections.actionDisplayAffirmationFragmentToCreateAffirmationFragment(
                    0L, binding.titleAffirmation.text.toString(), "", 0, 0
                )
            Navigation.findNavController(binding.root).safeNavigate(directions)
        }
    }

    private fun subscribeObservers() {
        viewModel.state.observe(viewLifecycleOwner) { mainState ->
            
            if (mainState.affirmations.isNotEmpty()) {
                binding.emptyAffirmation.visibility = View.GONE
            } else {
                binding.emptyAffirmation.visibility = View.VISIBLE
            }

            processQueue(
                context = context,
                queue = mainState.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onRemoveHeadFromQuery()
                    }
                })

            affirmationListAdapter.apply {
                binding.size.text = mainState.affirmations.size.toString()
                submitList(list = mainState.affirmations)
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = GridLayoutManager(getContext, 2)
            addItemDecoration(
                GridItemDecoration(
                    resources.getDimensionPixelOffset(R.dimen.grid_gap),
                    2,
                    true
                )
            )

//            addOnScrollListener(object : RecyclerView.OnScrollListener() {
//
//                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                    super.onScrolled(recyclerView, dx, dy)
//
//                    scrollingPosition = dy
//                }
//
//                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                    super.onScrollStateChanged(recyclerView, newState)
//
//                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
//                    val lastPosition = layoutManager.findLastVisibleItemPosition()
//
//                    if(scrollingPosition > 0) {
//                        Log.d(TAG, "onScrolled: Scrolling up.")
//                        Log.d(TAG, "onScrollStateChanged: exhausted? ${viewModel.state.value?.isQueryExhausted}")
//                        if (
//                            lastPosition == todoListAdapter.itemCount.minus(1) &&
//                            viewModel.state.value?.isLoading == false && viewModel.state.value?.isQueryExhausted == false
//                        ) {
//                            Log.d(TAG, "TodoFragment: attempting to load next page...")
//                            viewModel.fetchNextTodos()
//                        }
//                    } else {
//                        Log.d(TAG, "onScrolled: Scrolling down.")
//                    }
//
//                }
//            })
            adapter = affirmationListAdapter

            affirmationListAdapter.setAffirmationClickListener(object : AffirmationClickListener {
                override fun onItemSelected(affirmation: Affirmation, position: Int) {
                    try {
                        Log.d(TAG, "onItemSelected: The affirmation position is $position")
                        val directions =
                            DisplayAffirmationFragmentDirections.actionDisplayAffirmationFragmentToAffirmationDetailsFragment(
                                affirmation.id,
                                binding.titleAffirmation.text.toString(),
                                affirmation.affirmation,
                                Integer.parseInt(binding.size.text.toString()),
                                position
                            )
                        Navigation.findNavController(binding.root).navigate(directions)
                    } catch (e: Exception) {
                        e.printStackTrace()
                        getContext.displayToast("Error on click")
                    }
                }
            })
        }
    }

    override fun onRefresh() {
        viewModel.getAffirmations(binding.titleAffirmation.text.toString())
        subscribeObservers()
        binding.swipeRefresh.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}