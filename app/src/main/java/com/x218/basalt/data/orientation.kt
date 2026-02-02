package com.x218.basalt.data

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager

fun getAngleNorth(sObj: SensorDataObject): FloatArray {
    val R = FloatArray(9)
    val I = FloatArray(9)
    val gravity = sObj.gravity
    val geomagnetic = sObj.geomagnetic
    val values = FloatArray(3)

    SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)
    SensorManager.getOrientation(R, values)

    return values
}

fun initializeSensorData(sm: SensorManager): SensorDataObject {
    val accSensor = sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
    val magSensor = sm.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

    if ( (accSensor == null) || (magSensor == null) ) {
        TODO()
    }

    val sObj = SensorDataObject(FloatArray(3), FloatArray(3))

    val accListener = AccelerationSensorListener(sObj)
    val magListener = MagnetSensorListener(sObj)

    sm.registerListener(accListener, accSensor, SensorManager.SENSOR_DELAY_NORMAL)
    sm.registerListener(magListener, magSensor, SensorManager.SENSOR_DELAY_NORMAL)

    sm.unregisterListener(accListener)
    sm.unregisterListener(magListener)

    return sObj
}

class AccelerationSensorListener(val sObj: SensorDataObject): SensorEventListener {
    // Do nothing ig
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    override fun onSensorChanged(event: SensorEvent?) {
        sObj.gravity = if (event?.values != null) {
            event.values
        } else {
            FloatArray(3)
        }
    }
}

class MagnetSensorListener(val sObj: SensorDataObject): SensorEventListener {
    // Do nothing ig
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) = Unit

    override fun onSensorChanged(event: SensorEvent?) {
        sObj.geomagnetic = if (event?.values != null) {
            event.values
        } else {
            FloatArray(3)
        }
    }
}

data class SensorDataObject (var gravity: FloatArray, var geomagnetic: FloatArray) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as SensorDataObject

        if (!gravity.contentEquals(other.gravity)) return false
        if (!geomagnetic.contentEquals(other.geomagnetic)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = gravity.contentHashCode()
        result = 31 * result + geomagnetic.contentHashCode()
        return result
    }
}