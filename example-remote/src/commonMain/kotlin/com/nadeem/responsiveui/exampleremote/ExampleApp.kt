package com.nadeem.responsiveui.exampleremote

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.nadeem.responsiveui.*

@Composable
public fun ResponsiveUIExampleApp() {
    MaterialTheme {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 30.dp, bottom = 20.dp, end = 30.dp)
            .verticalScroll(rememberScrollState()),
    ) {
        // Header
        Text(
            text = "Responsive UI Library Demo (Remote Dependency)",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 16.dp),
        )

        // Example 1: Basic ResponsiveView
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Example 1: Basic ResponsiveView",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp),
                )

                ResponsiveView(
                    mobile = {
                        MobileLayout()
                    },
                    tablet = {
                        TabletLayout()
                    },
                    desktop = {
                        DesktopLayout()
                    },
                )
            }
        }

        // Example 2: Custom Breakpoints
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Example 2: Custom Breakpoints",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp),
                )

                ResponsiveView(
                    breakpoints = ScreenBreakpoints(
                        mobile = 480,
                        tablet = 768,
                        desktop = 1024,
                    ),
                    mobile = {
                        CustomMobileLayout()
                    },
                    tablet = {
                        CustomTabletLayout()
                    },
                    desktop = {
                        CustomDesktopLayout()
                    },
                )
            }
        }

        // Example 3: ScreenTypeLayoutBuilder
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Example 3: ScreenTypeLayoutBuilder",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp),
                )

                ScreenTypeLayoutBuilder.builder(
                    breakpoints = ScreenBreakpoints(
                        tablet = 600,
                        desktop = 1000,
                        watch = 300,
                    ),
                    mobile = {
                        BuilderMobileLayout()
                    },
                    tablet = {
                        BuilderTabletLayout()
                    },
                    desktop = {
                        BuilderDesktopLayout()
                    },
                )
            }
        }

        // Example 4: Conditional Rendering
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Example 4: Conditional Rendering",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp),
                )

                Text("Always visible content")

                ShowOnScreenType(
                    screenTypes = listOf(ScreenType.Mobile, ScreenType.Tablet),
                ) {
                    Text(
                        "Mobile/Tablet only content",
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                ShowOnScreenType(
                    screenTypes = listOf(ScreenType.Desktop),
                ) {
                    Text(
                        "Desktop only content",
                        color = MaterialTheme.colorScheme.secondary,
                    )
                }
            }
        }

        // Example 5: Screen Type Detection
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Example 5: Screen Type Detection",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp),
                )

                ScreenTypeAwareContent()
            }
        }

        // Example 6: Device Configuration
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "Example 6: Device Configuration",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(bottom = 8.dp),
                )

                DeviceInfoScreen()
            }
            }
        }
    }
}

// Layout Components
@Composable
private fun MobileLayout() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Mobile Layout", fontWeight = FontWeight.Bold)
        Text("Single column design")
        Text("Compact spacing")
    }
}

@Composable
private fun TabletLayout() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Text("Tablet Layout", fontWeight = FontWeight.Bold)
        Text("Two column design")
    }
}

@Composable
private fun DesktopLayout() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Text("Desktop Layout", fontWeight = FontWeight.Bold)
        Text("Wide layout with")
        Text("multiple columns")
    }
}

@Composable
private fun CustomMobileLayout() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Custom Mobile", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text("Breakpoint: 480dp")
    }
}

@Composable
private fun CustomTabletLayout() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Text("Custom Tablet", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text("Breakpoint: 768dp")
    }
}

@Composable
private fun CustomDesktopLayout() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Text("Custom Desktop", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
        Text("Breakpoint: 1024dp")
    }
}

@Composable
private fun BuilderMobileLayout() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text("Builder Mobile", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
        Text("Using ScreenTypeLayoutBuilder")
    }
}

@Composable
private fun BuilderTabletLayout() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Text("Builder Tablet", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
        Text("Using ScreenTypeLayoutBuilder")
    }
}

@Composable
private fun BuilderDesktopLayout() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
    ) {
        Text("Builder Desktop", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.secondary)
        Text("Using ScreenTypeLayoutBuilder")
    }
}

@Composable
private fun ScreenTypeAwareContent() {
    val currentScreenType = getScreenType()

    when (currentScreenType) {
        is ScreenType.Mobile -> {
            Text("Current: Mobile", color = MaterialTheme.colorScheme.primary)
        }

        is ScreenType.Tablet -> {
            Text("Current: Tablet", color = MaterialTheme.colorScheme.primary)
        }

        is ScreenType.Desktop -> {
            Text("Current: Desktop", color = MaterialTheme.colorScheme.primary)
        }

        is ScreenType.Watch -> {
            Text("Current: Watch", color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
private fun DeviceInfoScreen() {
    val screenWidth = DeviceConfig.screenWidth
    val screenHeight = DeviceConfig.screenHeight

    Column {
        Text("Screen Width: ${screenWidth}dp")
        Text("Screen Height: ${screenHeight}dp")
        Text("Aspect Ratio: ${if (screenHeight > 0) screenWidth.toFloat() / screenHeight.toFloat() else 0f}")
    }
} 