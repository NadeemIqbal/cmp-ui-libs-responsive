package com.nadeem.responsiveui

import androidx.compose.runtime.Composable

/**
 * Picks one of four values based on the current [ScreenType]. The most
 * common responsive pattern — works for any `T` (dp, pixel counts, strings,
 * column counts, lambdas, whole composables).
 *
 * ```
 * val padding = responsiveValue(
 *     mobile = 8.dp,
 *     tablet = 16.dp,
 *     desktop = 24.dp,
 * )
 *
 * val columnCount = responsiveValue(
 *     mobile = 1,
 *     tablet = 2,
 *     desktop = 4,
 * )
 * ```
 *
 * [watch] defaults to [mobile] since watch-sized layouts almost always reuse
 * the mobile design. Override it explicitly when watch UI diverges (e.g. a
 * single-column glanceable card).
 */
@Composable
public fun <T> responsiveValue(
    mobile: T,
    tablet: T,
    desktop: T,
    watch: T = mobile,
    breakpoints: ScreenBreakpoints = LocalScreenBreakpoints.current,
): T = when (rememberScreenType(breakpoints)) {
    ScreenType.Watch -> watch
    ScreenType.Mobile -> mobile
    ScreenType.Tablet -> tablet
    ScreenType.Desktop -> desktop
}
