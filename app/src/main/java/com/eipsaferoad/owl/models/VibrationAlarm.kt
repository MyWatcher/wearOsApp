package com.eipsaferoad.owl.models

import android.os.VibrationEffect

class VibrationAlarm(
    max: Int = 3,
    min: Int = 0,
    actual: Float = 0f,
    isActivate: Boolean = false,
) : AlarmType(max, min, actual, isActivate) {
    private val vibrationLevels: Array<Int> = arrayOf(
        VibrationEffect.DEFAULT_AMPLITUDE,
        VibrationEffect.DEFAULT_AMPLITUDE,
        VibrationEffect.DEFAULT_AMPLITUDE,
        VibrationEffect.DEFAULT_AMPLITUDE
    )

    override fun updateAlarm(bigger: Boolean): Int {
        if (bigger && actual < max) {
            actual += 1
        } else if (!bigger && actual > min) {
            actual -= 1
        }
        return vibrationLevels[actual.toInt()]
    }
}