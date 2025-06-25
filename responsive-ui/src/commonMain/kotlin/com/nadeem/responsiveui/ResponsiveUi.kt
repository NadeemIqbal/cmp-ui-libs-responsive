package com.nadeem.responsiveui

sealed class DeviceType {
    object Mobile : DeviceType()
    object Tablet : DeviceType()
    object Desktop : DeviceType()
    object Unknown : DeviceType()
}

expect fun getDeviceType(): DeviceType 