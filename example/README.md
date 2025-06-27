# Responsive UI Library - Example App

This is a comprehensive multiplatform example application demonstrating the usage of the Responsive UI Library across Android, iOS, Desktop, and Web platforms.

## 🚀 Features Demonstrated

The example app showcases 6 different examples of using the responsive UI library:

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

## 🏃‍♂️ How to Run

### Android
```bash
./gradlew :example:installDebug
```

### Desktop
```bash
./gradlew :example:runDistributable
```

### Web (JS)
```bash
./gradlew :example:jsBrowserDevelopmentRun
```

### iOS
1. Open in Xcode
2. Build and run the iOS target

## 🛠️ Project Structure

```
example/
├── src/
│   ├── commonMain/kotlin/               # Shared UI code
│   │   └── com/nadeem/responsiveui/example/
│   │       └── ExampleApp.kt           # Main example composable
│   ├── androidMain/                    # Android-specific code
│   │   ├── AndroidManifest.xml
│   │   ├── kotlin/.../MainActivity.kt
│   │   └── res/                        # Android resources
│   ├── desktopMain/kotlin/             # Desktop-specific code
│   │   └── .../Main.kt
│   ├── jsMain/                         # Web-specific code
│   │   ├── kotlin/.../Main.kt
│   │   └── resources/index.html
│   └── iosMain/kotlin/                 # iOS-specific code
│       └── .../MainViewController.kt
└── build.gradle.kts                    # Build configuration
```

## 📱 Testing Different Screen Sizes

- **Mobile**: Use phone emulator or resize window to < 600dp
- **Tablet**: Use tablet emulator or resize window to 600-900dp  
- **Desktop**: Use large screen or resize window to > 900dp

## 🎯 Purpose

This example demonstrates how to:
- Create responsive layouts that adapt to different screen sizes
- Use custom breakpoints for different device categories
- Conditionally show/hide content based on screen type
- Detect current screen type at runtime
- Access device configuration information
- Share UI code across multiple platforms (Android, iOS, Desktop, Web)

## 📚 Documentation

For more information about the Responsive UI Library, see:
- [Library README](../README.md)
- [Consumer Guide](../CONSUMER_GUIDE.md)
- [Usage Examples](../USAGE_EXAMPLE.md) 