package com.jaquelinebruzasco.currentweather.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaquelinebruzasco.currentweather.R
import com.jaquelinebruzasco.currentweather.domain.local.CurrentWeatherLocalRepository
import com.jaquelinebruzasco.currentweather.domain.remote.api.CurrentWeatherRepository
import com.jaquelinebruzasco.currentweather.domain.remote.model.LocationResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import javax.inject.Inject

@HiltViewModel
class AddLocationViewModel @Inject constructor(
    private val repository: CurrentWeatherRepository,
    private val localRepository: CurrentWeatherLocalRepository
) : ViewModel() {

    private val _addLocationResponseState =
        MutableStateFlow<AddLocationState>(AddLocationState.Idle)
    val addLocationResponseState: StateFlow<AddLocationState> = _addLocationResponseState

    private val _insertLocationResponseState =
        MutableStateFlow<InsertLocationState>(InsertLocationState.Idle)
    val insertLocationResponseState: StateFlow<InsertLocationState> = _insertLocationResponseState

    private val listOfLocations = mutableListOf<LocationResponseModel>()

    init {
        listOfLocations.clear()
        viewModelScope.launch {
            localRepository.getAll().collectLatest {
                listOfLocations.addAll(it)
            }
        }
    }

    fun loadLocation(locationName: String) {
        viewModelScope.launch {
            _addLocationResponseState.value = AddLocationState.Loading
            val locationResponse = repository.getLocation(locationName)
            if (locationResponse.isSuccessful) {
                locationResponse.body()?.let {
                    if (it.isEmpty()) {
                        checkError(locationResponse.errorBody())
                    } else {
                        _addLocationResponseState.value = AddLocationState.Success(it)
                    }
                } ?: kotlin.run {
                    _addLocationResponseState.value =
                        AddLocationState.Failure(R.string.error_response.toString())
                }
            } else {
                checkError(locationResponse.errorBody())
            }
        }
    }

    private fun checkError(errorResponse: ResponseBody?) {
        if (errorResponse == null) {
            _addLocationResponseState.value =
                AddLocationState.Failure(R.string.error_response.toString())
        } else {
            _addLocationResponseState.value = AddLocationState.Failure(errorResponse.toString())
        }
    }

    fun insert(location: LocationResponseModel) {
        viewModelScope.launch {
            _insertLocationResponseState.update { InsertLocationState.Loading }
            if (listOfLocations.isEmpty()) {
                insertAfterValidation(location)
            } else {
                val result = listOfLocations.none {
                    it.locationName == location.locationName &&
                            it.locationState == location.locationState &&
                            it.locationCountry == location.locationCountry
                }
                if (result) {
                    insertAfterValidation(location)
                } else {
                    _insertLocationResponseState.update { InsertLocationState.Failure }
                }
            }
        }
    }

    private suspend fun insertAfterValidation(location: LocationResponseModel) {
        try {
            localRepository.insert(location)
        } catch (e:Exception) {
            _insertLocationResponseState.update { InsertLocationState.Failure }
        }
        _insertLocationResponseState.update { InsertLocationState.Success }
    }
}

sealed class AddLocationState {
    object Idle : AddLocationState()
    object Loading : AddLocationState()
    class Success(val locationData: List<LocationResponseModel>) : AddLocationState()
    class Failure(val message: String) : AddLocationState()
}

sealed class InsertLocationState {
    object Idle : InsertLocationState()
    object Loading : InsertLocationState()
    object Success : InsertLocationState()
    object Failure : InsertLocationState()
}