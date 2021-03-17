package com.example.weatherforcast.data.db.entity

import androidx.room.*
import com.google.gson.annotations.SerializedName
import org.threeten.bp.Instant
import org.threeten.bp.ZoneId
import org.threeten.bp.ZonedDateTime

const val CURRENT_WEATHER_ID = 0

@Entity(tableName = "current_weather")
data class StandardWeather(
    val clouds: Int,
    @SerializedName("dew_point")
    val dewPoint: Double,
    val dt: Int,
    @SerializedName("feels_like")
    val feelsLike: Double,
    val humidity: Int,
    val pressure: Int,
    val sunrise: Int,
    val sunset: Int,
    val temp: Double,
    val visibility: Int,

   // @TypeConverters(WeatherListConverter::class)
    val weather: List<Weather>,
    @SerializedName("wind_speed")
    val windSpeed: Double
)
{
    @PrimaryKey(autoGenerate = false)
    var id: Int = CURRENT_WEATHER_ID
}