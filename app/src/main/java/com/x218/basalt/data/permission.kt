package com.x218.basalt.data

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.activity.ComponentActivity

const val TAG: String = "Permission"

data class PermissionState(var coarse: Boolean, var fine: Boolean)

fun requestPermission(perms: PermissionState, mContext: ComponentActivity) {
    val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(
        mContext,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) || ActivityCompat.shouldShowRequestPermissionRationale(
        mContext,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    val locationPermissionRequest = mContext.registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                // fine location granted
                perms.fine = true
                perms.coarse = true

                Log.println(Log.INFO, "Permissions", "Fine Location Granted")
            }

            permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                // only coarse location granted
                perms.coarse = true
                Log.println(Log.INFO, "Permissions", "Coarse Location Granted")
            }

            else -> {
                // No location granted, show error
                perms.coarse = false
                perms.fine = false
                Log.println(Log.INFO, "Permissions", "No Location Granted")
            }
        }
    }

    when {
        // Show rationale dialog if needed
        showRationale -> {}
        else -> {
            // launch permission request
            locationPermissionRequest.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }
}

fun checkPermissions(mContext: Context): PermissionState {
    val coarseGranted: Boolean = ActivityCompat.checkSelfPermission(
        mContext, Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    val fineGranted: Boolean = ActivityCompat.checkSelfPermission(
        mContext, Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    return PermissionState(coarseGranted, fineGranted)
}