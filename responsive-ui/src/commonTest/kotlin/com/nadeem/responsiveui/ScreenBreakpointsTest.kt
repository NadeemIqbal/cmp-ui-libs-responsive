package com.nadeem.responsiveui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ScreenBreakpointsTest {

    @Test
    fun defaultsMatchDocumentation() {
        val b = ScreenBreakpoints()
        assertEquals(600, b.mobile)
        assertEquals(900, b.tablet)
        assertEquals(1200, b.desktop)
        assertEquals(300, b.watch)
    }

    @Test
    fun customValuesArePreserved() {
        val b = ScreenBreakpoints(mobile = 500, tablet = 800, desktop = 1100, watch = 200)
        assertEquals(500, b.mobile)
        assertEquals(800, b.tablet)
        assertEquals(1100, b.desktop)
        assertEquals(200, b.watch)
    }

    @Test
    fun dataClassEqualityAndCopy() {
        val a = ScreenBreakpoints()
        val b = ScreenBreakpoints()
        assertEquals(a, b)
        assertEquals(a.hashCode(), b.hashCode())

        val c = a.copy(mobile = 400)
        assertNotEquals(a, c)
        assertEquals(400, c.mobile)
        assertEquals(a.tablet, c.tablet)
    }

    // Boundary tests for getScreenType. Locks in current behavior including
    // the known quirk that widths in [tablet, desktop) also return Tablet
    // (line 36 of ResponsiveUi.kt — width < desktop -> ScreenType.Tablet).
    // If the library intends Desktop for that range, fix the implementation
    // and update these tests.

    @Test
    fun belowWatchBreakpointReturnsWatch() {
        val b = ScreenBreakpoints()
        assertEquals(ScreenType.Watch, b.getScreenType(0))
        assertEquals(ScreenType.Watch, b.getScreenType(299))
    }

    @Test
    fun watchBreakpointReturnsMobile() {
        val b = ScreenBreakpoints()
        assertEquals(ScreenType.Mobile, b.getScreenType(300))
        assertEquals(ScreenType.Mobile, b.getScreenType(599))
    }

    @Test
    fun mobileBreakpointReturnsTablet() {
        val b = ScreenBreakpoints()
        assertEquals(ScreenType.Tablet, b.getScreenType(600))
        assertEquals(ScreenType.Tablet, b.getScreenType(899))
    }

    @Test
    fun tabletBreakpointAlsoReturnsTablet_currentBehavior() {
        // 900..1199 returns Tablet, not Desktop, per ResponsiveUi.kt:36.
        val b = ScreenBreakpoints()
        assertEquals(ScreenType.Tablet, b.getScreenType(900))
        assertEquals(ScreenType.Tablet, b.getScreenType(1199))
    }

    @Test
    fun desktopBreakpointReturnsDesktop() {
        val b = ScreenBreakpoints()
        assertEquals(ScreenType.Desktop, b.getScreenType(1200))
        assertEquals(ScreenType.Desktop, b.getScreenType(10_000))
    }

    @Test
    fun customBreakpointsAreRespected() {
        val b = ScreenBreakpoints(mobile = 500, tablet = 800, desktop = 1100, watch = 250)
        assertEquals(ScreenType.Watch, b.getScreenType(249))
        assertEquals(ScreenType.Mobile, b.getScreenType(250))
        assertEquals(ScreenType.Mobile, b.getScreenType(499))
        assertEquals(ScreenType.Tablet, b.getScreenType(500))
        assertEquals(ScreenType.Tablet, b.getScreenType(1099))
        assertEquals(ScreenType.Desktop, b.getScreenType(1100))
    }
}
