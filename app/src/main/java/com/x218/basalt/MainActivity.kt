package com.x218.basalt

import android.annotation.SuppressLint
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.x218.basalt.data.PermissionState
import com.x218.basalt.data.checkPermissions
import com.x218.basalt.data.checkGpsProvider
import com.x218.basalt.data.getAzimuth
import com.x218.basalt.data.getLocation
import com.x218.basalt.data.initializeSensorData
import com.x218.basalt.data.requestPermission
import com.x218.basalt.ui.MainScreen

const val TAG: String = "MainActivity"

class MainActivity : ComponentActivity() {

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lm = this.getSystemService(LOCATION_SERVICE) as LocationManager
        val sm = this.getSystemService(SENSOR_SERVICE) as SensorManager
        Log.i(TAG, "LocationManager: $lm , SensorManager: $sm")

        val perms = checkPermissions(this)
        Log.i(TAG, "Checked permissions: perms $perms")

        if( perms == PermissionState(false, false)) {
            requestPermission(perms, this)
        }

        var location by remember { mutableStateOf(Location(LocationManager.GPS_PROVIDER)) }

        if ( perms == PermissionState(false, false) && checkGpsProvider(lm) ) {
            lifecycleScope.launch {
                location = getLocation(
                    lm,
                    // Get executor from current coroutine
                    (this.coroutineContext[ContinuationInterceptor] as CoroutineDispatcher).asExecutor()
                )
            }
        }

        val sObj = initializeSensorData(sm)
        val northAngle = getAzimuth(sObj)
        Log.i(TAG, "Sensor data $sObj , $northAngle")

        val kaabaLocation = Location(LocationManager.GPS_PROVIDER).apply {
            latitude = 21.2445
            longitude = 39.82617
        }
        Log.println(Log.INFO, "MainActivity", "kaaba location: $kaabaLocation")

        val kaabaDirection = location.bearingTo(kaabaLocation)
        Log.println(Log.INFO, "MainActivity", "Kaaba direction : $kaabaDirection")

        Log.println(Log.INFO, "MainActivity", "Completed all setup")

        setContent {
            MainScreen(
                perms,
                CompassState(kaabaDirection, northAngle),
                location
            )
        }
    }
}

data class CompassState(var kaabaBearing: Float, var north: Float)