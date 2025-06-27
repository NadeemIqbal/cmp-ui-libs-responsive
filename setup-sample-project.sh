#!/bin/bash

# Responsive UI Library - Sample Project Setup Script
# This script creates a complete sample project that demonstrates the responsive UI library

set -e

echo "🚀 Setting up Responsive UI Library Sample Project..."

# Create project directory
PROJECT_NAME="responsive-ui-sample"
echo "📁 Creating project directory: $PROJECT_NAME"
mkdir -p "$PROJECT_NAME"
cd "$PROJECT_NAME"

# Create project structure
echo "📂 Creating project structure..."
mkdir -p app/src/main/kotlin/com/example/responsiveuisample
mkdir -p app/src/main/res/values
mkdir -p app/src/main/res/xml

# Copy responsive-ui module
echo "📦 Copying responsive-ui module..."
cp -r ../responsive-ui .

# Create settings.gradle.kts
echo "⚙️ Creating settings.gradle.kts..."
cat > settings.gradle.kts << 'EOF'
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

rootProject.name = "responsive-ui-sample"
include(":app")
include(":responsive-ui")
EOF

# Create root build.gradle.kts
echo "🔧 Creating root build.gradle.kts..."
cat > build.gradle.kts << 'EOF'
plugins {
    id("com.android.application") version "8.2.2" apply false
    id("org.jetbrains.compose") version "1.8.2" apply false
    id("org.jetbrains.kotlin.android") version "2.2.0" apply false
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
EOF

# Create app/build.gradle.kts
echo "📱 Creating app/build.gradle.kts..."
cat > app/build.gradle.kts << 'EOF'
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.compose")
}

android {
    namespace = "com.example.responsiveuisample"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.responsiveuisample"
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
EOF

# Create AndroidManifest.xml
echo "📄 Creating AndroidManifest.xml..."
cat > app/src/main/AndroidManifest.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.ResponsiveUISample"
        tools:targetApi="31">
        <activity
            android:name=".MainActivity"
            android:exported="true"
            android:theme="@style/Theme.ResponsiveUISample">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
EOF

# Create strings.xml
echo "📝 Creating strings.xml..."
cat > app/src/main/res/values/strings.xml << 'EOF'
<resources>
    <string name="app_name">Responsive UI Sample</string>
</resources>
EOF

# Create themes.xml
echo "🎨 Creating themes.xml..."
cat > app/src/main/res/values/themes.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <style name="Theme.ResponsiveUISample" parent="android:Theme.Material.Light.NoActionBar" />
</resources>
EOF

# Create backup_rules.xml
echo "💾 Creating backup_rules.xml..."
cat > app/src/main/res/xml/backup_rules.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<full-backup-content>
    <!-- Exclude specific files or directories from backup -->
</full-backup-content>
EOF

# Create data_extraction_rules.xml
echo "📊 Creating data_extraction_rules.xml..."
cat > app/src/main/res/xml/data_extraction_rules.xml << 'EOF'
<?xml version="1.0" encoding="utf-8"?>
<data-extraction-rules>
    <cloud-backup>
        <!-- Exclude specific files or directories from cloud backup -->
    </cloud-backup>
    <device-transfer>
        <!-- Exclude specific files or directories from device transfer -->
    </device-transfer>
</data-extraction-rules>
EOF

# Create MainActivity.kt
echo "📱 Creating MainActivity.kt..."
cat > app/src/main/kotlin/com/example/responsiveuisample/MainActivity.kt << 'EOF'
package com.example.responsiveuisample

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
EOF

# Create ResponsiveScreens.kt
echo "🖼️ Creating ResponsiveScreens.kt..."
cat > app/src/main/kotlin/com/example/responsiveuisample/ResponsiveScreens.kt << 'EOF'
package com.example.responsiveuisample

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
EOF

# Copy gradle wrapper
echo "📦 Copying gradle wrapper..."
cp -r ../gradle .
cp ../gradlew .
cp ../gradlew.bat .

# Create README.md
echo "📖 Creating README.md..."
cat > README.md << 'EOF'
# Responsive UI Library Sample Project

This sample project demonstrates how to use the **Responsive UI Library** for Kotlin Multiplatform Compose.

## 🚀 Quick Start

1. **Build the project:**
   ```bash
   ./gradlew build
   ```

2. **Run on Android:**
   ```bash
   ./gradlew :app:installDebug
   ```

3. **Open in Android Studio:**
   - Open the project in Android Studio
   - Sync Gradle files
   - Run on an emulator or device

## 📱 Features Demonstrated

- **Basic ResponsiveView**: Simple responsive layouts
- **Custom Breakpoints**: Custom screen size thresholds
- **Screen Type Detection**: Get current screen type dynamically

## 🎯 Testing Different Screen Sizes

- **Mobile**: Use phone emulator or rotate to portrait
- **Tablet**: Use tablet emulator or resize window
- **Desktop**: Use large tablet emulator or resize window

## 📚 Documentation

For more information, see:
- [Consumer Guide](../CONSUMER_GUIDE.md)
- [Library Documentation](../USAGE_EXAMPLE.md)
- [API Reference](../README.md)
EOF

echo "✅ Sample project created successfully!"
echo ""
echo "🎯 Next steps:"
echo "1. cd $PROJECT_NAME"
echo "2. ./gradlew build"
echo "3. Open in Android Studio"
echo "4. Run on an emulator or device"
echo ""
echo "📚 For more information, see:"
echo "- CONSUMER_GUIDE.md"
echo "- USAGE_EXAMPLE.md"
echo "- README.md" 