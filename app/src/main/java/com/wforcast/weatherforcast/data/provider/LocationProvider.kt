package com.wforcast.weatherforcast.data.provider

import com.wforcast.weatherforcast.data.db.entity.StandardWeatherApiResponse

interface LocationProvider {
    suspend fun hasLocationChanged(lastWeatherLocation: StandardWeatherApiResponse): Boolean
    suspend fun getPreferredLocationString(): String
}