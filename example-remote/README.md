# Responsive UI Example (Remote Dependency)

This example demonstrates how to use the Responsive UI library from a remote dependency using JitPack.

## Remote Dependency

Instead of using a local project reference, this example uses the published library from JitPack:

```kotlin
implementation("com.github.NadeemIqbal.cmp-ui-libs-responsive:responsive-ui:v0.0.6")
```

## Supported Platforms

- **Android** - Native Android application
- **Desktop** - JVM desktop application using Compose Desktop  

> **Note**: iOS platform is not available in the remote dependency version v0.0.6. Android, Desktop, and JavaScript/Web platforms are supported.

## Running the Examples

### Android
```bash
./gradlew :example-remote:installDebug
```

### Desktop
```bash
./gradlew :example-remote:run
```

### JavaScript/Web
```bash
./gradlew :example-remote:jsBrowserDevelopmentRun
```

## What's Demonstrated

The example showcases all the key features of the Responsive UI library:

1. **Basic ResponsiveView** - Automatic layout switching based on screen size
2. **Custom Breakpoints** - Define your own breakpoints for different screen sizes
3. **ScreenTypeLayoutBuilder** - Alternative API for building responsive layouts
4. **Conditional Rendering** - Show/hide content based on screen type
5. **Screen Type Detection** - Programmatically detect current screen type
6. **Device Configuration** - Access device screen dimensions and properties

## Key Differences from Local Example

- Uses remote dependency from JitPack instead of local project reference
- Updated package name: `com.nadeem.responsiveui.exampleremote`
- Updated app titles to indicate "Remote Dependency"
- All functionality remains identical to demonstrate library compatibility 