package com.toritark.app.presentation.main.app.theme

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
val LightPrimaryContainer = Color(0xffd4a577) // More orangey-brown
val LightOnPrimaryContainer = Color(0xFF4A2F1B)
val LightInversePrimary = Color(0xFFFFB97E)

val LightSecondary = Color(0xFFEEE6DB) // Very light, warm off-white/beige
val LightOnSecondary = Color(0xFF766343)
val LightSecondaryContainer = Color(0xFFFFD8B4) // Light, warm peach
val LightOnSecondaryContainer = Color(0xFF4A2F1B)

val LightTertiary = Color(0xFF8C5E3A) // Mid-dark, warm brown
val LightOnTertiary = Color.White
val LightTertiaryContainer = Color(0xFFFFE0C2) // Light, warm peach/beige
val LightOnTertiaryContainer = Color(0xFF3A2A1A)

val LightBackground = Color(0xFFFFFFFF)
val LightOnBackground = Color(0xFF3A2A1A)

val LightSurface = Color(0xFFF9F5F1) // Very light warm beige
val LightOnSurface = Color(0xFF3A2A1A)

val LightSurfaceVariant = Color(0xFFF3E3D3) // Light beige
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

// ========== Dark Theme Palette (REVISED) ==========

// Primary: Inspired by LightPrimaryContainer, but suitable for dark theme accents
val DarkPrimary = Color(0xFFE0C097) // Was FFB97E (too bright orange), now more golden beige
val DarkOnPrimary = Color(0xFF4A2F1B) // Kept: Good contrast
val DarkPrimaryContainer = Color(0xFF604630) // Was 744E2F, slightly warmer/more saturated
val DarkOnPrimaryContainer = Color(0xFFFFDEBC) // Was FFDBBF, ensuring good contrast

// Secondary: Inspired by LightSecondaryContainer, but for dark theme
val DarkSecondary = Color(0xFFDABCA0) // Was F2B07D (too bright peach), now softer beige
val DarkOnSecondary = Color(0xFF402D1A) // Was 432712, adjusted for new secondary
val DarkSecondaryContainer = Color(0xFF584330) // Was 6A3F24, adjusted
val DarkOnSecondaryContainer = Color(0xFFF7D8BB) // Was FFD8B4, adjusted

// Tertiary: Inspired by LightTertiaryContainer
val DarkTertiary = Color(0xFFC8BBA0) // Was D3A47C (a bit too pinkish), now more muted beige/khaki
val DarkOnTertiary = Color(0xFF372F1A) // Was 4A2F1B, adjusted
val DarkTertiaryContainer = Color(0xFF4F462F) // Was 5E4730, adjusted
val DarkOnTertiaryContainer = Color(0xFFE4D7BA) // Was FFE0C2, adjusted

// Background and Surfaces: Warmer, slightly lighter dark tones
val DarkBackground = Color(0xFF201A16) // Was 1F1B17 (very dark), now a bit softer dark brown
val DarkOnBackground = Color(0xFFEDE0D9) // Was EBDACF, slightly warmer off-white

val DarkSurface = Color(0xFF28211D) // Was 2A221E, slightly warmer, distinct from background
val DarkOnSurface = Color(0xFFF0E9E2) // Was F7E8DC, slightly warmer off-white

val DarkSurfaceVariant = Color(0xFF4F453C) // Was 4E3B2E, slightly warmer deep brown-gray
val DarkOnSurfaceVariant = Color(0xFFD3C4B8) // Was D6BAA7, adjusted

// Surface Containers: Progressively lighter, warm dark shades
val DarkSurfaceDim = Color(0xFF18120F)           // Darkest, for dimming, close to black but warm
val DarkSurfaceBright = Color(0xFF3C3632)         // For elements that need to stand out more
val DarkSurfaceContainerLowest = Color(0xFF1B1613) // Slightly above background
val DarkSurfaceContainerLow = Color(0xFF241F1B)    // Current DarkSurfaceContainerLowest
val DarkSurfaceContainer = Color(0xFF28211D)      // Current DarkSurface (or similar)
val DarkSurfaceContainerHigh = Color(0xFF332B26)   // A step lighter
val DarkSurfaceContainerHighest = Color(0xFF3E3630) // Lightest surface container

val DarkOutline = Color(0xFFA08D7E) // Was A6856E, slightly softer
val DarkOutlineVariant = Color(0xFF52443B) // Was 745C4A, for less prominent outlines

val DarkInversePrimary = Color(0xFF8C5E3A) // Using LightTertiary as a reference
val DarkInverseSurface = Color(0xFFEDE0D9) // Similar to DarkOnBackground
val DarkInverseOnSurface = Color(0xFF201A16) // Similar to DarkBackground

// Error colors are usually standard and the previous ones were fine
val DarkError = Color(0xFFFFB4AB)
val DarkOnError = Color(0xFF690005)
val DarkErrorContainer = Color(0xFF93000A)
val DarkOnErrorContainer = Color(0xFFFFDAD6)

val DarkScrim = Color(0xE6000000) // Kept: Standard scrim

// ========== Success Colors (REVISED) ==========

@Immutable
data class SuccessColors(
    val success: Color,
    val onSuccess: Color,
    val successContainer: Color,
    val onSuccessContainer: Color,
)

val LightSuccessColors = SuccessColors(
    success = Color(0xFF2E7D32), // Standard light success green
    onSuccess = Color.White,
    successContainer = Color(0xFFC8E6C9),
    onSuccessContainer = Color(0xFF1B5E20),
)

// Dark Success Colors: More vibrant and "successful" looking
val DarkSuccessColors = SuccessColors(
    success = Color(0xFF8BC34A),            // A clearer, more recognizable green
    onSuccess = Color(0xFF1B3E00),            // Dark green for good contrast on the success color
    successContainer = Color(0xFF385700),      // Darker, more muted green for containers
    onSuccessContainer = Color(0xFFB8F376),    // Light, vibrant green for text/icons on success container
)

private val LocalSuccessColors = staticCompositionLocalOf {
    SuccessColors( // Default to unspecified, will be overridden
        success = Color.Unspecified,
        onSuccess = Color.Unspecified,
        successContainer = Color.Unspecified,
        onSuccessContainer = Color.Unspecified,
    )
}


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

    surfaceTint = LightPrimary, // Use primary for surface tint

    inverseSurface = LightInverseSurface,
    inverseOnSurface = LightInverseOnSurface,

    error = LightError,
    onError = LightOnError,
    errorContainer = LightErrorContainer,
    onErrorContainer = LightOnErrorContainer,

    outline = LightOutline,
    outlineVariant = LightOutlineVariant,

    scrim = LightScrim,

    // M3 Surface Tones
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

    surfaceTint = DarkPrimary, // Use primary for surface tint

    inverseSurface = DarkInverseSurface,
    inverseOnSurface = DarkInverseOnSurface,

    error = DarkError,
    onError = DarkOnError,
    errorContainer = DarkErrorContainer,
    onErrorContainer = DarkOnErrorContainer,

    outline = DarkOutline,
    outlineVariant = DarkOutlineVariant,

    scrim = DarkScrim,

    // M3 Surface Tones
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

// Default to Light scheme values for the LocalExtendedColors provider
// This ensures that if AppTheme is somehow not used at the root,
// previews or other components still get some sensible defaults.
val LocalExtendedColors = staticCompositionLocalOf {
    ExtendedColors(LightColorScheme, LightSuccessColors)
}

@Composable
fun AppTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val successColors = if (darkTheme) DarkSuccessColors else LightSuccessColors

    // Provide both the Material color scheme and custom success colors
    CompositionLocalProvider(
        LocalExtendedColors provides ExtendedColors(colorScheme, successColors)
    ) {
        MaterialTheme(
            colorScheme = colorScheme, // This is the standard MaterialTheme colors
            // typography = Typography, // Add your typography if you have one
            // shapes = Shapes, // Add your shapes if you have them
            content = content,
        )
    }
}

object AppTheme {
    val colors: ExtendedColors
        @Composable
        get() = LocalExtendedColors.current
}
