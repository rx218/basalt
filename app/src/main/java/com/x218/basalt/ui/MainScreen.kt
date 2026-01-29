package com.x218.basalt

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.x218.basalt.ui.PermissionDialog


@Composable
fun MainScreen(perms: PermissionState) {
    Column {

        if (!(perms.fine && perms.coarse)) {
            PermissionDialog()
        } else {
            Header()
            HorizontalDivider()
            Compass()
            LocationBar()
            PrayerTimeDrawer()
        }
    }
}

@Composable
@Preview
fun MainScreenPreview() {
    MainScreen(PermissionState(true, true))
}

@Composable
fun PrayerTimeDrawer() {
    TODO("Not yet implemented")
}

@Composable
fun Compass() {
    TODO("Not yet implemented")
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
            modifier = Modifier.fillMaxWidth(),
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
