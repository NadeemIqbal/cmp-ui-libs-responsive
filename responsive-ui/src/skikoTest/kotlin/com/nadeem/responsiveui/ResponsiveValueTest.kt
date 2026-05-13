package com.nadeem.responsiveui

import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ResponsiveValueTest {

    private val forceWatch = ScreenBreakpoints(
        watch = Int.MAX_VALUE,
        mobile = Int.MAX_VALUE,
        tablet = Int.MAX_VALUE,
    )
    private val forceMobile = ScreenBreakpoints(watch = 0, mobile = Int.MAX_VALUE, tablet = Int.MAX_VALUE)
    private val forceTablet = ScreenBreakpoints(watch = 0, mobile = 0, tablet = Int.MAX_VALUE)
    private val forceDesktop = ScreenBreakpoints(watch = 0, mobile = 0, tablet = 0)

    @Test
    fun returnsMobileValueOnMobile() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(LocalScreenBreakpoints provides forceMobile) {
                Text(responsiveValue(mobile = "M", tablet = "T", desktop = "D"))
            }
        }
        onNodeWithText("M").assertExists()
        onAllNodesWithText("T").assertCountEquals(0)
        onAllNodesWithText("D").assertCountEquals(0)
    }

    @Test
    fun returnsTabletValueOnTablet() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(LocalScreenBreakpoints provides forceTablet) {
                Text(responsiveValue(mobile = "M", tablet = "T", desktop = "D"))
            }
        }
        onNodeWithText("T").assertExists()
    }

    @Test
    fun returnsDesktopValueOnDesktop() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(LocalScreenBreakpoints provides forceDesktop) {
                Text(responsiveValue(mobile = "M", tablet = "T", desktop = "D"))
            }
        }
        onNodeWithText("D").assertExists()
    }

    @Test
    fun watchDefaultsToMobileWhenOmitted() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(LocalScreenBreakpoints provides forceWatch) {
                Text(responsiveValue(mobile = "M", tablet = "T", desktop = "D"))
            }
        }
        // No `watch` arg → fall back to `mobile` value
        onNodeWithText("M").assertExists()
    }

    @Test
    fun watchExplicitlyOverridesMobile() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(LocalScreenBreakpoints provides forceWatch) {
                Text(responsiveValue(mobile = "M", tablet = "T", desktop = "D", watch = "W"))
            }
        }
        onNodeWithText("W").assertExists()
    }

    @Test
    fun worksWithIntsAndArbitraryTypes() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(LocalScreenBreakpoints provides forceTablet) {
                val cols: Int = responsiveValue(mobile = 1, tablet = 2, desktop = 4)
                Text("cols=$cols")
            }
        }
        onNodeWithText("cols=2").assertExists()
    }
}
