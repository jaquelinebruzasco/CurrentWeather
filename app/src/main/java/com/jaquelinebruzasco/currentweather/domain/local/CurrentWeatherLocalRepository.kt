package com.jaquelinebruzasco.currentweather.domain.local

import com.jaquelinebruzasco.currentweather.domain.remote.model.LocationResponseModel
import javax.inject.Inject

class CurrentWeatherLocalRepository @Inject constructor(
    private val dao: CurrentWeatherDao
) {
    suspend fun insert(locationName: LocationResponseModel) = dao.insert(locationName)
    fun getAll() = dao.getAll()
    suspend fun delete(locationName: LocationResponseModel) = dao.delete(locationName)
}