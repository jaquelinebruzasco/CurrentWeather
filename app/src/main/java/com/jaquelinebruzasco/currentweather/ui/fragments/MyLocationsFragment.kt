package com.jaquelinebruzasco.currentweather.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jaquelinebruzasco.currentweather.R
import com.jaquelinebruzasco.currentweather.databinding.FragmentMyLocationsBinding
import com.jaquelinebruzasco.currentweather.domain.remote.model.LocationResponseModel
import com.jaquelinebruzasco.currentweather.ui.fragments.adapter.MyLocationsAdapter
import com.jaquelinebruzasco.currentweather.ui.viewModel.MyLocationsFragmentViewModel
import com.jaquelinebruzasco.currentweather.ui.viewModel.MyLocationsState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MyLocationsFragment: Fragment() {

    private val viewModel by viewModels<MyLocationsFragmentViewModel>()
    private lateinit var _binding: FragmentMyLocationsBinding
    private val myLocationsAdapter by lazy { MyLocationsAdapter(::navigateToHome, ::navigateToAddLocation) }

    private companion object {
        const val BOTTOM_SHEET_TAG = "addLocationBottomSheetFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyLocationsBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycleView()
        viewModel.myLocations()
        initObservables()
    }

    private fun initObservables() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.myLocationsResponseState.collectLatest { state ->
                    when (state) {
                        is MyLocationsState.Empty -> {
                            hideProgressBar()
                            _binding.tvEmptyList.visibility = View.VISIBLE
                        }
                        is MyLocationsState.Loading -> {
                            showProgressBar()
                        }
                        is MyLocationsState.Success -> {
                            hideProgressBar()
                            _binding.tvEmptyList.visibility = View.GONE
                            myLocationsAdapter.list = state.data.toMutableList()
                        }
                    }
                }
            }
        }
    }

    private fun setupRecycleView() = with(_binding) {
        rvMyLocations.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = myLocationsAdapter
        }
        ItemTouchHelper(itemTouchHelperCallback()).attachToRecyclerView(rvMyLocations)
    }

    private fun showProgressBar() {
        _binding.apply {
            progressCircular.visibility = View.VISIBLE
            content.visibility = View.GONE
        }
    }

    private fun hideProgressBar() {
        _binding.apply {
            progressCircular.visibility = View.GONE
            content.visibility = View.VISIBLE
        }
    }

    private fun navigateToHome(location: LocationResponseModel) {}

    private fun navigateToAddLocation() {
        val dialog = AddLocationBSDFragment(
            ::newListOfLocations
        )
        dialog.show(requireActivity().supportFragmentManager, BOTTOM_SHEET_TAG)
    }

    private fun itemTouchHelperCallback(): ItemTouchHelper.SimpleCallback {
        return object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val location = myLocationsAdapter.getLocationAtPosition(viewHolder.bindingAdapterPosition)
                location.let {
                    viewModel.delete(it).also {
                        Toast.makeText(
                            requireContext(),
                            R.string.delete_location,
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun newListOfLocations() {
        viewModel.myLocations()
    }
}