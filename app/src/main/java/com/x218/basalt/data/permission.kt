package com.x218.basalt.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.activity.ComponentActivity

data class PermissionState(var coarse: Boolean, var fine: Boolean)

/**
 * Creates an activity to request location permission and launches it.
 * The `perms` object is updated to reflect the new state of permissions
 */
fun requestPermission(perms: PermissionState, mContext: ComponentActivity) {
    // See if we need to show rationale for permissions
    val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(
        mContext,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) || ActivityCompat.shouldShowRequestPermissionRationale(
        mContext,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    // Activity to request location permission
    val locationPermissionRequest = mContext.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // fine location granted
                perms.fine = true
                perms.coarse = true
            }

            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // only coarse location granted
                perms.coarse = true
            }

            else -> {
                // no location permission granted
                perms.coarse = false
                perms.fine = false
            }
        }
    }

    // launch permission request
    locationPermissionRequest.launch(
        arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    )
}

/**
 * Checks if we have both coarse and fine location granted
 */
fun checkPermissions(mContext: Context): PermissionState {
    val coarseGranted: Boolean = ActivityCompat.checkSelfPermission(
        mContext, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val fineGranted: Boolean = ActivityCompat.checkSelfPermission(
        mContext, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    return PermissionState(coarseGranted, fineGranted)
}