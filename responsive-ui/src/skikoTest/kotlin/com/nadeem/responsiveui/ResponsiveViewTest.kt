package com.nadeem.responsiveui

import androidx.compose.material.Text
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class ResponsiveViewTest {

    private val forceDesktop = ScreenBreakpoints(mobile = 0, tablet = 0, desktop = 0, watch = 0)
    private val forceMobile = ScreenBreakpoints(
        mobile = Int.MAX_VALUE,
        tablet = Int.MAX_VALUE,
        desktop = Int.MAX_VALUE,
        watch = 0,
    )

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
            // Force Desktop bucket, leave desktop slot null.
            ResponsiveView(breakpoints = forceDesktop)
        }
        onNodeWithText("No view available for Desktop screen size").assertExists()
        onAllNodesWithText("provided").assertCountEquals(0)
    }

    @Test
    fun builderObjectDelegatesToScreenTypeLayout() = runComposeUiTest {
        setContent {
            ScreenTypeLayoutBuilder.builder(
                breakpoints = forceMobile,
                mobile = { Text("via-builder") },
            )
        }
        onNodeWithText("via-builder").assertExists()
    }
}
