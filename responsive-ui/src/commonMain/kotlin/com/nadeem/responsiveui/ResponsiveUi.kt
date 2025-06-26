package com.nadeem.responsiveui

import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

/**
 * Represents different screen types based on screen width
 */
public sealed class ScreenType {
    public object Mobile : ScreenType()
    public object Tablet : ScreenType()
    public object Desktop : ScreenType()
    public object Watch : ScreenType()
}

/**
 * Configuration for screen breakpoints in dp
 */
public data class ScreenBreakpoints(
    val mobile: Int = 600,
    val tablet: Int = 900,
    val desktop: Int = 1200,
    val watch: Int = 300
) {
    /**
     * Determines the screen type based on the given width
     */
    public fun getScreenType(width: Int): ScreenType {
        return when {
            width < watch -> ScreenType.Watch
            width < mobile -> ScreenType.Mobile
            width < tablet -> ScreenType.Tablet
            width < desktop -> ScreenType.Tablet
            else -> ScreenType.Desktop
        }
    }
}

/**
 * Device configuration utility for responsive layouts
 */
public object DeviceConfig {
    private var _screenWidth by mutableStateOf(0)
    public val screenWidth: Int get() = _screenWidth
    
    private var _screenHeight by mutableStateOf(0)
    public val screenHeight: Int get() = _screenHeight
    
    /**
     * Updates the screen dimensions
     */
    public fun updateScreenDimensions(width: Int, height: Int) {
        _screenWidth = width
        _screenHeight = height
    }
}

/**
 * Composable builder for responsive layouts
 */
@Composable
public fun ScreenTypeLayout(
    breakpoints: ScreenBreakpoints = ScreenBreakpoints(),
    mobile: @Composable () -> Unit = { DefaultFallbackWidget("Mobile") },
    tablet: @Composable () -> Unit = { DefaultFallbackWidget("Tablet") },
    desktop: @Composable () -> Unit = { DefaultFallbackWidget("Desktop") },
    watch: @Composable () -> Unit = { DefaultFallbackWidget("Watch") },
    modifier: Modifier = Modifier
) {
    val screenWidth = getScreenWidth()
    val screenHeight = getScreenHeight()
    val screenType = breakpoints.getScreenType(screenWidth)
    
    // Update device config
    LaunchedEffect(screenWidth, screenHeight) {
        DeviceConfig.updateScreenDimensions(screenWidth, screenHeight)
    }
    
    Box(modifier = modifier) {
        when (screenType) {
            is ScreenType.Watch -> watch()
            is ScreenType.Mobile -> mobile()
            is ScreenType.Tablet -> tablet()
            is ScreenType.Desktop -> desktop()
        }
    }
}

/**
 * Builder-style responsive layout similar to Flutter's responsive_builder
 */
public object ScreenTypeLayoutBuilder {
    @Composable
    public fun builder(
        breakpoints: ScreenBreakpoints = ScreenBreakpoints(),
        mobile: (@Composable () -> Unit)? = null,
        tablet: (@Composable () -> Unit)? = null,
        desktop: (@Composable () -> Unit)? = null,
        watch: (@Composable () -> Unit)? = null,
        modifier: Modifier = Modifier
    ) {
        ScreenTypeLayout(
            breakpoints = breakpoints,
            mobile = mobile ?: { DefaultFallbackWidget("Mobile") },
            tablet = tablet ?: { DefaultFallbackWidget("Tablet") },
            desktop = desktop ?: { DefaultFallbackWidget("Desktop") },
            watch = watch ?: { DefaultFallbackWidget("Watch") },
            modifier = modifier
        )
    }
}

/**
 * Responsive view composable that mimics Flutter's ResponsiveView
 */
@Composable
public fun ResponsiveView(
    mobile: (@Composable () -> Unit)? = null,
    tablet: (@Composable () -> Unit)? = null,
    desktop: (@Composable () -> Unit)? = null,
    watch: (@Composable () -> Unit)? = null,
    breakpoints: ScreenBreakpoints = ScreenBreakpoints(),
    modifier: Modifier = Modifier
) {
    ScreenTypeLayoutBuilder.builder(
        breakpoints = breakpoints,
        mobile = mobile,
        tablet = tablet,
        desktop = desktop,
        watch = watch,
        modifier = modifier
    )
}

/**
 * Default fallback widget when no specific layout is provided
 */
@Composable
private fun DefaultFallbackWidget(screenType: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "No view available for $screenType screen size",
            color = androidx.compose.ui.graphics.Color.Black
        )
    }
}

/**
 * Utility composable to get current screen type
 */
@Composable
public fun getScreenType(breakpoints: ScreenBreakpoints = ScreenBreakpoints()): ScreenType {
    val screenWidth = getScreenWidth()
    return breakpoints.getScreenType(screenWidth)
}

/**
 * Conditional composable rendering based on screen type
 */
@Composable
public fun ShowOnScreenType(
    screenTypes: List<ScreenType>,
    breakpoints: ScreenBreakpoints = ScreenBreakpoints(),
    content: @Composable () -> Unit
) {
    val currentScreenType = getScreenType(breakpoints)
    if (currentScreenType in screenTypes) {
        content()
    }
}

// Platform-specific functions to be implemented in each platform
@Composable
public expect fun getScreenWidth(): Int
@Composable
public expect fun getScreenHeight(): Int

// Legacy support - keeping the old DeviceType for backward compatibility
@Deprecated("Use ScreenType instead", ReplaceWith("ScreenType"))
@Suppress("DEPRECATION")
public sealed class DeviceType {
    public object Mobile : DeviceType()
    public object Tablet : DeviceType()
    public object Desktop : DeviceType()
    public object Unknown : DeviceType()
}

@Deprecated("Use getScreenType() instead")
@Suppress("DEPRECATION")
public expect fun getDeviceType(): DeviceType 