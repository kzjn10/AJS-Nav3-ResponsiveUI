package com.example.adaptiveui.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.EntryProviderScope
import com.example.adaptiveui.navigation.Dest
import com.example.adaptiveui.navigation.Navigation
import com.example.adaptiveui.navigation.navigateToScreen
import com.example.adaptiveui.ui.LocalWindowWidthSizeClass
import com.example.adaptiveui.viewmodel.SettingsViewModel

@Composable
fun EntryProviderScope<Dest>.SettingEntry(
    settingsListState: LazyListState,
    navigation: Navigation,
) {
    entry<Dest.Settings> {
        SettingsScreen(
            listState = settingsListState,
            onNavigateToDetail = { title ->
                navigation.navigateToScreen(
                    Dest.SettingsDetail(
                        title = title,
                        desc = ""
                    )
                )
            },
        )
    }
}

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    onNavigateToDetail: (String) -> Unit,
    viewModel: SettingsViewModel = viewModel(),
) {
    val windowWidthSizeClass = LocalWindowWidthSizeClass.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (windowWidthSizeClass) {
        WindowWidthSizeClass.Expanded -> {
            Row(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxSize()
                ) {
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.settings) { item ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { viewModel.onSelect(item) }
                            ) {
                                Column(
                                    modifier = Modifier.padding(12.dp),
                                    verticalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Text(text = item)
                                    Text(text = if (uiState.selected == item) "Selected" else "Tap to view")
                                }
                            }
                        }
                    }
                }

                Card(
                    modifier = Modifier
                        .weight(2f)
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(text = uiState.selected)
                        Text(text = "Details pane shown side-by-side on large screens")
                    }
                }
            }
        }

        else -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(text = "Settings")
                LazyColumn(state = listState, verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(uiState.settings) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.onSelect(item)
                                    onNavigateToDetail(item)
                                }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = item)
                                Text(text = if (uiState.selected == item) "Selected" else "Tap to view")
                            }
                        }
                    }
                }
            }
        }
    }
}
