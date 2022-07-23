package com.wforcast.weatherforcast.data.db.format

import com.wforcast.weatherforcast.data.db.entity.Weather


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