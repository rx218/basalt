package com.x218.basalt.data

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn

/**
 * Wraps `SensorManager.getOrientation` and `SensorManager.getRotationMatrix`
 * Supply `gravity` vector and `geomagnetic` vector from the acceleration and magnetic sensors
 * Returns azimuth in degrees in range -180 to 180
 */
fun getAzimuth(gravity: FloatArray, geomagnetic: FloatArray): Float {
    val R = FloatArray(9)
    val I = FloatArray(9)
    val values = FloatArray(3)

    SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)
    SensorManager.getOrientation(R, values)

    return (values[0] / Math.PI * 180).toFloat()
}

/**
 * Creates a StateFlow object providing azimuth value of the device.
 * Uses the `getAzimuth()` function to transform sensor values
 */
fun getAzimuthFlow(sm: SensorManager): Flow<Float> {
    val accSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    val magSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    // Throw error if sensors are null
    if ( (accSensor == null) || (magSensor == null) ) {
        TODO()
    }

    // Flow of acceleration values
    val accFlow = callbackFlow {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let { trySend(it.values.copyOf()) }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sm.registerListener(listener, accSensor, SensorManager.SENSOR_DELAY_UI)

        awaitClose { sm.unregisterListener(listener) }
    }

    // Flow of magnetic values
    val magFlow = callbackFlow {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                event?.let { trySend(it.values.copyOf()) }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sm.registerListener(listener, magSensor, SensorManager.SENSOR_DELAY_UI)

        awaitClose { sm.unregisterListener(listener) }
    }

    // Combines the acceleration and magnetic flows using getAzimuth()
    return accFlow
        .combine (magFlow) { acc, mag -> getAzimuth(acc, mag) }
        .flowOn(Dispatchers.Default)
}