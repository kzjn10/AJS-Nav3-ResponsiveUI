package com.example.adaptiveui.navigation

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.listSaver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.example.adaptiveui.model.HomeItem
import com.example.adaptiveui.ui.screens.HomeDetailEntry
import com.example.adaptiveui.ui.screens.HomeEntry
import com.example.adaptiveui.ui.screens.SearchEntry
import com.example.adaptiveui.ui.screens.SettingDetailEntry
import com.example.adaptiveui.ui.screens.SettingEntry

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    tabKey: Int,
    startDestination: Dest,
    homeItems: List<HomeItem>,
    homeListState: LazyListState,
    homeGridState: LazyGridState,
    searchListState: LazyListState,
    settingsListState: LazyListState,
    onCurrentDestinationChanged: (Dest) -> Unit,
) {
    val backStack = rememberSaveable(
        tabKey,
        saver = listSaver(
            save = { list -> list.map { it.encode() } },
            restore = { restored ->
                mutableStateListOf<Dest>().apply {
                    addAll(restored.map { Dest.decode(it) })
                }
            },
        ),
    ) {
        mutableStateListOf(startDestination)
    }

    val navigation = remember(backStack, startDestination) {
        Navigation(backStack = backStack, startDestination = startDestination)
    }

    val currentDestination by remember {
        derivedStateOf { backStack.lastOrNull() ?: startDestination }
    }

    LaunchedEffect(currentDestination) {
        onCurrentDestinationChanged(currentDestination)
    }

    NavDisplay(
        backStack = backStack,
        entryProvider = entryProvider {
            HomeEntry(
                homeItems = homeItems,
                homeListState = homeListState,
                homeGridState = homeGridState,
                navigation = navigation
            )

            HomeDetailEntry(
                homeItems = homeItems,
                navigation = navigation
            )

            SearchEntry(
                searchListState = searchListState
            )

            SettingEntry(
                settingsListState = settingsListState,
                navigation = navigation
            )

            SettingDetailEntry(
                navigation = navigation
            )
        },
        onBack = {
            navigation.navigateUp()
        },
        modifier = modifier,
    )
}