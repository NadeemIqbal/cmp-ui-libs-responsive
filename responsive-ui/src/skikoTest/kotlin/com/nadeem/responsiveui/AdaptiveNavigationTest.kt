package com.nadeem.responsiveui

import androidx.compose.material3.Text
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.runComposeUiTest
import kotlin.test.Test
import kotlin.test.assertFailsWith

@OptIn(ExperimentalTestApi::class)
class AdaptiveNavigationTest {

    // Tiny icon placeholders — just Text so tests don't need a Material Icons dep.
    private val sampleItems = listOf(
        AdaptiveNavigationItem(label = "Home", icon = { Text("HOME-ICON") }),
        AdaptiveNavigationItem(label = "Settings", icon = { Text("SETTINGS-ICON") }),
    )

    private val forceMobile = ScreenBreakpoints(watch = 0, mobile = Int.MAX_VALUE, tablet = Int.MAX_VALUE)
    private val forceTablet = ScreenBreakpoints(watch = 0, mobile = 0, tablet = Int.MAX_VALUE)
    private val forceDesktop = ScreenBreakpoints(watch = 0, mobile = 0, tablet = 0)

    @Test
    fun rendersContentAndAllItemLabelsOnMobile() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(LocalScreenBreakpoints provides forceMobile) {
                AdaptiveNavigation(
                    items = sampleItems,
                    selectedIndex = 0,
                    onItemSelected = {},
                ) {
                    Text("BODY")
                }
            }
        }
        onNodeWithText("BODY").assertExists()
        onNodeWithText("Home").assertExists()
        onNodeWithText("Settings").assertExists()
    }

    @Test
    fun rendersContentAndItemsOnTablet() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(LocalScreenBreakpoints provides forceTablet) {
                AdaptiveNavigation(
                    items = sampleItems,
                    selectedIndex = 0,
                    onItemSelected = {},
                ) {
                    Text("BODY")
                }
            }
        }
        onNodeWithText("BODY").assertExists()
        onNodeWithText("Home").assertExists()
        onNodeWithText("Settings").assertExists()
    }

    @Test
    fun rendersContentAndItemsOnDesktop() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(LocalScreenBreakpoints provides forceDesktop) {
                AdaptiveNavigation(
                    items = sampleItems,
                    selectedIndex = 1,
                    onItemSelected = {},
                ) {
                    Text("BODY")
                }
            }
        }
        onNodeWithText("BODY").assertExists()
        onNodeWithText("Home").assertExists()
        onNodeWithText("Settings").assertExists()
    }

    @Test
    fun tappingAnItemCallsOnItemSelectedWithThatIndex() = runComposeUiTest {
        setContent {
            CompositionLocalProvider(LocalScreenBreakpoints provides forceMobile) {
                var selected by remember { mutableIntStateOf(0) }
                AdaptiveNavigation(
                    items = sampleItems,
                    selectedIndex = selected,
                    onItemSelected = { selected = it },
                ) {
                    Text("selected=$selected")
                }
            }
        }
        onNodeWithText("selected=0").assertExists()
        // Tap by item label (NavigationBarItem is semantically labelled by its label slot)
        onNodeWithText("Settings").performClick()
        waitForIdle()
        onNodeWithText("selected=1").assertExists()
    }

    @Test
    fun rejectsEmptyItemsList() {
        assertFailsWith<IllegalArgumentException> {
            runComposeUiTest {
                setContent {
                    AdaptiveNavigation(
                        items = emptyList(),
                        selectedIndex = 0,
                        onItemSelected = {},
                    ) {}
                }
            }
        }
    }
}
