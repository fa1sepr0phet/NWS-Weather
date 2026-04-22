package com.example.nwsweather.widget

import android.content.Context
import androidx.glance.GlanceId
import androidx.glance.appwidget.action.ActionCallback
import androidx.glance.action.ActionParameters
import com.example.nwsweather.worker.WidgetRefreshWorker

class RefreshWeatherAction : ActionCallback {
    override suspend fun onAction(
        context: Context,
        glanceId: GlanceId,
        parameters: ActionParameters
    ) {
        WidgetRefreshWorker.enqueueImmediate(context)
    }
}
