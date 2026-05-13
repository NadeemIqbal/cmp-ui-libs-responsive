package com.nadeem.responsiveui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * Side-by-side layout for list/detail and master/detail patterns.
 *
 * On screens at or above [showBothAt], both panes render side-by-side with
 * [primary] taking [splitFraction] of the width. On smaller screens only one
 * pane renders at a time — [primary] by default, or [secondary] when
 * [showSecondary] is `true`. Consumers manage `showSecondary` through their
 * own navigation state (e.g. selecting an item in the list).
 *
 * ```
 * var selectedId by remember { mutableStateOf<String?>(null) }
 * TwoPaneLayout(
 *     showSecondary = selectedId != null,
 *     primary = { MyList(onSelect = { selectedId = it }) },
 *     secondary = { MyDetail(selectedId) },
 * )
 * ```
 *
 * @param showSecondary When `true` and the screen is below [showBothAt],
 *   render [secondary] alone instead of [primary]. Ignored on wider screens.
 * @param primary Master / list pane.
 * @param secondary Detail pane.
 * @param showBothAt Smallest [ScreenType] at which both panes are visible
 *   side-by-side. Defaults to [ScreenType.Tablet].
 * @param splitFraction Width fraction allocated to [primary] when both panes
 *   are visible. Must be in `(0f, 1f)`. Defaults to `0.4f` (typical
 *   list/detail proportion).
 * @param breakpoints Defaults to [LocalScreenBreakpoints].
 */
@Composable
public fun TwoPaneLayout(
    showSecondary: Boolean,
    primary: @Composable () -> Unit,
    secondary: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    showBothAt: ScreenType = ScreenType.Tablet,
    splitFraction: Float = 0.4f,
    breakpoints: ScreenBreakpoints = LocalScreenBreakpoints.current,
) {
    require(splitFraction in 0.05f..0.95f) {
        "splitFraction must be in (0.05, 0.95); was $splitFraction"
    }

    val currentType = rememberScreenType(breakpoints)
    val bothVisible = currentType.atLeast(showBothAt)

    if (bothVisible) {
        Row(modifier = modifier.fillMaxSize()) {
            Box(Modifier.fillMaxHeight().weight(splitFraction)) { primary() }
            Box(Modifier.fillMaxHeight().weight(1f - splitFraction)) { secondary() }
        }
    } else {
        Box(modifier = modifier.fillMaxSize()) {
            if (showSecondary) secondary() else primary()
        }
    }
}

/**
 * Total ordering on [ScreenType] used by [TwoPaneLayout] to compare against
 * [TwoPaneLayout.showBothAt]. `Watch < Mobile < Tablet < Desktop`.
 */
internal fun ScreenType.atLeast(other: ScreenType): Boolean = ordinalValue() >= other.ordinalValue()

private fun ScreenType.ordinalValue(): Int = when (this) {
    ScreenType.Watch -> 0
    ScreenType.Mobile -> 1
    ScreenType.Tablet -> 2
    ScreenType.Desktop -> 3
}
