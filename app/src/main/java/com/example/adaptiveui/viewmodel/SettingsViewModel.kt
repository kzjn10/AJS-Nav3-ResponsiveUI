package com.example.adaptiveui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SettingsViewModel : ViewModel() {
    data class UiState(
        val settings: List<String> = listOf(
            "Notifications",
            "Theme",
            "Privacy",
            "About",
        ),
        val selected: String = "Notifications",
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun onSelect(item: String) {
        _uiState.update { it.copy(selected = item) }
    }
}
