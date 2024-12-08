import com.eipsaferoad.owl.presentation.PagesEnum
import org.junit.Assert.assertEquals
import org.junit.Test

class PagesEnumTest {

    @Test
    fun `PagesEnum should contain all expected values`() {
        val expectedValues = setOf("login", "home", "settings")
        val actualValues = PagesEnum.values().map { it.value }.toSet()

        assertEquals(expectedValues, actualValues)
    }

    @Test
    fun `PagesEnum values should map correctly`() {
        assertEquals("login", PagesEnum.LOGIN.value)
        assertEquals("home", PagesEnum.HOME.value)
        assertEquals("settings", PagesEnum.SETTINGS.value)
    }

    @Test
    fun `PagesEnum should have correct enum names`() {
        assertEquals(PagesEnum.LOGIN, PagesEnum.valueOf("LOGIN"))
        assertEquals(PagesEnum.HOME, PagesEnum.valueOf("HOME"))
        assertEquals(PagesEnum.SETTINGS, PagesEnum.valueOf("SETTINGS"))
    }
}
