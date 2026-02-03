package com.x218.basalt.data

import android.Manifest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.activity.ComponentActivity

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