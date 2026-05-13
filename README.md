# Responsive UI

A Kotlin Multiplatform Compose library that provides Flutter-like responsive
layouts. Pick a per-breakpoint composable slot (`mobile` / `tablet` / `desktop`
/ `watch`) and the library renders the right one based on the live screen
width.

Targets: **Android**, **Desktop (JVM)**, **iOS** (`iosArm64`, `iosSimulatorArm64`),
**Web (Wasm/JS)**.

## Demo

![Responsive UI Demo](https://raw.githubusercontent.com/NadeemIqbal/cmp-ui-libs-responsive/master/example.gif)

## Installation

```kotlin
// settings.gradle.kts
dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

// build.gradle.kts (a Kotlin Multiplatform module)
kotlin {
    sourceSets.commonMain.dependencies {
        implementation("io.github.nadeemiqbal:responsive-ui:0.0.9")
    }
}
```

For a pure-Android or pure-Desktop consumer the same coordinate works — the
Maven artifact's resolution picks the correct per-target klib.

## Quick start

```kotlin
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.nadeem.responsiveui.ResponsiveView

@Composable
fun MyApp() {
    ResponsiveView(
        mobile = { Text("Phone layout") },
        tablet = { Text("Tablet layout") },
        desktop = { Text("Desktop layout") },
        watch = { Text("Watch layout") },
    )
}
```

### Custom breakpoints

```kotlin
import com.nadeem.responsiveui.ScreenBreakpoints
import com.nadeem.responsiveui.ScreenTypeLayout

@Composable
fun MyApp() {
    ScreenTypeLayout(
        breakpoints = ScreenBreakpoints(
            watch = 280,
            mobile = 600,
            tablet = 900,
            desktop = 1280,
        ),
        mobile = { /* ... */ },
        tablet = { /* ... */ },
        desktop = { /* ... */ },
    )
}
```

### Show content only on specific screen types

```kotlin
import com.nadeem.responsiveui.ScreenType
import com.nadeem.responsiveui.ShowOnScreenType

@Composable
fun SidebarHostOnLargerScreens() {
    ShowOnScreenType(screenTypes = listOf(ScreenType.Tablet, ScreenType.Desktop)) {
        SideNav()
    }
}
```

### Query the current screen type

```kotlin
import com.nadeem.responsiveui.getScreenType

@Composable
fun MyComposable() {
    val type = getScreenType()
    // branch on `type` — ScreenType.Mobile / Tablet / Desktop / Watch
}
```

## Development

```bash
# Library compile per target (no Android SDK required for non-Android targets)
./gradlew :responsive-ui:compileKotlinDesktop
./gradlew :responsive-ui:compileKotlinWasmJs
./gradlew :responsive-ui:compileKotlinIosSimulatorArm64

# Tests
./gradlew :responsive-ui:desktopTest
./gradlew :responsive-ui:iosSimulatorArm64Test
./gradlew :responsive-ui:wasmJsBrowserTest

# Local publish (requires Android SDK)
./gradlew :responsive-ui:publishToMavenLocal
```

## Publishing

Releases are cut from a git tag (`v*`) via the `publish.yml` GitHub Actions
workflow against `macos-latest`. See `MAVEN_CENTRAL_SETUP.md` for the
credential/PGP setup.

## License

Apache License 2.0 — see [LICENSE](LICENSE).
