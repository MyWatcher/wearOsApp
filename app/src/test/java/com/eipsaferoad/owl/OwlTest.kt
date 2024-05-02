package com.eipsaferoad.owl

import android.os.VibrationEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.eipsaferoad.owl.models.Alarm
import com.eipsaferoad.owl.models.SoundAlarm
import com.eipsaferoad.owl.models.VibrationAlarm
import org.junit.Assert.assertEquals
import org.junit.Test

class OwlTest {

    @Test
    fun testAddition() {
        // Arrange
        val number1 = 1
        val number2 = 1

        // Act
        val sum = number1 + number2

        // Assert
        assertEquals(2, sum)
    }

    @Test
    fun testVibrationAlarm() {
        val alarm = VibrationAlarm(3, 0)
        assertEquals(alarm.max, 3)
        assertEquals(alarm.min, 0)
        assertEquals(alarm.actual, 0.0f)
        var res = alarm.updateAlarm()
        assertEquals(res, VibrationEffect.DEFAULT_AMPLITUDE)
        assertEquals(alarm.actual, 1.0f)
        res = alarm.updateAlarm(false)
        assertEquals(res, VibrationEffect.DEFAULT_AMPLITUDE)
        assertEquals(alarm.actual, 0.0f)
        alarm.updateAlarm()
        alarm.updateAlarm()
        alarm.updateAlarm()
        alarm.updateAlarm()
        alarm.updateAlarm()
        alarm.updateAlarm()
        alarm.updateAlarm()
        alarm.updateAlarm()
        alarm.updateAlarm()
        alarm.updateAlarm()
        alarm.updateAlarm()
        alarm.updateAlarm()
        alarm.updateAlarm()
        alarm.updateAlarm()
        alarm.updateAlarm()
        alarm.updateAlarm()
        alarm.updateAlarm()
        alarm.updateAlarm()
        alarm.updateAlarm()
        alarm.updateAlarm()
        alarm.updateAlarm()
        alarm.updateAlarm()
        alarm.updateAlarm()
        res = alarm.updateAlarm()
        assertEquals(res, VibrationEffect.DEFAULT_AMPLITUDE)
    }
}
