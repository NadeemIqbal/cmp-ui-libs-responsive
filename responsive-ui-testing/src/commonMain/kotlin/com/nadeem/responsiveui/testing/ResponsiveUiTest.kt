package com.nadeem.responsiveui.testing

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.ComposeUiTest
import androidx.compose.ui.test.ExperimentalTestApi
import com.nadeem.responsiveui.InternalResponsiveUiTestApi
import com.nadeem.responsiveui.LocalScreenWidthOverride

/**
 * Drop-in replacement for [ComposeUiTest.setContent] that forces
 * `rememberScreenWidth()` (and therefore every `responsive-ui` composable)
 * to behave as if the window is [widthDp] dp wide.
 *
 * On platforms where `LocalWindowInfo.containerSize.width` returns 0 in
 * headless test compositions (notably iOS simulator), this is the only
 * reliable way to write breakpoint-sensitive UI tests.
 *
 * ```
 * @OptIn(ExperimentalTestApi::class)
 * @Test
 * fun layoutShowsTabletPaneAt800Dp() = runComposeUiTest {
 *     setContentWithScreenWidth(widthDp = 800) {
 *         MyResponsiveScreen()
 *     }
 *     onNodeWithText("Tablet pane").assertExists()
 * }
 * ```
 */
@OptIn(ExperimentalTestApi::class, InternalResponsiveUiTestApi::class)
public fun ComposeUiTest.setContentWithScreenWidth(
    widthDp: Int,
    content: @Composable () -> Unit,
) {
    require(widthDp >= 0) { "widthDp must be non-negative; was $widthDp" }
    setContent {
        CompositionLocalProvider(LocalScreenWidthOverride provides widthDp) {
            content()
        }
    }
}
