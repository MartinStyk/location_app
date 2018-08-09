package sk.styk.martin.location.service

import android.app.NotificationManager
import android.app.Service
import android.arch.persistence.room.Room
import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import sk.styk.martin.location.db.LocationDataDao
import sk.styk.martin.location.db.LocationDataRepository
import sk.styk.martin.location.db.LocationDatabase
import sk.styk.martin.location.di.scope.ServiceScope
import javax.inject.Singleton

@Module
class LocationServiceModule {

    @Provides
    @ServiceScope
    fun provideLocationClient(context: Context): FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

    @Provides
    @ServiceScope
    fun provideNotificationManager(context: Context): NotificationManager =
            context.getSystemService(Service.NOTIFICATION_SERVICE) as NotificationManager

}