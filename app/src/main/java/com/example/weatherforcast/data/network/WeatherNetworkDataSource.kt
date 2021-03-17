package com.example.weatherforcast.data.network

import androidx.lifecycle.LiveData
import com.example.weatherforcast.data.db.entity.StandardWeatherApiResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<StandardWeatherApiResponse>

    suspend fun fetchCurrentWeather(
        lat: Double,
        lon: Double,
        lang : String,
        units : String
    )

}