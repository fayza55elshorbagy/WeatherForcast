package com.wforcast.weatherforcast.ui.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.wforcast.weatherforcast.data.repository.WeatherRepository

class AlarmViewModelFactory (
    private val weatherRepository: WeatherRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AlarmViewModel(weatherRepository ) as T
    }
}