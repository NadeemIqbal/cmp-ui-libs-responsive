package com.nadeem.responsiveui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

/**
 * A single destination in [AdaptiveNavigation].
 *
 * Icons are `@Composable () -> Unit` slots so consumers can render any icon
 * source they prefer (Material Icons, custom drawables, Compose Resources)
 * without this library taking a dependency on a specific icon set.
 *
 * @property label Shown next to the icon in rail/drawer mode; below the icon
 *   in bar mode.
 * @property icon Rendered when this item is *not* the selected destination.
 * @property selectedIcon Rendered when this item *is* the selected
 *   destination. Defaults to [icon].
 */
public class AdaptiveNavigationItem(
    public val label: String,
    public val icon: @Composable () -> Unit,
    public val selectedIcon: @Composable () -> Unit = icon,
)

/**
 * Adaptive navigation chrome that swaps shape based on screen size:
 *
 * - **Watch / Mobile**: bottom [NavigationBar] above [content].
 * - **Tablet**: left-side [NavigationRail] beside [content].
 * - **Desktop**: persistent left drawer (a labelled vertical rail) beside
 *   [content].
 *
 * The consumer owns selection state via [selectedIndex] / [onItemSelected].
 *
 * @param items Destinations to render. Must be non-empty.
 * @param selectedIndex Currently selected index into [items].
 * @param onItemSelected Called when the user taps a destination.
 * @param breakpoints Controls the bar→rail→drawer shape boundaries.
 *   Defaults to [LocalScreenBreakpoints].
 * @param content The main content area placed alongside the navigation.
 */
@Composable
public fun AdaptiveNavigation(
    items: List<AdaptiveNavigationItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    modifier: Modifier = Modifier,
    breakpoints: ScreenBreakpoints = LocalScreenBreakpoints.current,
    content: @Composable () -> Unit,
) {
    require(items.isNotEmpty()) { "AdaptiveNavigation requires at least one item" }
    val type = rememberScreenType(breakpoints)
    Box(modifier = modifier) {
        when (type) {
            ScreenType.Watch, ScreenType.Mobile -> BottomBarShell(items, selectedIndex, onItemSelected, content)
            ScreenType.Tablet -> RailShell(items, selectedIndex, onItemSelected, content)
            ScreenType.Desktop -> DrawerShell(items, selectedIndex, onItemSelected, content)
        }
    }
}

@Composable
private fun BottomBarShell(
    items: List<AdaptiveNavigationItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    content: @Composable () -> Unit,
) {
    Scaffold(
        bottomBar = {
            NavigationBar {
                items.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = index == selectedIndex,
                        onClick = { onItemSelected(index) },
                        icon = {
                            if (index == selectedIndex) item.selectedIcon() else item.icon()
                        },
                        label = { Text(item.label) },
                    )
                }
            }
        },
    ) { innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) { content() }
    }
}

@Composable
private fun RailShell(
    items: List<AdaptiveNavigationItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    content: @Composable () -> Unit,
) {
    Row(Modifier.fillMaxSize()) {
        NavigationRail {
            items.forEachIndexed { index, item ->
                NavigationRailItem(
                    selected = index == selectedIndex,
                    onClick = { onItemSelected(index) },
                    icon = {
                        if (index == selectedIndex) item.selectedIcon() else item.icon()
                    },
                    label = { Text(item.label) },
                )
            }
        }
        Box(Modifier.fillMaxSize()) { content() }
    }
}

@Composable
private fun DrawerShell(
    items: List<AdaptiveNavigationItem>,
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
    content: @Composable () -> Unit,
) {
    Row(Modifier.fillMaxSize()) {
        // A simple labelled drawer column — wider than the rail, items have
        // icon + label on the same row. Avoids using PermanentNavigationDrawer
        // (which requires a ModalDrawerSheet wrapper and pulls in a lot of
        // surface theming).
        Column(
            modifier = Modifier
                .width(256.dp)
                .fillMaxHeight()
                .padding(vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            items.forEachIndexed { index, item ->
                NavigationRailItem(
                    selected = index == selectedIndex,
                    onClick = { onItemSelected(index) },
                    icon = {
                        if (index == selectedIndex) item.selectedIcon() else item.icon()
                    },
                    label = { Text(item.label, modifier = Modifier.padding(start = 12.dp)) },
                    alwaysShowLabel = true,
                )
            }
        }
        Box(Modifier.fillMaxSize()) { content() }
    }
}
