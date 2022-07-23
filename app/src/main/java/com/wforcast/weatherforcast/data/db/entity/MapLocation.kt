package com.wforcast.weatherforcast.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "map_location")
data class MapLocation(
    @PrimaryKey
    val address : String,
    val maplat : Double,
    val maplon : Double
)