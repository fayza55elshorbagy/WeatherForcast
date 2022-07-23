package com.wforcast.weatherforcast.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wforcast.weatherforcast.data.db.entity.MapLocation

@Dao
interface MapLocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(mapLocation: MapLocation)

    @Query("select * from map_location" )
    fun getMapLocation(): List<MapLocation>

    @Query("DELETE FROM map_location WHERE address = :address")
     fun deleteLocation(address :String)
}