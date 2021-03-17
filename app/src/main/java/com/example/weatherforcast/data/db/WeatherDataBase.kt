package com.example.weatherforcast.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherforcast.data.db.entity.*

@Database(
    entities = [StandardWeather::class, StandardWeatherApiResponse::class, MapLocation::class,AlertData::class],
    version = 1
)
 @TypeConverters(WeatherListConverter::class)
abstract class  WeatherDataBase : RoomDatabase(){
    abstract fun currentWeatherDao(): CurrentWeatherDao
    abstract fun weatherLocationDao(): WeatherLocationDao
    abstract fun mapLocationDao(): MapLocationDao
    abstract fun alertDao(): AlertDao
    companion object {
        @Volatile private var instance: WeatherDataBase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(context.applicationContext,
                WeatherDataBase::class.java, "Weather.db")
                .build()
    }
}
