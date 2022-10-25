package com.jaquelinebruzasco.currentweather.domain.remote.api

import com.jaquelinebruzasco.currentweather.domain.remote.model.ApiConstants
import com.jaquelinebruzasco.currentweather.domain.remote.model.CurrentWeatherResponseModel
import com.jaquelinebruzasco.currentweather.domain.remote.model.LocationResponseModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CurrentWeatherApi {

    @GET("/geo/1.0/direct")
    suspend fun getLocation(
        @Query("q")
        locationName: String,
        @Query("appid")
        apiKey: String = ApiConstants.API_KEY
    ): Response<List<LocationResponseModel>>

    @GET("/data/2.5/onecall")
    suspend fun getWeather(
        @Query("lat")
        latitude: Double,
        @Query("lon")
        longitude: Double,
        @Query("appid")
        apiKey: String = ApiConstants.API_KEY,
        @Query("units")
        unit: String = ApiConstants.UNIT
    ): Response<CurrentWeatherResponseModel>
}