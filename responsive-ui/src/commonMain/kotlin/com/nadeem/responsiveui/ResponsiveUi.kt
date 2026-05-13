package com.nadeem.responsiveui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import com.nadeem.responsiveui.resources.Res
import com.nadeem.responsiveui.resources.responsive_ui_no_view_for_screen
import org.jetbrains.compose.resources.stringResource

/**
 * Screen-size buckets used to drive responsive layout decisions.
 *
 * The boundaries between buckets are controlled by [ScreenBreakpoints]; the
 * defaults align with common device sizes and broadly with Material 3's
 * [androidx.compose.material3.windowsizeclass.WindowWidthSizeClass].
 */
public sealed class ScreenType {
    public data object Watch : ScreenType()
    public data object Mobile : ScreenType()
    public data object Tablet : ScreenType()
    public data object Desktop : ScreenType()
}

/**
 * Configuration for screen breakpoints in dp.
 *
 * Each value is the **exclusive upper bound** of that screen type, so:
 * - `width < watch`              → [ScreenType.Watch]
 * - `watch <= width < mobile`    → [ScreenType.Mobile]
 * - `mobile <= width < tablet`   → [ScreenType.Tablet]
 * - `tablet <= width`            → [ScreenType.Desktop]
 *
 * Defaults match the Material 3 window-size-class boundaries closely
 * (600 dp = Compact/Medium boundary, 900 dp ≈ Medium/Expanded boundary).
 */
public data class ScreenBreakpoints(
    val watch: Int = 300,
    val mobile: Int = 600,
    val tablet: Int = 900,
) {
    /**
     * Resolves a screen width (in dp) to a [ScreenType].
     */
    public fun getScreenType(width: Int): ScreenType = when {
        width < watch -> ScreenType.Watch
        width < mobile -> ScreenType.Mobile
        width < tablet -> ScreenType.Tablet
        else -> ScreenType.Desktop
    }
}

/**
 * Marks an API that's part of the contract between `:responsive-ui` and
 * `:responsive-ui-testing`. Production consumer code should never need to
 * opt in.
 */
@RequiresOptIn(
    message = "This API is for the :responsive-ui-testing module only. Use the public testing helpers instead.",
    level = RequiresOptIn.Level.WARNING,
)
@Retention(AnnotationRetention.BINARY)
public annotation class InternalResponsiveUiTestApi

/**
 * Override used by `:responsive-ui-testing`'s `setContentWithScreenWidth(...)`
 * to inject a deterministic screen width into UI tests where
 * `LocalWindowInfo.containerSize.width` would otherwise report 0 in headless
 * test compositions.
 *
 * Production code should not touch this — use [rememberScreenWidth] directly.
 */
@InternalResponsiveUiTestApi
public val LocalScreenWidthOverride: androidx.compose.runtime.ProvidableCompositionLocal<Int?> =
    staticCompositionLocalOf { null }

/**
 * App-wide default [ScreenBreakpoints]. Install once at the app root:
 *
 * ```
 * CompositionLocalProvider(LocalScreenBreakpoints provides ScreenBreakpoints(...)) {
 *     App()
 * }
 * ```
 *
 * Every responsive composable in this library reads from this local by
 * default, so individual call sites no longer need to pass `breakpoints =`.
 */
public val LocalScreenBreakpoints: androidx.compose.runtime.ProvidableCompositionLocal<ScreenBreakpoints> =
    staticCompositionLocalOf { ScreenBreakpoints() }

/**
 * App-wide fallback content shown by [ScreenTypeLayout] and [ResponsiveView]
 * when the slot for the current screen type is null. Install at the app root
 * to brand the placeholder; defaults to a plain centred message.
 */
public val LocalResponsiveFallback: androidx.compose.runtime.ProvidableCompositionLocal<@Composable (ScreenType) -> Unit> =
    staticCompositionLocalOf { { type -> DefaultFallbackWidget(type) } }

/**
 * Current window width in dp, reactive to window resizes on every target.
 */
@OptIn(InternalResponsiveUiTestApi::class)
@Composable
public fun rememberScreenWidth(): Int {
    LocalScreenWidthOverride.current?.let { return it }
    val containerWidthPx = LocalWindowInfo.current.containerSize.width
    val density = LocalDensity.current
    return with(density) { containerWidthPx.toDp().value.toInt() }
}

/**
 * Current window height in dp, reactive to window resizes on every target.
 */
@Composable
public fun rememberScreenHeight(): Int {
    val containerHeightPx = LocalWindowInfo.current.containerSize.height
    val density = LocalDensity.current
    return with(density) { containerHeightPx.toDp().value.toInt() }
}

/**
 * Current [ScreenType], computed from [rememberScreenWidth] against the
 * provided [breakpoints] (defaulting to [LocalScreenBreakpoints]).
 */
@Composable
public fun rememberScreenType(
    breakpoints: ScreenBreakpoints = LocalScreenBreakpoints.current,
): ScreenType = breakpoints.getScreenType(rememberScreenWidth())

/**
 * Slot-based layout that renders one of [watch] / [mobile] / [tablet] /
 * [desktop] depending on the current screen size. Slots that are `null` fall
 * back to [LocalResponsiveFallback] (the library default just shows a text
 * placeholder).
 */
@Composable
public fun ResponsiveView(
    watch: (@Composable () -> Unit)? = null,
    mobile: (@Composable () -> Unit)? = null,
    tablet: (@Composable () -> Unit)? = null,
    desktop: (@Composable () -> Unit)? = null,
    breakpoints: ScreenBreakpoints = LocalScreenBreakpoints.current,
    modifier: Modifier = Modifier,
) {
    val type = rememberScreenType(breakpoints)
    val fallback = LocalResponsiveFallback.current
    Box(modifier = modifier) {
        when (type) {
            ScreenType.Watch -> watch?.invoke() ?: fallback(type)
            ScreenType.Mobile -> mobile?.invoke() ?: fallback(type)
            ScreenType.Tablet -> tablet?.invoke() ?: fallback(type)
            ScreenType.Desktop -> desktop?.invoke() ?: fallback(type)
        }
    }
}

/**
 * Slot-based layout where every slot is mandatory. Use when you want the
 * compiler to enforce that you've covered all four screen types; otherwise
 * use [ResponsiveView] which makes slots optional.
 */
@Composable
public fun ScreenTypeLayout(
    watch: @Composable () -> Unit,
    mobile: @Composable () -> Unit,
    tablet: @Composable () -> Unit,
    desktop: @Composable () -> Unit,
    breakpoints: ScreenBreakpoints = LocalScreenBreakpoints.current,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier) {
        when (rememberScreenType(breakpoints)) {
            ScreenType.Watch -> watch()
            ScreenType.Mobile -> mobile()
            ScreenType.Tablet -> tablet()
            ScreenType.Desktop -> desktop()
        }
    }
}

/**
 * Conditional rendering — [content] is composed only if the current
 * [ScreenType] is in [screenTypes].
 */
@Composable
public fun ShowOnScreenType(
    screenTypes: List<ScreenType>,
    breakpoints: ScreenBreakpoints = LocalScreenBreakpoints.current,
    content: @Composable () -> Unit,
) {
    if (rememberScreenType(breakpoints) in screenTypes) {
        content()
    }
}

@Composable
private fun DefaultFallbackWidget(type: ScreenType) {
    val typeName = type::class.simpleName ?: "Unknown"
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = stringResource(Res.string.responsive_ui_no_view_for_screen, typeName),
            color = LocalContentColor.current,
        )
    }
}
