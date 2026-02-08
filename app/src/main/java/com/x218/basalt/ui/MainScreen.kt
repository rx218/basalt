package com.x218.basalt.ui

import android.location.Location
import android.location.LocationManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(perms: PermissionState, compassState: CompassState, location: Location) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ){
        BottomSheetScaffold(
            topBar = {
                Header()
            },
            sheetContent = {
                PrayerTimeDrawer()
            }
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Compass(compassState)
                HorizontalDivider()
                LocationBar(location, perms)
                HorizontalDivider()
            }
        }
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