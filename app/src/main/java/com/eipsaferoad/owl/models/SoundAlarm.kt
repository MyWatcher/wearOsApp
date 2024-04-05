package com.eipsaferoad.owl.models

class SoundAlarm(
    max: Int = 3,
    min: Int = 0,
    actual: Float = 1.0f,
    isActivate: Boolean = false,
    ) : AlarmType(max, min, actual, isActivate) {

    override fun updateAlarm(bigger: Boolean): Int {
        if (bigger && actual < max.toFloat()) {
            actual += 0.2f
        } else if (!bigger && actual > min.toFloat()) {
            actual -= 0.2f
        }
        return 0
    }
}