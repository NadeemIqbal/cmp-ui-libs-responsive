# Responsive UI

[![Maven Central](https://img.shields.io/maven-central/v/io.github.nadeemiqbal/responsive-ui)](https://central.sonatype.com/artifact/io.github.nadeemiqbal/responsive-ui)
[![License](https://img.shields.io/badge/License-Apache_2.0-blue.svg)](LICENSE)
[![CI](https://github.com/NadeemIqbal/cmp-ui-libs-responsive/actions/workflows/ci.yml/badge.svg)](https://github.com/NadeemIqbal/cmp-ui-libs-responsive/actions/workflows/ci.yml)
[![API docs](https://img.shields.io/badge/API_docs-Dokka-orange)](https://nadeemiqbal.github.io/cmp-ui-libs-responsive/)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.3.21-7F52FF?logo=kotlin)](https://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.10.3-4285F4)](https://github.com/JetBrains/compose-multiplatform)

A small, focused Compose Multiplatform library for **responsive layouts** —
pick the right composable per screen size and let your UI swap shape from
phone to tablet to desktop without hand-rolling the plumbing.

**Targets:** Android · Desktop (JVM) · iOS (`iosArm64`, `iosSimulatorArm64`) · Wasm/JS

📚 **[API docs](https://nadeemiqbal.github.io/cmp-ui-libs-responsive/)** · auto-published from `master`

<video src="https://github.com/NadeemIqbal/cmp-ui-libs-responsive/raw/master/example.mp4" autoplay loop muted playsinline width="100%"></video>

> The demo above shows the `:example` app — resize the window and watch `AdaptiveNavigation` swap between bottom-bar / rail / drawer while the slots flip through breakpoints. [Run it yourself](#running-the-demo).

## What you get

| API | What it does |
|---|---|
| `ResponsiveView` | Slot-based layout — pick a composable for each of Watch / Mobile / Tablet / Desktop |
| `ScreenTypeLayout` | Same idea, mandatory slots — the compiler enforces full coverage |
| `responsiveValue<T>` | Pick any value (`Dp`, `Int`, `String`, …) based on the current screen type |
| `AdaptiveNavigation` | Bottom bar on phones, navigation rail on tablets, persistent drawer on desktop — from one items list |
| `TwoPaneLayout` | Side-by-side master/detail on tablet+, collapses to a single pane on phone |
| `ShowOnScreenType` | Conditional rendering by screen-type set |
| `LocalScreenBreakpoints` | Install custom breakpoints once at the app root |
| `LocalResponsiveFallback` | Install a branded placeholder for missing slots app-wide |
| `rememberScreenType` / `rememberScreenWidth` / `rememberScreenHeight` | Live, reactive screen state |

A companion `responsive-ui-testing` artifact ships `setContentWithScreenWidth { }`
so breakpoint-sensitive UI tests are deterministic across every platform.

## Install

### Kotlin Multiplatform / Compose Multiplatform

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories { mavenCentral() }
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
dependencies {
    implementation("io.github.nadeemiqbal:responsive-ui:1.0.0")
}
```

Gradle's variant resolution picks the right per-target artifact via the
published module metadata — no separate Android coordinate.

### Swift / SwiftUI (SwiftPM)

```swift
// Package.swift
dependencies: [
    .package(url: "https://github.com/NadeemIqbal/cmp-ui-libs-responsive", exact: "1.0.0")
]
```

```swift
import ResponsiveUI
```

## Quick start

```kotlin
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

Slots are nullable — pass only what you need. Null slots render a default
placeholder; replace it app-wide via `LocalResponsiveFallback`.

### Pick a value by screen type

```kotlin
val padding = responsiveValue(mobile = 8.dp, tablet = 16.dp, desktop = 24.dp)
val columns = responsiveValue(mobile = 1, tablet = 2, desktop = 4)
```

Generic over `T` — works for `Dp`, `Int`, `String`, lambdas, anything.

### Install breakpoints once at the app root

```kotlin
CompositionLocalProvider(
    LocalScreenBreakpoints provides ScreenBreakpoints(
        watch = 280, mobile = 600, tablet = 900,
    )
) {
    AppContent()  // every responsive composable picks these up
}
```

Defaults align with Material 3's `WindowSizeClass` boundaries
(600 dp = Compact↔Medium, 900 dp ≈ Medium↔Expanded).

### Adaptive navigation chrome

```kotlin
val items = listOf(
    AdaptiveNavigationItem("Home",     icon = { Icon(Icons.Filled.Home,     null) }),
    AdaptiveNavigationItem("Search",   icon = { Icon(Icons.Filled.Search,   null) }),
    AdaptiveNavigationItem("Settings", icon = { Icon(Icons.Filled.Settings, null) }),
)

var selected by remember { mutableIntStateOf(0) }
AdaptiveNavigation(items, selectedIndex = selected, onItemSelected = { selected = it }) {
    when (selected) {
        0 -> HomeContent()
        1 -> SearchContent()
        else -> SettingsContent()
    }
}
```

| Screen | Chrome |
|---|---|
| Watch / Mobile | bottom `NavigationBar` |
| Tablet | left `NavigationRail` |
| Desktop | labelled persistent drawer |

### List / detail with `TwoPaneLayout`

```kotlin
var selectedId by remember { mutableStateOf<String?>(null) }
TwoPaneLayout(
    showSecondary = selectedId != null,
    primary   = { ItemList(onSelect = { selectedId = it }) },
    secondary = { ItemDetail(selectedId) },
)
```

- **≥ Tablet:** both panes side-by-side
- **< Tablet:** one pane at a time — `secondary` when `showSecondary == true`, `primary` otherwise

### Conditional rendering

```kotlin
ShowOnScreenType(listOf(ScreenType.Tablet, ScreenType.Desktop)) {
    SideNavigation()  // hidden on Mobile / Watch
}
```

### Live screen state

```kotlin
val type   = rememberScreenType()    // ScreenType.Mobile | Tablet | Desktop | Watch
val width  = rememberScreenWidth()   // Int (dp)
val height = rememberScreenHeight()  // Int (dp)
```

## Testing

Headless test compositions report `containerSize.width = 0` on some
targets (notably iOS simulator), which makes breakpoint-sensitive UI
tests flaky. The companion artifact injects a deterministic width:

```kotlin
// build.gradle.kts
kotlin.sourceSets.commonTest.dependencies {
    implementation("io.github.nadeemiqbal:responsive-ui-testing:1.0.0")
}
```

```kotlin
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

## Running the demo

The `:example` module is a Compose Multiplatform app that exercises every
public API in a single window — adaptive navigation, slots, value picks,
two-pane, conditional rendering, custom fallback, live metrics.

```bash
./gradlew :example:run                                # Desktop
./gradlew :example:installDebug                        # Android (device/emulator attached)
./gradlew :example:wasmJsBrowserDevelopmentRun         # Web
./gradlew :example:linkDebugFrameworkIosSimulatorArm64 # iOS framework
```

Resize the desktop window to watch `AdaptiveNavigation` swap between
bottom-bar / rail / drawer and the slots flip through breakpoints live.

## Development

```bash
# Compile (non-Android targets need no Android SDK)
./gradlew :responsive-ui:compileKotlinDesktop
./gradlew :responsive-ui:compileKotlinWasmJs
./gradlew :responsive-ui:compileKotlinIosSimulatorArm64

# Test
./gradlew :responsive-ui:desktopTest
./gradlew :responsive-ui:iosSimulatorArm64Test
./gradlew :responsive-ui:test                     # Android pure-logic tests

# Local publish (Android SDK required)
./gradlew :responsive-ui:publishToMavenLocal
./gradlew :responsive-ui-testing:publishToMavenLocal

# XCFramework for Swift/ObjC consumers
./gradlew :responsive-ui:assembleResponsiveUIReleaseXCFramework
```

## Publishing

Tag a release on `master` (`v*`) and
[`publish.yml`](.github/workflows/publish.yml) on `macos-latest`:

1. Runs `publishAndReleaseToMavenCentral`
2. Builds the XCFramework, zips it, computes SHA-256
3. Creates a GitHub Release with the zip attached
4. Updates `Package.swift` URL + checksum and commits it back to `master`

API docs are published to GitHub Pages by
[`docs.yml`](.github/workflows/docs.yml) on every push to `master`.

<details>
<summary>Migrating from 0.0.x</summary>

| Before (0.0.x) | After (1.0.0) |
|---|---|
| `getScreenType()` | `rememberScreenType()` |
| `getScreenWidth()` / `getScreenHeight()` | `rememberScreenWidth()` / `rememberScreenHeight()` |
| `ScreenBreakpoints(mobile, tablet, desktop, watch)` | `ScreenBreakpoints(watch, mobile, tablet)` — the `desktop` field was redundant |
| `ScreenTypeLayoutBuilder.builder(...)` | `ResponsiveView(...)` |
| `DeviceType` / `getDeviceType()` | `ScreenType` / `rememberScreenType()` |
| `DeviceConfig.screenWidth` / `DeviceConfig.screenHeight` | `rememberScreenWidth()` / `rememberScreenHeight()` |

The `[tablet, desktop)` range now correctly resolves to `ScreenType.Desktop`
instead of `ScreenType.Tablet` — a 0.0.x bug. See [CHANGELOG.md](CHANGELOG.md)
for the full migration notes.

</details>

## License

Apache License 2.0 — see [LICENSE](LICENSE).
