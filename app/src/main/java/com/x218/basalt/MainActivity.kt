package com.x218.basalt

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier

import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager
import android.location.Location

import android.location.LocationListener
import android.location.LocationManager
import android.location.LocationManager.GPS_PROVIDER
import android.os.Build
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.x218.basalt.data.PermissionState
import com.x218.basalt.ui.MainScreen
import com.x218.basalt.ui.theme.MyApplicationTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    class MyLocationListener : LocationListener {
        val mContext: MainActivity

        constructor(mContext: MainActivity) {
            this.mContext = mContext
        }

        override fun onLocationChanged(location: Location) {
            mContext.updateLocation(location)
        }
    }

    // Permissions
    val perms = PermissionState(coarse = false, fine = false)
    val compass = CompassState(kaabaBearing = 0.0f, north = 0.0f)
    val kaabaLocation = Location(GPS_PROVIDER)

    val showRationale =
	    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ||
	    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)

    // Activity Result Launcher
    val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
		        // fine location granted
                perms.fine = true
            }
            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
		        // only coarse location granted
		        perms.coarse = true
            }
            else -> {
		        // No location granted, show error
                perms.coarse = false
                perms.fine = false
            }
        }
    }

    // val locationManager: LocationManager? = null
    val locationListener: LocationListener = MyLocationListener(this)
    val locationManager = this.getSystemService(LOCATION_SERVICE) as LocationManager

    override fun onCreate(savedInstanceState: Bundle?) {
	    kaabaLocation.latitude = 21.2445
        kaabaLocation.longitude = 39.82617

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermission()
        }
        locationManager.getLastKnownLocation(GPS_PROVIDER)
        locationManager.requestLocationUpdates(GPS_PROVIDER, 1000, 0.0f, locationListener)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            when {
                !locationManager.hasProvider(GPS_PROVIDER) -> {
                    // No GPS error
                }
                !locationManager.isProviderEnabled(GPS_PROVIDER) -> {
                    // GPS disabled
                }
                !locationManager.isLocationEnabled -> {
                    // Location disabled
                }
            }
        }

        enableEdgeToEdge()

        super.onCreate(savedInstanceState)
        setContent {
            MainScreen(perms, compass, kaabaLocation)
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen(perms, compass, kaabaLocation)
                }
            }
        }
    }

    fun updateLocation(location: Location) {
	    this.compass.kaabaBearing = location.bearingTo(kaabaLocation)
    }

    fun requestPermission() {
        when {
            // Show rationale dialog if needed
            showRationale -> {}
            else -> {
                // launch permission request
                this.locationPermissionRequest.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }
}

data class CompassState(var kaabaBearing: Float, var north: Float)