import com.eipsaferoad.owl.models.AlarmType
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AlarmTypeTest {

    private lateinit var alarmType: AlarmType

    @Before
    fun setup() {
        alarmType = AlarmType(max = 100, min = 10, actual = 50f, isActivate = true)
    }

    @Test
    fun `updateAlarm should increase actual value when bigger is true and actual is less than max`() {
        alarmType.updateAlarm(bigger = true)
        assertEquals(60f, alarmType.actual, 0.0f)
    }

    @Test
    fun `updateAlarm should not increase actual value when bigger is true and actual is equal to max`() {
        alarmType.actual = 100f
        alarmType.updateAlarm(bigger = true)
        assertEquals(100f, alarmType.actual, 0.0f)
    }

    @Test
    fun `updateAlarm should decrease actual value when bigger is false and actual is greater than min`() {
        alarmType.updateAlarm(bigger = false)
        assertEquals(40f, alarmType.actual, 0.0f)
    }

    @Test
    fun `updateAlarm should not decrease actual value when bigger is false and actual is equal to min`() {
        alarmType.actual = 10f
        alarmType.updateAlarm(bigger = false)
        assertEquals(10f, alarmType.actual, 0.0f)
    }

    @Test
    fun `updateAlarm should return 0 regardless of conditions`() {
        val result = alarmType.updateAlarm(bigger = true)
        assertEquals(0, result)
    }
}
