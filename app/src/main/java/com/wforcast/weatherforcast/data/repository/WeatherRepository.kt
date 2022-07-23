package com.wforcast.weatherforcast.data.repository

import androidx.lifecycle.LiveData
import com.wforcast.weatherforcast.data.db.entity.AlertData
import com.wforcast.weatherforcast.data.db.entity.MapLocation
import com.wforcast.weatherforcast.data.db.entity.StandardWeatherApiResponse
import com.wforcast.weatherforcast.data.db.format.CurrentWeatherUnit

interface WeatherRepository {
   suspend fun getCurrentWeather(metric: Boolean): LiveData<out CurrentWeatherUnit>
   suspend fun getWeatherLocation():LiveData<StandardWeatherApiResponse>
   suspend fun getMapLocation():List<MapLocation>
    fun insertMapLocation(l: MapLocation)
    suspend fun deleteMApLocation(address:String)
    suspend fun insetAlertData(alertData: AlertData)
    suspend fun getAlertData():List<AlertData>
    suspend fun deleteAlert(id:Int)

   }