package com.jaquelinebruzasco.currentweather.domain.remote.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "MyLocations")
data class LocationResponseModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    @SerializedName("name")
    val locationName: String,
    @SerializedName("lat")
    val latitude: Double,
    @SerializedName("lon")
    val longitude: Double,
    @SerializedName("country")
    val locationCountry: String,
    @SerializedName("state")
    val locationState: String
) : Serializable