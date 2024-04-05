package com.eipsaferoad.owl.models

open class AlarmType(
    public var max: Int,
    public var min: Int,
    public var actual: Float = 0f,
    public var isActivate: Boolean = false
) {
    open fun updateAlarm(bigger: Boolean = true): Int {
        if (bigger && actual < max) {
            actual += 10
        } else if (!bigger && actual > min) {
            actual -= 10
        }
        return 0
    }
}