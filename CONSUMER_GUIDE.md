# Responsive UI Library - Consumer Guide

This guide shows you how to consume the Responsive UI Library in different types of projects.

## 📱 Android Project Integration

### Step 1: Add the Library Dependency

#### Option A: Local Module (Recommended for Development)
```kotlin
// settings.gradle.kts
include(":responsive-ui")

// app/build.gradle.kts
dependencies {
    implementation(project(":responsive-ui"))
    // ... other dependencies
}
```

#### Option B: Remote Repository (When Published)
```kotlin
// app/build.gradle.kts
dependencies {
    implementation("com.nadeem:responsive-ui:1.0.0")
    // ... other dependencies
}
```

### Step 2: Import and Use

```kotlin
import com.nadeem.responsiveui.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MyResponsiveScreen() {
    ResponsiveView(
        mobile = {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("📱 Mobile Layout")
                Text("Single column design")
            }
        },
        tablet = {
            Row(modifier = Modifier.padding(24.dp)) {
                Text("📱 Tablet Layout")
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

## 🌐 Kotlin Multiplatform Project Integration

### Step 1: Add to Multiplatform Project

```kotlin
// settings.gradle.kts
include(":responsive-ui")

// build.gradle.kts (root)
kotlin {
    androidTarget()
    js(IR) { browser() }
    jvm("desktop")
    
    sourceSets {
        commonMain {
            dependencies {
                implementation(project(":responsive-ui"))
            }
        }
    }
}
```

### Step 2: Use in Common Code

```kotlin
// commonMain/kotlin/MyApp.kt
import com.nadeem.responsiveui.*
import androidx.compose.runtime.Composable

@Composable
fun SharedResponsiveLayout() {
    ResponsiveView(
        mobile = { MobileContent() },
        tablet = { TabletContent() },
        desktop = { DesktopContent() }
    )
}
```

## 🎯 Complete Example Project

Here's a complete example of how to set up a project that uses the responsive UI library:

### Project Structure
```
my-responsive-app/
├── app/
│   ├── build.gradle.kts
│   └── src/main/kotlin/
│       └── com/example/myapp/
│           ├── MainActivity.kt
│           └── ResponsiveScreens.kt
├── responsive-ui/          # Library module
├── build.gradle.kts
├── settings.gradle.kts
└── README.md
```

### settings.gradle.kts
```kotlin
pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "my-responsive-app"
include(":app")
include(":responsive-ui")
```

### build.gradle.kts (Root)
```kotlin
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.compose") version "1.8.2" apply false
    id("org.jetbrains.kotlin.android") version "2.2.0" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
```

### app/build.gradle.kts
```kotlin
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.compose")
}

android {
    namespace = "com.example.myapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myapp"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }
    
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.8"
    }
}

dependencies {
    implementation(project(":responsive-ui"))
    
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation(platform("androidx.compose:compose-bom:2024.02.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
}
```

### MainActivity.kt
```kotlin
package com.example.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ResponsiveScreens()
                }
            }
        }
    }
}
```

### ResponsiveScreens.kt
```kotlin
package com.example.myapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nadeem.responsiveui.*

@Composable
fun ResponsiveScreens() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Example 1: Basic ResponsiveView
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Basic ResponsiveView",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                
                ResponsiveView(
                    mobile = { MobileLayout() },
                    tablet = { TabletLayout() },
                    desktop = { DesktopLayout() }
                )
            }
        }

        // Example 2: Custom Breakpoints
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Custom Breakpoints",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                
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
            }
        }

        // Example 3: Screen Type Detection
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Screen Type Detection",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )
                
                ScreenTypeAwareContent()
            }
        }
    }
}

// Layout Components
@Composable
fun MobileLayout() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("📱 Mobile Layout", fontWeight = FontWeight.Bold)
        Text("Single column design")
    }
}

@Composable
fun TabletLayout() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("📱 Tablet Layout", fontWeight = FontWeight.Bold)
        Text("Two column design")
    }
}

@Composable
fun DesktopLayout() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("Desktop Layout", fontWeight = FontWeight.Bold)
        Text("Wide layout with multiple columns")
    }
}

@Composable
fun CustomMobileLayout() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("📱 Custom Mobile", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text("Breakpoint: 480dp")
    }
}

@Composable
fun CustomTabletLayout() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("📱 Custom Tablet", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text("Breakpoint: 768dp")
    }
}

@Composable
fun CustomDesktopLayout() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Text("🖥️ Custom Desktop", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text("Breakpoint: 1024dp")
    }
}

@Composable
fun ScreenTypeAwareContent() {
    val currentScreenType = getScreenType()
    
    when (currentScreenType) {
        is ScreenType.Mobile -> {
            Text("Current: 📱 Mobile", color = MaterialTheme.colorScheme.primary)
        }
        is ScreenType.Tablet -> {
            Text("Current: 📱 Tablet", color = MaterialTheme.colorScheme.primary)
        }
        is ScreenType.Desktop -> {
            Text("Current: 🖥️ Desktop", color = MaterialTheme.colorScheme.primary)
        }
        is ScreenType.Watch -> {
            Text("Current: ⌚ Watch", color = MaterialTheme.colorScheme.primary)
        }
    }
}
```

## 🚀 Quick Start Commands

### 1. Clone and Setup
```bash
# Clone the library
git clone https://github.com/NadeemIqbal/cmp-ui-libs-responsive.git
cd cmp-ui-libs-responsive

# Copy the responsive-ui module to your project
cp -r responsive-ui /path/to/your/project/

# Add to your settings.gradle.kts
echo 'include(":responsive-ui")' >> settings.gradle.kts

# Add dependency to your app/build.gradle.kts
echo 'implementation(project(":responsive-ui"))' >> app/build.gradle.kts
```

### 2. Build and Test
```bash
# Build the project
./gradlew build

# Run on Android
./gradlew :app:installDebug

# Run on Web (if configured)
./gradlew :app:jsBrowserDevelopmentRun
```

## 🎯 Testing Different Screen Sizes

### Android Emulator
- **Mobile**: Phone emulator (e.g., Pixel 4)
- **Tablet**: Tablet emulator (e.g., Nexus 9)
- **Desktop**: Large tablet emulator (e.g., Nexus 10)

### Web Browser
- **Mobile**: Resize browser to < 600px width
- **Tablet**: Resize browser to 600-900px width
- **Desktop**: Resize browser to > 900px width

### Real Devices
- **Mobile**: Smartphones in portrait/landscape
- **Tablet**: Tablets in portrait/landscape
- **Desktop**: Large tablets or desktop browsers

## 🔧 Advanced Usage

### Custom Breakpoints for Your Design System
```kotlin
val myBreakpoints = ScreenBreakpoints(
    mobile = 375,    // iPhone SE width
    tablet = 768,    // iPad width
    desktop = 1024   // Desktop minimum
)

ResponsiveView(
    breakpoints = myBreakpoints,
    mobile = { /* Your mobile layout */ },
    tablet = { /* Your tablet layout */ },
    desktop = { /* Your desktop layout */ }
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

### Device Configuration
```kotlin
val screenWidth = DeviceConfig.screenWidth
val screenHeight = DeviceConfig.screenHeight

// Use these values for custom calculations
val isLandscape = screenWidth > screenHeight
```

## 🐛 Troubleshooting

### Common Issues

1. **Build Errors**: Make sure you have the correct Compose and Kotlin versions
2. **Import Issues**: Ensure you're importing `com.nadeem.responsiveui.*`
3. **Screen Size Detection**: Check that your emulator/device has the correct screen size
4. **Gradle Sync**: Run `./gradlew clean build` to refresh dependencies

### Version Compatibility
- **Kotlin**: 1.9.22+
- **Compose**: 1.6.0+
- **Android Gradle Plugin**: 8.2.2+
- **Min SDK**: 24+

## 📚 Additional Resources

- [Library Documentation](USAGE_EXAMPLE.md)
- [API Reference](README.md)
- [GitHub Repository](https://github.com/NadeemIqbal/cmp-ui-libs-responsive)

## 🤝 Support

If you encounter any issues:
1. Check the troubleshooting section above
2. Review the example code in this guide
3. Open an issue on GitHub with your specific problem
4. Include your build.gradle.kts files and error messages 