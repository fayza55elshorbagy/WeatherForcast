package com.example.weatherforcast.data.provider

import com.example.weatherforcast.data.db.entity.StandardWeatherApiResponse

interface LocationProvider {
    suspend fun hasLocationChanged(lastWeatherLocation: StandardWeatherApiResponse): Boolean
    suspend fun getPreferredLocationString(): String
}