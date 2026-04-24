package com.nwsweather.widget

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.glance.GlanceId
import androidx.glance.GlanceModifier
import androidx.glance.Image
import androidx.glance.ImageProvider
import androidx.glance.action.actionStartActivity
import androidx.glance.appwidget.GlanceAppWidget
import androidx.glance.appwidget.provideContent
import androidx.glance.appwidget.appWidgetBackground
import androidx.glance.appwidget.cornerRadius
import androidx.glance.background
import androidx.glance.action.clickable
import androidx.glance.layout.*
import androidx.glance.text.FontWeight
import androidx.glance.text.Text
import androidx.glance.text.TextStyle
import androidx.glance.LocalSize
import androidx.glance.appwidget.SizeMode
import androidx.glance.unit.ColorProvider
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.DpSize
import com.nwsweather.MainActivity
import com.nwsweather.myapp.R
import com.nwsweather.data.local.AppDatabase
import com.nwsweather.data.local.WeatherSnapshotEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherAppWidget : GlanceAppWidget() {
    companion object {
        private val SIZE_2X1 = DpSize(140.dp, 70.dp)
        private val SIZE_4X1 = DpSize(280.dp, 70.dp)
        private val SIZE_2X2 = DpSize(140.dp, 140.dp)
        private val SIZE_4X2 = DpSize(280.dp, 140.dp)
    }

    override val sizeMode = SizeMode.Responsive(
        setOf(SIZE_2X1, SIZE_4X1, SIZE_2X2, SIZE_4X2)
    )

    override suspend fun provideGlance(context: Context, id: GlanceId) {
        val snapshot = withContext(Dispatchers.IO) {
            AppDatabase.getInstance(context).weatherSnapshotDao().getLatest()
        }

        provideContent {
            WeatherWidgetContent(snapshot = snapshot)
        }
    }
}

@Composable
private fun WeatherWidgetContent(snapshot: WeatherSnapshotEntity?) {
    val size = LocalSize.current
    val isPill = size.height < 100.dp
    val radius = if (isPill) 999.dp else 24.dp

    Box(
        modifier = GlanceModifier
            .fillMaxSize()
            .appWidgetBackground()
            .cornerRadius(radius)
            .background(backgroundColorFor(snapshot))
            .clickable(actionStartActivity<MainActivity>())
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        if (snapshot == null) {
            EmptyWeatherWidget()
        } else {
            when {
                size.width <= 160.dp && size.height <= 90.dp -> Loaded2x1Pill(snapshot)
                size.width <= 160.dp -> Loaded2x2Square(snapshot)
                size.height <= 90.dp -> Loaded4x1Pill(snapshot)
                else -> Loaded4x2Rect(snapshot)
            }
        }
    }
}

@Composable
private fun Loaded2x1Pill(snapshot: WeatherSnapshotEntity) {
    Row(
        modifier = GlanceModifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConditionIcon(snapshot.isDaytime)
        Spacer(modifier = GlanceModifier.width(8.dp))
        Column {
            Text(
                text = "${snapshot.temperature}°",
                style = TextStyle(color = ColorProvider(Color.White), fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )
            Text(
                text = snapshot.locationName,
                style = TextStyle(color = ColorProvider(Color(0xE6FFFFFF)), fontSize = 10.sp),
                maxLines = 1
            )
        }
    }
}

@Composable
private fun Loaded2x2Square(snapshot: WeatherSnapshotEntity) {
    Column(
        modifier = GlanceModifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConditionIcon(snapshot.isDaytime)
        Text(
            text = "${snapshot.temperature}°",
            style = TextStyle(color = ColorProvider(Color.White), fontWeight = FontWeight.Bold, fontSize = 32.sp)
        )
        Text(
            text = snapshot.locationName,
            style = TextStyle(color = ColorProvider(Color.White), fontSize = 12.sp),
            maxLines = 1
        )
        Text(
            text = snapshot.shortForecast,
            style = TextStyle(color = ColorProvider(Color(0xCCFFFFFF)), fontSize = 11.sp),
            maxLines = 1
        )
    }
}

@Composable
private fun Loaded4x1Pill(snapshot: WeatherSnapshotEntity) {
    Row(
        modifier = GlanceModifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        ConditionIcon(snapshot.isDaytime)
        Spacer(modifier = GlanceModifier.width(12.dp))
        Column(modifier = GlanceModifier.defaultWeight()) {
            Text(
                text = "${snapshot.temperature}° ${snapshot.locationName}",
                style = TextStyle(color = ColorProvider(Color.White), fontWeight = FontWeight.Bold, fontSize = 18.sp),
                maxLines = 1
            )
            Text(
                text = snapshot.shortForecast,
                style = TextStyle(color = ColorProvider(Color(0xE6FFFFFF)), fontSize = 12.sp),
                maxLines = 1
            )
        }
        UVIndexBadge("Moderate") // Placeholder UV
    }
}

@Composable
private fun Loaded4x2Rect(snapshot: WeatherSnapshotEntity) {
    Row(modifier = GlanceModifier.fillMaxSize()) {
        Column(
            modifier = GlanceModifier.defaultWeight().fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ConditionIcon(snapshot.isDaytime)
            Text(
                text = "${snapshot.temperature}°",
                style = TextStyle(color = ColorProvider(Color.White), fontWeight = FontWeight.Bold, fontSize = 36.sp)
            )
            Text(text = snapshot.locationName, style = TextStyle(color = ColorProvider(Color.White)))
        }
        
        // Right side: 3-day forecast placeholder
        Column(
            modifier = GlanceModifier.width(120.dp).fillMaxHeight().padding(start = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ForecastRow("Tonight", "42°")
            ForecastRow("Tue", "55°")
            ForecastRow("Tue Night", "38°")
        }
    }
}

@Composable
private fun ConditionIcon(isDaytime: Boolean) {
    Image(
        provider = ImageProvider(if (isDaytime) R.drawable.ic_sun else R.drawable.ic_moon),
        contentDescription = null,
        modifier = GlanceModifier.size(32.dp)
    )
}

@Composable
private fun UVIndexBadge(level: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = "UV", style = TextStyle(color = ColorProvider(Color.White), fontSize = 10.sp))
        Text(
            text = level,
            style = TextStyle(color = ColorProvider(Color.Yellow), fontWeight = FontWeight.Bold, fontSize = 12.sp)
        )
    }
}

@Composable
private fun ForecastRow(day: String, temp: String) {
    Row(modifier = GlanceModifier.fillMaxWidth().padding(vertical = 6.dp)) {
        Text(
            text = day,
            style = TextStyle(color = ColorProvider(Color.White), fontSize = 16.sp),
            modifier = GlanceModifier.defaultWeight()
        )
        Text(
            text = temp,
            style = TextStyle(color = ColorProvider(Color.White), fontWeight = FontWeight.Bold, fontSize = 16.sp)
        )
    }
}

@Composable
private fun EmptyWeatherWidget() {
    Text(text = "No data", style = TextStyle(color = ColorProvider(Color.White)))
}

private fun backgroundColorFor(snapshot: WeatherSnapshotEntity?): ColorProvider {
    val color = when {
        snapshot == null -> Color(0xFF31435F)
        snapshot.isDaytime -> Color(0xFF4F86C6)
        else -> Color(0xFF1F2A44)
    }
    return ColorProvider(color)
}
