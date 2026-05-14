package com.nadeem.responsiveui.example

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nadeem.responsiveui.AdaptiveNavigation
import com.nadeem.responsiveui.AdaptiveNavigationItem
import com.nadeem.responsiveui.LocalResponsiveFallback
import com.nadeem.responsiveui.LocalScreenBreakpoints
import com.nadeem.responsiveui.ResponsiveView
import com.nadeem.responsiveui.ScreenBreakpoints
import com.nadeem.responsiveui.ScreenType
import com.nadeem.responsiveui.ScreenTypeLayout
import com.nadeem.responsiveui.ShowOnScreenType
import com.nadeem.responsiveui.TwoPaneLayout
import com.nadeem.responsiveui.rememberScreenHeight
import com.nadeem.responsiveui.rememberScreenType
import com.nadeem.responsiveui.rememberScreenWidth
import com.nadeem.responsiveui.responsiveValue

@Composable
public fun ResponsiveUIExampleApp() {
    MaterialTheme {
        // 1) LocalScreenBreakpoints — app-wide default. Every downstream responsive
        //    composable reads from here unless given an explicit `breakpoints =`.
        CompositionLocalProvider(
            LocalScreenBreakpoints provides ScreenBreakpoints(
                watch = 280,
                mobile = 600,
                tablet = 900,
            ),
        ) {
            ShellWithAdaptiveNavigation()
        }
    }
}

@Composable
private fun ShellWithAdaptiveNavigation() {
    var section by remember { mutableIntStateOf(0) }

    // 2) AdaptiveNavigation — bottom NavigationBar on Watch/Mobile,
    //    NavigationRail on Tablet, labelled drawer on Desktop. Same items, three shapes.
    // 3) AdaptiveNavigationItem — icons are @Composable () -> Unit slots (no Material Icons dep needed).
    val items = listOf(
        AdaptiveNavigationItem(
            label = "Slots",
            icon = { GlyphIcon("◧") },
            selectedIcon = { GlyphIcon("◧", bold = true) },
        ),
        AdaptiveNavigationItem(
            label = "Values",
            icon = { GlyphIcon("≡") },
            selectedIcon = { GlyphIcon("≡", bold = true) },
        ),
        AdaptiveNavigationItem(
            label = "Two Pane",
            icon = { GlyphIcon("◫") },
            selectedIcon = { GlyphIcon("◫", bold = true) },
        ),
        AdaptiveNavigationItem(
            label = "Fallback",
            icon = { GlyphIcon("⚠") },
            selectedIcon = { GlyphIcon("⚠", bold = true) },
        ),
    )

    AdaptiveNavigation(
        items = items,
        selectedIndex = section,
        onItemSelected = { section = it },
    ) {
        Column(Modifier.fillMaxSize().padding(16.dp)) {
            LiveMetricsHeader()
            HorizontalDivider(Modifier.padding(vertical = 12.dp))
            when (section) {
                0 -> SlotsSection()
                1 -> ValuesSection()
                2 -> TwoPaneSection()
                3 -> FallbackSection()
            }
        }
    }
}

/* ---------- header ---------- */

// 4) rememberScreenType / rememberScreenWidth / rememberScreenHeight —
//    live screen state, recomposes when the window changes size.
@Composable
private fun LiveMetricsHeader() {
    val type = rememberScreenType()
    val w = rememberScreenWidth()
    val h = rememberScreenHeight()
    Text(
        text = "Responsive UI 1.0.0 — every feature in one app",
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold,
    )
    Spacer(Modifier.height(4.dp))
    Row(verticalAlignment = Alignment.CenterVertically) {
        Chip("ScreenType: ${type::class.simpleName}")
        Spacer(Modifier.padding(horizontal = 4.dp))
        Chip("${w} × ${h} dp")
    }
}

/* ---------- section 1: slots ---------- */

@Composable
private fun SlotsSection() {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        SectionTitle("Slot-based layouts")

        // 5) ResponsiveView — nullable slots, falls back to LocalResponsiveFallback for nulls.
        FeatureCard("ResponsiveView (nullable slots)") {
            ResponsiveView(
                modifier = Modifier.fillMaxWidth(),
                mobile = { SlotBox("Mobile slot", Color(0xFF7986CB)) },
                tablet = { SlotBox("Tablet slot", Color(0xFFF06292)) },
                desktop = { SlotBox("Desktop slot", Color(0xFFFFB74D)) },
                watch = { SlotBox("Watch slot", Color(0xFF4DB6AC)) },
            )
        }

        // 6) ScreenTypeLayout — mandatory slots. Compiler enforces you cover all 4 buckets.
        FeatureCard("ScreenTypeLayout (mandatory slots)") {
            ScreenTypeLayout(
                modifier = Modifier.fillMaxWidth(),
                watch = { SlotBox("watch (mandatory)", Color(0xFF4DB6AC)) },
                mobile = { SlotBox("mobile (mandatory)", Color(0xFF7986CB)) },
                tablet = { SlotBox("tablet (mandatory)", Color(0xFFF06292)) },
                desktop = { SlotBox("desktop (mandatory)", Color(0xFFFFB74D)) },
            )
        }

        // 7) ShowOnScreenType — conditional rendering by screen-type set.
        FeatureCard("ShowOnScreenType") {
            Text("Always visible row.")
            ShowOnScreenType(listOf(ScreenType.Mobile, ScreenType.Watch)) {
                Text(
                    "Small-screen-only row (Mobile + Watch)",
                    color = MaterialTheme.colorScheme.primary,
                )
            }
            ShowOnScreenType(listOf(ScreenType.Tablet, ScreenType.Desktop)) {
                Text(
                    "Large-screen-only row (Tablet + Desktop)",
                    color = MaterialTheme.colorScheme.secondary,
                )
            }
        }

        // 8) Custom ScreenBreakpoints passed explicitly — overrides LocalScreenBreakpoints
        //    for this one call site.
        FeatureCard("ScreenBreakpoints (override at one call site)") {
            ResponsiveView(
                breakpoints = ScreenBreakpoints(watch = 200, mobile = 480, tablet = 720),
                modifier = Modifier.fillMaxWidth(),
                mobile = { SlotBox("at custom mobile bp", Color(0xFFAED581)) },
                tablet = { SlotBox("at custom tablet bp", Color(0xFFF06292)) },
                desktop = { SlotBox("at custom desktop bp", Color(0xFFFFB74D)) },
            )
            Spacer(Modifier.height(8.dp))
            Text(
                text = "Boundaries 480/720 dp — narrower than the app-wide 600/900.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/* ---------- section 2: values ---------- */

@Composable
private fun ValuesSection() {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        SectionTitle("responsiveValue<T>")

        // 9) responsiveValue<T> — generic helper, works for any T.
        val columnCount: Int = responsiveValue(mobile = 1, tablet = 2, desktop = 4)
        val gridPadding: Dp = responsiveValue(mobile = 4.dp, tablet = 12.dp, desktop = 24.dp)
        val description: String = responsiveValue(
            mobile = "Single-column phone layout",
            tablet = "Two-column tablet layout",
            desktop = "Four-column desktop layout",
        )

        FeatureCard("Pick Int") {
            Text("Column count for current screen: $columnCount", fontWeight = FontWeight.Medium)
        }
        FeatureCard("Pick Dp") {
            Text("Padding for current screen: $gridPadding", fontWeight = FontWeight.Medium)
        }
        FeatureCard("Pick String") {
            Text(description, fontWeight = FontWeight.Medium)
        }

        FeatureCard("Composed example — a $columnCount-column grid at $gridPadding padding") {
            Row(Modifier.fillMaxWidth().padding(gridPadding)) {
                repeat(columnCount) { i ->
                    Box(
                        Modifier
                            .weight(1f)
                            .height(72.dp)
                            .padding(4.dp)
                            .background(
                                Color.hsl(
                                    hue = (i * 60f) % 360f,
                                    saturation = 0.4f,
                                    lightness = 0.65f,
                                ),
                                RoundedCornerShape(6.dp),
                            ),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("col ${i + 1}", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

/* ---------- section 3: two pane ---------- */

@Composable
private fun TwoPaneSection() {
    val items = listOf("Apollo", "Boreal", "Cyrene", "Delphi", "Echo", "Faelan")
    var selected by remember { mutableStateOf<String?>(null) }

    Column(Modifier.fillMaxSize()) {
        SectionTitle("TwoPaneLayout — list/detail collapse on small screens")
        Text(
            text = "Tap a name. On Tablet+ the detail appears next to the list; " +
                "on Mobile/Watch it replaces the list until you go back.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(vertical = 8.dp),
        )

        // 10) TwoPaneLayout — single pane on Mobile (controlled by showSecondary),
        //     side-by-side on Tablet+.
        TwoPaneLayout(
            showSecondary = selected != null,
            modifier = Modifier.fillMaxSize(),
            primary = {
                Column(Modifier.fillMaxSize().padding(8.dp)) {
                    Text("Items", fontWeight = FontWeight.Bold, modifier = Modifier.padding(8.dp))
                    items.forEach { item ->
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = if (selected == item)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                MaterialTheme.colorScheme.surfaceVariant,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clickable { selected = item },
                        ) {
                            Text(item, fontWeight = FontWeight.Medium, modifier = Modifier.padding(12.dp))
                        }
                    }
                }
            },
            secondary = {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                        .padding(16.dp),
                ) {
                    if (selected == null) {
                        Text("Detail pane is empty.", fontWeight = FontWeight.Medium)
                    } else {
                        Text(
                            "Detail for $selected",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                        )
                        Spacer(Modifier.height(8.dp))
                        Text("Lorem ipsum dolor sit amet, $selected ut labore et dolore magna aliqua.")
                        Spacer(Modifier.height(16.dp))
                        Surface(
                            shape = RoundedCornerShape(6.dp),
                            color = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.clickable { selected = null },
                        ) {
                            Text("← Back", modifier = Modifier.padding(8.dp))
                        }
                    }
                }
            },
        )
    }
}

/* ---------- section 4: fallback ---------- */

@Composable
private fun FallbackSection() {
    Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        SectionTitle("LocalResponsiveFallback")

        FeatureCard("Default fallback (built-in text)") {
            // ResponsiveView with NO slots provided — all default to LocalResponsiveFallback.
            ResponsiveView(modifier = Modifier.fillMaxWidth().height(120.dp))
        }

        // 11) LocalResponsiveFallback — install a branded placeholder once.
        FeatureCard("Custom fallback installed via CompositionLocalProvider") {
            CompositionLocalProvider(
                LocalResponsiveFallback provides { type -> BrandedFallback(type) },
            ) {
                ResponsiveView(modifier = Modifier.fillMaxWidth().height(120.dp))
            }
            Spacer(Modifier.height(8.dp))
            Text(
                "Apps install a branded placeholder once at the root and every " +
                    "ResponsiveView with a null slot picks it up.",
                fontSize = 12.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}

/* ---------- shared widgets ---------- */

@Composable
private fun SectionTitle(text: String) {
    Text(
        text = text,
        fontSize = 18.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier.padding(bottom = 8.dp),
    )
}

@Composable
private fun FeatureCard(title: String, content: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
    ) {
        Column(Modifier.padding(16.dp)) {
            Text(
                text = title,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 8.dp),
            )
            content()
        }
    }
}

@Composable
private fun SlotBox(label: String, color: Color) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(80.dp)
            .background(color, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center,
    ) {
        Text(label, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
    }
}

@Composable
private fun GlyphIcon(text: String, bold: Boolean = false) {
    Text(
        text = text,
        fontSize = 20.sp,
        fontWeight = if (bold) FontWeight.ExtraBold else FontWeight.Normal,
    )
}

@Composable
private fun Chip(text: String) {
    AssistChip(
        onClick = {},
        label = { Text(text, fontSize = 12.sp) },
        colors = AssistChipDefaults.assistChipColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
    )
}

@Composable
private fun BrandedFallback(type: ScreenType) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.errorContainer),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            "⚠ Custom fallback for ${type::class.simpleName} screen",
            color = MaterialTheme.colorScheme.onErrorContainer,
            fontWeight = FontWeight.Bold,
        )
    }
}
