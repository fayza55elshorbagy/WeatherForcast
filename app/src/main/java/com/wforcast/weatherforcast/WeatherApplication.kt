package com.wforcast.weatherforcast

import android.app.Application
import android.content.Context
import com.wforcast.weatherforcast.data.db.WeatherDataBase
import com.wforcast.weatherforcast.data.network.Connectivity
import com.wforcast.weatherforcast.data.network.ConnectivityImpl
import com.wforcast.weatherforcast.data.network.WeatherNetworkDataSource
import com.wforcast.weatherforcast.data.network.WeatherNetworkDataSourceImpl
import com.wforcast.weatherforcast.data.network.response.WeatherApiService
import com.wforcast.weatherforcast.data.provider.*
import com.wforcast.weatherforcast.data.repository.WeatherRepository
import com.wforcast.weatherforcast.data.repository.WeatherRepositoryImpl
import com.wforcast.weatherforcast.ui.alarm.AlarmViewModelFactory
import com.wforcast.weatherforcast.ui.locationlist.LocationListViewModelFactory
import com.wforcast.weatherforcast.ui.weather.CurrentWeatherViewModelFactory
import com.google.android.gms.location.LocationServices
import com.jakewharton.threetenabp.AndroidThreeTen
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton


class WeatherApplication: Application(),KodeinAware {
    override val kodein = Kodein.lazy {
        import(androidXModule(this@WeatherApplication))

        bind() from singleton { WeatherDataBase(instance()) }
        bind() from singleton { instance<WeatherDataBase>().currentWeatherDao() }
        bind() from singleton { instance<WeatherDataBase>().weatherLocationDao() }
        bind() from singleton { instance<WeatherDataBase>().mapLocationDao() }
        bind() from singleton { instance<WeatherDataBase>().alertDao() }
        bind() from singleton { WeatherApiService(instance()) }

        bind<Connectivity>() with singleton { ConnectivityImpl(instance())}
        bind<WeatherNetworkDataSource>() with singleton { WeatherNetworkDataSourceImpl(instance()) }
        bind<LocationProvider>() with singleton { LocationProviderImpl(instance(),instance()) }
        bind<WeatherRepository>() with singleton { WeatherRepositoryImpl(instance(), instance(),instance(),instance(),instance(),instance(),instance(),instance(),instance()) }
        bind<UnitProvider>() with singleton { UnitProviderImpl(instance()) }
        bind<LanguageProvider>() with singleton { LanguageProviderImpl(instance()) }

        bind() from provider { LocationServices.getFusedLocationProviderClient(instance<Context>()) }
        bind() from provider { CurrentWeatherViewModelFactory(instance(), instance()) }
        bind() from provider { LocationListViewModelFactory(instance()) }
        bind() from provider { AlarmViewModelFactory(instance()) }
    }

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)
        //PreferenceManager.setDefaultValues(this, R.xml.prefrences, false)
    }


}