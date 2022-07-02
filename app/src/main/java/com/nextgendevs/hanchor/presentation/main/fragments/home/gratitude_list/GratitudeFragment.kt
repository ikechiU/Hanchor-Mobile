package com.nextgendevs.hanchor.presentation.main.fragments.home.gratitude_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.nextgendevs.hanchor.business.domain.models.Gratitude
import com.nextgendevs.hanchor.business.domain.utils.StateMessageCallback
import com.nextgendevs.hanchor.databinding.FragmentGratitudeBinding
import com.nextgendevs.hanchor.presentation.main.fragments.home.gratitude_list.viewmodel.GratitudeViewModel
import com.nextgendevs.hanchor.presentation.main.fragments.home.todo_list.TodoFragmentDirections
import com.nextgendevs.hanchor.presentation.utils.TopSpacingItemDecoration
import com.nextgendevs.hanchor.presentation.utils.displayToast
import com.nextgendevs.hanchor.presentation.utils.processQueue
import com.nextgendevs.hanchor.presentation.utils.safeNavigate

class GratitudeFragment : BaseGratitudeFragment(), SwipeRefreshLayout.OnRefreshListener {
    private var _binding: FragmentGratitudeBinding? = null
    private val binding: FragmentGratitudeBinding get() = _binding!!
    private val viewModel: GratitudeViewModel by viewModels()
    private lateinit var gratitudeListAdapter: GratitudeListAdapter
    private var scrollingPosition  = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGratitudeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.fab.setOnClickListener {
            val directions =
                GratitudeFragmentDirections.actionGratitudeFragmentToCreateGratitudeFragment(null)
            Navigation.findNavController(it).safeNavigate(directions)
        }

        binding.navigateUp.setOnClickListener {
            val directions =
                GratitudeFragmentDirections.actionGratitudeFragmentToHomeFragment()
            Navigation.findNavController(it).safeNavigate(directions)
        }

        gratitudeListAdapter = GratitudeListAdapter(getContext.applicationContext)
        binding.swipeRefresh.setOnRefreshListener(this)

        viewModel.fetchGratitudes()
        subscribeObservers()
        initRecyclerView()
    }

    private fun subscribeObservers() {
        viewModel.state.observe(viewLifecycleOwner) { gratitudeState ->
            uiCommunicationListener.displayProgressBar(gratitudeState.isLoading)
            Log.d(TAG, "subscribeObservers: todoState list is ${gratitudeState.gratitudeList}")

            if (gratitudeState.gratitudeList.isNotEmpty()) {
                binding.emptyGratitude.visibility = View.GONE
            } else {
                binding.emptyGratitude.visibility = View.VISIBLE
            }

            processQueue(
                context = context,
                queue = gratitudeState.queue,
                stateMessageCallback = object : StateMessageCallback {
                    override fun removeMessageFromStack() {
                        viewModel.onRemoveHeadFromQuery()
                    }
                })

            gratitudeListAdapter.apply {
                submitList(list  = gratitudeState.gratitudeList)
            }
        }
    }

    private fun initRecyclerView() {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(getContext)
            val topSpacingDecorator = TopSpacingItemDecoration(10)
            removeItemDecoration(topSpacingDecorator) // does nothing if not applied already
            addItemDecoration(topSpacingDecorator)

            addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)

                    scrollingPosition = dy
                }

                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)

                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val lastPosition = layoutManager.findLastVisibleItemPosition()

                    if(scrollingPosition > 0) {
                        Log.d(TAG, "onScrolled: Scrolling up.")
                        Log.d(TAG, "onScrollStateChanged: exhausted? ${viewModel.state.value?.isQueryExhausted}")
                        if (
                            lastPosition == gratitudeListAdapter.itemCount.minus(1) &&
                            viewModel.state.value?.isLoading == false && viewModel.state.value?.isQueryExhausted == false
                        ) {
                            Log.d(TAG, "TodoFragment: attempting to load next page...")
                            viewModel.fetchNextGratitudes()
                        }
                    } else {
                        Log.d(TAG, "onScrolled: Scrolling down.")
                    }

                }
            })
            adapter = gratitudeListAdapter

            gratitudeListAdapter.setGratitudeClickListener(object : GratitudeClickListener {
                override fun onItemSelected(gratitude: Gratitude, position: Int) {
                    try {
                        Log.d(TAG, "onItemSelected: The todo position is $position")
                        val directions =
                            GratitudeFragmentDirections.actionGratitudeFragmentToCreateGratitudeFragment(gratitude)
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
        viewModel.fetchNextGratitudes()
        subscribeObservers()
        binding.swipeRefresh.isRefreshing = false
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}