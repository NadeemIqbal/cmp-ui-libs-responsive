# Responsive UI

[![Maven Central](https://img.shields.io/maven-central/v/io.github.nadeemiqbal/responsive-ui)](https://central.sonatype.com/artifact/io.github.nadeemiqbal/responsive-ui)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](LICENSE)
[![CI](https://github.com/NadeemIqbal/cmp-ui-libs-responsive/actions/workflows/ci.yml/badge.svg)](https://github.com/NadeemIqbal/cmp-ui-libs-responsive/actions/workflows/ci.yml)
[![API docs](https://img.shields.io/badge/API_docs-Dokka-orange)](https://nadeemiqbal.github.io/cmp-ui-libs-responsive/)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.3.21-7F52FF?logo=kotlin)](https://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.10.3-4285F4)](https://github.com/JetBrains/compose-multiplatform)

A Compose Multiplatform library for responsive layouts. Pick a slot per
screen size, drop in an adaptive nav rail / bottom bar / drawer, or split
a screen into list+detail panes — all with one consistent breakpoint
configuration.

Targets: **Android**, **Desktop (JVM)**, **iOS** (`iosArm64`, `iosSimulatorArm64`),
**Wasm/JS**.

📚 **[API docs →](https://nadeemiqbal.github.io/cmp-ui-libs-responsive/)** · auto-published from master on every push

![Demo](https://raw.githubusercontent.com/NadeemIqbal/cmp-ui-libs-responsive/master/example.gif)

## Install

### Kotlin Multiplatform / Compose Multiplatform

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

// build.gradle.kts
kotlin {
    sourceSets.commonMain.dependencies {
        implementation("io.github.nadeemiqbal:responsive-ui:1.0.0")
    }
}
```

### Android-only

```kotlin
// build.gradle.kts (Android library or app module)
dependencies {
    implementation("io.github.nadeemiqbal:responsive-ui:1.0.0")
}
```

Gradle's variant resolution picks the right per-target artifact via the
published module metadata — no separate Android coordinate needed.

### Swift / SwiftUI (via SwiftPM)

```swift
// Package.swift
dependencies: [
    .package(url: "https://github.com/NadeemIqbal/cmp-ui-libs-responsive", exact: "1.0.0")
]
```

```swift
import ResponsiveUI
```

## Core API

### `ResponsiveView` — slot per screen type

```kotlin
import androidx.compose.runtime.Composable
import com.nadeem.responsiveui.ResponsiveView

@Composable
fun MyScreen() {
    ResponsiveView(
        mobile = { MobileLayout() },
        tablet = { TabletLayout() },
        desktop = { DesktopLayout() },
    )
}
```

Slots are nullable — pass only what you need. Unfilled slots render a
default placeholder; replace the default app-wide via `LocalResponsiveFallback`.

### `responsiveValue<T>()` — one value per breakpoint

```kotlin
import com.nadeem.responsiveui.responsiveValue

val padding = responsiveValue(
    mobile = 8.dp,
    tablet = 16.dp,
    desktop = 24.dp,
)

val columnCount = responsiveValue(
    mobile = 1,
    tablet = 2,
    desktop = 4,
)
```

Generic over `T` — works for `Dp`, `Int`, `String`, lambdas, anything.

### `LocalScreenBreakpoints` — set breakpoints once at the app root

```kotlin
import com.nadeem.responsiveui.LocalScreenBreakpoints
import com.nadeem.responsiveui.ScreenBreakpoints

@Composable
fun App() {
    CompositionLocalProvider(
        LocalScreenBreakpoints provides ScreenBreakpoints(
            watch = 280,
            mobile = 600,
            tablet = 900,
        )
    ) {
        AppContent()  // every responsive composable picks these up
    }
}
```

Defaults are aligned with Material 3's `WindowSizeClass` boundaries
(600 dp = Compact↔Medium, 900 dp ≈ Medium↔Expanded).

### `AdaptiveNavigation` — bottom bar / rail / drawer auto-swap

```kotlin
import com.nadeem.responsiveui.AdaptiveNavigation
import com.nadeem.responsiveui.AdaptiveNavigationItem

val items = listOf(
    AdaptiveNavigationItem("Home", icon = { Icon(Icons.Filled.Home, null) }),
    AdaptiveNavigationItem("Search", icon = { Icon(Icons.Filled.Search, null) }),
    AdaptiveNavigationItem("Settings", icon = { Icon(Icons.Filled.Settings, null) }),
)

var selected by remember { mutableIntStateOf(0) }
AdaptiveNavigation(
    items = items,
    selectedIndex = selected,
    onItemSelected = { selected = it },
) {
    when (selected) {
        0 -> HomeContent()
        1 -> SearchContent()
        else -> SettingsContent()
    }
}
```

- **Watch / Mobile**: `NavigationBar` at the bottom
- **Tablet**: `NavigationRail` on the left
- **Desktop**: labelled drawer on the left

### `TwoPaneLayout` — list/detail collapse on small screens

```kotlin
import com.nadeem.responsiveui.TwoPaneLayout

var selectedItemId by remember { mutableStateOf<String?>(null) }
TwoPaneLayout(
    showSecondary = selectedItemId != null,
    primary = { ItemList(onSelect = { selectedItemId = it }) },
    secondary = { ItemDetail(selectedItemId) },
)
```

- **≥ Tablet**: both panes side-by-side
- **Mobile**: only one pane at a time — `secondary` when `showSecondary == true`,
  `primary` otherwise

### `ShowOnScreenType` — show content only at certain sizes

```kotlin
import com.nadeem.responsiveui.ScreenType
import com.nadeem.responsiveui.ShowOnScreenType

ShowOnScreenType(screenTypes = listOf(ScreenType.Tablet, ScreenType.Desktop)) {
    SideNavigation()  // hidden on Mobile / Watch
}
```

### Reading the current screen state

```kotlin
import com.nadeem.responsiveui.rememberScreenType
import com.nadeem.responsiveui.rememberScreenWidth
import com.nadeem.responsiveui.rememberScreenHeight

val type = rememberScreenType()      // ScreenType.Mobile | Tablet | Desktop | Watch
val width = rememberScreenWidth()    // Int (dp)
val height = rememberScreenHeight()  // Int (dp)
```

## Testing your responsive UI

Add the companion testing artifact to your test source set:

```kotlin
// build.gradle.kts
kotlin {
    sourceSets.commonTest.dependencies {
        implementation("io.github.nadeemiqbal:responsive-ui-testing:1.0.0")
    }
}
```

Force a specific screen width in tests — `LocalWindowInfo.containerSize.width`
otherwise reports `0` in headless test compositions:

```kotlin
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.runComposeUiTest
import com.nadeem.responsiveui.testing.setContentWithScreenWidth
import kotlin.test.Test

@OptIn(ExperimentalTestApi::class)
class MyResponsiveTest {
    @Test
    fun showsTabletPaneAt800Dp() = runComposeUiTest {
        setContentWithScreenWidth(widthDp = 800) {
            MyResponsiveScreen()
        }
        onNodeWithText("Tablet pane").assertExists()
    }
}
```

## Migration from 0.0.x

If you were using `0.0.x`, see [CHANGELOG.md](CHANGELOG.md) for the full
breaking-change list. Common migrations:

| Before (0.0.x) | After (1.0.0) |
|---|---|
| `getScreenType()` | `rememberScreenType()` |
| `getScreenWidth()` / `getScreenHeight()` | `rememberScreenWidth()` / `rememberScreenHeight()` |
| `ScreenBreakpoints(mobile, tablet, desktop, watch)` | `ScreenBreakpoints(watch, mobile, tablet)` — `desktop` field removed (was redundant) |
| `ScreenTypeLayoutBuilder.builder(...)` | `ResponsiveView(...)` |
| `DeviceType` / `getDeviceType()` | use `ScreenType` / `rememberScreenType()` |
| `DeviceConfig.screenWidth` | `rememberScreenWidth()` |

The `[tablet, desktop)` range now correctly resolves to `ScreenType.Desktop`
instead of `ScreenType.Tablet` (a 0.0.x bug).

## Development

```bash
# Compile non-Android targets (no Android SDK needed)
./gradlew :responsive-ui:compileKotlinDesktop
./gradlew :responsive-ui:compileKotlinWasmJs
./gradlew :responsive-ui:compileKotlinIosSimulatorArm64

# Tests
./gradlew :responsive-ui:desktopTest                # 35 tests
./gradlew :responsive-ui:iosSimulatorArm64Test      # 35 tests on iOS sim
./gradlew :responsive-ui:test                       # 8 pure-logic tests on Android

# Local publish (Android SDK required)
./gradlew :responsive-ui:publishToMavenLocal
./gradlew :responsive-ui-testing:publishToMavenLocal

# XCFramework for Swift/ObjC consumers
./gradlew :responsive-ui:assembleResponsiveUIReleaseXCFramework
```

A standalone consumer sample lives at
`/Users/nextgeni/Desktop/nadeem/projects/responsive-ui-consumer-sample/` —
proves end-to-end Maven Central consumption.

## Publishing

Tagged releases (`v*`) fire
[`publish.yml`](.github/workflows/publish.yml) on `macos-latest`, which runs
`publishAndReleaseToMavenCentral`, builds the XCFramework, attaches the zip
to a GitHub Release, and auto-updates `Package.swift` with the new
checksum.

## License

Apache License 2.0 — see [LICENSE](LICENSE).
