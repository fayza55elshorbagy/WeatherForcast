package com.wforcast.weatherforcast.data.repository

import android.content.Context
import android.location.Geocoder
import android.util.Log
import androidx.lifecycle.LiveData
import com.wforcast.weatherforcast.data.db.*
import com.wforcast.weatherforcast.data.db.entity.AlertData
import com.wforcast.weatherforcast.data.db.entity.MapLocation
import com.wforcast.weatherforcast.data.db.entity.StandardWeatherApiResponse
import com.wforcast.weatherforcast.data.db.format.CurrentWeatherUnit
import com.wforcast.weatherforcast.data.network.WeatherNetworkDataSource
import com.wforcast.weatherforcast.data.provider.LanguageProvider
import com.wforcast.weatherforcast.data.provider.LocationProvider
import com.wforcast.weatherforcast.data.provider.UnitProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime
import java.util.*


class WeatherRepositoryImpl(
    private val currentWeatherDao: CurrentWeatherDao,
    private val weatherLocationDao: WeatherLocationDao,
    private val mapLocationDao : MapLocationDao,
    private val alertDao: AlertDao,
    private val weatherNetworkDataSource: WeatherNetworkDataSource,
    private val locationProvider: LocationProvider,
    private val unitProvider: UnitProvider,
    private val languageProvider: LanguageProvider,
    context : Context
)
    : WeatherRepository {
    private val appContext = context.applicationContext
    init {
        weatherNetworkDataSource.apply {
            downloadedCurrentWeather.observeForever { newCurrentWeather ->
                persistFetchedCurrentWeather(newCurrentWeather)
                presistMapLoction()
            }
        }
    }

    private fun presistMapLoction() {
        GlobalScope.launch(Dispatchers.IO) {
            val lat_lon = locationProvider.getPreferredLocationString()
            val splitted_lat_lon = lat_lon.split(",")
            Log.i("info",updateLocation(splitted_lat_lon[0].toDouble(),splitted_lat_lon[1].toDouble())+"from REpo")
            insertMapLocation(MapLocation(updateLocation(splitted_lat_lon[0].toDouble(),splitted_lat_lon[1].toDouble()),splitted_lat_lon[0].toDouble(),splitted_lat_lon[1].toDouble())
            )
        }
    }

    override suspend fun getCurrentWeather(metric: Boolean): LiveData<out CurrentWeatherUnit> {
        return withContext(Dispatchers.IO) {
            initWeatherData()
            return@withContext if (metric) currentWeatherDao.getWeatherMetric()
            else currentWeatherDao.getWeatherImperial()
        }
    }

    override suspend fun getWeatherLocation(): LiveData<StandardWeatherApiResponse> {
            return withContext(Dispatchers.IO) {
                return@withContext weatherLocationDao.getLocation()
            }
        }

    private fun updateLocation(lat: Double, lon: Double) :String{
        val gcd = Geocoder(appContext, Locale.getDefault())
        val addresses = gcd.getFromLocation(lat, lon, 1)
        return addresses.get(0).countryName + ", " + addresses.get(0).locality
    }

    override suspend fun getMapLocation(): List<MapLocation> {
        return withContext(Dispatchers.IO) {
            return@withContext mapLocationDao.getMapLocation()
        }
    }

    override fun insertMapLocation(l: MapLocation) {
        GlobalScope.launch(Dispatchers.IO) {
            mapLocationDao.insert(MapLocation(l.address,l.maplat,l.maplon))
        }
    }

    override suspend fun insetAlertData(alertData: AlertData) {
        GlobalScope.launch(Dispatchers.IO) {
            alertDao.insert(alertData)
            Log.i("info","called"+alertData)
        }
    }

    override suspend fun getAlertData(): List<AlertData> {
        return withContext(Dispatchers.IO) {
            return@withContext alertDao.getAlertData()

        }
    }

    override suspend fun deleteAlert(id: Int) {
        return withContext(Dispatchers.IO) {
            return@withContext alertDao.deleteAlert(id)
        }
    }

    override suspend fun deleteMApLocation(address: String) {
        return withContext(Dispatchers.IO) {
            return@withContext mapLocationDao.deleteLocation(address)
        }
    }

    private fun persistFetchedCurrentWeather(fetchedWeather: StandardWeatherApiResponse) {
        GlobalScope.launch(Dispatchers.IO) {
            currentWeatherDao.upsert(fetchedWeather.standardWeather!!)
            weatherLocationDao.upsert(fetchedWeather)
        }
    }

    private suspend fun initWeatherData() {
        val lastWeatherLocation = weatherLocationDao.getLocationNonLive()
        System.out.println(lastWeatherLocation.toString())
        if(lastWeatherLocation == null ||
                locationProvider.hasLocationChanged(lastWeatherLocation)) {
            System.out.println(lastWeatherLocation.toString())
            fetchCurrentWeather()
            return
        }
        if (isFetchCurrentNeeded(lastWeatherLocation.zonedDateTime))
        {
            System.out.println(lastWeatherLocation.zonedDateTime.toString())
            fetchCurrentWeather()

        }
    }

    private suspend fun fetchCurrentWeather() {
        val lat_lon = locationProvider.getPreferredLocationString()
        val splitted_lat_lon = lat_lon.split(",")
        weatherNetworkDataSource.fetchCurrentWeather(splitted_lat_lon[0].toDouble(),splitted_lat_lon[1].toDouble(),changeLanguageFormat(languageProvider.getLanguageSystem().name),unitProvider.getUnitSystem().name)
    }

    private fun changeLanguageFormat(name: String): String {
        val changedName : String
        if(name == "Arabic" )
           changedName = "ar"
        else
            changedName = "en"

        return changedName
    }

    private fun isFetchCurrentNeeded(lastFetchTime: ZonedDateTime): Boolean {
        val thirtyMinutesAgo = ZonedDateTime.now().minusMinutes(30)
        return lastFetchTime.isBefore(thirtyMinutesAgo)
    }

}