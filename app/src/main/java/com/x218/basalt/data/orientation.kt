package com.x218.basalt.data

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOn

// returns azimuth of device in degrees from 0deg to 360deg
fun getAzimuth(gravity: FloatArray, geomagnetic: FloatArray): Float {
    val R = FloatArray(9)
    val I = FloatArray(9)
    val values = FloatArray(3)

    SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)
    SensorManager.getOrientation(R, values)

    return ( - (values[0] / Math.PI) * 180).toFloat()
}

fun getAzimuthFlow(sm: SensorManager): Flow<Float> {
    val accSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    val magSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    if ( (accSensor == null) || (magSensor == null) ) {
        TODO()
    }

    val accFlow = callbackFlow {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                Log.i("getAzimuthFlow accFlow", "Receive acceleration event $event")
                event?.let { trySend(it.values.copyOf()) }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sm.registerListener(listener, accSensor, SensorManager.SENSOR_DELAY_UI)

        awaitClose { sm.unregisterListener(listener) }
    }

    val magFlow = callbackFlow {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                Log.i("getAzimuthFlow: magFlow", "Receive geomagnetic event $event")
                event?.let { trySend(it.values.copyOf()) }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sm.registerListener(listener, magSensor, SensorManager.SENSOR_DELAY_UI)

        awaitClose { sm.unregisterListener(listener) }
    }

    return accFlow
        .combine (magFlow) { acc, mag -> getAzimuth(acc, mag) }
        .flowOn(Dispatchers.Default)
}