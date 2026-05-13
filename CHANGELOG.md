# Changelog

All notable changes to this project will be documented in this file. Format
loosely follows [Keep a Changelog](https://keepachangelog.com/en/1.1.0/).

## [0.0.10] - 2026-05-13

### Added

- **iOS support** (`iosArm64`, `iosSimulatorArm64`). `getScreenWidth`,
  `getScreenHeight`, and the deprecated `getDeviceType` now have iOS
  `actual` implementations using `UIScreen.mainScreen.bounds` and
  `UIDevice.currentDevice.userInterfaceIdiom`.
- Full test coverage with `kotlin.test` and `compose.uiTest`. Tests split
  across two source sets so they run on every target without runtime issues:
  - `commonTest` (runs on Android + Desktop + iOS + Wasm): pure-logic
    coverage of `ScreenBreakpoints` boundary conditions and `DeviceConfig`
    state — 11 tests.
  - `skikoTest` (runs on Desktop + iOS + Wasm, skipped on Android JVM unit
    tests since `compose.uiTest` needs a Skiko runtime): Compose UI tests
    for `ScreenTypeLayout`, `ResponsiveView`, `ShowOnScreenType`, and the
    `ScreenTypeLayoutBuilder` delegate — 11 tests.
  Total locally verified: Android 11/11, Desktop 22/22, iOS sim 22/22.
- `kotlinx-binary-compatibility-validator` plugin at the root, with KLib
  ABI dumps enabled so iOS/Wasm public-API regressions are caught at PR
  review.
- CI now runs `iosSimulatorArm64Test` on `macos-latest`,
  `wasmJsBrowserTest` on `ubuntu-latest`, plus `:example:assembleDebug`,
  `:example:linkDebugFrameworkIosSimulatorArm64`, and
  `:example:packageDistributionForCurrentOS`.
- `:example` now declares its iOS and Wasm targets (previously the source
  dirs existed but the targets were undeclared, so the code was dead).

### Changed

- **Kotlin 2.1.21 → 2.3.21.**
- **Compose Multiplatform 1.8.1 → 1.10.3.**
- **Android Gradle Plugin 8.7.3 → 9.2.0** (major). Bypass flags
  `android.builtInKotlin=false` / `android.newDsl=false` are set in
  `gradle.properties` so `com.android.library` + `kotlin.multiplatform`
  keep working together; migration to `com.android.kotlin.multiplatform.library`
  is queued for a follow-up.
- **Gradle wrapper 8.14.2 → 9.5.0.**
- **`com.vanniktech.maven.publish` 0.29.0 → 0.36.0.**
- **Dokka 2.0.0 → 2.2.0.**
- **`compileSdk` / `targetSdk` 35 → 36.**
- License switched from MIT to Apache 2.0 (the POM already declared
  Apache 2.0 — this resolves the file/POM mismatch).
- `gradle.properties` deduplicated (`android.enableJetifier` set twice
  with conflicting values), stale Kotlin/JS config removed,
  `version=0.0.4` reconciled with the build file's `0.0.9`.
- `settings.gradle.kts` no longer includes the missing-from-disk
  `:example-remote` module.
- `:responsive-ui/build.gradle.kts` rewritten cleanly — ~200 lines of
  commented-out historical alternatives removed.

### Removed

- **Legacy `js(IR)` target dropped.** The library no longer publishes a
  JS klib; only Wasm/JS for web. `compose.html.core` dependency gone,
  `:responsive-ui/src/jsMain/` deleted, `jsTest` CI matrix entry gone.
- `iosX64` target intentionally not added (deprecated in Kotlin, dropped
  in CMP 1.11). Only `iosArm64` and `iosSimulatorArm64` ship.

### Fixed

- iOS targets now exist as compilable, testable, publishable targets — the
  library has never been built or tested on iOS prior to this release.
- License file (`LICENSE`) matches the POM-declared license (Apache 2.0).

### Known issues / follow-ups

- `signAllPublications()` remains commented out in
  `:responsive-ui/build.gradle.kts` pending GPG key rotation (the
  previously-committed `private-key_new.asc` is being treated as
  compromised).
- `:responsive-ui` and `:example` still use the legacy
  `com.android.library` / `com.android.application` plugins via the AGP 9
  bypass flags. Migration to `com.android.kotlin.multiplatform.library`
  is a future task.
- `ScreenBreakpoints.getScreenType` returns `Tablet` (not `Desktop`) for
  widths in `[tablet, desktop)` — see `ResponsiveUi.kt:36`. Tests pin
  the existing behavior; fixing the bug is a separate behavior change.
