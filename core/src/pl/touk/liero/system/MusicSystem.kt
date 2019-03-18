package pl.touk.liero.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Music.OnCompletionListener
import pl.touk.liero.utils.FirstOrderFilter

class MusicSystem(enabled: Boolean) : OnCompletionListener {

    val FOREGROUND_VOLUME = 0.4f
    val BACKGROUND_VOLUME = 0.12f
    val VOLUME_THRESHOLD = 0.05f
    val FADE_OUT_SPEED = 1.5f

    enum class MusicTrack {
        Preparations, Deathmatch, Battle
    }

    private var enabled: Boolean = false

    override fun onCompletion(music: Music?) {
        this.music = tracks[currentTrack]
        this.music?.volume = filter.value()
        this.music?.play()
    }

    private val trackPathMap = mapOf(
            Pair(MusicTrack.Preparations, "music/Preparations.ogg"),
            Pair(MusicTrack.Deathmatch, "music/Deathmatch.ogg"),
            Pair(MusicTrack.Battle, "music/Battle.ogg")
    )

    private val filter = FirstOrderFilter(3f)
    private var targetVolume = FOREGROUND_VOLUME

    private val tracks = mutableMapOf<MusicTrack, Music>()
    private var currentTrack = MusicTrack.Preparations
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
        val vol = filter.update(targetVolume, deltaSec * FADE_OUT_SPEED)
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
        tracks += trackPathMap
                .mapValues { Gdx.audio.newMusic(Gdx.files.internal(it.value)) }
        tracks.forEach {it.value.setOnCompletionListener(this)}
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
            it.value.setOnCompletionListener(null)
            it.value.dispose()
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

    fun playTrack(track: MusicTrack) {
        if(currentTrack != track) {
            this.music?.stop()
            currentTrack = track
            this.music = tracks[currentTrack]
            this.music?.volume = filter.value()
            fadeIn()
        }
    }
}