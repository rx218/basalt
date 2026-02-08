package com.x218.basalt.data

import android.Manifest
import android.content.Context
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresPermission

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
fun getLocation(lm: LocationManager, mContext: Context): Location {
    val TAG: String = "getLocation"
    var location : Location? = null

    // See if we have a cached Location
    if (lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
        Log.i(TAG, "Location obtained by getLastKnownLocation")
        return location
    }

    // Otherwise get a new copy

    // For android versions above R, use current location
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        lm.getCurrentLocation(
            LocationManager.GPS_PROVIDER,
            null,
            mContext.mainExecutor,
            { l: Location -> location = l }
        )
        if (location != null) {
            Log.i(TAG, "Location obtained by getCurrentLocation")
            return location
        }
    }

    // For older versions of android use Location Listener
    val listener = LocationListener { l: Location -> location = l }
    lm.requestLocationUpdates(
        LocationManager.GPS_PROVIDER,
        0,
        0f,
        listener
    )
    // Wait until we are provided a location
    //while (location == null) {}
    TODO("See for a way to wait for location value. also see if it even is necessary")
    Log.i(TAG, "Location obtained by requestLocationUpdates")
    // We only need location once, so remove updates after
    lm.removeUpdates(listener)

    return location!!
}

fun checkGpsProvider(lm: LocationManager): Boolean {
    var locationEnabled: Boolean
    val providers = lm.getProviders(true)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        locationEnabled = lm.isLocationEnabled
    } else {
        locationEnabled= true
    }
    val hasGPS = providers.find { el -> el.equals(LocationManager.GPS_PROVIDER) } != null

    Log.i(TAG, "Location Enabled $locationEnabled")
    Log.i(TAG, "Has GPS $hasGPS")

    return locationEnabled && hasGPS
}