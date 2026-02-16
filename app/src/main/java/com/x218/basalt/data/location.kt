package com.x218.basalt.data

import android.Manifest
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.annotation.RequiresPermission
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/**
 * Helper function that uses the many android location retrieval
 * mechanisms to get a single location.
 * `getLocation` may take a long time to complete, so it is preferred ot invoke it as a coroutine
 *
 * Parameter `Executor exec` is used to run the `LocationManager.getCurrentLocation()` function
 * Provide a non-blocking executor
 *
 * Methods used are:
 * - `LocationManager.getLastKnownLocation()` to get a cached location if available, or else
 * - `LocationManager.getCurrentLocation()` for new android versions
 * - `LocationManager.requestLocationUpdates()` for older android versions
 *
 * The provider is chosen in the following order, whichever is available first:
 * 1. LocationManager.FUSED_PROVIDER
 * 2. LocationManager.PASSIVE_PROVIDER
 * 3. LocationManager.NETWORK_PROVIDER
 * 4. LocationManager.GPS_PROVIDER
 */
@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
suspend fun getLocation(lm: LocationManager, exec: Executor): Location {
    // Get a suitable provider
    val provider = when {
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
                lm.isProviderEnabled(LocationManager.FUSED_PROVIDER) -> LocationManager.FUSED_PROVIDER
        lm.isProviderEnabled(LocationManager.PASSIVE_PROVIDER) -> LocationManager.PASSIVE_PROVIDER
        lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
        else -> LocationManager.GPS_PROVIDER
    }

    // See if we have a cached Location
    lm.getLastKnownLocation(provider)?.let { return it }

    // Otherwise get a new copy
    // suspendCoroutine waits until the respective callbacks are executed
    return suspendCoroutine { continuation ->
        // For android versions above R, use getCurrentLocation
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            lm.getCurrentLocation(
                provider,
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
                provider,
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


/** Checks if device has a GPS provider and if location is enabled */
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