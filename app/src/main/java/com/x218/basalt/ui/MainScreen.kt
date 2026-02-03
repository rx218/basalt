package com.x218.basalt.ui

import android.location.Location
import android.location.LocationManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.x218.basalt.CompassState
import com.x218.basalt.R
import com.x218.basalt.data.PermissionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlin.time.Duration.Companion.seconds


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


@Composable
fun PrayerTimeDrawer() {
    PrayerTime()
}

@Composable
fun PrayerTime() {
    Row(
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "FAJR",
            style = MaterialTheme.typography.displaySmall
        )
        VerticalDivider()
        Text(
            text = "14:00",
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.displayMedium
        )
    }
}

@Composable
fun Compass(compassState: CompassState) {
    Box {
        Image(
            painter = painterResource(id = R.drawable.rose),
            modifier = Modifier
                .align(Alignment.Center)
                .rotate(compassState.north),
            contentDescription = "Compass Rose"
        )
        Image(
            painter = painterResource(id = R.drawable.needle),
            modifier = Modifier
                .rotate(compassState.kaabaBearing)
                .scale(0.75f)
                .align(Alignment.Center),
            contentDescription = "Needle Kaaba"
        )
    }
}


//@Preview
@Composable
fun Header() {
    Row(
        modifier = Modifier.padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            modifier = Modifier.size(36.dp),
            contentDescription = stringResource(id = R.string.app_logo_description)
        )

        Spacer(
            modifier = Modifier.width(4.dp)
        )

        Text(
            text = stringResource(R.string.app_name),
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(
            modifier = Modifier.fillMaxWidth()
        )

        // Help Icon
        IconButton(
                onClick = { print("Info Button clicked") }
                ) {
                Icon(
                    imageVector = Icons.Filled.Info,
                    contentDescription = stringResource(R.string.info_desc)
                )
            }
    }
}

@Composable
fun LocationBar(location: Location, perms: PermissionState) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            //.padding(16.dp)
            .height(48.dp)
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val tint: Color = when (perms) {
            PermissionState(true, true) -> Color.Green
            PermissionState(true, false) -> Color.Yellow
            PermissionState(false, false) -> Color.Red
            else -> Color.Gray
        }
        IconButton(
            onClick = { TODO("Show help dialog") },
            modifier = Modifier.padding(3.dp)
        ) {
            Icon(
                imageVector =  Icons.Filled.LocationOn,
                tint = tint,
                contentDescription = stringResource(R.string.location_info)
            )
        }

        VerticalDivider(color = MaterialTheme.colorScheme.primary)

        Row (
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = location.latitude.toString(),
                style = MaterialTheme.typography.displayMedium
            )
            VerticalDivider(color = MaterialTheme.colorScheme.secondary)
            Text(
                text = location.longitude.toString(),
                style = MaterialTheme.typography.displayMedium
            )
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen(
        PermissionState(true, true),
        CompassState(200f, 30f),
        Location(LocationManager.GPS_PROVIDER).apply {
            latitude = 25.0
            longitude = 47.0
        }
    )
}

//@Preview
@Composable
fun PreviewCompass() {
    val cs = CompassState(0f, 90f)
    Compass(cs)
    for(i in 0..360) {
        cs.kaabaBearing += i
        cs.north -= i/2
    }
    runBlocking { delay(1.seconds) }
}

//@Preview
@Composable
fun PreviewCompassAdjustable() {
    Column {
        var v1 by remember { mutableFloatStateOf(0f) }
        var v2 by remember { mutableFloatStateOf(0f) }
        Compass(CompassState(v1, v2))
        Slider(
            value = v1,
            onValueChange = { v1 = it },
            valueRange = 0f..360f,
            steps = 10
        )
        Text(text = v1.toString())
        Slider(
            value = v2,
            onValueChange = { v2 = it },
            valueRange = 0f..360f,
            steps = 10
        )
        Text(text = v2.toString())
    }
}

@Preview
@Composable
fun PreviewLocationBar() {
    LocationBar(
        Location(
            LocationManager.GPS_PROVIDER
        ).apply{
            latitude = 67.0
            longitude = 167.0
        },
        PermissionState(false, true)
    )
}