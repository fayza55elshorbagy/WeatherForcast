package com.example.weatherforcast.data.network.response

import com.example.weatherforcast.data.db.entity.StandardWeatherApiResponse
import com.example.weatherforcast.data.network.Connectivity
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

const val API_KEY = "7ff4ae65181632ec5d67b2c200038afe"
//http://api.openweathermap.org/data/2.5/weather?q=Egypt&appid=7ff4ae65181632ec5d67b2c200038afe
//https://api.openweathermap.org/data/2.5/onecall?lat=33.441792&lon=-94.037689&appid=7ff4ae65181632ec5d67b2c200038afe
interface WeatherApiService {

    @GET("onecall")
    fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("lang") languageCode: String = "en",
        @Query("units") unit: String = "metric"
    ): Deferred<StandardWeatherApiResponse>

    companion object {
        operator fun invoke(
            connectivity: Connectivity
        ): WeatherApiService {
            val requestInterceptor = Interceptor { chain ->

                val url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("appid",
                        API_KEY
                    )
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()

                return@Interceptor chain.proceed(request)
            }

            val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivity)
                .build()

            return Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl("http://api.openweathermap.org/data/2.5/")
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(WeatherApiService::class.java)
        }
    }
}