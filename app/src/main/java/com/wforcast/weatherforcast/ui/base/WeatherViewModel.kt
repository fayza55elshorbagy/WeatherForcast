package com.wforcast.weatherforcast.ui.base

import androidx.lifecycle.ViewModel
import com.wforcast.weatherforcast.util.UniteSystem
import com.wforcast.weatherforcast.util.lazyDeferred
import com.wforcast.weatherforcast.data.provider.UnitProvider
import com.wforcast.weatherforcast.data.repository.WeatherRepository

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