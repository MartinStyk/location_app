package sk.styk.martin.location.ui.locationdetailsheet

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.ViewModel
import sk.styk.martin.location.db.LocationData
import sk.styk.martin.location.db.LocationDataRepository
import javax.inject.Inject


class LocationDetailViewModel @Inject constructor(
        private val locationDataRepository: LocationDataRepository) : ViewModel() {

    lateinit var locationData: LiveData<LocationData>

    fun init(locationId: Long) {
        locationData = locationDataRepository.get(id = locationId)
    }

}