package com.jaquelinebruzasco.currentweather.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jaquelinebruzasco.currentweather.R
import com.jaquelinebruzasco.currentweather.databinding.FragmentHomeBinding
import com.jaquelinebruzasco.currentweather.domain.remote.model.CurrentWeatherResponseModel
import com.jaquelinebruzasco.currentweather.ui.convertToDayDateHour
import com.jaquelinebruzasco.currentweather.ui.convertToHour
import com.jaquelinebruzasco.currentweather.ui.fragments.adapter.DailyAdapter
import com.jaquelinebruzasco.currentweather.ui.fragments.adapter.HourlyAdapter
import com.jaquelinebruzasco.currentweather.ui.loadIcon
import com.jaquelinebruzasco.currentweather.ui.viewModel.CurrentWeatherState
import com.jaquelinebruzasco.currentweather.ui.viewModel.HomeFragmentViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class HomeFragment: Fragment() {

    private val viewModel by viewModels<HomeFragmentViewModel>()
    private lateinit var _binding: FragmentHomeBinding
    private lateinit var hourlyAdapter: HourlyAdapter
    private lateinit var dailyAdapter: DailyAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return _binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding.content.visibility = View.GONE
        _binding.layoutSun.root.visibility = View.GONE
        _binding.layoutInfo.root.visibility = View.GONE
        performSearch()
        setupRecycleView()
        initObservables()
    }

    private fun setupRecycleView() = with(_binding) {
        hourlyAdapter = HourlyAdapter()
        rvContentHourly.apply {
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = hourlyAdapter
        }
        dailyAdapter = DailyAdapter()
        rvContentDaily.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = dailyAdapter
        }
    }

    private fun initObservables() {
        lifecycleScope.launchWhenCreated {
            viewModel.weatherResponseState.collectLatest { state ->
                when (state) {
                    is CurrentWeatherState.Idle -> hideProgressBar()
                    is CurrentWeatherState.Loading -> showProgressBar()
                    is CurrentWeatherState.Failure -> {
                        hideProgressBar()
                        showFailureMessage(state.message)
                    }
                    is CurrentWeatherState.Success -> {
                        hideProgressBar()
                        loadView(state.weatherData, state.locationName)
                    }
                }
            }
        }
    }

    private fun loadView(data: CurrentWeatherResponseModel, locationName: String) {
        _binding.apply {
            tvCurrentTemp.text = resources.getString(R.string.temp_symbol, data.current.temp.toInt())
            tvLocation.text = locationName
            tvLocation.setCompoundDrawablesWithIntrinsicBounds(0,0, R.drawable.ic_location, 0)
            tvLocation.compoundDrawablePadding = 8
            loadIcon(
                imageView = ivIcon,
                code = data.current.weather[0].icon
            )
            tvDescription.text = data.current.weather[0].description
            tvCurrentMax.text = resources.getString(R.string.temp_symbol_bar, data.daily[0].temp.max.toInt())
            tvCurrentMin.text = resources.getString(R.string.temp_symbol, data.daily[0].temp.min.toInt())
            tvFeelsLikeInfo.text = resources.getString(R.string.feels_like_temp_symbol, data.current.feelsLike.toInt())
            tvDayTime.text = data.current.dayTime.convertToDayDateHour()
            layoutSun.root.visibility = View.VISIBLE
            layoutInfo.root.visibility = View.VISIBLE
            layoutSun.tvTimeSunrise.text = data.current.sunrise.convertToHour()
            layoutSun.tvTimeSunset.text = data.current.sunset.convertToHour()
            when(data.current.uvi.toInt()) {
                in 0..2 -> layoutInfo.tvUviInfo.text = resources.getString(R.string.uvi_low)
                in 3..5 -> layoutInfo.tvUviInfo.text = resources.getString(R.string.uvi_moderate)
                in 6..7 -> layoutInfo.tvUviInfo.text = resources.getString(R.string.uvi_high)
                in 8..10 -> layoutInfo.tvUviInfo.text = resources.getString(R.string.uvi_very_high)
                else -> layoutInfo.tvUviInfo.text = resources.getString(R.string.uvi_extreme)
            }
            layoutInfo.tvWindInfo.text = resources.getString(R.string.velocity_symbol, data.current.wind.toInt())
            layoutInfo.tvHumidityInfo.text = resources.getString(R.string.percentage_symbol, data.current.humidity.toInt())
            hourlyAdapter.list = data.hourly
            dailyAdapter.list = data.daily
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

    private fun performSearch() {
        _binding.svSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { viewModel.loadLocation(it) }
                return false
            }

            override fun onQueryTextChange(newText: String?) = false
        })
    }
}

