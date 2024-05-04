package com.eipsaferoad.owl.utils

import android.os.VibrationEffect

fun getVibrationEffects(): Array<VibrationEffect> {
    val customVibrationPatternOne = longArrayOf(0, 200, 200, 200)
    val amplitudesOne = intArrayOf(0, 255, 255, 255)
    val customVibrationEffectOne = VibrationEffect.createWaveform(customVibrationPatternOne, amplitudesOne, -1)

    val customVibrationPatternTwo = longArrayOf(0, 200, 100, 300)
    val amplitudesTwo = intArrayOf(0, 255, 0, 255)
    val customVibrationEffectTwo = VibrationEffect.createWaveform(customVibrationPatternTwo, amplitudesTwo, -1)

    val customVibrationPatternThree = longArrayOf(0, 200, 100, 300, 200, 400)
    val amplitudesThree = intArrayOf(0, 255, 0, 255, 0, 255)
    val customVibrationEffectThree = VibrationEffect.createWaveform(customVibrationPatternThree, amplitudesThree, -1)

    return arrayOf(customVibrationEffectOne, customVibrationEffectTwo, customVibrationEffectThree, customVibrationEffectThree)
}