package com.wforcast.weatherforcast.data.network

import android.content.Context
import android.net.ConnectivityManager
import com.wforcast.weatherforcast.util.NoConnectivityException
import okhttp3.Interceptor
import okhttp3.Response

class ConnectivityImpl(
    context:Context
) : Connectivity {
    private val appContext = context.applicationContext
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!isOnline())
            throw NoConnectivityException()
        return chain.proceed(chain.request())
    }

    private fun isOnline(): Boolean {
        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}