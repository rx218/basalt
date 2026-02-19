package com.x218.basalt.ui.components

import android.location.Location
import android.location.LocationManager
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.batoulapps.adhan2.CalculationMethod
import com.batoulapps.adhan2.Coordinates
import com.batoulapps.adhan2.Prayer
import com.batoulapps.adhan2.PrayerTimes
import com.batoulapps.adhan2.data.DateComponents
import com.x218.basalt.R
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

@Composable
@OptIn(ExperimentalTime::class)
fun PrayerTimeDrawer(location: Location) {
    val date = LocalDate.now()

    val pt = PrayerTimes(
        Coordinates(location.latitude, location.longitude),
        DateComponents(date.year, date.monthValue, date.dayOfMonth),
        CalculationMethod.UMM_AL_QURA.parameters
    )

    val list = pt.run {
        listOf(
            fajr, sunrise, dhuhr, asr, maghrib, isha
        )
    }.map {
        SimpleDateFormat.getTimeInstance().format(Date(it.toEpochMilliseconds()))
    }.zip(Prayer.entries.filter { it != Prayer.NONE })

    val nextPrayer = if(pt.nextPrayer(Clock.System.now()) != Prayer.NONE) {
        pt.nextPrayer(Clock.System.now())
    } else {
        Prayer.FAJR
    }
    val nextPrayerTime =
        SimpleDateFormat.getTimeInstance().format(Date(pt.timeForPrayer(nextPrayer)!!.toEpochMilliseconds()))

    Column {
        // Next prayer
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        ) {
            Text(
                text = "Next prayer",
                style = MaterialTheme.typography.displayMedium,
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.prayer_times),
                contentDescription = "Prayer Time Icon"
            )
        }

        HorizontalDivider()

        PrayerTime(nextPrayer, nextPrayerTime)

        HorizontalDivider(thickness = 3.dp)

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        ) {
            Text(
                text = "Prayer Times",
                style = MaterialTheme.typography.displayMedium,
            )
            Icon(
                imageVector = ImageVector.vectorResource(R.drawable.prayer_times),
                contentDescription = "Prayer Time Icon"
            )
        }

        HorizontalDivider()

        // list of prayers
        Column {
            list.forEach { PrayerTime(it.second, it.first) }
        }
    }
}

@Composable
fun PrayerTime(prayer: Prayer, time: String) {
    Row(
        modifier = Modifier
            .padding(4.dp)
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            modifier = Modifier.weight(0.9f),
            text = prayer.toString(),
            style = MaterialTheme.typography.headlineMedium
        )
        VerticalDivider(
            modifier = Modifier.padding(16.dp, 0.dp)
        )
        Text(
            modifier = Modifier.weight(1.1f),
            text = time,
            style = MaterialTheme.typography.headlineMedium
        )
    }
}

@Preview(widthDp = 640)
@Composable
fun PreviewPrayerTimeDrawer() {
    PrayerTimeDrawer(
        Location(
            LocationManager.GPS_PROVIDER
        ).apply{
            latitude = 24.0
            longitude = 46.0
        }
    )
}