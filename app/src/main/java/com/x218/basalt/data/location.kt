package com.x218.basalt.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat

@RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
fun getLocation(lm: LocationManager, mContext: Context): Location {
    var location : Location? = null

    // See if we have a cached Location
    if (lm.getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
        location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
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
        return location!!
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
    while (location == null) {}
    // We only need location once, so remove updates after
    lm.removeUpdates(listener)

    return location
}

fun checkPermissions(mContext: Context): Boolean {
    val coarseGranted: Boolean = ActivityCompat.checkSelfPermission(
            mContext, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    val fineGranted: Boolean = ActivityCompat.checkSelfPermission(
            mContext, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

    return coarseGranted && fineGranted
}

fun checkGPSprovider(lm: LocationManager): Boolean {
    var locationEnabled: Boolean
    val providers = lm.getProviders(true)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        locationEnabled = lm.isLocationEnabled
    } else {
        locationEnabled= true
    }
    val hasGPS = providers.find { el -> el.equals(LocationManager.GPS_PROVIDER) } != null

    return locationEnabled && hasGPS
}