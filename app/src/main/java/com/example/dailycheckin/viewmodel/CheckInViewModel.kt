package com.example.dailycheckin.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.dailycheckin.data.CheckInRecord
import com.example.dailycheckin.repository.CheckInRepository
import java.time.LocalDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CheckInUiState(
    val isLoading: Boolean = true,
    val todayRecord: CheckInRecord? = null,
    val currentStreak: Int = 0,
    val totalDays: Int = 0,
    val records: List<CheckInRecord> = emptyList(),
    val errorMessage: String? = null,
)

class CheckInViewModel(
    private val repository: CheckInRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(CheckInUiState())
    val uiState: StateFlow<CheckInUiState> = _uiState.asStateFlow()

    fun checkInIfNeeded() {
        viewModelScope.launch {
            runCatching { repository.checkInAndLoad() }
                .onSuccess(::showRecords)
                .onFailure {
                    _uiState.value = CheckInUiState(
                        isLoading = false,
                        errorMessage = "读取打卡记录失败，请重新打开 App",
                    )
                }
        }
    }

    private fun showRecords(records: List<CheckInRecord>) {
        val today = LocalDate.now()
        _uiState.value = CheckInUiState(
            isLoading = false,
            todayRecord = records.firstOrNull { it.date == today.toString() },
            currentStreak = calculateCurrentStreak(
                recordDates = records.map { it.date },
                today = today,
            ),
            totalDays = records.size,
            records = records,
        )
    }

    class Factory(
        private val repository: CheckInRepository,
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return CheckInViewModel(repository) as T
        }
    }
}
