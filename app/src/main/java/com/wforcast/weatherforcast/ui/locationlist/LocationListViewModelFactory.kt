package com.wforcast.weatherforcast.ui.locationlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wforcast.weatherforcast.data.repository.WeatherRepository

class LocationListViewModelFactory (
    private val weatherRepository: WeatherRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LocationListViewModel(weatherRepository ) as T
    }
}