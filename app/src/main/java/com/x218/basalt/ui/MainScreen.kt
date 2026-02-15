package com.x218.basalt.ui

import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.x218.basalt.data.PermissionState
import com.x218.basalt.ui.components.Compass
import com.x218.basalt.ui.components.Header
import com.x218.basalt.ui.components.LocationBar
import com.x218.basalt.ui.components.PrayerTimeDrawer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val kaabaBearing = uiState.location.bearingTo(kaabaLocation)
    val northAngle by viewModel.azimuthFlow.collectAsState()
    Log.i("MainScreen", "New northAngle $northAngle")
    Log.i("MainScreen", "kaaba Bearing $kaabaBearing")
    Log.i("MainScreen", "Permissions : ${uiState.perms}")
    Log.i("MainScreen", "Location : ${uiState.location}")

    BottomSheetScaffold(
        topBar = {
            Header()
        },
        sheetPeekHeight = 160.dp,
        sheetContent = {
            PrayerTimeDrawer(uiState.location)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Compass(
                north = northAngle,
                kaabaBearing =  kaabaBearing
            )
            HorizontalDivider()
            LocationBar(
                location = uiState.location,
                perms =  uiState.perms
            )
            HorizontalDivider()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(northAngle: Float, location: Location, perms: PermissionState) {
    val kaabaBearing = location.bearingTo(kaabaLocation)

    BottomSheetScaffold(
        topBar = {
            Header()
        },
        sheetPeekHeight = 160.dp,
        sheetContent = {
            PrayerTimeDrawer(location)
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Compass(
                north = northAngle,
                kaabaBearing = kaabaBearing
            )
            HorizontalDivider()
            LocationBar(
                location = location,
                perms = perms
            )
            HorizontalDivider()
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    val location = Location(LocationManager.GPS_PROVIDER).apply{
        latitude = 38.0
        longitude = 41.0
    }
    val perms = PermissionState(coarse = false, fine = false)
    val northAngle = 0.0f
    println(location)
    MainScreen(northAngle, location, perms)
}