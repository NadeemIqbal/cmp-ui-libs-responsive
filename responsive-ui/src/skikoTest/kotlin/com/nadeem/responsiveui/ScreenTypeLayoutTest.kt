package com.nadeem.responsiveui

import androidx.compose.material3.Text
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

/**
 * Forcing breakpoints — make [ScreenTypeLayout] resolve to a known [ScreenType]
 * regardless of the test composition's reported width on each platform.
 *
 * Headless test compositions can report width=0 on some targets (notably iOS),
 * so we set each forcing breakpoint to either 0 or [Int.MAX_VALUE] depending
 * on which bucket we want width=0 to land in.
 *
 * Trace `ScreenBreakpoints.getScreenType(width)` for width >= 0:
 *   forceWatch    (MAX, MAX, MAX) → width < MAX → Watch
 *   forceMobile   (0, MAX, MAX)   → width<0 false, width<MAX → Mobile
 *   forceTablet   (0, 0, MAX)     → width<0 ×2 false, width<MAX → Tablet
 *   forceDesktop  (0, 0, 0)       → all `<0` false → else → Desktop
 */
@OptIn(ExperimentalTestApi::class)
class ScreenTypeLayoutTest {

    private val forceWatch = ScreenBreakpoints(
        watch = Int.MAX_VALUE,
        mobile = Int.MAX_VALUE,
        tablet = Int.MAX_VALUE,
    )
    private val forceMobile = ScreenBreakpoints(watch = 0, mobile = Int.MAX_VALUE, tablet = Int.MAX_VALUE)
    private val forceTablet = ScreenBreakpoints(watch = 0, mobile = 0, tablet = Int.MAX_VALUE)
    private val forceDesktop = ScreenBreakpoints(watch = 0, mobile = 0, tablet = 0)

    @Test
    fun rendersDesktopSlotWhenForcedDesktop() = runComposeUiTest {
        setContent {
            ScreenTypeLayout(
                breakpoints = forceDesktop,
                watch = { Text("W") },
                mobile = { Text("M") },
                tablet = { Text("T") },
                desktop = { Text("D") },
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
                watch = { Text("W") },
                mobile = { Text("M") },
                tablet = { Text("T") },
                desktop = { Text("D") },
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
                watch = { Text("W") },
                mobile = { Text("M") },
                tablet = { Text("T") },
                desktop = { Text("D") },
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
                watch = { Text("W") },
                mobile = { Text("M") },
                tablet = { Text("T") },
                desktop = { Text("D") },
            )
        }
        onNodeWithText("T").assertExists()
        onAllNodesWithText("M").assertCountEquals(0)
        onAllNodesWithText("D").assertCountEquals(0)
        onAllNodesWithText("W").assertCountEquals(0)
    }
}
