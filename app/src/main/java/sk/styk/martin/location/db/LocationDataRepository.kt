package sk.styk.martin.location.db

import android.arch.lifecycle.LiveData
import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationDataRepository @Inject constructor(
        val locationDataDao: LocationDataDao,
        val executor: Executor) {

    fun getAll(): LiveData<List<LocationData>> {
        return locationDataDao.getAll()
    }

    fun get(id: Long): LiveData<LocationData> {
        return locationDataDao.get(id)
    }

    fun insert(locationData: LocationData) {
        executor.execute {
            locationDataDao.insert(locationData)
        }
    }

    fun deleteAll() {
        executor.execute {
            locationDataDao.deleteAll()
        }
    }
}