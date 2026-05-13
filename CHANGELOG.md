# Changelog

All notable changes to this project will be documented in this file. Format
loosely follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [1.0.0] - 2026-05-14

First **stable** release. Bundles every breaking change so consumers
migrate once. If you were on `0.0.x`, the migration table in this entry is
the single source of truth.

### Added

- **`responsiveValue<T>(mobile, tablet, desktop, watch = mobile)`** — generic
  helper that picks one value per screen type. Works for `Dp`, `Int`,
  `String`, lambdas, anything.
- **`LocalScreenBreakpoints` CompositionLocal** — install at the app root
  once, every `ResponsiveView` / `ScreenTypeLayout` / `responsiveValue` /
  `ShowOnScreenType` reads the default from it. No more passing
  `breakpoints =` to every call site.
- **`LocalResponsiveFallback` CompositionLocal** — apps install their
  branded fallback widget once; replaces the previously-`private`
  default placeholder.
- **`AdaptiveNavigation`** composable — bottom `NavigationBar` on
  Watch/Mobile, `NavigationRail` on Tablet, labelled drawer on Desktop,
  all from one `items` list. Icons are `@Composable () -> Unit` slots so
  no Material Icons dependency is required.
- **`TwoPaneLayout`** composable — single pane on Mobile, side-by-side
  on Tablet+ with configurable split ratio and `showBothAt` threshold.
  Covers list/detail and master/detail patterns in ~25 lines of consumer code.
- **Localized default fallback string** — English, Spanish, French, Urdu
  shipped in `composeResources/values{-es,-fr,-ur}/strings.xml`. Apps
  using non-shipped locales get the English fallback; apps overriding
  `LocalResponsiveFallback` get whatever they install.
- **`:responsive-ui-testing` companion artifact** —
  `io.github.nadeemiqbal:responsive-ui-testing:1.0.0`. Exposes
  `ComposeUiTest.setContentWithScreenWidth(widthDp) { … }` which injects
  a deterministic screen width via the internal `LocalScreenWidthOverride`.
  Required for breakpoint-sensitive UI tests on iOS simulator (where
  `LocalWindowInfo.containerSize.width` is 0).
- **SwiftPM distribution** — `Package.swift` at repo root, auto-updated
  by `publish.yml` on each tag push. Swift consumers add a package
  dependency on the GitHub URL pinned to `exact("1.0.0")`.

### Changed (breaking)

- **`ScreenBreakpoints.getScreenType` fixed** — widths in `[tablet, ∞)` now
  correctly resolve to `ScreenType.Desktop`. The 0.0.x code had
  `width < desktop -> ScreenType.Tablet` which incorrectly returned
  Tablet for `[tablet, desktop)`. *If you were relying on the buggy
  behaviour*, raise your `tablet` value to what your `desktop` value was.
- **`ScreenBreakpoints.desktop` field removed** — it was redundant given
  the 3-bucket model (3 boundaries → 4 buckets). Constructor is now
  `ScreenBreakpoints(watch = 300, mobile = 600, tablet = 900)`. Defaults
  align with Material 3's `WindowSizeClass`.
- **`getScreenType()` → `rememberScreenType()`** — idiomatic Kotlin
  (`remember*` pattern). Same applies to `getScreenWidth()` and
  `getScreenHeight()`.
- **`@Composable expect fun getScreenWidth() / getScreenHeight()` removed**
  — replaced with one common-source impl using
  `LocalWindowInfo.current.containerSize` + `LocalDensity`. All four
  `actual` files (`ResponsiveUi.android.kt`, `.desktop.kt`, `.ios.kt`,
  `.wasmJs.kt`) deleted.
- **`DeviceType` (sealed class) and `expect fun getDeviceType()` removed**
  — they were `@Deprecated` since 0.0.x. Migrate to `ScreenType` and
  `rememberScreenType()`.
- **`DeviceConfig` removed** — was a global mutable singleton (anti-pattern
  in Compose). Migrate to `rememberScreenWidth()` / `rememberScreenHeight()`
  which are reactive and don't require manual updates.
- **`ScreenTypeLayoutBuilder.builder()` removed** — was functionally
  identical to `ResponsiveView`. Migrate to `ResponsiveView`.
- **`ScreenTypeLayout` slots are now non-nullable.** Use `ResponsiveView`
  if you want nullable slots with fallback behavior.
- **Slot parameter order standardised** to small-to-large: `watch`,
  `mobile`, `tablet`, `desktop` (was `mobile`, `tablet`, `desktop`,
  `watch`). All callers using named arguments are unaffected; positional
  callers need to reorder.
- **`DefaultFallbackWidget` now uses `Material 3 Text` and
  `LocalContentColor`** — was hardcoded `Color.Black` on Material 2
  `Text`. Renders correctly in dark themes.

### Migration table

| Before (0.0.x) | After (1.0.0) |
|---|---|
| `getScreenType(breakpoints)` | `rememberScreenType(breakpoints)` |
| `getScreenWidth()` | `rememberScreenWidth()` |
| `getScreenHeight()` | `rememberScreenHeight()` |
| `ScreenBreakpoints(mobile, tablet, desktop, watch)` | `ScreenBreakpoints(watch, mobile, tablet)` |
| `ScreenTypeLayoutBuilder.builder(...)` | `ResponsiveView(...)` |
| `DeviceType` | `ScreenType` |
| `getDeviceType()` | `rememberScreenType()` |
| `DeviceConfig.screenWidth` | `rememberScreenWidth()` |
| Hand-rolled `when (rememberScreenType()) { … }` | `responsiveValue(mobile, tablet, desktop)` |
| Passing `breakpoints =` to every call | `CompositionLocalProvider(LocalScreenBreakpoints provides …)` at app root |

### Deferred to a future release

- **Material 3 `WindowSizeClass` interop** — `WindowSizeClass` lives in
  `androidx.compose.material3:material3-window-size-class`, an
  Android-only artifact not present in CMP's Material 3. Will be added
  if a non-Android CMP `WindowSizeClass` materialises, or as an
  Android-only `responsive-ui-material3` companion artifact.
- **Dokka HTML site auto-publish to GitHub Pages** — Dokka 2.2 is wired
  in the build, but the `gh-pages` workflow is pending. Will ship in a
  1.0.x patch release.
- **Foldable / hinge awareness** — `androidx.window` is Android-only;
  pending a CMP-friendly equivalent or an opt-in Android module.

### Internal

- New `skikoTest` source set in the main library so Compose UI tests
  (`compose.uiTest`) only run on Desktop / iOS / Wasm — Android JVM unit
  tests stay focused on pure-logic checks and don't try to instantiate
  Skiko.
- Applied `applyDefaultHierarchyTemplate()` explicitly because manual
  `dependsOn` for `skikoTest` would otherwise silently disable the
  default `appleMain → iosMain → iosArm64Main` hierarchy.

### Tested

- 35 tests pass on Desktop and iOS Simulator targets
- 8 tests pass on Android (pure-logic, no Skiko)
- 5 tests pass on the `:responsive-ui-testing` smoke suite

## [0.0.11] - 2026-05-13

### Added

- iOS support (`iosArm64`, `iosSimulatorArm64`). `getScreenWidth`,
  `getScreenHeight`, and the deprecated `getDeviceType` got iOS
  `actual` implementations using `UIScreen.mainScreen.bounds` and
  `UIDevice.currentDevice.userInterfaceIdiom`.
- Full test coverage with `kotlin.test` and `compose.uiTest`. Tests
  split across two source sets so they run on every target without
  runtime issues:
  - `commonTest` (Android + Desktop + iOS + Wasm): pure-logic coverage
    of `ScreenBreakpoints` boundaries and `DeviceConfig` state.
  - `skikoTest` (Desktop + iOS + Wasm, skipped on Android JVM):
    Compose UI tests for `ScreenTypeLayout`, `ResponsiveView`,
    `ShowOnScreenType`, and the `ScreenTypeLayoutBuilder` delegate.
- `kotlinx-binary-compatibility-validator` plugin at the root.
- CI runs `iosSimulatorArm64Test`, `wasmJsBrowserTest`,
  `:example:assembleDebug`, and `:example:linkDebugFrameworkIosSimulatorArm64`.

### Changed

- Kotlin 2.1.21 → 2.3.21.
- Compose Multiplatform 1.8.1 → 1.10.3.
- Android Gradle Plugin 8.7.3 → 9.2.0 (with `android.builtInKotlin=false`
  / `android.newDsl=false` bypass flags for KMP coexistence).
- Gradle wrapper 8.14.2 → 9.5.0.
- `com.vanniktech.maven.publish` 0.29.0 → 0.36.0.
- Dokka 2.0.0 → 2.2.0.
- `compileSdk` / `targetSdk` 35 → 36.
- License switched from MIT to Apache 2.0.

### Removed

- Legacy `js(IR)` target dropped; Wasm/JS only for web.
- `iosX64` target intentionally not added (deprecated in Kotlin, dropped
  in CMP 1.11). Only `iosArm64` and `iosSimulatorArm64` ship.

### Fixed

- POM `<organization>` / `<organizationUrl>` missing fields that were
  blocking Maven Central publication.
