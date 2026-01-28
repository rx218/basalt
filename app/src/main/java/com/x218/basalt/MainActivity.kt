package com.x218.basalt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier

import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat

import android.content.pm.PackageManager
import android.location.LocationManager
import android.location.Location

import android.Manifest.permission
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.activity.result.ActivityResultCaller
import androidx.activity.result.ActivityResultLauncher

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

import com.x218.basalt.ui

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

        // called in some Android version and fails
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
        }
    }

    // Permissions
    val perms = PermissionState(false, false)
    val compass = CompassState(0.0f, 0.0f)
    val KaabaLocation = Location(GPS_PROVIDER)

    val shouldShowRationale =
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
		LocationDialog()
            }
        }
    }

    val locationManager: LocationManager? = null
    val locationListener: MyLocationListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
	KaabaLocation.setLatitude(21.2445)
	KaabaLocation.setLongitude(39.82617)

	locationManager.getLastKnownLocation(GPS_PROVIDER)
	locationManager.requestLocationUpdates(GPS_PROVIDER, 1000, 0.0f, locationListener)

	checkPermissions()
	requestPermission()

	locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

	when {
	    !hasProvider(GPS_PROVIDER) -> {
		// No GPS error
	    }
	    !isProviderEnabled(GPS_PROVIDER) -> {
		// GPS disabled
	    }
	    !isLocationEnabled() -> {
		// Location disabled
	    }
	}

	enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            ),
            navigationBarStyle = SystemBarStyle.auto(
                Color.TRANSPARENT,
                Color.TRANSPARENT
            )
        )

        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainNavigation()
                }
            }
        }
    }

    fun updateLocation(Location location) {
	this.bearingKaaba = location.bearingTo(KaabaLocation)
    }

    fun requestPermission() {
	when {
	    // Test if we have permission
	    perms.coarse || perms.fine -> {
		// continue as usual
		return
	    }
	    // Show rationale dialog if needed
	    showRationale -> { LocationDialog() }
	    else -> {
		// launch permission request
		locationPermmissionRequest.launch(
		    arrayOf(
			Manifest.permission.ACCESS_FINE_LOCATION,
			Manifest.permission.ACCESS_COARSE_LOCATION
		    )
		)
	    }
	}
    }

    fun checkPermissions() {
	this.perms.coarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
	this.perms.fine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }
}

data class PermissionState(val coarse: Boolean, val fine: Boolean)
