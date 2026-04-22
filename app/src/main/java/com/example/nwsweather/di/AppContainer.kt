package com.example.nwsweather.di

import android.content.Context
import com.example.nwsweather.data.local.AppDatabase
import com.example.nwsweather.data.network.ApiModule
import com.example.nwsweather.data.repository.WeatherRepository
import com.example.nwsweather.location.AndroidLocationClient

class AppContainer(context: Context) {
    private val database = AppDatabase.getInstance(context.applicationContext)
    private val locationClient = AndroidLocationClient(context.applicationContext)

    val weatherRepository: WeatherRepository = WeatherRepository(
        nwsApi = ApiModule.nwsApi,
        savedLocationDao = database.savedLocationDao(),
        pointCacheDao = database.pointCacheDao(),
        weatherSnapshotDao = database.weatherSnapshotDao(),
        locationClient = locationClient,
        appContext = context.applicationContext
    )
}
