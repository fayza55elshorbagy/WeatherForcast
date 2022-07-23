package com.wforcast.weatherforcast.data.db.entity

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

class WeatherListConverter {
    @TypeConverter
    fun convertWeathertoObject(data: String?): List<Weather?>? {
        val gson = Gson()
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object :
            TypeToken<List<Weather?>?>() {}.type
        return gson.fromJson<List<Weather?>>(data, listType)
    }

    @TypeConverter
    fun convertWeathertoString(myObjects: List<Weather?>?): String? {
        val gson = Gson()
        return gson.toJson(myObjects)
    }

    @TypeConverter
    fun convertDailyrtoObject(data: String?): List<Daily?>? {
        val gson = Gson()
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object :
            TypeToken<List<Daily?>?>() {}.type
        return gson.fromJson<List<Daily?>>(data, listType)
    }

    @TypeConverter
    fun convertDailytoString(myObjects: List<Daily?>?): String? {
        val gson = Gson()
        return gson.toJson(myObjects)
    }


    @TypeConverter
    fun convertHourlyrtoObject(data: String?): List<Hourly?>? {
        val gson = Gson()
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object :
            TypeToken<List<Hourly?>?>() {}.type
        return gson.fromJson<List<Hourly?>>(data, listType)
    }

    @TypeConverter
    fun convertHourlytoString(myObjects: List<Hourly?>?): String? {
        val gson = Gson()
        return gson.toJson(myObjects)
    }

    @TypeConverter
    fun convertAlertstoObject(data: String?): List<Alerts?>? {
        val gson = Gson()
        if (data == null) {
            return Collections.emptyList()
        }
        val listType: Type = object :
            TypeToken<List<Alerts?>?>() {}.type
        return gson.fromJson<List<Alerts?>>(data, listType)
    }

    @TypeConverter
    fun convertAlertstoString(myObjects: List<Alerts?>?): String? {
        val gson = Gson()
        return gson.toJson(myObjects)
    }

}