import android.content.Context
import android.content.res.Resources
import com.eipsaferoad.owl.R
import com.eipsaferoad.owl.utils.ReadEnvVar
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class ReadEnvVarTest {

    private lateinit var mockContext: Context
    private lateinit var mockResources: Resources

    @Before
    fun setup() {
        mockContext = mockk()
        mockResources = mockk()
        every { mockContext.resources } returns mockResources
    }

    @Test
    fun `readEnvVar should return value from resources if not cached`() {
        val apiUrl = "https://api.example.com"
        every { mockResources.getString(R.string.api_url) } returns apiUrl
        val result = ReadEnvVar.readEnvVar(mockContext, ReadEnvVar.EnvVar.API_URL)
        assertEquals(apiUrl, result)
        val cachedResult = ReadEnvVar.readEnvVar(mockContext, ReadEnvVar.EnvVar.API_URL)
        assertEquals(apiUrl, cachedResult)
        verify(exactly = 1) { mockResources.getString(R.string.api_url) }
    }

    @Test
    fun `readEnvVar should return cached value if already loaded`() {
        val cachedValue = "https://cached-api.example.com"
        ReadEnvVar.Companion.vars[ReadEnvVar.EnvVar.API_URL] = cachedValue
        val result = ReadEnvVar.readEnvVar(mockContext, ReadEnvVar.EnvVar.API_URL)
        assertEquals(cachedValue, result)
    }
}
