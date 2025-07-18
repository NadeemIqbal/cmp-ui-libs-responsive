# Responsive UI Library

A Kotlin Multiplatform Compose library that provides Flutter-like responsive layouts for Android, Desktop, and Web applications.

## Demo

![Responsive UI Demo](https://raw.githubusercontent.com/NadeemIqbal/cmp-ui-libs-responsive/master/example.gif)

*The library automatically adapts layouts based on screen size - mobile, tablet, and desktop views*

## Installation

### Via Maven Central (Recommended)
```kotlin
dependencies {
    implementation("io.github.nadeemiqbal:responsive-ui:0.0.7")
}
```

**In your module `build.gradle.kts`:**
```kotlin
dependencies {
    implementation("com.github.NadeemIqbal:cmp-ui-libs-responsive:0.0.4")
}
```

## Quick Start

```kotlin
import com.nadeem.responsiveui.ResponsiveLayout

@Composable
fun MyApp() {
    ResponsiveLayout(
        mobile = { MobileLayout() },
        tablet = { TabletLayout() },
        desktop = { DesktopLayout() }
    )
}
```

## Supported Platforms

- Android
- Desktop (JVM)
- Web (Wasm-JS)
- iOS (Coming Soon)

## Development

To build the library locally:
```bash
./gradlew build
```

To run tests:
```bash
./gradlew test
```

## Publishing

Coming soon

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.
