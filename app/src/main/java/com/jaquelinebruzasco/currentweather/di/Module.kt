package com.jaquelinebruzasco.currentweather.di

import android.content.Context
import androidx.room.Room
import com.jaquelinebruzasco.currentweather.domain.local.CurrentWeatherDatabase
import com.jaquelinebruzasco.currentweather.domain.local.DatabaseConstants.Companion.DATABASE_NAME
import com.jaquelinebruzasco.currentweather.domain.remote.api.CurrentWeatherApi
import com.jaquelinebruzasco.currentweather.domain.remote.model.ApiConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object Module {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(
        context,
        CurrentWeatherDatabase::class.java,
        DATABASE_NAME
    ).build()

    @Singleton
    @Provides
    fun provideCurrentWeatherDao(database: CurrentWeatherDatabase) = database.currentWeatherDao()

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConstants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideServiceApi(retrofit: Retrofit): CurrentWeatherApi {
        return retrofit.create(CurrentWeatherApi::class.java)
    }
}