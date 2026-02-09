package com.x218.basalt.data

import android.location.Location
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZonedDateTime
import kotlin.math.abs
import kotlin.math.acos
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.floor
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.math.tan

fun prayerTimes(location: Location): List<LocalTime> {
    val fajrAngle = 18.0
    val shadowFactor = 2
    val ishaAngle = 18.0
    val descendCorrection = 2.0/60 // in hours
    val elevation = if(location.hasAltitude()) { location.altitude } else { 0.0 }
    val zone = ZonedDateTime.now().offset.totalSeconds / 3600.0

    val dateTime = LocalDateTime.now()

    var year = dateTime.year
    var month = dateTime.month.value
    val day = dateTime.dayOfMonth
    val hour = dateTime.hour
    val minute = dateTime.minute
    val second = dateTime.second

    if (month <= 2) {
        month += 12
        year -= 1
    }

    var b = 0
    if (dateTime.isAfter(LocalDateTime.of(1582, 10, 14, 0, 0))) {
        val a = (year / 100)
        b = 2 + (a / 4) - a
    }

    // Julian day
    val jd = 1720994.5 + (365.25 * year).toInt() + (30.6001 * (month + 1)).toInt() + b + day +
            ((hour * 3600 + minute * 60 + second) / 86400) - (zone / 24)

    // Declination of time
    val t = 2 * Math.PI * (jd - 2451545) / 365.25
    val delta = 0.37877 + 23.264 * sin(57.297 * t - 79.547) +
            0.3812 * sin(2*57.297*t - 82.682) +
            0.17132 * sin(3*57.297*t - 59.722)

    // Equation of time
    val u = (jd - 2451545) / 36525
    val l0 = 280.46607 + 36000.7698*u
    val et1000 = -(1789 + 237*u) * sin(l0) -
            (7146 - 62*u) * cos(l0) +
            (9934 - 14*u) * sin(2*l0) -
            (29 + 5*u) * cos(2*l0) +
            (74 + 10*u) * sin(3*l0) +
            (320 - 4*u) * cos(3*l0) -
            212*sin(4*l0)
    val et = et1000 / 1000

    // transit time, local noon time
    val tt = 12 + zone - (location.longitude / 15) - (et / 60)

    // sun altitudes
    val saFajr = -(fajrAngle)
    val saSunrise = -0.8333 - (0.0347 * sqrt(elevation))
    val saAsr = (Math.PI / 2) - atan(shadowFactor + tan(abs(delta - location.latitude)))
    val saMaghrib = saSunrise
    val saIsha = -(ishaAngle)

    // hour angles
    val haFajr = acos(
        (sin(saFajr) - sin(location.latitude) * sin(delta) / cos(location.latitude) * cos(delta))
    )
    val haSunrise = acos(
        (sin(saSunrise) - sin(location.latitude) * sin(delta) / cos(location.latitude) * cos(delta))
    )
    val haAsr = acos(
        (sin(saAsr) - sin(location.latitude) * sin(delta) / cos(location.latitude) * cos(delta))
    )
    val haMaghrib = acos(
        (sin(saMaghrib) - sin(location.latitude) * sin(delta) / cos(location.latitude) * cos(delta))
    )
    val haIsha = acos(
        (sin(saIsha) - sin(location.latitude) * sin(delta) / cos(location.latitude) * cos(delta))
    )

    // prayer times in hours
    val fajr    = tt - haFajr / 15
    val sunrise = tt - haSunrise / 15
    val zuhr    = tt + descendCorrection
    val asr     = tt + haAsr / 15
    val maghrib = tt + haMaghrib / 15
    val isha    = tt + haIsha / 15

    // create list of time (in hours) and map the hours to LocalTime
    return listOf(fajr, sunrise, zuhr, asr, maghrib, isha).map { time -> hoursToLocalTime(time) }
}

fun hoursToLocalTime(time: Double): LocalTime {
    val hours = floor(time).toInt()
    val minutes = floor((time - hours) * 60).toInt()

    return LocalTime.of(hours, minutes)
}

enum class Prayer(val displayName: String) {
    FAJR("Fajr"),
    DHUHR("Dhuhr"),
    ASR("Asr"),
    MAGHRIB("Maghrib"),
    ISHA("Isha")
}