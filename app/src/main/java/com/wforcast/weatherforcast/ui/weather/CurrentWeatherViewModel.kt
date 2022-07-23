package com.wforcast.weatherforcast.ui.weather

import androidx.lifecycle.ViewModel
import com.wforcast.weatherforcast.util.UniteSystem
import com.wforcast.weatherforcast.util.lazyDeferred
import com.wforcast.weatherforcast.data.repository.WeatherRepository
import com.wforcast.weatherforcast.data.provider.UnitProvider

open class CurrentWeatherViewModel(
    private val weatherRepository: WeatherRepository,
    unitProvider: UnitProvider
    ) : ViewModel() {
    private val uniteSystem = unitProvider.getUnitSystem()

    val isMetric:Boolean
         get() = uniteSystem == UniteSystem.METRIC

    val weather by lazyDeferred {
        weatherRepository.getCurrentWeather(isMetric)
    }

    val weatherLocation by lazyDeferred {
        weatherRepository.getWeatherLocation()
    }


}