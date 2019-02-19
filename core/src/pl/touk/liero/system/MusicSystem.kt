package pl.touk.liero.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Music.OnCompletionListener
import pl.touk.liero.utils.FirstOrderFilter

class MusicSystem(enabled: Boolean) : OnCompletionListener {

    val FOREGROUND_VOLUME = 0.4f
    val BACKGROUND_VOLUME = 0.12f
    val VOLUME_THRESHOLD = 0.05f

    private var enabled: Boolean = false

    override fun onCompletion(music: Music?) {
        currentTrack = ++currentTrack % tracks.size
        this.music = tracks[currentTrack]
        this.music?.volume = filter.value()
        this.music?.play()
    }

    private val filter = FirstOrderFilter(3f)
    private var targetVolume = FOREGROUND_VOLUME

    private val tracks = mutableListOf<Music>()
    private var currentTrack = 0
    private var music: Music? = null

    init {
        if(enabled) {
            enable()
        }
    }

    fun fadeIn() {
        targetVolume = FOREGROUND_VOLUME
    }

    fun fadeOut() {
        targetVolume = BACKGROUND_VOLUME
    }

    fun update(deltaSec: Float) {
        val vol = filter.update(targetVolume, deltaSec)
        music?.volume = vol
        if (vol < VOLUME_THRESHOLD) {
            music?.pause()
        } else {
            music?.play()
        }
    }

    fun enable() {
        if (enabled) {
            return
        }
        tracks += listOf(
                "music/Hip Hop1 85.wav")
                .map { Gdx.audio.newMusic(Gdx.files.internal(it)) }
        tracks.forEach {it.setOnCompletionListener(this)}
        music = tracks[currentTrack]
        filter.state = VOLUME_THRESHOLD
        music?.volume = filter.value()
        music?.play()
        enabled = true
    }

    fun dispose() {
        if (!enabled) {
            return
        }
        music?.stop()
        music = null
        tracks.forEach {
            it.setOnCompletionListener(null)
            it.dispose()
        }
        tracks.clear()
        enabled = false
    }

    fun disable() = dispose()

    fun enable(value: Boolean) {
        if(value) {
            enable()
        } else {
            disable()
        }
    }
}