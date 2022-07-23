package com.wforcast.weatherforcast.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wforcast.weatherforcast.data.db.entity.AlertData


@Dao
interface AlertDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(alertData: AlertData)

    @Query("select * from alert_data" )
    fun getAlertData(): List<AlertData>

    @Query("DELETE FROM alert_data WHERE Id = :id")
     fun deleteAlert(id:Int)
}