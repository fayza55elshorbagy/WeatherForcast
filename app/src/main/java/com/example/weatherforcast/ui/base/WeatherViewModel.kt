package com.example.weatherforcast.ui.base

import androidx.lifecycle.ViewModel
import com.example.weatherforcast.util.UniteSystem
import com.example.weatherforcast.util.lazyDeferred
import com.example.weatherforcast.data.provider.UnitProvider
import com.example.weatherforcast.data.repository.WeatherRepository

abstract class WeatherViewModel(
    private val weatherRepository: WeatherRepository,
    unitProvider: UnitProvider
) : ViewModel() {

    private val unitSystem = unitProvider.getUnitSystem()

    val isMetricUnit: Boolean
        get() = unitSystem == UniteSystem.METRIC

    val weatherLocation by lazyDeferred {
        weatherRepository.getWeatherLocation()
    }
}