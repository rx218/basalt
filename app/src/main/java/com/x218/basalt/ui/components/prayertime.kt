package com.x218.basalt.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

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

@Preview
@Composable
fun PreviewPrayerTimeDrawer() {
    PrayerTimeDrawer()
}