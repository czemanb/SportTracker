package hu.bme.aut.android.mysporttrackerapp.database

import androidx.lifecycle.LiveData
import androidx.room.*
import hu.bme.aut.android.mysporttrackerapp.model.SportEntity

@Dao
interface SportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRun(run: SportEntity)

    @Delete
    suspend fun deleteRun(run: SportEntity)

    @Query("SELECT * FROM sport ORDER BY timestamp DESC")
    fun getSortDate(): LiveData<List<SportEntity>>

    @Query("SELECT * FROM sport ORDER BY distanceInMeters DESC")
    fun getSortDistance(): LiveData<List<SportEntity>>

    @Query("SELECT SUM(timeInMillis) FROM sport")
    fun getTimeSum(): LiveData<Long>

    @Query("SELECT SUM(caloriesBurned) FROM sport")
    fun getCalorieSum(): LiveData<Int>

    @Query("SELECT SUM(distanceInMeters) FROM sport")
    fun getDistanceSum(): LiveData<Int>

    @Query("SELECT AVG(avgSpeedInKMH) FROM sport")
    fun getSpeedAvg(): LiveData<Float>
}