package com.example.adaptiveui.ui.screens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.example.adaptiveui.ui.theme.AdaptiveUiTheme
import com.example.adaptiveui.navigation.AppNavHost
import com.example.adaptiveui.ui.LocalWindowWidthSizeClass
import com.example.adaptiveui.viewmodel.MainViewModel
import kotlinx.coroutines.launch

private sealed interface TopLevelDestination {
    data object Home : TopLevelDestination
    data object Search : TopLevelDestination
    data object Settings : TopLevelDestination
}

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Composable
@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
fun MainScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel = viewModel(),
) {
    val activity = LocalContext.current.findActivity() ?: return
    val windowSizeClass = calculateWindowSizeClass(activity)
    val coroutineScope = rememberCoroutineScope()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val tabs = listOf(
        TopLevelDestination.Home,
        TopLevelDestination.Search,
        TopLevelDestination.Settings,
    )
    val selectedTab = tabs.getOrElse(uiState.selectedTabIndex) { TopLevelDestination.Home }

    val homeListState = rememberLazyListState()
    val homeGridState = rememberLazyGridState()
    val searchListState = rememberLazyListState()
    val settingsListState = rememberLazyListState()

    CompositionLocalProvider(LocalWindowWidthSizeClass provides windowSizeClass.widthSizeClass) {
        Scaffold(
            modifier = modifier.fillMaxSize(),
            bottomBar = {
                AnimatedVisibility(
                    visible = uiState.showBottomBar,
                    enter = slideInVertically(initialOffsetY = { it }) + fadeIn() + expandVertically(),
                    exit = slideOutVertically(targetOffsetY = { it }) + fadeOut() + shrinkVertically()
                ) {
                    NavigationBar {
                        tabs.forEach { tab ->
                            val tabIndex = tabs.indexOf(tab)
                            NavigationBarItem(
                                selected = selectedTab == tab,
                                onClick = {
                                    if (uiState.selectedTabIndex == tabIndex) {
                                        when (tab) {
                                            TopLevelDestination.Home -> {
                                                coroutineScope.launch {
                                                    if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) {
                                                        homeGridState.animateScrollToItem(0)
                                                    } else {
                                                        homeListState.animateScrollToItem(0)
                                                    }
                                                }
                                            }

                                            TopLevelDestination.Search -> {
                                                coroutineScope.launch {
                                                    searchListState.animateScrollToItem(0)
                                                }
                                            }

                                            TopLevelDestination.Settings -> {
                                                coroutineScope.launch {
                                                    settingsListState.animateScrollToItem(0)
                                                }
                                            }
                                        }
                                    } else {
                                        viewModel.onTabClicked(tabIndex)
                                    }
                                },
                                label = {
                                    Text(
                                        text = when (tab) {
                                            TopLevelDestination.Home -> "Home"
                                            TopLevelDestination.Search -> "Search"
                                            TopLevelDestination.Settings -> "Settings"
                                        }
                                    )
                                },
                                icon = {
                                    when (tab) {
                                        TopLevelDestination.Home -> {
                                            BadgedBox(
                                                badge = {
                                                    if (uiState.homeBadgeCount > 0) {
                                                        Badge(
                                                            containerColor = Color.Red,
                                                            contentColor = Color.White,
                                                        ) {
                                                            Text(text = uiState.homeBadgeCount.toString())
                                                        }
                                                    }
                                                }
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Filled.Home,
                                                    contentDescription = "Home"
                                                )
                                            }
                                        }

                                        TopLevelDestination.Search -> Icon(
                                            imageVector = Icons.Filled.Search,
                                            contentDescription = "Search"
                                        )

                                        TopLevelDestination.Settings -> Icon(
                                            imageVector = Icons.Filled.Settings,
                                            contentDescription = "Settings"
                                        )
                                    }
                                }
                            )
                        }
                    }
                }
            },
        ) { innerPadding ->
            AppNavHost(
                tabKey = uiState.selectedTabIndex,
                startDestination = uiState.startDestination,
                homeItems = uiState.homeItems,
                homeListState = homeListState,
                homeGridState = homeGridState,
                searchListState = searchListState,
                settingsListState = settingsListState,
                onCurrentDestinationChanged = { dest -> viewModel.onCurrentDestinationChanged(dest) },
                modifier = Modifier.padding(innerPadding),
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AdaptiveUiTheme {
        MainScreen()
    }
}
