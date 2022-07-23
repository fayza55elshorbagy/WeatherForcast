package com.wforcast.weatherforcast.ui.locationlist

import androidx.lifecycle.ViewModel
import com.wforcast.weatherforcast.util.lazyDeferred
import com.wforcast.weatherforcast.data.db.entity.MapLocation
import com.wforcast.weatherforcast.data.repository.WeatherRepository

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