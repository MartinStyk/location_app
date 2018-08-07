package sk.styk.martin.location.util.extensions

import android.location.Location

fun Location.basicString(): String {

    val longitude = Location.convert(longitude, Location.FORMAT_MINUTES)
    val latitude = Location.convert(latitude, Location.FORMAT_MINUTES)

    return latitude + ", " + longitude
}