package com.wforcast.weatherforcast.ui.alarm

import android.app.Activity
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import com.wforcast.weatherforcast.ui.base.Constants


class AlarmService (private val activity: Activity){
    private val alarmManager: AlarmManager? =
            activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager?

    fun setExactAlarm(timeInMillis: Long,description:String,details:String,alarm_sound:String,alarm_id:Int) {
        setAlarm(
                timeInMillis,
                getPendingIntent(
                        getIntent().apply {
                            action = Constants.ACTION_SET_EXACT
                            putExtra(Constants.EXTRA_EXACT_ALARM_TIME, description+","+details+","+alarm_sound)
                        }
                ,alarm_id)
        )
        Log.i("info",timeInMillis.toString()+"inside set exact alarm")
    }

    //1 Week
    fun setRepetitiveAlarm(timeInMillis: Long,description:String,details:String,alarm_sound:String,alarm_id:Int) {
        setAlarm(
                timeInMillis,
                getPendingIntent(
                        getIntent().apply {
                            action = Constants.ACTION_SET_REPETITIVE_EXACT
                            putExtra(Constants.EXTRA_EXACT_ALARM_TIME,description+","+details+","+alarm_sound+","+timeInMillis)
                        }
                ,alarm_id)
        )
    }

    private fun getPendingIntent(intent: Intent,id:Int) =
            PendingIntent.getBroadcast(
                    activity,
                    id,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT
            )


    private fun setAlarm(timeInMillis: Long, pendingIntent: PendingIntent) {
        alarmManager?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        timeInMillis,
                        pendingIntent
                )
                Log.i("info",timeInMillis.toString()+"inside set alarm..")

            } else {
                alarmManager.setExact(
                        AlarmManager.RTC_WAKEUP,
                        timeInMillis,
                        pendingIntent
                )
                Log.i("info",timeInMillis.toString()+"inside set alarm.....")

            }
        }
    }



     fun cancelAlarm(id: Int) {
        Log.i("info",id.toString()+"id")

         val pendingBroadcastIntent = getPendingIntent(getIntent(),id)
         alarmManager!!.cancel(pendingBroadcastIntent)
         pendingBroadcastIntent.cancel()
    }

    private fun getIntent() = Intent(activity, AlarmReceiver::class.java)

}