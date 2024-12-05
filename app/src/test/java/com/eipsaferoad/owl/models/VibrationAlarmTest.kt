package com.eipsaferoad.owl.models

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class VibrationAlarmTest {

    private lateinit var vibrationAlarm: VibrationAlarm

    @Before
    fun setUp() {
        // Initialize the VibrationAlarm with default values
        vibrationAlarm = VibrationAlarm(max = 2, min = 0, actual = 0f, isActivate = false)
    }

    @Test
    fun `updateAlarm increases actual when bigger is true and actual is less than max`() {
        // Initial value of actual is 0f
        val result = vibrationAlarm.updateAlarm(bigger = true)

        // Expected value after increasing
        assertEquals(1, result)
        assertEquals(1f, vibrationAlarm.actual, 0.0f)
    }

    @Test
    fun `updateAlarm does not increase actual when bigger is true and actual equals max`() {
        // Set actual to max
        vibrationAlarm.actual = 2f

        val result = vibrationAlarm.updateAlarm(bigger = true)

        // Verify actual remains unchanged
        assertEquals(2, result)
        assertEquals(2f, vibrationAlarm.actual, 0.0f)
    }

    @Test
    fun `updateAlarm decreases actual when bigger is false and actual is greater than min`() {
        // Set actual to 1f
        vibrationAlarm.actual = 1f

        val result = vibrationAlarm.updateAlarm(bigger = false)

        // Expected value after decreasing
        assertEquals(0, result)
        assertEquals(0f, vibrationAlarm.actual, 0.0f)
    }

    @Test
    fun `updateAlarm does not decrease actual when bigger is false and actual equals min`() {
        // Set actual to min
        vibrationAlarm.actual = 0f

        val result = vibrationAlarm.updateAlarm(bigger = false)

        // Verify actual remains unchanged
        assertEquals(0, result)
        assertEquals(0f, vibrationAlarm.actual, 0.0f)
    }
}
