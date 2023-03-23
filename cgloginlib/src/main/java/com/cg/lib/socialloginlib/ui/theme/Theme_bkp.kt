package com.cg.lib.socialloginlib.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = themeDarkPrimary,
    primaryVariant = themeDarkPrimaryVariant,
    secondary = themeDarkPrimary,
//    secondaryVariant = themeDarkSecondaryVariant,
//    background = themeDarkBackground,
    surface = themeDarkSurface,
//    error = themeDarkError,
//    onPrimary = themeDarkOnPrimary,
//    onSecondary = themeDarkOnSecondary,
//    onBackground = themeDarkOnBackground,
//    onSurface = themeDarkOnSurface,
//    onError = themeDarkOnError
)

private val LightColorPalette = lightColors(
    primary = themeLightPrimary,
    primaryVariant = themeLightPrimaryVariant,
    secondary = themeLightPrimary,
//    secondaryVariant = themeLightSecondaryVariant,
//    background = themeLightBackground,
    surface = themeLightSurface,
//    error = themeLightError,
//    onPrimary = themeLightOnPrimary,
//    onSecondary = themeLightOnSecondary,
//    onBackground = themeLightOnBackground,
//    onSurface = themeLightOnSurface,
//    onError = themeLightOnError

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun CGLibTheme1(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}