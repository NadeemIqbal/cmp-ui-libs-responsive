# Responsive UI Library Usage Examples

This library provides a responsive UI system for Kotlin Multiplatform Compose, similar to Flutter's `responsive_builder` package.

## Basic Usage

### Simple Responsive View

```kotlin
import com.nadeem.responsiveui.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MyResponsiveScreen() {
    ResponsiveView(
        mobile = {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Mobile Layout")
                Text("Single column design")
            }
        },
        tablet = {
            Row(modifier = Modifier.padding(24.dp)) {
                Text("Tablet Layout")
                Spacer(modifier = Modifier.width(16.dp))
                Text("Two column design")
            }
        },
        desktop = {
            Row(modifier = Modifier.padding(32.dp)) {
                Text("Desktop Layout")
                Spacer(modifier = Modifier.width(24.dp))
                Text("Wide layout with")
                Spacer(modifier = Modifier.width(24.dp))
                Text("multiple columns")
            }
        }
    )
}
```

### Custom Breakpoints

```kotlin
@Composable
fun MyCustomResponsiveScreen() {
    ResponsiveView(
        breakpoints = ScreenBreakpoints(
            mobile = 480,
            tablet = 768,
            desktop = 1024
        ),
        mobile = { MobileContent() },
        tablet = { TabletContent() },
        desktop = { DesktopContent() }
    )
}
```

### Advanced Usage with ScreenTypeLayoutBuilder

```kotlin
@Composable
fun AdvancedResponsiveScreen() {
    ScreenTypeLayoutBuilder.builder(
        breakpoints = ScreenBreakpoints(
            tablet = 600,
            desktop = 1000,
            watch = 300
        ),
        mobile = {
            // Update device config (similar to Flutter example)
            LaunchedEffect(Unit) {
                // Device config is automatically updated
            }
            MobileView()
        },
        tablet = {
            TabletView()
        },
        desktop = {
            DesktopView()
        }
    )
}
```

### Conditional Rendering Based on Screen Type

```kotlin
@Composable
fun ConditionalContent() {
    Column {
        Text("Always visible content")
        
        // Show only on mobile and tablet
        ShowOnScreenType(
            screenTypes = listOf(ScreenType.Mobile, ScreenType.Tablet)
        ) {
            Text("Mobile/Tablet only content")
        }
        
        // Show only on desktop
        ShowOnScreenType(
            screenTypes = listOf(ScreenType.Desktop)
        ) {
            Text("Desktop only content")
        }
    }
}
```

### Getting Current Screen Type

```kotlin
@Composable
fun ScreenTypeAwareContent() {
    val currentScreenType = getScreenType()
    
    when (currentScreenType) {
        is ScreenType.Mobile -> {
            Text("Current: Mobile")
        }
        is ScreenType.Tablet -> {
            Text("Current: Tablet")
        }
        is ScreenType.Desktop -> {
            Text("Current: Desktop")
        }
        is ScreenType.Watch -> {
            Text("Current: Watch")
        }
    }
}
```

### Using Device Configuration

```kotlin
@Composable
fun DeviceInfoScreen() {
    val screenWidth = DeviceConfig.screenWidth
    val screenHeight = DeviceConfig.screenHeight
    
    Column {
        Text("Screen Width: ${screenWidth}dp")
        Text("Screen Height: ${screenHeight}dp")
    }
}
```

## Screen Breakpoints

The default breakpoints are:
- **Watch**: < 300dp
- **Mobile**: 300dp - 599dp  
- **Tablet**: 600dp - 899dp
- **Desktop**: >= 900dp

You can customize these breakpoints by providing your own `ScreenBreakpoints` configuration.

## Platform Support

Currently supports:
- ✅ Android (using `LocalConfiguration`)
- ✅ JavaScript/Web (using window APIs)
- 🚧 Desktop and iOS support coming soon

## Migration from Flutter responsive_builder

This library provides similar functionality to Flutter's `responsive_builder`:

### Flutter (responsive_builder)
```dart
ScreenTypeLayout.builder(
  mobile: (context) => MobileView(),
  tablet: (context) => TabletView(),
  desktop: (context) => DesktopView(),
)
```

### Kotlin Multiplatform (this library)
```kotlin
ScreenTypeLayoutBuilder.builder(
  mobile = { MobileView() },
  tablet = { TabletView() },
  desktop = { DesktopView() }
)
```

The API is designed to be familiar for developers coming from Flutter's `responsive_builder` package. 