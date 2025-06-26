package com.nadeem.responsiveui

import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp

@Composable
public actual fun getScreenWidth(): Int {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp
}

@Composable
public actual fun getScreenHeight(): Int {
    val configuration = LocalConfiguration.current
    return configuration.screenHeightDp
}

@Suppress("DEPRECATION")
public actual fun getDeviceType(): DeviceType {
    val metrics: DisplayMetrics = Resources.getSystem().displayMetrics
    val widthDp = metrics.widthPixels / metrics.density
    return when {
        widthDp < 600 -> DeviceType.Mobile
        widthDp < 840 -> DeviceType.Tablet
        else -> DeviceType.Desktop
    }
} 