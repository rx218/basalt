package com.x218.basalt.data

import android.location.Location
import kotlin.math.asin
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin

fun prayer_time(prayer: Prayer, location: Location) {
    val jd = 0
    val d = jd - 2451545.0;  // Julian date offset

    val g = 357.529 + 0.98560028* d;
    val q = 280.459 + 0.98564736* d;
    val L = q + 1.915 * sin(g) + 0.020 * sin(2*g);

    val R = 1.00014 - 0.01671* cos(g) - 0.00014* cos(2*g);
    val e = 23.439 - 0.00000036* d;
    val RA = atan2(cos(e) * sin(L), cos(L)) / 15;

    val D = asin(sin(e) * sin(L));  // Declination of the Sun
    val EqT = q/15 - RA;  // Equation of Time

    val timezone = 0
    val tdhuhr = 12 + timezone - location.longitude / 15 - EqT
    
    when(prayer) {
        Prayer.FAJR -> TODO()
        Prayer.DHUHR -> TODO()
        Prayer.ASR -> TODO()
        Prayer.MAGHRIB -> TODO()
        Prayer.ISHA -> TODO()
    }
}

enum class Prayer(val displayName: String) {
    FAJR("Fajr"),
    DHUHR("Dhuhr"),
    ASR("Asr"),
    MAGHRIB("Maghrib"),
    ISHA("Isha")
}