import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.assertIsDisplayed
import org.junit.Rule
import org.junit.Test
import com.eipsaferoad.owl.components.DisplayIcon

class DisplayIconTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `DisplayIcon should display the icon with correct content description`() {
        composeTestRule.setContent {
            DisplayIcon(
                imageVector = Icons.Rounded.Favorite,
                tint = Color.Blue
            )
        }
        composeTestRule.onNodeWithContentDescription("Favorite Icon")
            .assertExists()
            .assertIsDisplayed()
    }
}
