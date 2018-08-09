package sk.styk.martin.location.ui.statistics

import android.arch.lifecycle.*
import sk.styk.martin.location.db.LocationData
import sk.styk.martin.location.db.LocationDataRepository
import javax.inject.Inject


class StatisticsViewModel @Inject constructor(
        private val locationDataRepository: LocationDataRepository) : ViewModel() {


    val data: LiveData<List<LocationData>> by lazy {
        locationDataRepository.getAll()
    }

    val fastestSpeed = Transformations.map(data) {
        it.maxBy {
            it.speed
        }?.speed ?: 0f
    }

    val slowestSpeed = Transformations.map(data) {
        it.minBy {
            it.speed
        }?.speed ?: 0f
    }

    val averageSpeed = Transformations.map(data) {
        val result = it.sumByDouble {
            it.speed.toDouble()
        }.div(it.size).toFloat()
        if (result.isNaN()) {
            0f
        } else
            result
    }

}