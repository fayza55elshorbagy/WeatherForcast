package com.wforcast.weatherforcast.ui.alarm

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.os.Bundle
import android.text.format.DateFormat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.wforcast.weatherforcast.R
import com.wforcast.weatherforcast.data.db.entity.AlertData
import com.wforcast.weatherforcast.ui.base.Listener
import com.wforcast.weatherforcast.ui.base.RandomUtil
import com.wforcast.weatherforcast.ui.base.SharedViewModel
import com.wforcast.weatherforcast.ui.weather.CurrentWeatherViewModel
import com.wforcast.weatherforcast.ui.weather.CurrentWeatherViewModelFactory
import kotlinx.android.synthetic.main.alarm_setting_fragment.*
import kotlinx.coroutines.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.CoroutineContext


class AlarmSettingFragment : DialogFragment(), KodeinAware, CoroutineScope {

    private lateinit var job: Job

    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.Main

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()
    private lateinit var currentWeatherViewModel: CurrentWeatherViewModel

    private val alarmViewModelFactory: AlarmViewModelFactory by instance()
    private lateinit var alarmViewModel: AlarmViewModel
    lateinit var alarmService: AlarmService
    private var timeInmilli : Long? = 0L
    private lateinit var sharedViewModel: SharedViewModel
    var id:Int? = 1
    private lateinit var listener: Listener

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        currentWeatherViewModel = ViewModelProvider(this,viewModelFactory).get(CurrentWeatherViewModel::class.java)
        alarmViewModel = ViewModelProvider(this,alarmViewModelFactory).get(AlarmViewModel::class.java)
        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        var rootView : View = inflater.inflate(R.layout.alarm_setting_fragment,container,false)
        return rootView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        job = Job()
        alarmService = AlarmService(requireActivity())
        set_alarm.setOnClickListener {
            launch {
               alarmChecksEvents()
            }
        }
        alarm_check_date.setOnClickListener {
           getTimeInMili()
        }
    }

    private fun sendMessageToServiceJustOnce(callback: (Long) -> Unit)
    {
        callback(timeInmilli!!)
        Log.i("info",timeInmilli!!.toString()+"inside get time")

    }
    private fun sendMessageToServiceRepetitive(callback: (Long) -> Unit)
    {
        callback(timeInmilli!!)
        Log.i("info",timeInmilli!!.toString()+"inside get time")

    }
    private fun getTimeInMili() : Long {
        Calendar.getInstance().apply {
            this.set(Calendar.SECOND, 0)
            this.set(Calendar.MILLISECOND, 0)
            DatePickerDialog(
                    requireContext(),
                    0,
                    { _, year, month, day ->
                        this.set(Calendar.YEAR, year)
                        this.set(Calendar.MONTH, month)
                        this.set(Calendar.DAY_OF_MONTH, day)
                        TimePickerDialog(
                                requireContext(),
                                0,
                                { _, hour, minute ->
                                    this.set(Calendar.HOUR_OF_DAY, hour)
                                    this.set(Calendar.MINUTE, minute)
//                                    callback(this.timeInMillis)
                                    timeInmilli = this.timeInMillis
                                },
                                this.get(Calendar.HOUR_OF_DAY),
                                this.get(Calendar.MINUTE),
                                false
                        ).show()

                    },
                    this.get(Calendar.YEAR),
                    this.get(Calendar.MONTH),
                    this.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        return this!!.timeInmilli!!
    }
//
//    private lateinit var  description :String
//    private lateinit var   time :String
//    var alarmDescription = ArrayList<String>()
//    var alraTime = ArrayList<String>()
//    private fun events()
//    {
//        GlobalScope.launch {
//            val j1 = launch {
//                val weatherDetails = currentWeatherViewModel.weatherLocation.await()
//                weatherDetails.observe(this@AlarmSettingFragment, androidx.lifecycle.Observer {
//                    if (it == null) return@Observer
//                    description = it.standardWeather?.weather?.get(0)?.description.toString()
//                    time = timeAndDateFormat(it.standardWeather?.dt.toString())
//                    Log.i("info","weather"+description+"----"+time)
//                })
//            }
//            val j2 = launch {
//                val alarmDetails = alarmViewModel.alarm_data.await()
//                for (i in alarmDetails.indices) {
//                   alarmDescription.add(alarmDetails.get(i).alertName)
//                   alraTime.add(alarmDetails.get(i).alertTime)
//                }
//                delay(5000)
//            }
//            j1.join()
//            for (i in alarmDescription.indices) {
//                if(description.contains(alarmDescription.get(i)) && time.equals(alraTime.get(i)) )
//                {
//                    if (just_once.isChecked)
//                        sendMessageToServiceJustOnce { alarmService.setExactAlarm(it, description!!, "Stay at Home for Safety") }
//                    else
//                        sendMessageToServiceRepetitive { alarmService.setRepetitiveAlarm(it, description!!, "Stay at Home for Safety") }
//
//                }
//
//            }
//
//        }
//
//
//
//    }

    private suspend fun alarmChecksEvents() {
           id = RandomUtil.getRandomInt()

        var j = launch{
            val weatherDetails = currentWeatherViewModel.weatherLocation.await()
            //val alarmDetails = alarmViewModel.alarm_data.await()
            Log.i("info", "called")
            weatherDetails.observe(this@AlarmSettingFragment, androidx.lifecycle.Observer {
                if (it == null) return@Observer
                val description = it.standardWeather?.weather?.get(0)?.description
                Log.i("info", description!! + """"""""")
                if (rain_radio_button.isChecked) {
                    runBlocking {
                        alarmViewModel.insertAlarmData(AlertData(id!!,"Alert : Rain",convertDate(timeInmilli!!)))
                        Log.i("info", "added")
                    }
                    if (description?.contains("rain")!!) {
                        if (just_once.isChecked)
                        {
                            if(notification.isChecked)
                                sendMessageToServiceJustOnce { alarmService.setExactAlarm(it, description!!, "Stay at Home for Safety","notify",id!!) }
                            else
                                sendMessageToServiceJustOnce { alarmService.setExactAlarm(it, description!!, "Stay at Home for Safety","alarm",id!!) }
                        }
                        else
                        {
                            if(notification.isChecked)
                                sendMessageToServiceRepetitive { alarmService.setExactAlarm(it, description!!, "Stay at Home for Safety","notify",id!!) }
                            else
                                sendMessageToServiceRepetitive { alarmService.setExactAlarm(it, description!!, "Stay at Home for Safety","alarm",id!!) }

                        }
                    }

                } else if (cloud_radio_button.isChecked) {
                    runBlocking {
                        alarmViewModel.insertAlarmData(AlertData(id!!,"Alert : Cloud",convertDate(timeInmilli!!))) }
                    if (description?.contains("cloud")!!) {
                        if (just_once.isChecked)
                        {
                            if(notification.isChecked)
                            {
                                sendMessageToServiceJustOnce { alarmService.setExactAlarm(it, description!!, "Stay at Home for Safety","notify",id!!) }
                                Log.i("info",id.toString()+"inside cloud")


                            }
                            else
                                sendMessageToServiceJustOnce { alarmService.setExactAlarm(it, description!!, "Stay at Home for Safety","alarm",id!!) }
                        }
                        else
                        {
                            if(notification.isChecked)
                                sendMessageToServiceRepetitive { alarmService.setExactAlarm(it, description!!, "Stay at Home for Safety","notify",id!!) }
                            else
                                sendMessageToServiceRepetitive { alarmService.setExactAlarm(it, description!!, "Stay at Home for Safety","alarm",id!!) }

                        }
                    }
                } else if (wind_radio_button.isChecked) {
                    runBlocking {
                        alarmViewModel.insertAlarmData(AlertData(id!!,"Alert : Wind",convertDate(timeInmilli!!))) }
                    if (description?.contains("wind")!!) {
                        if (just_once.isChecked)
                        {
                            if(notification.isChecked)
                                sendMessageToServiceJustOnce { alarmService.setExactAlarm(it, description!!, "Stay at Home for Safety","notify",id!!) }
                            else
                                sendMessageToServiceJustOnce { alarmService.setExactAlarm(it, description!!, "Stay at Home for Safety","alarm",id!!) }
                        }
                        else
                        {
                            if(notification.isChecked)
                                sendMessageToServiceRepetitive { alarmService.setExactAlarm(it, description!!, "Stay at Home for Safety","notify",id!!) }
                            else
                                sendMessageToServiceRepetitive { alarmService.setExactAlarm(it, description!!, "Stay at Home for Safety","alarm",id!!) }

                        }
                    }
                } else if (fog_radio_button.isChecked) {
                    runBlocking {
                        alarmViewModel.insertAlarmData(AlertData(id!!,"Alert : Fog",convertDate(timeInmilli!!))) }
                    if (description?.contains("fog")!!) {
                        if (just_once.isChecked)
                        {
                            if(notification.isChecked)
                                sendMessageToServiceJustOnce { alarmService.setExactAlarm(it, description!!, "Stay at Home for Safety","notify",id!!) }
                            else
                                sendMessageToServiceJustOnce { alarmService.setExactAlarm(it, description!!, "Stay at Home for Safety","alarm",id!!) }
                        }
                        else
                        {
                            if(notification.isChecked)
                                sendMessageToServiceRepetitive { alarmService.setExactAlarm(it, description!!, "Stay at Home for Safety","notify",id!!) }
                            else
                                sendMessageToServiceRepetitive { alarmService.setExactAlarm(it, description!!, "Stay at Home for Safety","alarm",id!!) }

                        }
                    }
                } else if (storm_radio_button.isChecked) {
                    runBlocking {
                        alarmViewModel.insertAlarmData(AlertData(id!!,"Alert : Storm",convertDate(timeInmilli!!))) }
                    if (description?.contains("clear")!!) {
                        if (just_once.isChecked)
                        {
                            if(notification.isChecked)
                                sendMessageToServiceJustOnce { alarmService.setExactAlarm(it, description!!, "Stay at Home for Safety","notify",id!!) }
                            else
                                sendMessageToServiceJustOnce { alarmService.setExactAlarm(it, description!!, "Stay at Home for Safety","alarm",id!!) }
                        }
                        else
                        {
                            if(notification.isChecked)
                                sendMessageToServiceRepetitive { alarmService.setExactAlarm(it, description!!, "Stay at Home for Safety","notify",id!!) }
                            else
                                sendMessageToServiceRepetitive { alarmService.setExactAlarm(it, description!!, "Stay at Home for Safety","alarm",id!!) }

                        }
                    }
                }
                if(it != null)
                {
                    runBlocking {
                       sharedViewModel.select(5)
                        Log.i("info","5--f1")
                    }
                    fragmentManager?.findFragmentByTag("add Alarm")?.let {
                        (it as DialogFragment).dismiss() }
                    fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment, AlarmFragment())?.addToBackStack(null)?.commit()

                }
            }

            )
        }

    }
    private fun convertDate(timeInMillis: Long): String =
            DateFormat.format("dd MM yyyy hh:mm", timeInMillis).toString()

    override fun onDestroy() {
        super.onDestroy()
        Log.i("info","destroy")
        job.cancel()

    }

}