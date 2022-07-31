package com.himanshoe.kalendar.common.theme


import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.ui.graphics.Color

data class KalendarColor(
    val selectedColor: Color,
    val eventTextColor: Color,
    val todayColor: Color,
    val background: Color,
    val transparent: Color,
    val white: Color,
    val black: Color,
)

internal fun colorPalette() = KalendarColor(
    eventTextColor = Color(56, 152, 242),
    selectedColor = Color(56, 109, 242),
    todayColor = Color(56, 174, 242),
    background = Color.White,
    transparent = Color.Transparent,
    white = Color.White,
    black = Color.Black
)

/**
 * A Material [Colors] implementation which sets all colors to [debugColor] to discourage usage of
 * [MaterialTheme.colors] in preference to [KalendarTheme.colors].
 */
fun debugColors(
    darkTheme: Boolean = false,
    debugColor: Color = Color.Magenta,
) = Colors(
    primary = debugColor,
    primaryVariant = debugColor,
    secondary = debugColor,
    secondaryVariant = debugColor,
    background = debugColor,
    surface = debugColor,
    error = debugColor,
    onPrimary = debugColor,
    onSecondary = debugColor,
    onBackground = debugColor,
    onSurface = debugColor,
    onError = debugColor,
    isLight = !darkTheme
)
