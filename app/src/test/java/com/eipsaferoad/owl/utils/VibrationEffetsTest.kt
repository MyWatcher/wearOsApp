import android.os.VibrationEffect
import com.eipsaferoad.owl.utils.getVibrationEffects
import io.mockk.*
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class GetVibrationEffectsTest {

    @Before
    fun setup() {
        mockkStatic(VibrationEffect::class)
    }

    @Test
    fun `getVibrationEffects should return correct number of vibration effects`() {
        val mockEffectOne = mockk<VibrationEffect>()
        val mockEffectTwo = mockk<VibrationEffect>()
        val mockEffectThree = mockk<VibrationEffect>()

        every { VibrationEffect.createWaveform(longArrayOf(0, 200, 200, 200), intArrayOf(0, 255, 255, 255), -1) } returns mockEffectOne
        every { VibrationEffect.createWaveform(longArrayOf(0, 200, 100, 300), intArrayOf(0, 255, 0, 255), -1) } returns mockEffectTwo
        every { VibrationEffect.createWaveform(longArrayOf(0, 200, 100, 300, 200, 400), intArrayOf(0, 255, 0, 255, 0, 255), -1) } returns mockEffectThree

        val result = getVibrationEffects()

        verify(exactly = 1) { VibrationEffect.createWaveform(longArrayOf(0, 200, 200, 200), intArrayOf(0, 255, 255, 255), -1) }
        verify(exactly = 1) { VibrationEffect.createWaveform(longArrayOf(0, 200, 100, 300), intArrayOf(0, 255, 0, 255), -1) }
        verify(exactly = 1) { VibrationEffect.createWaveform(longArrayOf(0, 200, 100, 300, 200, 400), intArrayOf(0, 255, 0, 255, 0, 255), -1) }

        assertEquals(4, result.size)
        assertEquals(mockEffectOne, result[0])
        assertEquals(mockEffectTwo, result[1])
        assertEquals(mockEffectThree, result[2])
        assertEquals(mockEffectThree, result[3])
    }
}
