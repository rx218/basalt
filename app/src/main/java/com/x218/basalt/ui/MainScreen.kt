package com.x218.basalt.ui

import android.location.Location
import android.location.LocationManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.x218.basalt.data.PermissionState
import com.x218.basalt.ui.components.Compass
import com.x218.basalt.ui.components.Header
import com.x218.basalt.ui.components.LocationBar
import com.x218.basalt.ui.components.PermissionDialog
import com.x218.basalt.ui.components.PrayerTimeDrawer
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val kaabaBearing = uiState.location.bearingTo(kaabaLocation)
    val northAngle by viewModel.azimuthFlow.collectAsState()

    fun normalizeAngle(angle: Float): Float {
        return (angle + 360) % 360
    }

    BottomSheetScaffold(
        topBar = {
            Header(onClickInfo = { viewModel.setShowDialog() })
        },
        sheetPeekHeight = 160.dp,
        contentColor = MaterialTheme.colorScheme.background,
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
            Text(
                modifier = Modifier.padding(4.dp),
                text = "${normalizeAngle(northAngle).toInt()}°",
                style = MaterialTheme.typography.displaySmall,
                color = Color.Red
            )
            Compass(
                north = - northAngle,
                kaabaBearing =  kaabaBearing
            )
            Text(
                modifier = Modifier.padding(4.dp),
                text = if(abs(kaabaBearing - northAngle) < 3) { "Pointing to the Kaaba" } else {"--"},
                style = MaterialTheme.typography.displaySmall,
                color = Color.Green
            )
            HorizontalDivider()
            LocationBar(
                location = uiState.location,
                perms =  uiState.perms,
                onClickLocation = { viewModel.setShowDialog() }
            )
            HorizontalDivider()
        }
    }

    if(uiState.showDialog) {
        PermissionDialog(uiState.perms, { viewModel.unsetShowDialog() })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(northAngle: Float, location: Location, perms: PermissionState, showDialog: Boolean) {
    val kaabaBearing = location.bearingTo(kaabaLocation)
    fun normalizeAngle(angle: Float): Float {
        return (angle + 360) % 360
    }

    BottomSheetScaffold(
        topBar = {
            Header(onClickInfo = {})
        },
        sheetPeekHeight = 160.dp,
        contentColor = MaterialTheme.colorScheme.background,
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
            Text(
                modifier = Modifier.padding(4.dp),
                text = "${normalizeAngle(northAngle).toInt()}°",
                style = MaterialTheme.typography.displaySmall,
                color = Color.Red
            )
            Compass(
                north = - northAngle,
                kaabaBearing =  kaabaBearing
            )
            Text(
                modifier = Modifier.padding(4.dp),
                text = if(abs(kaabaBearing - northAngle) < 3) { "Pointing to the Kaaba" } else {"--"},
                style = MaterialTheme.typography.displaySmall,
                color = Color.Green
            )
            HorizontalDivider()
            LocationBar(
                location = location,
                perms =  perms,
                onClickLocation = {}
            )
            HorizontalDivider()
        }
    }

    if(showDialog) {
        PermissionDialog(perms, {})
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
    val showDialog = false
    MainScreen(northAngle, location, perms, showDialog)
}