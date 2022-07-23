package com.wforcast.weatherforcast.ui.alarm

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.text.format.DateFormat
import android.util.Log
import com.wforcast.weatherforcast.ui.base.Constants
import java.util.*
import java.util.concurrent.TimeUnit

class AlarmReceiver : BroadcastReceiver() {
    private lateinit var dataForNotification : String
    private lateinit var splitedData : List<String>
    override fun onReceive(context: Context, intent: Intent) {
        dataForNotification = intent.getStringExtra(Constants.EXTRA_EXACT_ALARM_TIME)!!
        splitedData = dataForNotification?.split(",")
        when (intent.action) {
            Constants.ACTION_SET_EXACT -> {
                Log.i("info",splitedData[2])
                if(splitedData[2].equals("notify"))
                {
                    Notifications.showNotification_withSound(context, splitedData.get(0), splitedData.get(1))
                }
                else
                    Notifications.showNotification_withAlarm(context, splitedData.get(0), splitedData.get(1))



            }

            Constants.ACTION_SET_REPETITIVE_EXACT -> {
                setRepetitiveAlarm(AlarmService(context as Activity))
                if(splitedData[2].equals("notify"))
                {
                    Notifications.showNotification_withSound(context, splitedData.get(0), splitedData.get(1))
                }
                else
                    Notifications.showNotification_withAlarm(context, splitedData.get(0), splitedData.get(1))
            }
        }
    }


    private fun setRepetitiveAlarm(alarmService: AlarmService) {
        val cal = Calendar.getInstance().apply {
            this.timeInMillis = timeInMillis + TimeUnit.DAYS.toMillis(7)
        }
        alarmService.setRepetitiveAlarm(cal.timeInMillis,splitedData.get(0), splitedData.get(1),splitedData.get(2),1)
    }


}