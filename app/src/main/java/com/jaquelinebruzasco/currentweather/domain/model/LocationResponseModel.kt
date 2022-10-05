package com.jaquelinebruzasco.currentweather.domain.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LocationResponseModel(
    @SerializedName("name")
    val locationName: String,
    @SerializedName("lat")
    val latitude: Double,
    @SerializedName("lon")
    val longitude: Double
): Serializable