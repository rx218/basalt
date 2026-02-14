package com.x218.basalt.ui.components

import android.location.Location
import android.location.LocationManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.x218.basalt.R
import com.x218.basalt.data.PermissionState

@Composable
fun LocationBar(location: Location, perms: PermissionState, onClickLocation: () -> Unit = {}) {
    val lat = if (location.latitude == 0.0) {
        "--"
    } else {
        "${"%.2f".format(location.latitude)}°"
    }
    val long = if (location.longitude == 0.0) {
        "--"
    } else {
        "${"%.2f".format(location.longitude)}°"
    }

    Row(
        modifier = Modifier
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val tint: Color = when (perms) {
            PermissionState(coarse = true, fine = true) -> Color.Green
            PermissionState(coarse = true, fine = false) -> Color.Yellow
            PermissionState(coarse = false, fine = false) -> Color.Red
            else -> Color.Gray
        }
        IconButton(
            onClick = onClickLocation
        ) {
            Icon(
                imageVector =  Icons.Filled.LocationOn,
                tint = tint,
                contentDescription = stringResource(R.string.location_info)
            )
        }

        VerticalDivider(color = MaterialTheme.colorScheme.primary)

        Row (
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(
                text = lat,
                style = MaterialTheme.typography.displayMedium
            )
            VerticalDivider(color = MaterialTheme.colorScheme.secondary)
            Text(
                text = long,
                style = MaterialTheme.typography.displayMedium
            )
        }
    }
}

@Preview(widthDp = 640)
@Composable
fun PreviewLocationBar() {
    LocationBar(
        Location(
            LocationManager.GPS_PROVIDER
        )
            .apply {
                latitude = 67.0
                longitude = 167.0
            },
        PermissionState(coarse = true, fine = true)
    )
}