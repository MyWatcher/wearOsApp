import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import org.junit.Rule
import org.junit.Test
import com.eipsaferoad.owl.components.Button
import com.eipsaferoad.owl.components.ButtonTypeEnum
import com.eipsaferoad.owl.components.ContentExample

class ButtonTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `Button should call action when clicked`() {
        val actionTriggered = mutableStateOf(false)

        composeTestRule.setContent {
            Button(
                type = ButtonTypeEnum.PRIMARY,
                content = { ContentExample() },
                action = { actionTriggered.value = true }
            )
        }
        composeTestRule.onNode(hasClickAction()).performClick()
        assert(actionTriggered.value)
    }

    @Test
    fun `Button should display content`() {
        composeTestRule.setContent {
            Button(
                type = ButtonTypeEnum.SECONDARY,
                content = { ContentExample() },
                action = {}
            )
        }
        composeTestRule.onNodeWithText("coucou").assertIsDisplayed()
    }
}
