package com.example.weatherforcast.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.weatherforcast.data.repository.WeatherRepository
import com.example.weatherforcast.data.provider.UnitProvider

class CurrentWeatherViewModelFactory (
    private val weatherRepository: WeatherRepository,
    private val unitProvider: UnitProvider
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return CurrentWeatherViewModel(weatherRepository, unitProvider) as T
    }
}