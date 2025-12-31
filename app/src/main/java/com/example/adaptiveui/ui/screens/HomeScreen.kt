package com.example.adaptiveui.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyGridState
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.EntryProviderScope
import com.example.adaptiveui.model.HomeItem
import com.example.adaptiveui.navigation.Dest
import com.example.adaptiveui.navigation.Navigation
import com.example.adaptiveui.navigation.navigateToScreen
import com.example.adaptiveui.ui.LocalWindowWidthSizeClass

@Composable
fun EntryProviderScope<Dest>.HomeEntry(
    homeItems: List<HomeItem>,
    homeListState: LazyListState,
    homeGridState: LazyGridState,
    navigation: Navigation,
) {
    entry<Dest.Home> {
        HomeScreen(
            items = homeItems,
            listState = homeListState,
            gridState = homeGridState,
            onNavigateToDetail = { item -> navigation.navigateToScreen(Dest.HomeDetail(item.id)) },
        )
    }
}
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    items: List<HomeItem>,
    listState: LazyListState,
    gridState: LazyGridState,
    onNavigateToDetail: (HomeItem) -> Unit,
) {
    val windowWidthSizeClass = LocalWindowWidthSizeClass.current
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(text = "Home")

        when (windowWidthSizeClass) {
            WindowWidthSizeClass.Compact, WindowWidthSizeClass.Medium -> {
                LazyColumn(state = listState, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(items) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToDetail(item) }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = item.title)
                                Text(text = item.subtitle)
                            }
                        }
                    }
                }
            }

            WindowWidthSizeClass.Expanded -> {
                LazyVerticalGrid(
                    state = gridState,
                    columns = GridCells.Adaptive(minSize = 240.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    items(items) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToDetail(item) }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = item.title)
                                Text(text = "Grid layout on large screens")
                            }
                        }
                    }
                }
            }

            else -> {
                LazyColumn(state = listState, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(items) { item ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onNavigateToDetail(item) }
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(text = item.title)
                                Text(text = "Fallback layout")
                            }
                        }
                    }
                }
            }
        }
    }
}
