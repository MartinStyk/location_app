package sk.styk.martin.location.util

import android.arch.lifecycle.LiveData
import android.content.SharedPreferences
import java.util.concurrent.TimeUnit
import javax.inject.Inject

class Preferences @Inject constructor(
        private val sharedPreferences: SharedPreferences) {

    fun initialize() {
        sharedPreferences.edit()
                .putBoolean(KEY_FIRST_RUN, false)
                .putString(KEY_LOCATION_UPDATES_INTERVAL, DEFAULT_LOCATION_UPDATES_INTERVAL)
                .putString(KEY_LINE_COLOR, DEFAULT_LINE_COLOR)
                .putString(KEY_LINE_WIDTH, DEFAULT_LINE_WIDTH)
                .apply()
    }

    fun isFirstRun(): Boolean {
        return sharedPreferences
                .getBoolean(KEY_FIRST_RUN, true)
    }

    fun requestingLocationUpdates(): Boolean {
        return sharedPreferences
                .getBoolean(KEY_REQUESTING_LOCATION_UPDATES, false)
    }

    fun requestingLocationUpdatesLive(): LiveData<Boolean> {
        return sharedPreferences
                .booleanLiveData(Preferences.KEY_REQUESTING_LOCATION_UPDATES, false)
    }

    fun setRequestingLocationUpdates(requestingLocationUpdates: Boolean) {
        sharedPreferences
                .edit()
                .putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates)
                .apply()
    }

    fun locationUpdateInterval(): Long {
        return TimeUnit.SECONDS.toMillis(sharedPreferences
                .getString(Preferences.KEY_LOCATION_UPDATES_INTERVAL, DEFAULT_LOCATION_UPDATES_INTERVAL)
                .toLong()
        )
    }

    fun lineColor(): Int {
        return sharedPreferences
                .getString(Preferences.KEY_LINE_COLOR, DEFAULT_LINE_COLOR)
                .toLong(radix = 16)
                .toInt()
    }

    fun lineWidth(): Float {
        return sharedPreferences
                .getString(Preferences.KEY_LINE_WIDTH, DEFAULT_LINE_WIDTH)
                .toFloat()
    }

    companion object {

        const val KEY_FIRST_RUN = "is_first_run"
        const val KEY_REQUESTING_LOCATION_UPDATES = "requesting_locaction_updates"
        const val KEY_REQUESTING_COMPLETE_VIEW = "requesting_complete_view"
        const val KEY_LOCATION_UPDATES_INTERVAL = "preference_update_interval"
        const val KEY_LINE_COLOR = "preference_line_color"
        const val KEY_LINE_WIDTH = "preference_line_width"

        const val DEFAULT_LOCATION_UPDATES_INTERVAL = 15.toString()
        const val DEFAULT_LINE_COLOR = "FF000000"
        const val DEFAULT_LINE_WIDTH = "15"
    }


}