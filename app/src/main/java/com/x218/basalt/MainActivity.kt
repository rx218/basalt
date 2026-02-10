package com.x218.basalt

import android.annotation.SuppressLint
import android.hardware.SensorManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.lifecycleScope
import com.x218.basalt.data.PermissionState
import com.x218.basalt.data.checkPermissions
import com.x218.basalt.data.checkGpsProvider
import com.x218.basalt.data.getAzimuthFlow
import com.x218.basalt.data.getLocation
import com.x218.basalt.data.requestPermission
import com.x218.basalt.ui.MainScreen
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.asExecutor
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.coroutines.ContinuationInterceptor

class MainActivity : ComponentActivity() {

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val lm = this.getSystemService(LOCATION_SERVICE) as LocationManager
        val sm = this.getSystemService(SENSOR_SERVICE) as SensorManager

        val perms = checkPermissions(this)

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

        val azimuthFlow = getAzimuthFlow(sm)
            .stateIn(
                lifecycleScope,
                SharingStarted.WhileSubscribed(),
                1.0f
            )

        val northAngle by azimuthFlow.collectAsState()

        val kaabaLocation = Location(LocationManager.GPS_PROVIDER).apply {
            latitude = 21.2445
            longitude = 39.82617
        }

        val kaabaDirection = location.bearingTo(kaabaLocation)

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