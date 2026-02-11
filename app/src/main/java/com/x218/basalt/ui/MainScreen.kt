package com.x218.basalt.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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

    BottomSheetScaffold(
        topBar = {
            Header()
        },
        sheetPeekHeight = 160.dp,
        sheetContent = {
            PrayerTimeDrawer(uiState.location)
        }
    ) {
        Column(
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

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(viewModel<MainViewModel>())
}