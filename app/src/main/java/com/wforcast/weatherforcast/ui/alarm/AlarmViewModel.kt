package com.wforcast.weatherforcast.ui.alarm

import android.util.Log
import androidx.lifecycle.ViewModel
import com.wforcast.weatherforcast.util.lazyDeferred
import com.wforcast.weatherforcast.data.db.entity.AlertData
import com.wforcast.weatherforcast.data.repository.WeatherRepository

class AlarmViewModel(
        private val weatherRepository: WeatherRepository
) : ViewModel() {

    suspend fun insertAlarmData(data : AlertData ) {
        weatherRepository.insetAlertData(data)
        Log.i("info","called repo"+data)

    }

    val alarm_data by lazyDeferred {
        weatherRepository.getAlertData()
    }
   suspend fun deleteAlarm(id : Int ): List<AlertData>{
        weatherRepository.deleteAlert(id)
       return  weatherRepository.getAlertData()
    }
}
