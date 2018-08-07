package sk.styk.martin.location.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context

@Database(entities = [(LocationData::class)], version = 1)
abstract class LocationDatabase : RoomDatabase() {

    abstract fun locationDataDao(): LocationDataDao

    companion object {
        private var INSTANCE: LocationDatabase? = null

        fun getInstance(context: Context): LocationDatabase {
            if (INSTANCE == null) {
                synchronized(Database::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                            LocationDatabase::class.java, "location.db")
                            .build()
                }
            }
            return INSTANCE ?: throw IllegalStateException()
        }

    }
}