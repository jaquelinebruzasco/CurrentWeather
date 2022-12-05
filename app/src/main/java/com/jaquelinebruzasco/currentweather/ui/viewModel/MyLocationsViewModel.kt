package com.jaquelinebruzasco.currentweather.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jaquelinebruzasco.currentweather.domain.local.CurrentWeatherLocalRepository
import com.jaquelinebruzasco.currentweather.domain.remote.model.LocationResponseModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyLocationsFragmentViewModel @Inject constructor(
    private val localRepository: CurrentWeatherLocalRepository
): ViewModel() {

    private val _myLocationsResponseState = MutableStateFlow<MyLocationsState>(MyLocationsState.Empty)
    val myLocationsResponseState: StateFlow<MyLocationsState> = _myLocationsResponseState

    fun myLocations() {
        viewModelScope.launch {
            _myLocationsResponseState.value = MyLocationsState.Loading
            localRepository.getAll().collectLatest {
                if (it.isEmpty()) {
                    _myLocationsResponseState.value = MyLocationsState.Empty
                } else {
                    _myLocationsResponseState.value = MyLocationsState.Success(it)
                }
            }
        }
    }

    fun delete(myLocation: LocationResponseModel) = viewModelScope.launch {
        localRepository.delete(myLocation)
    }
}

sealed class MyLocationsState {
    object Empty: MyLocationsState()
    object Loading: MyLocationsState()
    class Success(val data: List<LocationResponseModel>) : MyLocationsState()
}