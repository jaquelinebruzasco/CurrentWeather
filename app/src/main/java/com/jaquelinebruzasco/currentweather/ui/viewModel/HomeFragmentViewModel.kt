package com.jaquelinebruzasco.currentweather.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaquelinebruzasco.currentweather.R
import com.jaquelinebruzasco.currentweather.domain.remote.api.CurrentWeatherRepository
import com.jaquelinebruzasco.currentweather.domain.remote.model.CurrentWeatherResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class HomeFragmentViewModel @Inject constructor(
    private val repository: CurrentWeatherRepository
): ViewModel() {

    private val _weatherResponseState = MutableStateFlow<CurrentWeatherState>(CurrentWeatherState.Idle)
    val weatherResponseState: StateFlow<CurrentWeatherState> = _weatherResponseState

    fun loadLocation(location: String) {
        viewModelScope.launch {
            _weatherResponseState.value = CurrentWeatherState.Loading
            val locationResponse = repository.getLocation(location)
            if (locationResponse.isSuccessful) {
                locationResponse.body()?.let {
                    if (it.isEmpty()) {
                        checkError(locationResponse.errorBody())
                    } else {
                        loadWeatherInfo(locationName = it[0].locationName, latitude = it[0].latitude, longitude = it[0].longitude)
                    }
                } ?: kotlin.run { _weatherResponseState.value = CurrentWeatherState.Failure(R.string.error_response.toString()) }
            } else {
                checkError(locationResponse.errorBody())
            }
        }
    }

    private fun loadWeatherInfo(locationName: String, latitude: Double, longitude: Double) {
        viewModelScope.launch {
            val weatherResponse = repository.getWeather(latitude, longitude)
            if (weatherResponse.isSuccessful) {
                weatherResponse.body()?.let {
                    _weatherResponseState.value = CurrentWeatherState.Success(it, locationName)
                } ?: kotlin.run { _weatherResponseState.value = CurrentWeatherState.Failure(R.string.error_response.toString()) }
            } else {
                checkError(weatherResponse.errorBody())
            }
        }
    }

    private fun checkError(errorResponse: ResponseBody?) {
        if (errorResponse == null) {
            _weatherResponseState.value = CurrentWeatherState.Failure(R.string.error_response.toString())
        } else {
            _weatherResponseState.value = CurrentWeatherState.Failure(errorResponse.toString())
        }
    }
}

sealed class CurrentWeatherState {
    object Idle: CurrentWeatherState()
    object Loading: CurrentWeatherState()
    class Success(val weatherData: CurrentWeatherResponseModel, val locationName: String) : CurrentWeatherState()
    class Failure(val message: String) : CurrentWeatherState()
}