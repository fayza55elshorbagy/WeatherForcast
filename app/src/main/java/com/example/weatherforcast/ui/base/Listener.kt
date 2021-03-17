package com.example.weatherforcast.ui.base

import com.example.weatherforcast.data.db.entity.AlertData
import com.example.weatherforcast.data.db.entity.MapLocation

interface Listener {
    fun go_home(lat:Double,lon:Double)
    suspend fun deleteMapLocatio(address:String)
    suspend fun deletAlert(id:Int):List<AlertData>
    fun cancelAlarm(id:Int)
}