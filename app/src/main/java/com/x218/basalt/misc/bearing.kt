package com.x218.basalt

import java.lang.Math

// Geographic coordinates in decimal degrees
data class GeographicCoordinate (val latitude: Double, val longitude: Double)

Location(GPS_PROVIDER)


// Factor for converting from degrees to radians
val k = Math.PI / 180

// Coordinates of the Kaaba
val KAABA_COORDINATE = GeographicCoordinate(21.4225, 039.82617)

fun bearing(from: GeographicCoordinate, to: GeographicCoordinate): Double {

    // Latitude/longitude of the starting point in radians
    val lat1 = from.latitude * k
    val long1 = from.longitude * k

    // Latitude/longitude of the terminal point in radians
    val lat2 = to.latitude * k
    val long2 = to.longitude * k

    // Formula to calculate bearing
    // Reference: https://www.movable-type.co.uk/scripts/latlong.html
    val y = Math.sin(long2 - long1) * Math.cos(lat2)
    val x = Math.cos(lat1) * Math.sin(lat2) -
	    Math.sin(lat1) * Math.cos(lat2) * Math.cos(long2 - long1)
    val theta = Math.atan2(y, x)
    val bearing = (theta * 180 / Math.PI + 360) % 360

    return bearing
}

fun qiblaDirection(position : GeographicCoordinate) : Double {
    return bearing(position, KAABA_COORDINATE)
}
