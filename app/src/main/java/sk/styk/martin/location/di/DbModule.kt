package sk.styk.martin.location.di

import android.arch.persistence.room.Room
import android.content.Context
import dagger.Module
import dagger.Provides
import sk.styk.martin.location.db.LocationDataDao
import sk.styk.martin.location.db.LocationDataRepository
import sk.styk.martin.location.db.LocationDatabase
import javax.inject.Singleton

@Module
class DbModule {

    @Singleton
    @Provides
    fun provideDatabase(context: Context): LocationDatabase =
            Room.databaseBuilder(context.applicationContext,
                    LocationDatabase::class.java, "location.db")
                    .build()

    @Singleton
    @Provides
    fun provideLocationDataDao(database: LocationDatabase): LocationDataDao {
        return database.locationDataDao()
    }

}