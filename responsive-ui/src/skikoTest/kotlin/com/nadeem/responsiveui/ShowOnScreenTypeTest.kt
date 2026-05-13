package com.nadeem.responsiveui

import androidx.compose.material3.Text
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ShowOnScreenTypeTest {

    private val forceDesktop = ScreenBreakpoints(watch = 0, mobile = 0, tablet = 0)
    private val forceMobile = ScreenBreakpoints(watch = 0, mobile = Int.MAX_VALUE, tablet = Int.MAX_VALUE)

    @Test
    fun showsContentWhenCurrentTypeMatches() = runComposeUiTest {
        setContent {
            ShowOnScreenType(
                screenTypes = listOf(ScreenType.Desktop),
                breakpoints = forceDesktop,
            ) {
                Text("desktop-only")
            }
        }
        onNodeWithText("desktop-only").assertExists()
    }

    @Test
    fun hidesContentWhenCurrentTypeMissing() = runComposeUiTest {
        setContent {
            ShowOnScreenType(
                screenTypes = listOf(ScreenType.Mobile, ScreenType.Tablet),
                breakpoints = forceDesktop,
            ) {
                Text("hidden")
            }
        }
        onAllNodesWithText("hidden").assertCountEquals(0)
    }

    @Test
    fun supportsMultipleTypes() = runComposeUiTest {
        setContent {
            ShowOnScreenType(
                screenTypes = listOf(ScreenType.Mobile, ScreenType.Desktop),
                breakpoints = forceMobile,
            ) {
                Text("visible-on-mobile-or-desktop")
            }
        }
        onNodeWithText("visible-on-mobile-or-desktop").assertExists()
    }
}
