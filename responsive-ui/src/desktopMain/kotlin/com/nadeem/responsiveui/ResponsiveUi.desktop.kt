package com.nadeem.responsiveui

import java.awt.Toolkit

actual fun getDeviceType(): DeviceType {
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    val width = screenSize.width
    return when {
        width < 800 -> DeviceType.Mobile
        width < 1200 -> DeviceType.Tablet
        else -> DeviceType.Desktop
    }
} 