package com.wforcast.weatherforcast.data.network

import androidx.lifecycle.LiveData
import com.wforcast.weatherforcast.data.db.entity.StandardWeatherApiResponse

interface WeatherNetworkDataSource {
    val downloadedCurrentWeather: LiveData<StandardWeatherApiResponse>

    suspend fun fetchCurrentWeather(
        lat: Double,
        lon: Double,
        lang : String,
        units : String
    )

}