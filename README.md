# Responsive UI Library for Kotlin Multiplatform

[![Maven Central](https://img.shields.io/maven-central/v/com.nadeemiqbal/responsive-ui.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.nadeemiqbal%22%20AND%20a:%22responsive-ui%22)
[![Kotlin](https://img.shields.io/badge/kotlin-1.9.22-blue.svg?logo=kotlin)](http://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.6.0-blue)](https://github.com/JetBrains/compose-multiplatform)

A **Kotlin Multiplatform Compose** library that provides Flutter-like responsive layouts for Android, Desktop, iOS, and Web platforms. Build adaptive UIs that automatically adjust to different screen sizes with a familiar, declarative API.

## ✨ Key Features

- 🎯 **Flutter-like API**: Familiar responsive design patterns
- 🚀 **Kotlin Multiplatform**: Android, Desktop, iOS, and Web support
- 📱 **Adaptive Layouts**: Automatic screen size detection and adaptation
- 🎨 **Compose Native**: Built with Jetpack/Compose Multiplatform
- ⚡ **Type-safe**: Compile-time safety with sealed classes
- 🔧 **Customizable**: Configurable breakpoints for any design system

## 🛠️ Supported Platforms

| Platform | Status | Notes |
|----------|--------|-------|
| 🤖 **Android** | ✅ Working | Full support with Compose |
| 🖥️ **Desktop** | ✅ Working | JVM/Compose Desktop |
| 🍎 **iOS** | ✅ Working | Compose Multiplatform |
| 🌐 **Web (WasmJS)** | ✅ Working | Kotlin/Wasm + Compose |

## 📦 Installation

### Via JitPack (Recommended - No GPG Required!)

Add JitPack repository to your project:

**In your root `build.gradle.kts`:**
```kotlin
allprojects {
    repositories {
        google()
        mavenCentral()
        maven("https://jitpack.io") // Add this line
    }
}
```

**In your module `build.gradle.kts`:**
```kotlin
dependencies {
    implementation("com.github.NadeemIqbal:cmp-ui-libs-responsive:v1.0.11")
}
```

### Via Maven Central (When Available)
```kotlin
dependencies {
    implementation("io.github.nadeemiqbal:responsive-ui:1.0.11")
}
```

## 🚀 Quick Start

### Basic Responsive Layout
```kotlin
ResponsiveView(
    mobile = { 
        Text("Mobile Layout") 
    },
    tablet = { 
        Text("Tablet Layout") 
    },
    desktop = { 
        Text("Desktop Layout") 
    }
)
```

### Screen Type Detection
```kotlin
val screenType = getScreenType()
when (screenType) {
    is ScreenType.Mobile -> Text("📱 Mobile")
    is ScreenType.Tablet -> Text("📺 Tablet") 
    is ScreenType.Desktop -> Text("🖥️ Desktop")
    is ScreenType.Watch -> Text("⌚ Watch")
}
```

### Conditional Content
```kotlin
ShowOnScreenType(
    screenTypes = listOf(ScreenType.Mobile, ScreenType.Tablet)
) {
    Text("Only visible on mobile and tablet")
}
```

## 🎯 Usage Examples

The library showcases 6 different examples of responsive UI patterns:

### 1. **Basic ResponsiveView**
Simple responsive layout with default breakpoints:
- Mobile: Single column design
- Tablet: Two column design  
- Desktop: Wide layout with multiple columns

### 2. **Custom Breakpoints**
Responsive layout with custom screen size thresholds:
- Mobile: < 480dp
- Tablet: 480dp - 767dp
- Desktop: ≥ 768dp

### 3. **ScreenTypeLayoutBuilder**
Advanced builder pattern usage with custom breakpoints:
- Mobile: < 600dp
- Tablet: 600dp - 999dp
- Desktop: ≥ 1000dp

### 4. **Conditional Rendering**
Show/hide content based on screen type:
- Mobile/Tablet only content
- Desktop only content

### 5. **Screen Type Detection**
Get current screen type and display it dynamically

### 6. **Device Configuration**
Display screen dimensions and aspect ratio

## 📱 Running the Example App

### Prerequisites
- **Android Studio** Flamingo or later
- **Android SDK** 24+
- **Kotlin** 1.9.22+
- **Node.js** 18+ (for Web/WasmJS)

### Platform-Specific Commands

#### 🤖 Android
```bash
./gradlew :example:assembleDebug
./gradlew :example:installDebug
```

#### 🖥️ Desktop (Compose Desktop)
```bash
./gradlew :example:runDistributable
```

#### 🌐 Web (WasmJS)
```bash
./gradlew :example:wasmJsBrowserDevelopmentRun
# Opens browser at http://localhost:8080
```

#### 🍎 iOS
Open `iosApp/iosApp.xcodeproj` in Xcode and run the project.

### Testing Different Screen Sizes
- **Resize your browser window** (Web)
- **Use device emulators** (Android/iOS)
- **Resize the desktop window** (Desktop)
- **Use browser developer tools** to simulate different devices

## 🛠️ Project Structure

```
cmp-library-responsive-ui/
├── responsive-ui/                 # 📚 Core library module
│   └── src/
│       ├── commonMain/            # Common multiplatform code
│       ├── androidMain/           # Android-specific implementations
│       ├── desktopMain/           # Desktop-specific implementations  
│       ├── iosMain/               # iOS-specific implementations
│       ├── jsMain/                # JavaScript/Web implementations
│       └── wasmJsMain/            # WebAssembly implementations
├── example/                       # 📱 Example application
│   └── src/
│       ├── commonMain/            # Shared example code
│       ├── androidMain/           # Android example app
│       ├── desktopMain/           # Desktop example app
│       ├── iosMain/               # iOS example app
│       └── wasmJsMain/            # Web example app
├── build.gradle.kts              # Root build configuration
├── settings.gradle.kts           # Project settings
└── README.md                     # This documentation
```

## 📖 Usage Examples

### Basic Usage
```kotlin
ResponsiveView(
    mobile = { MobileLayout() },
    tablet = { TabletLayout() },
    desktop = { DesktopLayout() }
)
```

### Custom Breakpoints
```kotlin
ResponsiveView(
    breakpoints = ScreenBreakpoints(
        mobile = 480,
        tablet = 768,
        desktop = 1024
    ),
    mobile = { CustomMobileLayout() },
    tablet = { CustomTabletLayout() },
    desktop = { CustomDesktopLayout() }
)
```

### Builder Pattern
```kotlin
ScreenTypeLayoutBuilder.builder(
    breakpoints = ScreenBreakpoints(
        tablet = 600,
        desktop = 1000
    ),
    mobile = { MobileView() },
    tablet = { TabletView() },
    desktop = { DesktopView() }
)
```

### Conditional Rendering
```kotlin
ShowOnScreenType(
    screenTypes = listOf(ScreenType.Mobile, ScreenType.Tablet)
) {
    Text("Mobile/Tablet only content")
}
```

### Screen Type Detection
```kotlin
val currentScreenType = getScreenType()
when (currentScreenType) {
    is ScreenType.Mobile -> { /* Mobile logic */ }
    is ScreenType.Tablet -> { /* Tablet logic */ }
    is ScreenType.Desktop -> { /* Desktop logic */ }
}
```

## 🎯 Key Benefits

1. **Flutter-like API**: Familiar for Flutter developers
2. **Type-safe**: Compile-time safety with sealed classes
3. **Customizable**: Configurable breakpoints for any design system
4. **Cross-platform**: Works on Android and JavaScript/Web
5. **Composable-friendly**: Built with Compose best practices
6. **Backward compatible**: Maintains support for existing code

## 🔧 Customization

### Default Breakpoints
- **Watch**: < 300dp
- **Mobile**: 300dp - 599dp
- **Tablet**: 600dp - 899dp
- **Desktop**: ≥ 900dp

### Custom Breakpoints
You can define your own breakpoints based on your design system:

```kotlin
ScreenBreakpoints(
    mobile = 480,    // Your mobile breakpoint
    tablet = 768,    // Your tablet breakpoint
    desktop = 1024   // Your desktop breakpoint
)
```

## 📚 Documentation

For more detailed documentation and advanced usage examples, see:
- [Library Documentation](../cmp-library-responsive-ui/USAGE_EXAMPLE.md)
- [API Reference](../cmp-library-responsive-ui/README.md)

## 📊 Default Breakpoints

| Screen Type | Width Range | Typical Devices |
|-------------|-------------|-----------------|
| ⌚ **Watch** | < 300dp | Smart watches |
| 📱 **Mobile** | 300dp - 599dp | Phones |
| 📺 **Tablet** | 600dp - 899dp | Tablets |
| 🖥️ **Desktop** | ≥ 900dp | Desktops, Large tablets |

### Custom Breakpoints
```kotlin
ScreenBreakpoints(
    mobile = 480,    // Your mobile breakpoint
    tablet = 768,    // Your tablet breakpoint  
    desktop = 1024   // Your desktop breakpoint
)
```

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

### Development Setup
1. Clone the repository
2. Open in Android Studio or IntelliJ IDEA
3. Run `./gradlew build` to ensure everything compiles
4. Run platform-specific examples to test changes

## 📄 License

```
MIT License

Copyright (c) 2024 Nadeem Iqbal

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```

---

⭐ **Star this repo** if you find it helpful!

🐛 **Found a bug?** [Open an issue](https://github.com/NadeemIqbal/responsive-ui/issues)

💡 **Have an idea?** [Start a discussion](https://github.com/NadeemIqbal/responsive-ui/discussions)
