package com.example.weatherforcast.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.weatherforcast.data.db.entity.CURRENT_WEATHER_ID
import com.example.weatherforcast.data.db.entity.StandardWeather
import com.example.weatherforcast.data.db.format.CurrentWeatherImperialFormat
import com.example.weatherforcast.data.db.format.CurrentWeatherMetricFormat


@Dao
interface CurrentWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(standardWeather: StandardWeather)

    @Query("select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun getWeatherMetric(): LiveData<CurrentWeatherMetricFormat>

//    @Query("select * from current_weather where id = $CURRENT_WEATHER_ID")
//    fun getWeather(): LiveData<StandardWeather>

    @Query("select * from current_weather where id = $CURRENT_WEATHER_ID")
    fun getWeatherImperial(): LiveData<CurrentWeatherImperialFormat>
}