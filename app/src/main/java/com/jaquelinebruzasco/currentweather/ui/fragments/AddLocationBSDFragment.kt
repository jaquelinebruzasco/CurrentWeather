package com.jaquelinebruzasco.currentweather.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.jaquelinebruzasco.currentweather.R
import com.jaquelinebruzasco.currentweather.databinding.FragmentBottomSheetDialogAddLocationBinding
import com.jaquelinebruzasco.currentweather.domain.remote.model.LocationResponseModel
import com.jaquelinebruzasco.currentweather.ui.fragments.adapter.AddLocationBSDAdapter
import com.jaquelinebruzasco.currentweather.ui.viewModel.AddLocationState
import com.jaquelinebruzasco.currentweather.ui.viewModel.AddLocationViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class AddLocationBSDFragment(
    private val newListOfLocations: () -> Unit
) : BottomSheetDialogFragment() {

    private lateinit var _binding: FragmentBottomSheetDialogAddLocationBinding
    private val viewModel by viewModels<AddLocationViewModel>()
    private val addLocationBSDAdapter by lazy { AddLocationBSDAdapter(::saveLocation) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBottomSheetDialogAddLocationBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        performSearch()
        setupRecycleView()
        initObservables()
    }

    private fun initObservables() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.addLocationResponseState.collectLatest { state ->
                    when (state) {
                        is AddLocationState.Failure -> {
                            showFailureMessage(state.message)
                            _binding.svEnterLocation.isEnabled = true
                        }
                        AddLocationState.Idle -> _binding.svEnterLocation.isEnabled = true
                        AddLocationState.Loading -> _binding.svEnterLocation.isEnabled = false
                        is AddLocationState.Success -> {
                            addLocationBSDAdapter.list = state.locationData.toMutableList()
                            _binding.svEnterLocation.isEnabled = true
                        }
                    }
                }
            }
        }
    }

    private fun showFailureMessage(message: String) {
        if (message.isEmpty()) {
            Snackbar.make(
                requireView(),
                resources.getString(R.string.error_response),
                Snackbar.LENGTH_LONG
            ).show()
        } else {
            Snackbar.make(requireView(), message, Snackbar.LENGTH_LONG).show()
        }
    }

    private fun performSearch() {
        _binding.svEnterLocation.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrBlank()) {
                    if (newText.length >= 3) {
                        viewModel.loadLocation(newText)
                    }
                }
                return false
            }
        })
    }

    private fun setupRecycleView() = with(_binding) {
        rvEnterLocation.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = addLocationBSDAdapter
        }
    }

    private fun saveLocation(locationData: LocationResponseModel) {
        viewModel.insert(locationData)
        this@AddLocationBSDFragment.dismiss()
        Toast.makeText(
            requireContext(),
            getString(R.string.location_saved),
            Toast.LENGTH_LONG
        ).show()
        newListOfLocations.invoke()
    }
}