import androidx.compose.ui.test.assertIsOff
import androidx.compose.ui.test.assertIsOn
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.performClick
import com.eipsaferoad.owl.components.ToggleSwitch
import org.junit.Rule
import org.junit.Test

class ToggleSwitchTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `ToggleSwitch should display as checked when isActivate is true`() {
        composeTestRule.setContent {
            ToggleSwitch(isActivate = true, action = {})
        }

        composeTestRule.onNode(hasClickAction()).assertIsOn()
    }

    @Test
    fun `ToggleSwitch should display as unchecked when isActivate is false`() {
        composeTestRule.setContent {
            ToggleSwitch(isActivate = false, action = {})
        }

        composeTestRule.onNode(hasClickAction()).assertIsOff()
    }

    @Test
    fun `ToggleSwitch should trigger action on toggle`() {
        var isChecked = false

        composeTestRule.setContent {
            ToggleSwitch(isActivate = isChecked, action = { isChecked = it })
        }
        composeTestRule.onNode(hasClickAction()).performClick()
        assert(isChecked)
    }
}
