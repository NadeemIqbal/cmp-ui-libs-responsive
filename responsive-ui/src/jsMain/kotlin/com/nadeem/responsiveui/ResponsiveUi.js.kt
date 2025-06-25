package com.nadeem.responsiveui

import kotlin.js.Date
import org.w3c.dom.Window
import org.w3c.dom.get
import kotlin.browser.window

actual fun getDeviceType(): DeviceType {
    val width = window.innerWidth
    return when {
        width < 600 -> DeviceType.Mobile
        width < 900 -> DeviceType.Tablet
        else -> DeviceType.Desktop
    }
} 