package com.nadeem.responsiveui

import kotlin.test.Test
import kotlin.test.assertEquals

class DeviceConfigTest {

    @Test
    fun updateScreenDimensionsRecordsValues() {
        DeviceConfig.updateScreenDimensions(0, 0)
        assertEquals(0, DeviceConfig.screenWidth)
        assertEquals(0, DeviceConfig.screenHeight)

        DeviceConfig.updateScreenDimensions(400, 800)
        assertEquals(400, DeviceConfig.screenWidth)
        assertEquals(800, DeviceConfig.screenHeight)
    }

    @Test
    fun updateScreenDimensionsOverwritesPreviousValues() {
        DeviceConfig.updateScreenDimensions(100, 200)
        DeviceConfig.updateScreenDimensions(1024, 768)
        assertEquals(1024, DeviceConfig.screenWidth)
        assertEquals(768, DeviceConfig.screenHeight)
    }
}
