package com.eipsaferoad.owl.models

import android.os.VibrationEffect
import com.eipsaferoad.owl.utils.getVibrationEffects

class VibrationAlarm(
    max: Int = 2,
    min: Int = 0,
    actual: Float = 0f,
    isActivate: Boolean = false,
) : AlarmType(max, min, actual, isActivate) {

    override fun updateAlarm(bigger: Boolean): Int {
        if (bigger && actual < max) {
            actual += 1
        } else if (!bigger && actual > min) {
            actual -= 1
        }
        return actual.toInt()
    }
}