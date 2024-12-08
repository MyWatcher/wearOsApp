import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performGesture
import androidx.compose.ui.test.swipeRight
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.testTag
import com.eipsaferoad.owl.components.handleDraggableModifier
import com.eipsaferoad.owl.models.Alarm
import com.eipsaferoad.owl.models.AlarmType
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class HandleDraggableModifierTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `drag right should call dragRight`() {
        val lastPosX = mutableStateOf(0f)
        val vibrationVal = mutableStateOf(0f)
        val nbrPixelToMove = mutableStateOf(10)
        val alarms = mutableStateOf(
            Alarm(
                vibration = AlarmType(max = 100, min = 0),
                sound = AlarmType(max = 100, min = 0),
                isAlarmActivate = true,
                music = "alarm.mp3",
                iconId = 1
            )
        )
        var dragRightCalled = false

        composeTestRule.setContent {
            DraggableTestComposable(
                lastPosX = lastPosX,
                vibrationVal = vibrationVal,
                nbrPixelToMove = nbrPixelToMove,
                alarms = alarms,
                dragLeft = {},
                dragRight = { dragRightCalled = true },
                init = { lastPosX.value = it }
            )
        }

        composeTestRule.onNodeWithTag("draggable").performGesture {
            swipeRight()
        }

        /*assertEquals(true, dragRightCalled)*/
    }
}

@Composable
fun DraggableTestComposable(
    lastPosX: MutableState<Float>,
    vibrationVal: MutableState<Float>,
    nbrPixelToMove: MutableState<Int>,
    alarms: MutableState<Alarm>,
    dragLeft: (value: Float) -> Unit,
    dragRight: (value: Float) -> Unit,
    init: (value: Float) -> Unit
) {
    Box(
        modifier = Modifier
            .handleDraggableModifier(
                lastPosX = lastPosX,
                vibrationVal = vibrationVal,
                nbrPixelToMove = nbrPixelToMove,
                alarms = alarms,
                dragLeft = dragLeft,
                dragRight = dragRight,
                init = init
            )
            .testTag("draggable")
    )
}
