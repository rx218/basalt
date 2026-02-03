package com.x218.basalt.ui

import android.location.Location
import android.location.LocationManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import com.x218.basalt.CompassState
import com.x218.basalt.data.PermissionState
import com.x218.basalt.ui.components.Compass
import com.x218.basalt.ui.components.Header
import com.x218.basalt.ui.components.LocationBar
import com.x218.basalt.ui.components.PrayerTimeDrawer

@Composable
fun MainScreen(perms: PermissionState, compassState: CompassState, location: Location) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ){
        Header()
        HorizontalDivider()
        Compass(compassState)
        HorizontalDivider()
        LocationBar(location, perms)
        HorizontalDivider()
        PrayerTimeDrawer()
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(
        PermissionState(coarse = true, fine = true),
        CompassState(200f, 30f),
        Location(LocationManager.GPS_PROVIDER).apply {
            latitude = 25.0
            longitude = 47.0
        }
    )
}