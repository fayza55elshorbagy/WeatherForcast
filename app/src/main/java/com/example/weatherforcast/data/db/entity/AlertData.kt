package com.example.weatherforcast.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "alert_data")
data class AlertData(
    @PrimaryKey(autoGenerate = false)
    var Id: Int ,
    val alertName : String,
    val alertTime : String
)
