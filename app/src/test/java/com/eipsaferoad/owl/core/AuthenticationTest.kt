import android.content.Context
import androidx.navigation.NavHostController
import com.eipsaferoad.owl.api.Request
import com.eipsaferoad.owl.core.Authentication
import com.eipsaferoad.owl.presentation.PagesEnum
import com.eipsaferoad.owl.utils.EnvEnum
import com.eipsaferoad.owl.utils.LocalStorage
import io.mockk.*
import okhttp3.FormBody
import okhttp3.Headers
import org.json.JSONObject
import org.junit.Before
import org.junit.Test

class AuthenticationTest {

    private lateinit var mockContext: Context
    private lateinit var mockNavController: NavHostController
    private lateinit var mockSetAccessToken: (String) -> Unit

    @Before
    fun setup() {
        mockContext = mockk(relaxed = true)
        mockNavController = mockk(relaxed = true)
        mockSetAccessToken = mockk(relaxed = true)

        mockkStatic(LocalStorage::class)
        mockkObject(Request)
    }

    @Test
    fun `login should call setAccessToken with the correct token`() {
        val apiUrl = "https://example.com"
        val email = "test@example.com"
        val password = "password123"
        val mockResponse = mockk<JSONObject>()
        val mockData = mockk<JSONObject>()

        mockkConstructor(JSONObject::class)
        every { anyConstructed<JSONObject>().getJSONObject("data") } returns mockData
        every { mockData.getString("token") } returns "access_token_value"

        every {
            Request.makeRequest(
                any(),
                any(),
                any(),
                Request.Companion.REQUEST_TYPE.POST,
                captureLambda()
            )
        } answers {
            lambda<(String) -> Unit>().invoke("")
        }

        Authentication.login(
            context = mockContext,
            isNew = false,
            apiUrl = apiUrl,
            email = email,
            password = password,
            navController = mockNavController,
            setAccessToken = mockSetAccessToken
        )

        verify { mockSetAccessToken("access_token_value") }
    }
}
