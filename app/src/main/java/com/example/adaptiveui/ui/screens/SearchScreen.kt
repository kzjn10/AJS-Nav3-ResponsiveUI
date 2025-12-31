package com.example.adaptiveui.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.EntryProviderScope
import com.example.adaptiveui.navigation.Dest
import com.example.adaptiveui.ui.LocalWindowWidthSizeClass
import com.example.adaptiveui.viewmodel.SearchViewModel

@Composable
fun EntryProviderScope<Dest>.SearchEntry(
    searchListState: LazyListState,
) {
    entry<Dest.Search> {
        SearchScreen(
            listState = searchListState,
        )
    }
}

@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    listState: LazyListState,
    viewModel: SearchViewModel = viewModel(),
) {
    val windowWidthSizeClass = LocalWindowWidthSizeClass.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val horizontalPadding = when (windowWidthSizeClass) {
        WindowWidthSizeClass.Compact -> 16.dp
        WindowWidthSizeClass.Medium -> 24.dp
        WindowWidthSizeClass.Expanded -> 32.dp
        else -> 16.dp
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = horizontalPadding, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Search")

        TextField(
            value = uiState.query,
            onValueChange = viewModel::onQueryChanged,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            placeholder = { Text(text = "Type to filter results") },
        )

        LazyColumn(state = listState, verticalArrangement = Arrangement.spacedBy(10.dp)) {
            items(uiState.results) { title ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = title)
                        Text(text = "Query: ${uiState.query.ifBlank { "(none)" }}")
                    }
                }
            }
        }
    }
}
