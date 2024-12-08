import android.content.Context
import android.content.SharedPreferences
import com.eipsaferoad.owl.utils.LocalStorage
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class LocalStorageTest {

    private lateinit var mockContext: Context
    private lateinit var mockSharedPreferences: SharedPreferences
    private lateinit var mockEditor: SharedPreferences.Editor

    @Before
    fun setup() {
        mockContext = mockk()
        mockSharedPreferences = mockk()
        mockEditor = mockk()
        every { mockContext.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE) } returns mockSharedPreferences
        every { mockSharedPreferences.edit() } returns mockEditor
        every { mockEditor.putString(any(), any()) } returns mockEditor
        every { mockEditor.remove(any()) } returns mockEditor
        every { mockEditor.apply() } just runs
    }

    @Test
    fun `setData should store key-value pair in SharedPreferences`() {
        val key = "testKey"
        val value = "testValue"

        LocalStorage.setData(mockContext, key, value)

        verify {
            mockEditor.putString(key, value)
            mockEditor.apply()
        }
    }

    @Test
    fun `getData should retrieve value for a given key`() {
        val key = "testKey"
        val value = "testValue"

        every { mockSharedPreferences.getString(key, "") } returns value

        val result = LocalStorage.getData(mockContext, key)

        assertEquals(value, result)
    }

    @Test
    fun `getData should return default value when key does not exist`() {
        val key = "nonexistentKey"

        every { mockSharedPreferences.getString(key, "") } returns ""

        val result = LocalStorage.getData(mockContext, key)

        assertEquals("", result)
    }

    @Test
    fun `deleteData should remove key from SharedPreferences`() {
        val key = "testKey"

        LocalStorage.deleteData(mockContext, key)

        verify {
            mockEditor.remove(key)
            mockEditor.apply()
        }
    }
}
