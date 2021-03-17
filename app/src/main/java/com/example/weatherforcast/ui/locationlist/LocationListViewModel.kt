package com.example.weatherforcast.ui.locationlist

import androidx.lifecycle.ViewModel
import com.example.weatherforcast.util.lazyDeferred
import com.example.weatherforcast.data.db.entity.MapLocation
import com.example.weatherforcast.data.repository.WeatherRepository

class LocationListViewModel(
    private val weatherRepository: WeatherRepository
    ) : ViewModel() {

    val map_location by lazyDeferred {
        weatherRepository.getMapLocation()
    }
    suspend fun delete(address: String): List<MapLocation> {
        weatherRepository.deleteMApLocation(address)
        Thread.sleep(500)
        return  weatherRepository.getMapLocation()
    }



}