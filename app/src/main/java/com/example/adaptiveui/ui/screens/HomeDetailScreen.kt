package com.example.adaptiveui.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.EntryProviderScope
import com.example.adaptiveui.model.HomeItem
import com.example.adaptiveui.navigation.Dest
import com.example.adaptiveui.navigation.Navigation
import com.example.adaptiveui.ui.LocalWindowWidthSizeClass

@Composable
fun EntryProviderScope<Dest>.HomeDetailEntry(
    homeItems: List<HomeItem>,
    navigation: Navigation,
) {
    entry<Dest.HomeDetail> { key ->
        val item = homeItems.firstOrNull { it.id == key.id }
        HomeDetailScreen(
            item = item,
            onNavigateUp = { navigation.navigateUp() },
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun HomeDetailScreen(
    modifier: Modifier = Modifier,
    item: HomeItem?,
    onNavigateUp: () -> Unit,
) {
    val windowWidthSizeClass = LocalWindowWidthSizeClass.current
    val horizontalPadding = when (windowWidthSizeClass) {
        WindowWidthSizeClass.Compact -> 16.dp
        WindowWidthSizeClass.Medium -> 24.dp
        WindowWidthSizeClass.Expanded -> 32.dp
        else -> 16.dp
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "Detail") },
                navigationIcon = {
                    IconButton(onClick = onNavigateUp) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                windowInsets = WindowInsets(0, 0, 0, 0),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = horizontalPadding, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = item?.title ?: "Item not found")
                    Text(text = item?.subtitle ?: "")
                    Text(text = "This is a detail page. Use the back button to return.")
                }
            }
        }
    }
}
