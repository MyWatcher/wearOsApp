import android.content.Context
import android.media.MediaPlayer
import com.eipsaferoad.owl.utils.soundPlayer
import io.mockk.*
import org.junit.Before
import org.junit.Test

class SoundPlayerTest {

    private lateinit var mockContext: Context
    private lateinit var mockMediaPlayer: MediaPlayer

    @Before
    fun setup() {
        mockContext = mockk()
        mockMediaPlayer = mockk(relaxed = true)

        mockkStatic(MediaPlayer::class)
        every { MediaPlayer.create(mockContext, any<Int>()) } returns mockMediaPlayer
    }

    @Test
    fun `soundPlayer should configure MediaPlayer with correct parameters`() {
        val fileId = 123
        val volume = 0.5f
        val loop = true

        soundPlayer(context = mockContext, volume = volume, loop = loop, fileId = fileId)
        verify {
            mockMediaPlayer.isLooping = loop
            mockMediaPlayer.setVolume(volume, volume)
            mockMediaPlayer.start()
        }
        verify { MediaPlayer.create(mockContext, fileId) }
    }

    @Test
    fun `soundPlayer should release MediaPlayer on completion`() {
        val fileId = 123

        soundPlayer(context = mockContext, fileId = fileId)
        val completionSlot = slot<MediaPlayer.OnCompletionListener>()
        verify { mockMediaPlayer.setOnCompletionListener(capture(completionSlot)) }
        completionSlot.captured.onCompletion(mockMediaPlayer)
        verify { mockMediaPlayer.release() }
    }
}
