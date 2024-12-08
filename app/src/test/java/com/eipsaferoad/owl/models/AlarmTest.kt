import com.eipsaferoad.owl.models.Alarm
import com.eipsaferoad.owl.models.AlarmType
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class AlarmTest {

    private lateinit var vibrationAlarm: AlarmType
    private lateinit var soundAlarm: AlarmType
    private lateinit var alarm: Alarm

    @Before
    fun setup() {
        vibrationAlarm = AlarmType(max = 100, min = 10, actual = 50f, isActivate = true)
        soundAlarm = AlarmType(max = 80, min = 20, actual = 40f, isActivate = false)

        alarm = Alarm(
            vibration = vibrationAlarm,
            sound = soundAlarm,
            isAlarmActivate = true,
            music = "alarm_music.mp3",
            iconId = 123
        )
    }

    @Test
    fun `Alarm properties should be correctly initialized`() {
        assertEquals(vibrationAlarm, alarm.vibration)
        assertEquals(soundAlarm, alarm.sound)
        assertEquals(true, alarm.isAlarmActivate)
        assertEquals("alarm_music.mp3", alarm.music)
        assertEquals(123, alarm.iconId)
    }

    @Test
    fun `Alarm properties should be mutable`() {
        alarm.isAlarmActivate = false
        alarm.music = "new_alarm_music.mp3"
        alarm.iconId = 456

        assertEquals(false, alarm.isAlarmActivate)
        assertEquals("new_alarm_music.mp3", alarm.music)
        assertEquals(456, alarm.iconId)
    }

    @Test
    fun `Vibration and Sound AlarmTypes should reflect updates`() {
        alarm.vibration.actual = 60f
        alarm.sound.isActivate = true

        assertEquals(60f, alarm.vibration.actual, 0.0f)
        assertEquals(true, alarm.sound.isActivate)
    }
}
