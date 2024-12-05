package com.eipsaferoad.owl.models

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class SoundAlarmTest {

    private lateinit var soundAlarm: SoundAlarm

    @Before
    fun setUp() {
        // Initialize the SoundAlarm with default values
        soundAlarm = SoundAlarm(max = 3, min = 0, actual = 1.0f, isActivate = false)
    }

    @Test
    fun `updateAlarm increases actual when bigger is true and actual is less than max`() {
        // Initial value of actual is 1.0f
        soundAlarm.updateAlarm(bigger = true)

        // Expected value after increasing
        assertEquals(1.2f, soundAlarm.actual, 0.0f)
    }

    @Test
    fun `updateAlarm does not increase actual when bigger is true and actual equals max`() {
        // Set actual to max
        soundAlarm.actual = 3.0f

        soundAlarm.updateAlarm(bigger = true)

        // Verify actual remains unchanged
        assertEquals(3.0f, soundAlarm.actual, 0.0f)
    }

    @Test
    fun `updateAlarm decreases actual when bigger is false and actual is greater than min`() {
        // Initial value of actual is 1.0f
        soundAlarm.updateAlarm(bigger = false)

        // Expected value after decreasing
        assertEquals(0.8f, soundAlarm.actual, 0.0f)
    }

    @Test
    fun `updateAlarm does not decrease actual when bigger is false and actual equals min`() {
        // Set actual to min
        soundAlarm.actual = 0.0f

        soundAlarm.updateAlarm(bigger = false)

        // Verify actual remains unchanged
        assertEquals(0.0f, soundAlarm.actual, 0.0f)
    }
}
