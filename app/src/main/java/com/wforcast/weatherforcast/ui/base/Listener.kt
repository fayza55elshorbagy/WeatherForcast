package com.wforcast.weatherforcast.ui.base

import com.wforcast.weatherforcast.data.db.entity.AlertData

interface Listener {
    fun go_home(lat:Double,lon:Double)
    suspend fun deleteMapLocatio(address:String)
    suspend fun deletAlert(id:Int):List<AlertData>
    fun cancelAlarm(id:Int)
}