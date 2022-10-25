package com.jaquelinebruzasco.currentweather.domain.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jaquelinebruzasco.currentweather.domain.remote.model.LocationResponseModel

@Database(entities = [LocationResponseModel::class], version = 1, exportSchema = false)
abstract class CurrentWeatherDatabase : RoomDatabase() {
    abstract fun currentWeatherDao(): CurrentWeatherDao
}