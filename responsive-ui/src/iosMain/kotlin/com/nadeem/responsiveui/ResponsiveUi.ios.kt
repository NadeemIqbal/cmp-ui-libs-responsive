package com.nadeem.responsiveui

import platform.UIKit.UIScreen

actual fun getDeviceType(): DeviceType {
    val width = UIScreen.mainScreen.bounds.size.width
    return when {
        width < 600.0 -> DeviceType.Mobile
        width < 900.0 -> DeviceType.Tablet
        else -> DeviceType.Desktop
    }
} 