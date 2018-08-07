package sk.styk.martin.location.ui.locationdetailsheet

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import sk.styk.martin.location.db.LocationData
import sk.styk.martin.location.db.LocationDatabase


class LocationDetailViewModel(app: Application) : AndroidViewModel(app) {

    lateinit var locationData: LiveData<LocationData>

    fun init(locationId: Long) {
        locationData = LocationDatabase.getInstance(getApplication()).locationDataDao().get(id = locationId)
    }

}