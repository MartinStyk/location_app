package sk.styk.martin.location.db

import java.util.concurrent.Executor
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationDataService @Inject constructor(
        val locationDataRepository: LocationDataRepository,
        val executor: Executor) {


}