package com.nadeem.responsiveui.testing

import androidx.compose.material3.Text
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import com.nadeem.responsiveui.ResponsiveView
import com.nadeem.responsiveui.ScreenBreakpoints
import kotlin.test.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalTestApi::class)
class ResponsiveUiTestTest {

    private val realBreakpoints = ScreenBreakpoints(watch = 280, mobile = 600, tablet = 900)

    @Test
    fun forcesMobileAt400Dp() = runComposeUiTest {
        setContentWithScreenWidth(widthDp = 400) {
            ResponsiveView(
                breakpoints = realBreakpoints,
                mobile = { Text("M") },
                tablet = { Text("T") },
                desktop = { Text("D") },
            )
        }
        onNodeWithText("M").assertExists()
    }

    @Test
    fun forcesTabletAt800Dp() = runComposeUiTest {
        setContentWithScreenWidth(widthDp = 800) {
            ResponsiveView(
                breakpoints = realBreakpoints,
                mobile = { Text("M") },
                tablet = { Text("T") },
                desktop = { Text("D") },
            )
        }
        onNodeWithText("T").assertExists()
    }

    @Test
    fun forcesDesktopAt1400Dp() = runComposeUiTest {
        setContentWithScreenWidth(widthDp = 1400) {
            ResponsiveView(
                breakpoints = realBreakpoints,
                mobile = { Text("M") },
                tablet = { Text("T") },
                desktop = { Text("D") },
            )
        }
        onNodeWithText("D").assertExists()
    }

    @Test
    fun forcesWatchAt200Dp() = runComposeUiTest {
        setContentWithScreenWidth(widthDp = 200) {
            ResponsiveView(
                breakpoints = realBreakpoints,
                watch = { Text("W") },
                mobile = { Text("M") },
                tablet = { Text("T") },
                desktop = { Text("D") },
            )
        }
        onNodeWithText("W").assertExists()
    }

    @Test
    fun rejectsNegativeWidth() {
        assertFailsWith<IllegalArgumentException> {
            runComposeUiTest {
                setContentWithScreenWidth(widthDp = -1) { /* unused */ }
            }
        }
    }
}
