package com.eipsaferoad.owl.utils

import android.content.Context
import androidx.compose.runtime.MutableState
import com.eipsaferoad.owl.models.Alarm

fun initAlarms(context: Context, alarms: MutableState<Alarm>) {
    val vibration = LocalStorage.getData(context, KeysEnum.ALARM_VIBRATION.value)
    val sound = LocalStorage.getData(context, KeysEnum.ALARM_SOUND.value)
    val activate = LocalStorage.getData(context, KeysEnum.ALARM_ACTIVATE.value)
    if (activate != null) {
        alarms.value.isAlarmActivate = activate == "1"
    }
    if (!vibration.isNullOrEmpty()) {
        alarms.value.vibration.decompress(vibration)
    }
    if (!sound.isNullOrEmpty()) {
        alarms.value.sound.decompress(sound)
    }
}