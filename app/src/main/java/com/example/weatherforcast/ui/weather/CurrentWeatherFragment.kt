package com.example.weatherforcast.ui.weather

import android.app.Activity
import android.icu.util.ULocale.getLanguage
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.preference.ListPreference
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherforcast.R
import com.example.weatherforcast.data.provider.CUSTOM_LOCATION
import com.example.weatherforcast.data.provider.LanguageProvider
import com.example.weatherforcast.ui.alarm.Notifications
import com.example.weatherforcast.ui.base.LocalHelper
import com.example.weatherforcast.ui.base.ScopedFragment
import com.example.weatherforcast.ui.locationlist.LocationListFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.coroutines.launch
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import org.kodein.di.generic.instance
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CurrentWeatherFragment : ScopedFragment(),KodeinAware {

    override val kodein: Kodein by closestKodein()
    private val viewModelFactory: CurrentWeatherViewModelFactory by instance()

    private lateinit var currentWeatherViewModel: CurrentWeatherViewModel


    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        currentWeatherViewModel = ViewModelProvider(this,viewModelFactory).get(CurrentWeatherViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        return root
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        BottomSheetBehavior.from(sheet).apply {
            peekHeight = 700
            this.state = BottomSheetBehavior.STATE_COLLAPSED
        }
        bindUI()
      }

    private fun bindUI() = launch {
        val currentWeather = currentWeatherViewModel.weather.await()
        val weatherLocation_and_FutureWeather = currentWeatherViewModel.weatherLocation.await()


        weatherLocation_and_FutureWeather.observe(this@CurrentWeatherFragment, Observer { weatherLocation ->
          if (weatherLocation == null) return@Observer
              updateLocation(weatherLocation.lat,weatherLocation.lon)
            //notify of disaster
            if(weatherLocation.alerts != null)
            {
             Notifications.showNotification_withAlarm(requireContext(),weatherLocation.alerts.get(0).event,weatherLocation.alerts.get(0).description)
            }
            //HourlyWeather
              var listOfHourlyTime = ArrayList<String>()
              var listofHourlyTemp = ArrayList<String>()
              var listOfHourlyIcons = ArrayList<String>()
              for (i in weatherLocation.hourly.indices) {
                  listOfHourlyTime.add(weatherLocation.hourly.get(i).dt.toString())
                  listofHourlyTemp.add(weatherLocation.hourly.get(i).temp.toString())
                  listOfHourlyIcons.add(weatherLocation.hourly.get(i).weather.get(0).icon)
                 if (i > 24)
                     break
                }
           //DailyWeather
            var listOfDailyTime = ArrayList<String>()
            var listofDailyDescription= ArrayList<String>()
            var listOfDailyTemp = ArrayList<String>()
            var listofDailyWind = ArrayList<String>()
            var listOfDailyHumi = ArrayList<String>()
            var listofDailyPressure = ArrayList<String>()
            var listOfDailyCloud = ArrayList<String>()
            var listOfDailyIcons = ArrayList<String>()
            for (i in weatherLocation.daily.indices) {
                listOfDailyTime.add(weatherLocation.daily.get(i).dt.toString())
                listofDailyDescription.add(weatherLocation.daily.get(i).weather.get(0).description)
                listOfDailyTemp.add(weatherLocation.daily.get(i).temp.day.toString())
                listofDailyWind.add(weatherLocation.daily.get(i).windSpeed.toString())
                listOfDailyHumi.add(weatherLocation.daily.get(i).humidity.toString())
                listOfDailyCloud.add(weatherLocation.daily.get(i).clouds.toString())
                listofDailyPressure.add(weatherLocation.daily.get(i).pressure.toString())
                listOfDailyIcons.add(weatherLocation.hourly.get(i).weather.get(0).icon)
            }
            bindHOurlyWeatherRecyclerView(listOfHourlyTime,listofHourlyTemp,listOfHourlyIcons)

            bindDailyWeatherRecyclerView(listOfDailyTime,listofDailyDescription,listOfDailyTemp,listofDailyWind,listOfDailyHumi,listofDailyPressure,listOfDailyCloud,listOfDailyIcons)
        })


        currentWeather.observe(this@CurrentWeatherFragment, Observer {
            if (it == null) return@Observer
            updateDateToToday(it.dt)
            updateCondition(it.weather.get(0).description)
            updateTemperatures(it.temp)
            updateCloud(it.clouds)
            updateHumidity(it.humidity)
            updatePressure(it.pressure)
            updateWind(it.wind_speed)


            Glide.with(this@CurrentWeatherFragment)
                .load("http://openweathermap.org/img/w/" + it.weather.get(0).icon + ".png")
                .into(imageView_condition_icon)
               })

        open_location_list.setOnClickListener()
        {
            view ->
            fragmentManager?.beginTransaction()?.replace(R.id.nav_host_fragment, LocationListFragment())?.addToBackStack(null)?.commit()
        }
    }


    private fun bindDailyWeatherRecyclerView(listOfDailyTime: ArrayList<String>, listofDailyDescription: ArrayList<String>, listOfDailyTemp: ArrayList<String>, listofDailyWind: ArrayList<String>, listOfDailyHumi: ArrayList<String>, listofDailyPressure: ArrayList<String>, listOfDailyCloud: ArrayList<String>, listOfDailyIcons: ArrayList<String>) {
        val layoutManager =
                LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        daily_recycler_view.layoutManager = layoutManager
        daily_recycler_view.adapter = DailyWeatherAdapter(requireContext(), listOfDailyTime, listofDailyDescription, listOfDailyTemp, listofDailyWind, listOfDailyHumi, listofDailyPressure, listOfDailyCloud, listOfDailyIcons)
    }

    private fun bindHOurlyWeatherRecyclerView( time:ArrayList<String>, temp:ArrayList<String>,imgs:ArrayList<String>) {
        val layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = HourlyWeatherAdapter(requireContext(), time, temp, imgs)

    }
    private fun  chooseLocalizedUnitAbbreviation(metric: String, imperial: String): String {
        return if (currentWeatherViewModel.isMetric) metric else imperial
    }

    private fun updateLocation(lat: Double, lon: Double) {
        var locale:Locale?=null
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        val item = sharedPreferences.getString("LANGUAGE_SYSTEM","en")
        Log.i("info","Language "+sharedPreferences.getString("LANGUAGE_SYSTEM","en"))
        when (item) {
            "Arabic" -> {
                locale = Locale("ar")
            }
            "English" -> {
                locale = Locale("en")
            }
        }
        val gcd = Geocoder(context, locale)
        val addresses = gcd.getFromLocation(lat, lon, 1)
        location.text = addresses.get(0).countryName + ", " + addresses.get(0).locality
        Log.i(
            "info",
            "location Name is " + addresses.get(0).countryName + ", " + addresses.get(0).adminArea + ", " + addresses.get(
                0
            ).locality
        )
    }


    private fun updateDateToToday(dt:Int) {
        try {
            var locale:Locale?=null
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
            val item = sharedPreferences.getString("LANGUAGE_SYSTEM","enmcm")
            Log.i("info","Language "+sharedPreferences.getString("LANGUAGE_SYSTEM","enmcm"))
            when (item) {
                "Arabic" -> {
                    locale = Locale("ar")
                }
                "English" -> {
                    locale = Locale("en")
                }
            }
            val calendar = Calendar.getInstance()
            val tz = TimeZone.getDefault()
            calendar.timeInMillis = dt.toLong() * 1000
            val sdf = SimpleDateFormat("EEE dd MMM",locale)
            val currenTimeZone = calendar.time as Date
            date.text =  sdf.format(currenTimeZone)
        } catch (e: Exception) {
        }
    }

    private fun updateTemperatures(temperature: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("°C", "°F")
          textView_temp.text = "$temperature$unitAbbreviation"
      }
    private fun updateCondition(condition: String) {
        textView_condition.text = condition
    }

    private fun updateWind(windSpeed: Double) {
        val unitAbbreviation = chooseLocalizedUnitAbbreviation("m/s", "m/h")
        textView_wind.text = " $windSpeed$unitAbbreviation"
    }
    private fun updateHumidity(humidity: Int) {
        //val unitAbbreviation = chooseLocalizedUnitAbbreviation("kph", "mph")
        textView_humidity.text = " $humidity"
    }
    private fun updatePressure(pressure: Int) {
        //val unitAbbreviation = chooseLocalizedUnitAbbreviation("kph", "mph")
        textView_pressure.text = " $pressure"
    }
    private fun updateCloud(cloud: Int) {
        //val unitAbbreviation = chooseLocalizedUnitAbbreviation("kph", "mph")
        textView_cloud.text = " $cloud"
    }



}