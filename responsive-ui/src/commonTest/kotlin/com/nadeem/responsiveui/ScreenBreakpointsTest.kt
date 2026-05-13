package com.nadeem.responsiveui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class ScreenBreakpointsTest {

    @Test
    fun defaultsMatchDocumentation() {
        val b = ScreenBreakpoints()
        assertEquals(300, b.watch)
        assertEquals(600, b.mobile)
        assertEquals(900, b.tablet)
    }

    @Test
    fun customValuesArePreserved() {
        val b = ScreenBreakpoints(watch = 200, mobile = 500, tablet = 800)
        assertEquals(200, b.watch)
        assertEquals(500, b.mobile)
        assertEquals(800, b.tablet)
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

    // Boundary tests for getScreenType. After the 1.0 fix:
    //   width < watch  → Watch
    //   width < mobile → Mobile
    //   width < tablet → Tablet
    //   else           → Desktop
    // (the buggy 0.0.x branch that returned Tablet for [tablet, ∞) is gone.)

    @Test
    fun belowWatchBreakpointReturnsWatch() {
        val b = ScreenBreakpoints()
        assertEquals(ScreenType.Watch, b.getScreenType(0))
        assertEquals(ScreenType.Watch, b.getScreenType(299))
    }

    @Test
    fun watchBoundaryReturnsMobile() {
        val b = ScreenBreakpoints()
        assertEquals(ScreenType.Mobile, b.getScreenType(300))
        assertEquals(ScreenType.Mobile, b.getScreenType(599))
    }

    @Test
    fun mobileBoundaryReturnsTablet() {
        val b = ScreenBreakpoints()
        assertEquals(ScreenType.Tablet, b.getScreenType(600))
        assertEquals(ScreenType.Tablet, b.getScreenType(899))
    }

    @Test
    fun tabletBoundaryReturnsDesktop() {
        // Behavior change in 1.0: widths >= tablet now correctly resolve to Desktop
        // (was returning Tablet erroneously in 0.0.x).
        val b = ScreenBreakpoints()
        assertEquals(ScreenType.Desktop, b.getScreenType(900))
        assertEquals(ScreenType.Desktop, b.getScreenType(1199))
        assertEquals(ScreenType.Desktop, b.getScreenType(10_000))
    }

    @Test
    fun customBreakpointsAreRespected() {
        val b = ScreenBreakpoints(watch = 250, mobile = 500, tablet = 1100)
        assertEquals(ScreenType.Watch, b.getScreenType(249))
        assertEquals(ScreenType.Mobile, b.getScreenType(250))
        assertEquals(ScreenType.Mobile, b.getScreenType(499))
        assertEquals(ScreenType.Tablet, b.getScreenType(500))
        assertEquals(ScreenType.Tablet, b.getScreenType(1099))
        assertEquals(ScreenType.Desktop, b.getScreenType(1100))
    }
}
