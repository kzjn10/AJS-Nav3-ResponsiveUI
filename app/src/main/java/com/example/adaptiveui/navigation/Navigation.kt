package com.example.adaptiveui.navigation

import androidx.compose.runtime.snapshots.SnapshotStateList

class Navigation(
    private val backStack: SnapshotStateList<Dest>,
    private val startDestination: Dest,
) {
    val currentDestination: Dest
        get() = backStack.lastOrNull() ?: startDestination

    val canNavigateUp: Boolean
        get() = backStack.size > 1

    internal fun navigate(dest: Dest, launchSingleTop: Boolean = true) {
        if (launchSingleTop && backStack.lastOrNull() == dest) return
        backStack.add(dest)
    }

    fun navigateUp(): Boolean {
        if (canNavigateUp.not()) return false
        backStack.removeLastOrNull()
        return true
    }
}

fun Navigation.navigateToScreen(dest: Dest, launchSingleTop: Boolean = true) {
    navigate(dest = dest, launchSingleTop = launchSingleTop)
}
