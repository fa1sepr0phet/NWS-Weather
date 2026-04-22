package com.example.nwsweather

import android.app.Application
import com.example.nwsweather.worker.WidgetRefreshWorker

class NwsWeatherApp : Application() {
    override fun onCreate() {
        super.onCreate()
        WidgetRefreshWorker.schedulePeriodic(this)
    }
}
