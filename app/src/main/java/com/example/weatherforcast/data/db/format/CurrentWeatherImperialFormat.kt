package com.example.weatherforcast.data.db.format

import androidx.room.ColumnInfo
import com.example.weatherforcast.data.db.entity.Weather
import com.example.weatherforcast.data.db.format.CurrentWeatherUnit

data class CurrentWeatherImperialFormat(
        @ColumnInfo(name = "temp")
    override val temp: Double,
        @ColumnInfo(name = "windSpeed")
    override val wind_speed: Double,
        @ColumnInfo(name = "feelsLike")
    override val feelsLike: Double,
        override val clouds: Int,
        override val humidity: Int,
        override val pressure: Int,
        override val weather: List<Weather>,
        override val dt: Int
) : CurrentWeatherUnit
