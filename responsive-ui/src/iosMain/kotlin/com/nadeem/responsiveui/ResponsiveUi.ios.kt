package com.nadeem.responsiveui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.useContents
import platform.UIKit.UIDevice
import platform.UIKit.UIScreen
import platform.UIKit.UIUserInterfaceIdiomPad
import platform.UIKit.UIUserInterfaceIdiomPhone

@OptIn(ExperimentalForeignApi::class)
@Composable
public actual fun getScreenWidth(): Int = remember {
    UIScreen.mainScreen.bounds.useContents { size.width.toInt() }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
public actual fun getScreenHeight(): Int = remember {
    UIScreen.mainScreen.bounds.useContents { size.height.toInt() }
}

@Suppress("DEPRECATION")
public actual fun getDeviceType(): DeviceType =
    when (UIDevice.currentDevice.userInterfaceIdiom) {
        UIUserInterfaceIdiomPhone -> DeviceType.Mobile
        UIUserInterfaceIdiomPad -> DeviceType.Tablet
        else -> DeviceType.Unknown
    }
