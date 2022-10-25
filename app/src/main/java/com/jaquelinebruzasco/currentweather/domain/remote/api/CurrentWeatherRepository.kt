package com.jaquelinebruzasco.currentweather.domain.remote.api

import com.jaquelinebruzasco.currentweather.domain.remote.model.CurrentWeatherResponseModel
import com.jaquelinebruzasco.currentweather.domain.remote.model.LocationResponseModel
import retrofit2.Response
import javax.inject.Inject

class CurrentWeatherRepository @Inject constructor(
    private val api: CurrentWeatherApi
) {
    suspend fun getLocation(locationName: String): Response<List<LocationResponseModel>> {
        return api.getLocation(locationName)
    }
    suspend fun getWeather(latitude: Double, longitude: Double): Response<CurrentWeatherResponseModel> {
        return api.getWeather(latitude, longitude)
    }
}