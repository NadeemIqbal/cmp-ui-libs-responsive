package com.nadeem.responsiveui

import android.content.res.Resources
import android.util.DisplayMetrics

actual fun getDeviceType(): DeviceType {
    val metrics: DisplayMetrics = Resources.getSystem().displayMetrics
    val widthDp = metrics.widthPixels / metrics.density
    return when {
        widthDp < 600 -> DeviceType.Mobile
        widthDp < 840 -> DeviceType.Tablet
        else -> DeviceType.Desktop
    }
} 