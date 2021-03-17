package com.example.weatherforcast.data.db.format

import com.example.weatherforcast.data.db.entity.Weather
import com.example.weatherforcast.data.db.entity.WeatherListConverter

interface CurrentWeatherUnit {
    val temp: Double
    val wind_speed: Double
    val feelsLike: Double

    val clouds: Int
    val humidity: Int
    val pressure: Int
    val weather: List<Weather>
    val dt:Int
}