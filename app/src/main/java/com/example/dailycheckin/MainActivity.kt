package com.example.dailycheckin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.dailycheckin.data.AppDatabase
import com.example.dailycheckin.repository.CheckInRepository
import com.example.dailycheckin.ui.HistoryScreen
import com.example.dailycheckin.ui.HomeScreen
import com.example.dailycheckin.ui.theme.DailyCheckInTheme
import com.example.dailycheckin.viewmodel.CheckInViewModel

class MainActivity : ComponentActivity() {
    private val checkInViewModel: CheckInViewModel by viewModels {
        CheckInViewModel.Factory(
            CheckInRepository(
                AppDatabase.getInstance(applicationContext).checkInDao(),
            ),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DailyCheckInTheme {
                val state by checkInViewModel.uiState.collectAsState()
                var showHistory by remember { mutableStateOf(false) }

                if (showHistory) {
                    HistoryScreen(
                        records = state.records,
                        onBack = { showHistory = false },
                    )
                } else {
                    HomeScreen(
                        state = state,
                        onOpenHistory = { showHistory = true },
                    )
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkInViewModel.checkInIfNeeded()
    }
}
