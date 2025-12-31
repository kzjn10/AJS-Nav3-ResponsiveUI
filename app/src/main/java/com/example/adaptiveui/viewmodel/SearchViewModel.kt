package com.example.adaptiveui.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SearchViewModel : ViewModel() {
    data class UiState(
        val query: String = "",
        val allItems: List<String> = List(80) { index -> "Result ${index + 1}" },
    ) {
        val results: List<String> = run {
            val q = query.trim()
            if (q.isEmpty()) allItems else allItems.filter { it.contains(q, ignoreCase = true) }
        }
    }

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun onQueryChanged(value: String) {
        _uiState.update { it.copy(query = value) }
    }
}
