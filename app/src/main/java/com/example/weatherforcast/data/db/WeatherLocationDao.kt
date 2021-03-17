package com.example.weatherforcast.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.weatherforcast.data.db.entity.StandardWeatherApiResponse
import com.example.weatherforcast.data.db.entity.WEATHER_LOCATION_ID

@Dao
interface WeatherLocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(weatherLocation: StandardWeatherApiResponse)

    @Query("select * from weather_location where id = $WEATHER_LOCATION_ID")
    fun getLocation(): LiveData<StandardWeatherApiResponse>

    @Query("select * from weather_location where id = $WEATHER_LOCATION_ID")
    fun getLocationNonLive(): StandardWeatherApiResponse?

//    @Query("select * from weather_Maplocation where id = id")
//    fun getMapLocation(id:Int): LiveData<LocationData>
}