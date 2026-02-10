package com.x218.basalt.data

import android.Manifest
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

// Returns the current location
// getLocation may take a long time to complete, so it is preferred to invoke it as a coroutine
@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
suspend fun getLocation(lm: LocationManager, exec: Executor): Location {
    // See if we have a cached Location
    if (lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
        return lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
    }

    // Otherwise get a new copy
    // suspendCoroutine waits until the respective callbacks are executed
    return suspendCoroutine { continuation ->
        // For android versions above R, use getCurrentLocation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            lm.getCurrentLocation(
                LocationManager.GPS_PROVIDER,
                null,
                exec,
                { l: Location ->
                    continuation.resume(l)
                }
            )
        }
        // For older versions of android use Location Listener
        else {
            lm.requestLocationUpdates(
                LocationManager.GPS_PROVIDER,
                0,
                0f,
                object: LocationListener {
                    override fun onLocationChanged(l: Location) {
                        lm.removeUpdates(this)
                        continuation.resume(l)
                    }
                }
            )
        }
    }
}


// checks if device has GPS provider and if location is enabled
fun checkGpsProvider(lm: LocationManager): Boolean {
    val locationEnabled: Boolean
    val hasGPS = lm.getProviders(true)
        .find {
            provider -> provider.equals(LocationManager.GPS_PROVIDER)
        } != null

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        locationEnabled = lm.isLocationEnabled
    } else {
        locationEnabled= true
    }

    return locationEnabled && hasGPS
}