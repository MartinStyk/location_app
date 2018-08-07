package sk.styk.martin.location.ui.main

interface LocationTrackingController {

    fun startLocationTracking()

    fun stopLocationTracking()

    fun updateLocationTrackingInterval(newInterval: Long)
}