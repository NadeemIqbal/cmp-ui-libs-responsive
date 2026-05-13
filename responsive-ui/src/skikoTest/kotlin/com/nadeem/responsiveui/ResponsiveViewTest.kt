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
class ResponsiveViewTest {

    private val forceDesktop = ScreenBreakpoints(watch = 0, mobile = 0, tablet = 0)
    private val forceMobile = ScreenBreakpoints(watch = 0, mobile = Int.MAX_VALUE, tablet = Int.MAX_VALUE)

    @Test
    fun rendersSlotWhenProvided() = runComposeUiTest {
        setContent {
            ResponsiveView(
                breakpoints = forceDesktop,
                desktop = { Text("desktop-provided") },
            )
        }
        onNodeWithText("desktop-provided").assertExists()
    }

    @Test
    fun rendersFallbackWhenSlotNull() = runComposeUiTest {
        setContent {
            // Force Desktop bucket, leave desktop slot null — built-in fallback renders.
            ResponsiveView(breakpoints = forceDesktop)
        }
        onNodeWithText("No view available for Desktop screen size").assertExists()
        onAllNodesWithText("provided").assertCountEquals(0)
    }

    @Test
    fun customFallbackInstalledViaCompositionLocalReplacesDefault() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(
                LocalResponsiveFallback provides { type -> Text("custom-${type::class.simpleName}") },
            ) {
                ResponsiveView(breakpoints = forceMobile)
            }
        }
        onNodeWithText("custom-Mobile").assertExists()
        onAllNodesWithText("No view available for").assertCountEquals(0)
    }

    @Test
    fun breakpointsFromLocalAreUsedWhenNotPassedExplicitly() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(LocalScreenBreakpoints provides forceMobile) {
                ResponsiveView(mobile = { Text("from-local") })
            }
        }
        onNodeWithText("from-local").assertExists()
    }
}
