package com.example.weatherforcast.ui.weather

import androidx.lifecycle.ViewModel
import com.example.weatherforcast.util.UniteSystem
import com.example.weatherforcast.util.lazyDeferred
import com.example.weatherforcast.data.repository.WeatherRepository
import com.example.weatherforcast.data.provider.UnitProvider

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