import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.assertIsDisplayed
import org.junit.Rule
import org.junit.Test
import com.eipsaferoad.owl.components.TextInput

class TextInputTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `TextInput should display placeholder when value is null or empty`() {
        val placeholder = "placeholder"

        composeTestRule.setContent {
            TextInput(placeholder = placeholder, value = "", onChange = {})
        }
        composeTestRule.onNodeWithText(placeholder).assertIsDisplayed()
    }

    @Test
    fun `TextInput should display the value when provided`() {
        val value = "Test Input"

        composeTestRule.setContent {
            TextInput(placeholder = "placeholder", value = value, onChange = {})
        }
        composeTestRule.onNodeWithText(value).assertIsDisplayed()
    }

    @Test
    fun `TextInput should allow click on placeholder or value`() {
        val placeholder = "placeholder"

        composeTestRule.setContent {
            TextInput(placeholder = placeholder, value = "", onChange = {})
        }
        composeTestRule.onNodeWithText(placeholder).performClick()
    }
}
