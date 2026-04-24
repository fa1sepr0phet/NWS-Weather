package com.nwsweather.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.core.content.edit
import kotlin.math.sqrt

class MovementTracker(private val context: Context) : SensorEventListener {
    private val sensorManager = context.getSensorManager()
    private val prefs = context.getSharedPreferences("movement_tracker", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_LAST_MOVEMENT = "last_movement_timestamp"
        private const val MOVEMENT_THRESHOLD = 0.5f // Acceleration threshold

        fun isPhoneStationary(context: Context, thresholdMinutes: Int): Boolean {
            val prefs = context.getSharedPreferences("movement_tracker", Context.MODE_PRIVATE)
            val lastMovement = prefs.getLong(KEY_LAST_MOVEMENT, 0L)
            
            if (lastMovement == 0L) return true

            val stationaryDuration = System.currentTimeMillis() - lastMovement
            val isStationary = stationaryDuration > (thresholdMinutes * 60 * 1000)

            if (isStationary) {
                // Privacy: Delete the specific timestamp once the threshold is met
                prefs.edit { remove(KEY_LAST_MOVEMENT) }
            }
            return isStationary
        }
    }

    fun start() {
        val sensor = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION)
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun stop() {
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return
        
        val x = event.values[0]
        val y = event.values[1]
        val z = event.values[2]
        val acceleration = sqrt(x * x + y * y + z * z)

        if (acceleration > MOVEMENT_THRESHOLD) {
            prefs.edit {
                putLong(KEY_LAST_MOVEMENT, System.currentTimeMillis())
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

private fun Context.getSensorManager() = getSystemService(Context.SENSOR_SERVICE) as SensorManager
