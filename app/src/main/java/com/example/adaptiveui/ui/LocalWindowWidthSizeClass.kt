package com.example.adaptiveui.ui

import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.staticCompositionLocalOf

val LocalWindowWidthSizeClass = staticCompositionLocalOf<WindowWidthSizeClass> {
    error("LocalWindowWidthSizeClass not provided")
}
