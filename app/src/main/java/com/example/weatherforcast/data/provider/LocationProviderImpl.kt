package com.example.weatherforcast.data.provider

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.core.content.ContextCompat
import com.example.weatherforcast.util.LocationPermissionNotGrantedException
import com.example.weatherforcast.data.db.entity.StandardWeatherApiResponse
import com.example.weatherforcast.internal.asDeferred
import com.google.android.gms.location.FusedLocationProviderClient
import kotlinx.coroutines.Deferred


const val USE_DEVICE_LOCATION = "USE_DEVICE_LOCATION"
const val CUSTOM_LOCATION = "CUSTOM_LOCATION"
class LocationProviderImpl(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
     context : Context
    ) : PrefrenceProvider(context),LocationProvider {

    private val appContext = context.applicationContext
    private lateinit var favLoc : String

    override suspend fun hasLocationChanged(lastWeatherLocation: StandardWeatherApiResponse): Boolean {
        val deviceLocationChanged = try {
            hasDeviceLocationChanged(lastWeatherLocation)
        } catch (e: LocationPermissionNotGrantedException) {
            false
        }

        return deviceLocationChanged || hasCustomLocationChanged(lastWeatherLocation)
    }

    private suspend fun hasDeviceLocationChanged(lastWeatherLocation: StandardWeatherApiResponse): Boolean {
        if (!isUsingDeviceLocation())
            return false

        val deviceLocation = getLastDeviceLocation().await()
                ?: return false

        // Comparing doubles cannot be done with "=="
        val comparisonThreshold = 0.03
        return Math.abs(deviceLocation.latitude - lastWeatherLocation.lat) > comparisonThreshold &&
                Math.abs(deviceLocation.longitude - lastWeatherLocation.lon) > comparisonThreshold
    }

    private fun isUsingDeviceLocation(): Boolean {
        return preferences.getBoolean(USE_DEVICE_LOCATION, true)
    }

    private fun hasCustomLocationChanged(lastWeatherLocation: StandardWeatherApiResponse): Boolean {
        if (!isUsingDeviceLocation()) {
            val customLocationName = getCustomLocationName()
           // return customLocationName != getCityNameFromApiLatling(lastWeatherLocation.lat,lastWeatherLocation.lon)
        }
        return false
    }
    private fun getCustomLocationName(): String? {
        return preferences.getString(CUSTOM_LOCATION, "Damietta")
    }

    override suspend fun getPreferredLocationString(): String {
        if (isUsingDeviceLocation()) {
            try {
                Log.i("info","${getLastDeviceLocation().await()!!.latitude},${getLastDeviceLocation().await()!!.longitude}+*****")
                val deviceLocation = getLastDeviceLocation().await()
                   return "${deviceLocation!!.latitude},${deviceLocation!!.longitude}"
            } catch (e: LocationPermissionNotGrantedException) {
                return "${convertPreferredLocationStringToLatling(getCustomLocationName()!!)}"
            }
            Log.i("info","using")
        }
        else
        {
            Log.i("info","${convertPreferredLocationStringToLatling(getCustomLocationName()!!)}+fromProvider")
            return "${convertPreferredLocationStringToLatling(getCustomLocationName()!!)}"

        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastDeviceLocation(): Deferred<Location?> {
        return if (hasLocationPermission())
            fusedLocationProviderClient.lastLocation.asDeferred()
        else
            throw LocationPermissionNotGrantedException()
    }

    private fun hasLocationPermission(): Boolean {
        return ContextCompat.checkSelfPermission(appContext,
            Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun convertPreferredLocationStringToLatling(preferredLocationString: String): String {
        var addressList: List<Address> = emptyList()
        var lat : Double = 0.0
        var lon : Double = 0.0
        val geoCoder = Geocoder(appContext)
        if (preferredLocationString != null && !preferredLocationString.isEmpty()) {
            try {
                addressList = geoCoder.getFromLocationName(preferredLocationString, 1)
                if (addressList != null && addressList.size > 0) {
                    lat = addressList[0].latitude
                   lon = addressList[0].longitude
                }
                return lat.toString()+","+lon.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            } // end catch
        } // end i
        return  lat.toString()+","+lon.toString()
    }

}