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
class TwoPaneLayoutTest {

    private val forceMobile = ScreenBreakpoints(watch = 0, mobile = Int.MAX_VALUE, tablet = Int.MAX_VALUE)
    private val forceTablet = ScreenBreakpoints(watch = 0, mobile = 0, tablet = Int.MAX_VALUE)
    private val forceDesktop = ScreenBreakpoints(watch = 0, mobile = 0, tablet = 0)

    @Test
    fun onMobile_showsPrimaryOnlyWhenShowSecondaryFalse() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(LocalScreenBreakpoints provides forceMobile) {
                TwoPaneLayout(
                    showSecondary = false,
                    primary = { Text("PRIMARY") },
                    secondary = { Text("SECONDARY") },
                )
            }
        }
        onNodeWithText("PRIMARY").assertExists()
        onAllNodesWithText("SECONDARY").assertCountEquals(0)
    }

    @Test
    fun onMobile_showsSecondaryOnlyWhenShowSecondaryTrue() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(LocalScreenBreakpoints provides forceMobile) {
                TwoPaneLayout(
                    showSecondary = true,
                    primary = { Text("PRIMARY") },
                    secondary = { Text("SECONDARY") },
                )
            }
        }
        onNodeWithText("SECONDARY").assertExists()
        onAllNodesWithText("PRIMARY").assertCountEquals(0)
    }

    @Test
    fun onTablet_showsBothPanesRegardlessOfShowSecondary() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(LocalScreenBreakpoints provides forceTablet) {
                TwoPaneLayout(
                    showSecondary = false,  // ignored at this breakpoint
                    primary = { Text("PRIMARY") },
                    secondary = { Text("SECONDARY") },
                )
            }
        }
        onNodeWithText("PRIMARY").assertExists()
        onNodeWithText("SECONDARY").assertExists()
    }

    @Test
    fun onDesktop_showsBothPanes() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(LocalScreenBreakpoints provides forceDesktop) {
                TwoPaneLayout(
                    showSecondary = false,
                    primary = { Text("PRIMARY") },
                    secondary = { Text("SECONDARY") },
                )
            }
        }
        onNodeWithText("PRIMARY").assertExists()
        onNodeWithText("SECONDARY").assertExists()
    }

    @Test
    fun customShowBothAtRaisesTheTwoPaneThreshold() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(LocalScreenBreakpoints provides forceTablet) {
                // Bump threshold to Desktop — Tablet should now show only one pane
                TwoPaneLayout(
                    showSecondary = true,
                    showBothAt = ScreenType.Desktop,
                    primary = { Text("PRIMARY") },
                    secondary = { Text("SECONDARY") },
                )
            }
        }
        onNodeWithText("SECONDARY").assertExists()
        onAllNodesWithText("PRIMARY").assertCountEquals(0)
    }
}
