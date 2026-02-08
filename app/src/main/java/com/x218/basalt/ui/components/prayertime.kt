package com.x218.basalt.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.x218.basalt.data.Prayer

@Composable
fun PrayerTimeDrawer() {
    // Next prayer
    Text("Next prayer")
    PrayerTime(Prayer.FAJR)

    HorizontalDivider()

    // list of prayers
    Column {
        for (prayer in Prayer.entries) {
            PrayerTime(prayer)
        }
    }
}

@Composable
fun PrayerTime(prayer: Prayer) {
    Row(
        modifier = Modifier
            .padding(4.dp)
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = prayer.displayName,
            style = MaterialTheme.typography.displaySmall
        )
        VerticalDivider(
            modifier = Modifier.padding(16.dp, 0.dp)
        )
        Text(
            // TODO
            text = "TODO",
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