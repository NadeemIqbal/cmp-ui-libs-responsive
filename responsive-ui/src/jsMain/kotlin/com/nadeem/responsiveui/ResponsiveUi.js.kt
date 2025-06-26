package com.nadeem.responsiveui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@JsName("window")
private external object Window {
    val innerWidth: Int
    val innerHeight: Int
}

private external val window: Window

@Composable
public actual fun getScreenWidth(): Int {
    return remember {
        // Convert pixels to approximate dp (assuming 96 DPI)
        (window.innerWidth * 160 / 96)
    }
}

@Composable
public actual fun getScreenHeight(): Int {
    return remember {
        // Convert pixels to approximate dp (assuming 96 DPI)
        (window.innerHeight * 160 / 96)
    }
}

@Suppress("DEPRECATION")
public actual fun getDeviceType(): DeviceType {
    val width = window.innerWidth
    return when {
        width < 600 -> DeviceType.Mobile
        width < 900 -> DeviceType.Tablet
        else -> DeviceType.Desktop
    }
} 