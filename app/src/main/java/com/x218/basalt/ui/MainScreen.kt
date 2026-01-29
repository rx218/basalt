package com.x218.basalt

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.x218.basalt.ui.PermissionDialog


@Composable
fun MainScreen(perms: PermissionState, compassState: CompassState) {
    Column {

        if (!(perms.fine && perms.coarse)) {
            PermissionDialog()
        } else {
            Header()
            HorizontalDivider()
            Compass(compassState)
            LocationBar()
            PrayerTimeDrawer()
        }
    }
}

@Composable
@Preview
fun MainScreenPreview() {
    MainScreen(PermissionState(true, true), CompassState(200f, 30f))
}

@Composable
fun PrayerTimeDrawer() {
    Text("2 o clock or something")
}

@Composable
fun Compass(compassState: CompassState) {
    Box {
        Image(
            painter = painterResource(id = R.drawable.rose),
            alpha = 0.3f,
            contentDescription = "Compass Rose"
        )
        Image(
            painter = painterResource(id = R.drawable.needle),
            modifier = Modifier
                .align(Alignment.Center)
                .rotate(compassState.north + 90),
            contentDescription = "Needle North"
        )
        Image(
            painter = painterResource(id = R.drawable.needle),
            modifier = Modifier
                .rotate(compassState.kaabaBearing + 90)
                .scale(0.75f)
                .align(Alignment.Center),
            contentDescription = "Needle Kaaba"
        )
    }
}

@Preview
@Composable
fun PreviewCompass() {
    Compass(CompassState(0f, 90f))
}

@Preview
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
    Compass(CompassState(4f, 4f))
}

@Preview
@Composable
fun Header() {
    Row {
        Image(
            painter = painterResource(id = R.drawable.app_logo),
            contentDescription = stringResource(id = R.string.app_logo_description)
        )

        Text(stringResource(R.string.app_name))

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

@Preview
@Composable
fun LocationBar() {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = { print("Location Button Clicked") }// show help dialog,
        ) {
            Icon(
                imageVector =  Icons.Filled.LocationOn,
                contentDescription = stringResource(R.string.location_info)
            )
        }

        Row (
            modifier = Modifier.fillMaxHeight(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            VerticalDivider(color = MaterialTheme.colorScheme.secondary)
            Text(text = "latitude")
            VerticalDivider(color = MaterialTheme.colorScheme.secondary)
            Text(text = "longitude")
        }
    }
}

@Preview
@Composable
fun Greeting() {
    Column {
        Text("Android")
        Text("Good Morning")
    }
}
