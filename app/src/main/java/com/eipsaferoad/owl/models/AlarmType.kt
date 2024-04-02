package com.eipsaferoad.owl.models

class AlarmType(
    public var max: Int,
    public var min: Int,
    public var actual: Float = 0f,
    public var isActivate: Boolean = false
) {

}