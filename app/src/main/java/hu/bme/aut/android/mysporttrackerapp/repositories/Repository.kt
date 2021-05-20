package hu.bme.aut.android.mysporttrackerapp.repositories

import hu.bme.aut.android.mysporttrackerapp.database.SportDao
import hu.bme.aut.android.mysporttrackerapp.model.SportEntity
import javax.inject.Inject

class Repository @Inject constructor(
        private val runDao: SportDao
) {
    suspend fun insertRun(run: SportEntity) = runDao.insertRun(run)

    suspend fun deleteRun(run: SportEntity) = runDao.deleteRun(run)

    fun getAllRunsSortedByDate() = runDao.getSortDate()

    fun getAllRunsSortedByDistance() = runDao.getSortDistance()

    fun getTotalAvgSpeed() = runDao.getSpeedAvg()

    fun getTotalDistance() = runDao.getDistanceSum()

    fun getTotalCaloriesBurned() = runDao.getCalorieSum()

    fun getTotalTimeInMillis() = runDao.getTimeSum()
}