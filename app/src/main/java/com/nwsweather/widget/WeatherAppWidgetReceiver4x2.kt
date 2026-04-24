package com.nwsweather.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class WeatherAppWidgetReceiver4x2 : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = WeatherAppWidget()
}
