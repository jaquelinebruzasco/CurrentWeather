package com.jaquelinebruzasco.currentweather.domain.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import com.jaquelinebruzasco.currentweather.domain.remote.model.LocationResponseModel

@Dao
interface CurrentWeatherDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(locationName: LocationResponseModel) : Long

    @Query("SELECT * FROM mylocations order by locationName")
    fun getAll(): Flow<List<LocationResponseModel>>

    @Delete
    suspend fun delete(locationName: LocationResponseModel)
}