package sk.styk.martin.location

import android.app.Activity
import android.app.Application
import android.app.Service
import dagger.android.AndroidInjector
import dagger.android.DispatchingAndroidInjector
import dagger.android.HasActivityInjector
import dagger.android.HasServiceInjector
import dagger.android.support.DaggerApplication
import sk.styk.martin.location.di.component.DaggerAppComponent
import sk.styk.martin.location.util.Preferences
import javax.inject.Inject

/**
 * @author Martin Styk
 * @date 30.03.2018.
 */
class LocationApp: Application(), HasActivityInjector, HasServiceInjector {

    @Inject
    lateinit var activityInjector: DispatchingAndroidInjector<Activity>

    @Inject
    lateinit var serviceInjector: DispatchingAndroidInjector<Service>

    @Inject
    lateinit var preferences: Preferences

    override fun onCreate() {
        super.onCreate()

        DaggerAppComponent.builder()
                .app(this)
                .build().inject(this)

        preferences.setRequestingLocationUpdates(false)

        if(preferences.isFirstRun()){
            preferences.initialize()
        }
    }

    override fun activityInjector(): DispatchingAndroidInjector<Activity> = activityInjector

    override fun serviceInjector(): DispatchingAndroidInjector<Service> = serviceInjector


}