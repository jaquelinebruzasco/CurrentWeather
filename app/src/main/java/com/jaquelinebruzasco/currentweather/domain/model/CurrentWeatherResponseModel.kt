package com.jaquelinebruzasco.currentweather.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

interface MultipleWeatherModel

data class CurrentWeatherResponseModel(
    @SerializedName("current")
    val current: CurrentInfoModel,
    @SerializedName("hourly")
    val hourly: List<HourlyInfoModel>,
    @SerializedName("daily")
    val daily: List<DailyInfoModel>
): Serializable

data class CurrentInfoModel(
    @SerializedName("dt")
    val dayTime: Int,
    @SerializedName("sunrise")
    val sunrise: Int,
    @SerializedName("sunset")
    val sunset: Int,
    @SerializedName("temp")
    val temp: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("humidity")
    val humidity: Double,
    @SerializedName("uvi")
    val uvi: Double,
    @SerializedName("wind_speed")
    val wind: Double,
    @SerializedName("weather")
    val weather: List<WeatherInfoModel>
): Serializable, MultipleWeatherModel

data class HourlyInfoModel(
    @SerializedName("dt")
    val dayTime: Int,
    @SerializedName("temp")
    val temp: Double,
    @SerializedName("weather")
    val weather: List<WeatherInfoModel>
): Serializable, MultipleWeatherModel

data class DailyInfoModel(
    @SerializedName("dt")
    val dayTime: Int,
    @SerializedName("temp")
    val temp: TemperatureInfoModel,
    @SerializedName("weather")
    val weather: List<WeatherInfoModel>
): Serializable, MultipleWeatherModel

data class WeatherInfoModel(
    @SerializedName("main")
    val main: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("icon")
    val icon: String
): Serializable

data class TemperatureInfoModel(
    @SerializedName("day")
    val day: Double,
    @SerializedName("min")
    val min: Double,
    @SerializedName("max")
    val max: Double
): Serializable