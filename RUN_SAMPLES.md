# Running the Responsive UI Sample Applications

This project includes sample applications for **Desktop (Windows)** and **Web** platforms, in addition to the existing Android sample.

## Prerequisites

- **Java 11+** (for desktop and build system)
- **Modern web browser** (for web demo)

## Available Sample Apps

### 1. Android Sample ✅
**Location:** `app/` module  
**Run command:**
```bash
./gradlew :app:installDebug
# Then launch the "Responsive UI Sample" app on your Android device/emulator
```

### 2. Desktop Sample ✅ 
**Location:** `desktop-sample/` module  
**Run command:**
```bash
./gradlew :desktop-sample:run
```

This will launch a native desktop window with the responsive UI demo.

### 3. Web Demo ✅ (Recommended)
**Location:** `web-demo/index.html`  
**Run:** Simply open the HTML file in any web browser

This is a **pure HTML/CSS/JavaScript demo** that shows the same responsive behavior as the Kotlin samples. It demonstrates:
- Responsive breakpoints (Mobile < 600px, Tablet 600-899px, Desktop ≥ 900px)
- Real-time screen type detection
- Device information display
- Custom breakpoint examples

**To test:** Resize your browser window to see different layouts!

### 4. Web Sample (Kotlin/JS) ⚠️
**Location:** `web-sample/` module  
**Status:** Currently has build configuration issues with Node.js setup

If you want to try the Kotlin/JS version, you may need to:
1. Install Node.js locally
2. Configure repository settings
3. Run: `./gradlew :web-sample:jsBrowserDevelopmentRun`

## What the Samples Show

All samples demonstrate the same responsive UI features:

1. **Basic ResponsiveView** - Automatic layout switching based on screen size
2. **Custom Breakpoints** - Using custom screen size thresholds
3. **ScreenTypeLayoutBuilder** - Builder pattern for responsive layouts  
4. **Conditional Rendering** - Show/hide content based on screen type
5. **Screen Type Detection** - Runtime detection of current screen type
6. **Device Configuration** - Access to screen dimensions and aspect ratio

## Quick Start

**🚀 Fastest way to see the responsive UI in action:**

1. **Desktop:** Run `./gradlew :desktop-sample:run`
2. **Web:** Open `web-demo/index.html` in your browser and resize the window
3. **Android:** Run `./gradlew :app:installDebug` and install on device

## Features Demonstrated

- **Mobile Layout**: Single column, compact spacing
- **Tablet Layout**: Two-column design with medium spacing  
- **Desktop Layout**: Multi-column layout with wide spacing
- **Responsive Breakpoints**: Mobile (<600dp), Tablet (600-900dp), Desktop (>900dp)
- **Custom Breakpoints**: Configurable screen size thresholds
- **Dynamic Adaptation**: Real-time layout changes when resizing windows

## Troubleshooting

**Desktop Sample Issues:**
- Ensure Java 11+ is installed
- Try running `./gradlew clean :desktop-sample:run`

**Web Demo Issues:**
- Any modern browser should work
- Enable JavaScript if disabled

**Web Sample (Kotlin/JS) Issues:**
- This requires Node.js and complex setup
- Use the HTML web demo instead for quick testing

**Build Issues:**
- Run `./gradlew clean build` to clean and rebuild all modules
- Check that all dependencies are properly downloaded

## Project Structure

```
├── app/                    # Android sample app
├── responsive-ui/          # Core responsive UI library (KMP)
├── desktop-sample/         # Desktop sample app (Compose Desktop)
├── web-sample/            # Web sample app (Kotlin/JS) - Build issues
├── web-demo/              # Web demo (HTML/CSS/JS) - ✅ Working
└── README.md              # Project documentation
```

The core `responsive-ui` library is built with **Kotlin Multiplatform (KMP)** and **Compose Multiplatform (CMP)**, allowing the same responsive UI logic to work across Android, Desktop, and Web platforms.

## Summary

**✅ Working Samples:**
- Android (Compose Android)
- Desktop (Compose Desktop) 
- Web Demo (HTML/CSS/JS)

**⚠️ In Progress:**
- Web Sample (Kotlin/JS + Compose Web) - has build configuration challenges

The HTML web demo provides the same functionality and is the easiest way to test responsive behavior in a browser! 