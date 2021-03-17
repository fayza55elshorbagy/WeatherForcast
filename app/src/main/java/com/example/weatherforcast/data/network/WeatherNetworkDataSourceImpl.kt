package com.example.weatherforcast.data.network

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.weatherforcast.util.NoConnectivityException
import com.example.weatherforcast.data.db.entity.StandardWeatherApiResponse
import com.example.weatherforcast.data.network.response.WeatherApiService

class WeatherNetworkDataSourceImpl(
    private val weatherApiService: WeatherApiService
) : WeatherNetworkDataSource {
    private val _downloadedCurrentWeather = MutableLiveData<StandardWeatherApiResponse>()
    override val downloadedCurrentWeather: LiveData<StandardWeatherApiResponse>
        get() = _downloadedCurrentWeather

    override suspend fun fetchCurrentWeather(lat: Double, lon: Double, lang: String, units: String) {
        try {
            val fetchedCurrentWeather = weatherApiService
                .getCurrentWeather(lat,lon,lang,units)
                .await()
            _downloadedCurrentWeather.postValue(fetchedCurrentWeather)
        }
        catch (e: NoConnectivityException) {
            Log.e("Connectivity", "No internet connection.", e)
        }
    }
}