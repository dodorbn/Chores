package fr.eseo.ld.e5e.android.robinedo.chores.domain.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.eseo.ld.e5e.android.robinedo.chores.domain.model.Chore
import fr.eseo.ld.e5e.android.robinedo.chores.domain.model.ChoreStatus
import fr.eseo.ld.e5e.android.robinedo.chores.domain.model.TimeLog
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.util.UUID
import javax.inject.Inject
import fr.eseo.ld.e5e.android.robinedo.chores.roomdb.repositories.ChoreRoomRepository

@HiltViewModel
class ChoresViewModel @Inject constructor(
    private val roomRepository: ChoreRoomRepository
) : ViewModel() {
    val chores : StateFlow<List<Chore>> = roomRepository.allChores
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Lazily,
            initialValue = emptyList()
        )
    //Boolean is the timer running (or not)
    private val _isTimerRunning = MutableStateFlow(false)
    val isTimerRunning : StateFlow<Boolean> = _isTimerRunning.asStateFlow()
    // integer, number of seconds spent currently on the chore
    private val _currentSessionTime = MutableStateFlow(0)
    val currentSessionTime : StateFlow<Int> = _currentSessionTime.asStateFlow()
    // Job to run timer in the background
    private var timerJob : Job? = null
    // what is the current chore being timed?
    private var currentTimedChoreId: UUID? = null
    // What is the current timelog instance
    private var currentTimeLog: TimeLog? = null

    private val initialChore = Chore(
        id = UUID.fromString("00000000-0000-0000-0000-000000000000"),
        title = "A test",
        creator = "Bob"
    )

    fun addOrUpdateChore(chore : Chore) {
        viewModelScope.launch {
            roomRepository.insertOrUpdate(chore)
        }
    }

    fun deleteChore(chore: Chore) {
        viewModelScope.launch {
            roomRepository.deleteChore(chore)
        }
    }

    private fun startTimer(choreId : UUID?) {
        viewModelScope.launch {
            val chore = roomRepository.getChoreById(choreId !!) ?:
                return@launch
            _isTimerRunning.value = true
            currentTimedChoreId = chore.id
            _currentSessionTime.value = 0
            currentTimeLog = TimeLog(
                choreId = chore.id,
                startTime = LocalDateTime.now(),
                endTime = LocalDateTime.now(),
                userName = chore.creator
            )
            timerJob = viewModelScope.launch {
                while(isActive) {
                    delay(1000)
                    _currentSessionTime.update { it + 1 }
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
        val choreId = currentTimedChoreId ?: return
        val logToFinalize = currentTimeLog ?: return
        viewModelScope.launch {
            val chore = roomRepository.getChoreById(choreId)
            val completedLog = logToFinalize.copy(endTime = LocalDateTime.now())
            roomRepository.addTimeLogAndUpdateChore(
                choreId = choreId,
                status = ChoreStatus.ON_GOING,
                timeLog = completedLog
            )
        }
        resetTimerState()
    }

    private fun resetTimerState() {
        currentTimedChoreId = null
        currentTimeLog = null
        _currentSessionTime.value = 0
        _isTimerRunning.value = false
    }
    fun toggleTimer(choreId : UUID?) {
        val isSameChore = currentTimedChoreId == choreId
        if(_isTimerRunning.value) {
            stopTimer()
            if(!isSameChore) {
                startTimer(choreId)
            }
        }
        else {
            startTimer(choreId)
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("LIFECYCLE","onCleared ChoreViewModel")
        if(_isTimerRunning.value){
            stopTimer()
        }
    }
}
