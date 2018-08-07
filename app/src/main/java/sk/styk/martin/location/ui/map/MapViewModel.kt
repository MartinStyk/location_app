package sk.styk.martin.location.ui.map

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import sk.styk.martin.location.db.LocationData
import sk.styk.martin.location.db.LocationDatabase
import sk.styk.martin.location.util.Preferences


class MapViewModel(app: Application) : AndroidViewModel(app) {


    var trackingStatus: LiveData<Boolean> = Preferences.requestingLocationUpdatesLive(app)

    var lineColor: Int = Preferences.lineColor(app)

    var lineWidth: Float = Preferences.lineWidth(app)

    var locationUpdateInterval: Long = Preferences.locationUpdateInterval(app)

    var locationData = LocationDatabase.getInstance(getApplication()).locationDataDao().getAll()

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

    fun deleteLocationData() {
        Thread { LocationDatabase.getInstance(getApplication()).locationDataDao().deleteAll() }
                .start()
    }

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