package com.asaad27.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    primary = darkPurple_light_primary,
    onPrimary = white_light_onPrimary,
    primaryContainer = lightPurple_light_primaryContainer,
    onPrimaryContainer = deepPurple_light_onPrimaryContainer,
    secondary = grayishPurple_light_secondary,
    onSecondary = white_light_onSecondary,
    secondaryContainer = lightLavender_light_secondaryContainer,
    onSecondaryContainer = darkGrayishPurple_light_onSecondaryContainer,
    tertiary = mutedRed_light_tertiary,
    onTertiary = white_light_onTertiary,
    tertiaryContainer = pink_light_tertiaryContainer,
    onTertiaryContainer = darkRed_light_onTertiaryContainer,
    error = red_light_error,
    onError = white_light_onError,
    errorContainer = lightRed_light_errorContainer,
    onErrorContainer = darkRed_light_onErrorContainer,
    outline = gray_light_outline,
    background = offWhite_light_background,
    onBackground = darkGray_light_onBackground,
    surface = offWhite_light_surface,
    onSurface = darkGray_light_onSurface,
    surfaceVariant = lightGray_light_surfaceVariant,
    onSurfaceVariant = darkGray_light_onSurfaceVariant,
    inverseSurface = darkGray_light_inverseSurface,
    inverseOnSurface = lightGray_light_inverseOnSurface,
    inversePrimary = lightPurple_light_inversePrimary,
    surfaceTint = darkPurple_light_surfaceTint,
    outlineVariant = grayishLavender_light_outlineVariant,
    scrim = black_light_scrim,
)


private val DarkColors = darkColorScheme(
    primary = lightPurple_dark_primary,
    onPrimary = darkPurple_dark_onPrimary,
    primaryContainer = mediumPurple_dark_primaryContainer,
    onPrimaryContainer = lightPurple_dark_onPrimaryContainer,
    secondary = lightLavender_dark_secondary,
    onSecondary = darkGrayishPurple_dark_onSecondary,
    secondaryContainer = darkLavender_dark_secondaryContainer,
    onSecondaryContainer = lightLavender_dark_onSecondaryContainer,
    tertiary = lightPink_dark_tertiary,
    onTertiary = darkRed_dark_onTertiary,
    tertiaryContainer = darkPink_dark_tertiaryContainer,
    onTertiaryContainer = pink_dark_onTertiaryContainer,
    error = lightRed_dark_error,
    onError = darkRed_dark_onError,
    errorContainer = red_dark_errorContainer,
    onErrorContainer = lightRed_dark_onErrorContainer,
    outline = lightGray_dark_outline,
    background = darkGray_dark_background,
    onBackground = lightGray_dark_onBackground,
    surface = darkGray_dark_surface,
    onSurface = lightGray_dark_onSurface,
    surfaceVariant = darkGray_dark_surfaceVariant,
    onSurfaceVariant = lightGray_dark_onSurfaceVariant,
    inverseSurface = lightGray_dark_inverseSurface,
    inverseOnSurface = darkGray_dark_inverseOnSurface,
    inversePrimary = darkPurple_dark_inversePrimary,
    surfaceTint = lightPurple_dark_surfaceTint,
    outlineVariant = darkGray_dark_outlineVariant,
    scrim = black_dark_scrim,
)

@Composable
fun CommonTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (!useDarkTheme) {
        LightColors
    } else {
        DarkColors
    }

    MaterialTheme(
        colorScheme = colors,
        content = content
    )
}