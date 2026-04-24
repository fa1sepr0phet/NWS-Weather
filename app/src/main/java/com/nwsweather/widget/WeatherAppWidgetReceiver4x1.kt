package com.nwsweather.widget

import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.GlanceAppWidgetReceiver

class WeatherAppWidgetReceiver4x1 : GlanceAppWidgetReceiver() {
    override val glanceAppWidget: GlanceAppWidget = WeatherAppWidget()
}
