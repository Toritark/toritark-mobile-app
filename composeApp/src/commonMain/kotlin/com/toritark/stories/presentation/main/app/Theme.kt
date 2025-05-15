package com.toritark.stories.presentation.main.app

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

// ========== Base Colors ==========

// ========== Light Theme Palette ==========

val LightPrimary = Color(0xff473522)
val LightOnPrimary = Color.White
val LightPrimaryContainer = Color(0xffd4a577)
val LightOnPrimaryContainer = Color(0xFF4A2F1B)
val LightInversePrimary = Color(0xFFFFB97E)

val LightSecondary = Color(0xFFEEE6DB)
val LightOnSecondary = Color(0xFF766343)
val LightSecondaryContainer = Color(0xFFFFD8B4)
val LightOnSecondaryContainer = Color(0xFF4A2F1B)

val LightTertiary = Color(0xFF8C5E3A)
val LightOnTertiary = Color.White
val LightTertiaryContainer = Color(0xFFFFE0C2)
val LightOnTertiaryContainer = Color(0xFF3A2A1A)

val LightBackground = Color(0xFFFFFFFF)
val LightOnBackground = Color(0xFF3A2A1A)

val LightSurface = Color(0xFFF9F5F1)
val LightOnSurface = Color(0xFF3A2A1A)

val LightSurfaceVariant = Color(0xFFF3E3D3)
val LightOnSurfaceVariant = Color(0xFF5D4433)

val LightSurfaceDim = Color(0xFFEBDACF)
val LightSurfaceBright = Color(0xFFFFF9F4)
val LightSurfaceContainerLowest = Color(0xFFFFF3EC)
val LightSurfaceContainerLow = Color(0xFFFCECD6)
val LightSurfaceContainer = Color(0xFFF3E3D3)
val LightSurfaceContainerHigh = Color(0xFFF0DFCC)
val LightSurfaceContainerHighest = Color(0xFFEDEBD9)

val LightOutline = Color(0xFF9D7E67)
val LightOutlineVariant = Color(0xFFD6BAA7)

val LightInverseSurface = Color(0xFF3A2A1A)
val LightInverseOnSurface = Color(0xFFFFF6EF)

val LightError = Color(0xFFBA1A1A)
val LightOnError = Color.White
val LightErrorContainer = Color(0xFFFFDAD6)
val LightOnErrorContainer = Color(0xFF410002)

val LightScrim = Color(0xE6000000)

// ========== Dark Theme Palette ==========

val DarkPrimary = Color(0xFFFFB97E)
val DarkOnPrimary = Color(0xFF4A2F1B)
val DarkPrimaryContainer = Color(0xFF744E2F)
val DarkOnPrimaryContainer = Color(0xFFFFDBBF)
val DarkInversePrimary = Color(0xFFB87D4B)

val DarkSecondary = Color(0xFFF2B07D)
val DarkOnSecondary = Color(0xFF432712)
val DarkSecondaryContainer = Color(0xFF6A3F24)
val DarkOnSecondaryContainer = Color(0xFFFFD8B4)

val DarkTertiary = Color(0xFFD3A47C)
val DarkOnTertiary = Color(0xFF4A2F1B)
val DarkTertiaryContainer = Color(0xFF5E4730)
val DarkOnTertiaryContainer = Color(0xFFFFE0C2)

val DarkBackground = Color(0xFF1F1B17)
val DarkOnBackground = Color(0xFFEBDACF)

val DarkSurface = Color(0xFF2A221E)
val DarkOnSurface = Color(0xFFF7E8DC)

val DarkSurfaceVariant = Color(0xFF4E3B2E)
val DarkOnSurfaceVariant = Color(0xFFD6BAA7)

val DarkSurfaceDim = Color(0xFF1F1B17)
val DarkSurfaceBright = Color(0xFFEBDACF)
val DarkSurfaceContainerLowest = Color(0xFF241F1B)
val DarkSurfaceContainerLow = Color(0xFF2A221E)
val DarkSurfaceContainer = Color(0xFF302823)
val DarkSurfaceContainerHigh = Color(0xFF3B322C)
val DarkSurfaceContainerHighest = Color(0xFF4E3B2E)

val DarkOutline = Color(0xFFA6856E)
val DarkOutlineVariant = Color(0xFF745C4A)

val DarkInverseSurface = Color(0xFFEBDACF)
val DarkInverseOnSurface = Color(0xFF2A221E)

val DarkError = Color(0xFFFFB4AB)
val DarkOnError = Color(0xFF690005)
val DarkErrorContainer = Color(0xFF93000A)
val DarkOnErrorContainer = Color(0xFFFFDAD6)

val DarkScrim = Color(0xE6000000)

// ========== Success Colors ==========

@Immutable
data class SuccessColors(
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,
)

val LightSuccessColors = SuccessColors(
    success = Color(0xFF2E7D32),
    onSuccess = Color(0xFFFFFFFF),
    successContainer = Color(0xFFC8E6C9),
    onSuccessContainer = Color(0xFF1B5E20),
)
private val LocalSuccessColors = staticCompositionLocalOf {
    SuccessColors(
        success = Color.Unspecified,
        onSuccess = Color.Unspecified,
        successContainer = Color.Unspecified,
        onSuccessContainer = Color.Unspecified,
    )
}

val DarkSuccessColors = SuccessColors(
    success = Color(0xA5D6A7),
    onSuccess = Color(0xFF000000),
    successContainer = Color(0xFF388E3C),
    onSuccessContainer = Color(0xFFDCEDC8),
)


val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    inversePrimary = LightInversePrimary,

    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,

    tertiary = LightTertiary,
    onTertiary = LightOnTertiary,
    tertiaryContainer = LightTertiaryContainer,
    onTertiaryContainer = LightOnTertiaryContainer,

    background = LightBackground,
    onBackground = LightOnBackground,

    surface = LightSurface,
    onSurface = LightOnSurface,

    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,

    surfaceTint = LightPrimary,

    inverseSurface = LightInverseSurface,
    inverseOnSurface = LightInverseOnSurface,

    error = LightError,
    onError = LightOnError,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer,

    outline = LightOutline,
    outlineVariant = LightOutlineVariant,

    scrim = LightScrim,

    surfaceBright = LightSurfaceBright,
    surfaceContainer = LightSurfaceContainer,
    surfaceContainerHigh = LightSurfaceContainerHigh,
    surfaceContainerHighest = LightSurfaceContainerHighest,
    surfaceContainerLow = LightSurfaceContainerLow,
    surfaceContainerLowest = LightSurfaceContainerLowest,
    surfaceDim = LightSurfaceDim,
)

val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    inversePrimary = DarkInversePrimary,

    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,

    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    tertiaryContainer = DarkTertiaryContainer,
    onTertiaryContainer = DarkOnTertiaryContainer,

    background = DarkBackground,
    onBackground = DarkOnBackground,

    surface = DarkSurface,
    onSurface = DarkOnSurface,

    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,

    surfaceTint = DarkPrimary,

    inverseSurface = DarkInverseSurface,
    inverseOnSurface = DarkInverseOnSurface,

    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,

    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,

    scrim = DarkScrim,

    surfaceBright = DarkSurfaceBright,
    surfaceContainer = DarkSurfaceContainer,
    surfaceContainerHigh = DarkSurfaceContainerHigh,
    surfaceContainerHighest = DarkSurfaceContainerHighest,
    surfaceContainerLow = DarkSurfaceContainerLow,
    surfaceContainerLowest = DarkSurfaceContainerLowest,
    surfaceDim = DarkSurfaceDim,
)

data class ExtendedColors(
    val material: ColorScheme,
    val success: SuccessColors,
)

val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(LightColorScheme, LightSuccessColors)
}

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colors = if (darkTheme) DarkColorScheme else LightColorScheme
    val success = if (darkTheme) DarkSuccessColors else LightSuccessColors

    CompositionLocalProvider(
        LocalExtendedColors provides ExtendedColors(colors, success)
    ) {
        MaterialTheme(
            colorScheme = colors,
            content = content
        )
    }
}