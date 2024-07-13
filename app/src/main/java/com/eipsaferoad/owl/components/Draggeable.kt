package com.eipsaferoad.owl.components

import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.MutableState
import androidx.compose.ui.input.pointer.pointerInput
import kotlin.math.abs
import androidx.compose.ui.Modifier
import com.eipsaferoad.owl.models.Alarm

fun Modifier.handleDraggableModifier(
    lastPosX: MutableState<Float>,
    vibrationVal: MutableState<Float>,
    nbrPixelToMove: MutableState<Int>,
    alarms: MutableState<Alarm>,
    dragLeft: (value: Float) -> Unit,
    dragRight: (value: Float) -> Unit,
    init: (value: Float) -> Unit
): Modifier {
    return this.pointerInput(Unit) {
        detectDragGestures { change, dragAmount ->
            change.consume()
            if (lastPosX.value == 0.0f) {
                init(change.position.x)
            }
            if (change.previousPosition.x < change.position.x && abs(lastPosX.value - change.position.x) > nbrPixelToMove.value && vibrationVal.value < alarms.value.vibration.max.toFloat()) {
                dragRight(change.position.x)
            } else if (change.previousPosition.x > change.position.x && abs(lastPosX.value - change.position.x) > nbrPixelToMove.value && vibrationVal.value > alarms.value.vibration.min) {
                dragLeft(change.position.x)
            }
            println(vibrationVal)
        }
    }
}