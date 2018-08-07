package sk.styk.martin.location.db

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.location.Location
import com.google.android.gms.maps.model.LatLng

@Entity(tableName = "locationData")
data class LocationData(
        @PrimaryKey(autoGenerate = true) var id: Long?,
        val provider: String,
        val latitude: Double,
        val longitude: Double,
        val altitude: Double,
        val speed: Float,
        val bearing: Float,
        val timeStamp: Long) {

    constructor(location: Location) : this(null,
            provider = location.provider,
            latitude = location.latitude,
            longitude = location.longitude,
            altitude = location.altitude,
            speed = location.speed,
            bearing = location.bearing,
            timeStamp = location.time
    )

    fun latLng(): LatLng = LatLng(latitude, longitude)
}