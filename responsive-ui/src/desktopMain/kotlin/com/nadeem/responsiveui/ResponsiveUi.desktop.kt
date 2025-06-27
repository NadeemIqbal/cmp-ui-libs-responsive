package com.nadeem.responsiveui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import java.awt.Toolkit

@OptIn(ExperimentalComposeUiApi::class)
@Composable
public actual fun getScreenWidth(): Int {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    
    return remember(windowInfo.containerSize.width, density.density) {
        with(density) {
            windowInfo.containerSize.width.toDp().value.toInt()
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
public actual fun getScreenHeight(): Int {
    val windowInfo = LocalWindowInfo.current
    val density = LocalDensity.current
    
    return remember(windowInfo.containerSize.height, density.density) {
        with(density) {
            windowInfo.containerSize.height.toDp().value.toInt()
        }
    }
}

@Suppress("DEPRECATION")
public actual fun getDeviceType(): DeviceType {
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val width = screenSize.width
    return when {
        width < 600 -> DeviceType.Mobile
        width < 900 -> DeviceType.Tablet
        else -> DeviceType.Desktop
    }
} 