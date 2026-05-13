package com.nadeem.responsiveui

import androidx.compose.material.Text
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

/**
 * Forcing breakpoints — make `ScreenTypeLayout` resolve to a known `ScreenType`
 * regardless of the test composition's reported width on each platform.
 *
 * iOS test composition reports width=0 (no real UIScreen attached), desktop
 * reports the window width, wasm reports the browser viewport. Using 0 as
 * the bottom threshold ensures width=0 falls *past* the Watch check rather
 * than into it.
 *
 * Trace through `ScreenBreakpoints.getScreenType(width)` for width >= 0:
 *   forceWatch    (MAX,MAX,MAX,MAX) → width < MAX → Watch
 *   forceMobile   (MAX,MAX,MAX,0)   → width<0 false, width<MAX → Mobile
 *   forceTablet   (0,MAX,MAX,0)     → width<0 ×2 false, width<MAX → Tablet
 *   forceDesktop  (0,0,0,0)         → all <0 false → else → Desktop
 */
@OptIn(ExperimentalTestApi::class)
class ScreenTypeLayoutTest {

    private val forceWatch = ScreenBreakpoints(
        mobile = Int.MAX_VALUE,
        tablet = Int.MAX_VALUE,
        desktop = Int.MAX_VALUE,
        watch = Int.MAX_VALUE,
    )
    private val forceMobile = ScreenBreakpoints(
        mobile = Int.MAX_VALUE,
        tablet = Int.MAX_VALUE,
        desktop = Int.MAX_VALUE,
        watch = 0,
    )
    private val forceTablet = ScreenBreakpoints(
        mobile = 0,
        tablet = Int.MAX_VALUE,
        desktop = Int.MAX_VALUE,
        watch = 0,
    )
    private val forceDesktop = ScreenBreakpoints(mobile = 0, tablet = 0, desktop = 0, watch = 0)

    @Test
    fun rendersDesktopSlotWhenForcedDesktop() = runComposeUiTest {
        setContent {
            ScreenTypeLayout(
                breakpoints = forceDesktop,
                mobile = { Text("M") },
                tablet = { Text("T") },
                desktop = { Text("D") },
                watch = { Text("W") },
            )
        }
        onNodeWithText("D").assertExists()
        onAllNodesWithText("M").assertCountEquals(0)
        onAllNodesWithText("T").assertCountEquals(0)
        onAllNodesWithText("W").assertCountEquals(0)
    }

    @Test
    fun rendersWatchSlotWhenForcedWatch() = runComposeUiTest {
        setContent {
            ScreenTypeLayout(
                breakpoints = forceWatch,
                mobile = { Text("M") },
                tablet = { Text("T") },
                desktop = { Text("D") },
                watch = { Text("W") },
            )
        }
        onNodeWithText("W").assertExists()
        onAllNodesWithText("M").assertCountEquals(0)
        onAllNodesWithText("T").assertCountEquals(0)
        onAllNodesWithText("D").assertCountEquals(0)
    }

    @Test
    fun rendersMobileSlotWhenForcedMobile() = runComposeUiTest {
        setContent {
            ScreenTypeLayout(
                breakpoints = forceMobile,
                mobile = { Text("M") },
                tablet = { Text("T") },
                desktop = { Text("D") },
                watch = { Text("W") },
            )
        }
        onNodeWithText("M").assertExists()
        onAllNodesWithText("T").assertCountEquals(0)
        onAllNodesWithText("D").assertCountEquals(0)
        onAllNodesWithText("W").assertCountEquals(0)
    }

    @Test
    fun rendersTabletSlotWhenForcedTablet() = runComposeUiTest {
        setContent {
            ScreenTypeLayout(
                breakpoints = forceTablet,
                mobile = { Text("M") },
                tablet = { Text("T") },
                desktop = { Text("D") },
                watch = { Text("W") },
            )
        }
        onNodeWithText("T").assertExists()
        onAllNodesWithText("M").assertCountEquals(0)
        onAllNodesWithText("D").assertCountEquals(0)
        onAllNodesWithText("W").assertCountEquals(0)
    }

    @Test
    fun defaultSlotsRenderFallbackWidget() = runComposeUiTest {
        setContent {
            // All slots default; whichever ScreenType the platform resolves to
            // should yield a "No view available for ..." message.
            ScreenTypeLayout(breakpoints = forceDesktop)
        }
        onNodeWithText("No view available for Desktop screen size").assertExists()
    }
}
