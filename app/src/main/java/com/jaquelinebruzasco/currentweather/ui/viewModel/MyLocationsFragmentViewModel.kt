package com.jaquelinebruzasco.currentweather.ui.viewModel

import androidx.lifecycle.ViewModel
import com.jaquelinebruzasco.currentweather.domain.local.CurrentWeatherLocalRepository
import com.jaquelinebruzasco.currentweather.domain.remote.api.CurrentWeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MyLocationsFragmentViewModel @Inject constructor(
    private val repository: CurrentWeatherRepository,
    private val localRepository: CurrentWeatherLocalRepository
): ViewModel() {
}