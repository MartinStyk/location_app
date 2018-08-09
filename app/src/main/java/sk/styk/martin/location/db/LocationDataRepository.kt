package sk.styk.martin.location.db

import android.arch.lifecycle.LiveData
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class LocationDataRepository @Inject constructor(
        val locationDataDao: LocationDataDao) {

    fun getAll(): LiveData<List<LocationData>> {
        return locationDataDao.getAll()
    }

    fun get(id: Long): LiveData<LocationData> {
        return locationDataDao.get(id)
    }

    fun insert(locationData: LocationData) {
        return locationDataDao.insert(locationData)
    }

    fun deleteAll() {
        return locationDataDao.deleteAll()
    }
}