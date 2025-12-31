package com.example.adaptiveui.viewmodel

import androidx.lifecycle.ViewModel
import com.example.adaptiveui.model.HomeItem
import com.example.adaptiveui.navigation.Dest
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel : ViewModel() {
    data class UiState(
        val homeItems: List<HomeItem> = emptyList(),
        val homeBadgeCount: Int = 0,
        val selectedTabIndex: Int = 0,
        val currentDestination: Dest = Dest.Home,
    ) {
        val startDestination: Dest = when (selectedTabIndex) {
            0 -> Dest.Home
            1 -> Dest.Search
            2 -> Dest.Settings
            else -> Dest.Home
        }

        val showBottomBar: Boolean = currentDestination is Dest.Home ||
            currentDestination is Dest.Search ||
            currentDestination is Dest.Settings
    }

    private val _uiState = MutableStateFlow(
        UiState(
            homeItems = List(30) { index ->
                val id = index + 1
                HomeItem(
                    id = id,
                    title = "Feed item #$id",
                    subtitle = "A responsive card that works on phones and tablets",
                )
            },
            homeBadgeCount = 3,
            selectedTabIndex = 0,
            currentDestination = Dest.Home,
        )
    )
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun onTabClicked(index: Int) {
        _uiState.update { state ->
            state.copy(
                selectedTabIndex = index,
                currentDestination = when (index) {
                    0 -> Dest.Home
                    1 -> Dest.Search
                    2 -> Dest.Settings
                    else -> Dest.Home
                },
            )
        }
    }

    fun onCurrentDestinationChanged(dest: Dest) {
        _uiState.update { it.copy(currentDestination = dest) }
    }
}
