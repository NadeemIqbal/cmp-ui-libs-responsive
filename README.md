# Responsive UI Multiplatform Library

A Kotlin Multiplatform library for responsive UI detection across Android, iOS, Web, and Desktop platforms.

## Features
- Detects device type (Mobile, Tablet, Desktop) on all supported platforms
- Platform-specific implementations for accurate screen size detection

## Usage
Import the library and use the `getDeviceType()` function to determine the current device type:

```kotlin
val deviceType = getDeviceType()
when (deviceType) {
    DeviceType.Mobile -> // Handle mobile UI
    DeviceType.Tablet -> // Handle tablet UI
    DeviceType.Desktop -> // Handle desktop UI
    DeviceType.Unknown -> // Handle unknown
}
```

## Platform Support
- **Android**: Uses screen width in dp
- **iOS**: Uses UIKit screen width (requires macOS to build)
- **Web (JS)**: Uses `window.innerWidth`
- **Desktop**: Uses Java AWT Toolkit

## iOS/macOS Requirements
> **Note:** Building and testing the iOS target requires macOS and Xcode. You can write and maintain iOS code on Windows, but you cannot build or run it without a Mac.

## License
This project is open source under the MIT License. See [LICENSE](LICENSE) for details.

---

© 2024 Nadeem Iqbal. Please attribute the author in any derivative works.
