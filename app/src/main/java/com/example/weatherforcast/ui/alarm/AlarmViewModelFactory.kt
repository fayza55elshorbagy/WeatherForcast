package com.example.weatherforcast.ui.alarm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforcast.data.repository.WeatherRepository
import com.example.weatherforcast.data.provider.UnitProvider

class AlarmViewModelFactory (
    private val weatherRepository: WeatherRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return AlarmViewModel(weatherRepository ) as T
    }
}