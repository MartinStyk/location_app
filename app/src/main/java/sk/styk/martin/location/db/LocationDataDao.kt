package sk.styk.martin.location.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query

@Dao
interface LocationDataDao {

    @Query("SELECT * from locationData")
    fun getAll(): LiveData<List<LocationData>>

    @Insert(onConflict = REPLACE)
    fun insert(locationData: LocationData)

    @Query("DELETE from locationData")
    fun deleteAll()

    @Query("SELECT * FROM locationData WHERE id = :id ")
    fun get(id:Long): LiveData<LocationData>

    @Query("SELECT * FROM locationData ORDER BY speed ASC LIMIT 1")
    fun getSlowestSpeed(): LiveData<LocationData>

    @Query("SELECT * FROM locationData ORDER BY speed DESC LIMIT 1")
    fun getFastestSpeed(): LiveData<LocationData>
}