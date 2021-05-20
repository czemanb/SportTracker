package hu.bme.aut.android.mysporttrackerapp.presentation


import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.mysporttrackerapp.repositories.Repository

import javax.inject.Inject

@HiltViewModel
class StatisticsViewModel @Inject constructor(
    repo: Repository
): ViewModel() {

    val totalTimeRun = repo.getTotalTimeInMillis()
    val totalDistance = repo.getTotalDistance()
    val totalCaloriesBurned = repo.getTotalCaloriesBurned()
    val totalAvgSpeed = repo.getTotalAvgSpeed()
    val runsSortedByDate = repo.getAllRunsSortedByDate()
}