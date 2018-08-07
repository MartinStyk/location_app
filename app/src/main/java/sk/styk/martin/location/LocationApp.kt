package sk.styk.martin.location

import android.app.Application
import sk.styk.martin.location.util.Preferences

/**
 * @author Martin Styk
 * @date 30.03.2018.
 */
class LocationApp: Application() {

    override fun onCreate() {
        super.onCreate()

        Preferences.setRequestingLocationUpdates(this, false)

        if(Preferences.isFirstRun(applicationContext)){
            Preferences.initialize(applicationContext)
        }
    }

}