package com.x218.basalt.ui.components

import android.location.Location
import android.location.LocationManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.x218.basalt.data.prayerTimes
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun PrayerTimeDrawer(location: Location) {
    val time = LocalTime.now()

    val ptimes: List<LocalTime> = prayerTimes(location)
    val prayers = Prayer.entries.zip(ptimes)
    val nextPrayer: Pair<Prayer, LocalTime>? = prayers.firstOrNull { time.isBefore(it.second) }

    Column {
        // Next prayer
        Text("Next prayer")
        if(nextPrayer != null) {
            PrayerTime(nextPrayer.first, nextPrayer.second)
        } else {
            PrayerTime(Prayer.FAJR, ptimes.first())
        }

        HorizontalDivider()

        Text("Prayer Times")

        // list of prayers
        Column {
            prayers.forEach {
                    (prayer, time) -> PrayerTime(prayer, time)
            }
        }
    }
}

@Composable
fun PrayerTime(prayer: Prayer, time: LocalTime) {
    val timestr = time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
    Row(
        modifier = Modifier
            .padding(4.dp)
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.weight(0.7f),
            text = prayer.displayName,
            style = MaterialTheme.typography.displaySmall
        )
        VerticalDivider(
            modifier = Modifier.padding(16.dp, 0.dp)
        )
        Text(
            modifier = Modifier.weight(1.3f),
            text = timestr,
            style = MaterialTheme.typography.displayMedium
        )
    }
}

@Preview
@Composable
fun PreviewPrayerTimeDrawer() {
    PrayerTimeDrawer(
        Location(
            LocationManager.GPS_PROVIDER
        ).apply{
            latitude = 67.0
            longitude = 167.0
        }
    )
}