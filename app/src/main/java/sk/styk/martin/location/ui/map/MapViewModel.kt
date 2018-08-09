package sk.styk.martin.location.ui.map

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import sk.styk.martin.location.db.LocationData
import sk.styk.martin.location.db.LocationDataDao
import sk.styk.martin.location.db.LocationDataRepository
import sk.styk.martin.location.util.Preferences
import javax.inject.Inject


class MapViewModel @Inject constructor(
        private val locationRepository: LocationDataRepository,
        private val preferences: Preferences) : ViewModel() {


    var trackingStatus: LiveData<Boolean> = preferences.requestingLocationUpdatesLive()

    var lineColor: Int = preferences.lineColor()

    var lineWidth: Float = preferences.lineWidth()

    var locationUpdateInterval: Long = preferences.locationUpdateInterval()

    var locationData = locationRepository.getAll()

    var viewTypeData = MutableLiveData<MapViewType>().apply { postValue(MapViewType.TRACKING) }


    fun changeViewType() {
        viewTypeData.postValue(viewTypeData.value?.change() ?: MapViewType.TRACKING)
    }

    fun changeViewType(viewType: MapViewType) {
        viewTypeData.postValue(viewType)
    }

    fun getLatestLocation(): LocationData? {
        return locationData.value?.let {
            if (it.isEmpty()) {
                null
            } else
                it.last()
        }
    }

    fun deleteLocationData() = locationRepository.deleteAll()

    fun getCompleteViewBounds(): LatLngBounds? {
        return locationData.value?.let { list ->
            if (list.isEmpty()) {
                null
            } else {
                val lastLatLng = LatLng(list.last().latitude, list.last().longitude)
                var bounds = LatLngBounds(lastLatLng, lastLatLng)

                list.map { locationData ->
                    LatLng(locationData.latitude, locationData.longitude)
                }.forEach { point ->
                    bounds = bounds.including(point)
                }
                bounds
            }
        }
    }
}