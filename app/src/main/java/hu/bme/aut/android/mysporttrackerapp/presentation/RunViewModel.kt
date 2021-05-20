package hu.bme.aut.android.mysporttrackerapp.presentation

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hu.bme.aut.android.mysporttrackerapp.model.SportEntity
import hu.bme.aut.android.mysporttrackerapp.repositories.Repository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RunViewModel @Inject constructor(
    private val repo: Repository
): ViewModel() {

    private val sortedDate = repo.getAllRunsSortedByDate()
    private val sortedDistance = repo.getAllRunsSortedByDistance()

    val runs = MediatorLiveData<List<SportEntity>>()
    var sortedRunType = SortedRunType.DATE

    init {
        runs.addSource(sortedDate) { result ->
            if(sortedRunType == SortedRunType.DATE) {
                result?.let { runs.value = it }
            }
        }

        runs.addSource(sortedDistance) { result ->
            if(sortedRunType == SortedRunType.DISTANCE) {
                result?.let { runs.value = it }
            }
        }
    }

    fun sortRuns(sortedRunType: SortedRunType) = when(sortedRunType) {
        SortedRunType.DATE -> sortedDate.value?.let { runs.value = it }
        SortedRunType.DISTANCE -> sortedDistance.value?.let { runs.value = it }
    }.also {
        this.sortedRunType = sortedRunType
    }

    fun insertRun(run: SportEntity) = viewModelScope.launch {
        repo.insertRun(run)
    }

    fun delete(run: SportEntity) = viewModelScope.launch {
        repo.deleteRun(run)
    }
}

enum class SortedRunType {
    DATE, DISTANCE
}
