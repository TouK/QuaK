package pl.touk.liero.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound

class SoundSystem(enabled: Boolean) {
    enum class SoundSample {
        Select, Back, Quack1, Quack2, Shoot1, Shoot2, Shoot3, Pew, Perish, Jump, Hurt, HurtLow
    }

    private var enabled: Boolean = false
    private val sounds = mutableMapOf<SoundSample, Sound>()
    private val soundPathMap = mapOf(
            Pair(SoundSample.Select, "sfx/sfx-select.ogg"),
            Pair(SoundSample.Back, "sfx/sfx-cancel.ogg"),
            Pair(SoundSample.Quack1, "sfx/quack1.ogg"),
            Pair(SoundSample.Quack2, "sfx/quack2.ogg"),
            Pair(SoundSample.Shoot1, "sfx/sfx-shoot1.ogg"),
            Pair(SoundSample.Shoot2, "sfx/sfx-shoot2.ogg"),
            Pair(SoundSample.Shoot3, "sfx/sfx-shoot3.ogg"),
            Pair(SoundSample.Pew, "sfx/sfx-pew.ogg"),
            Pair(SoundSample.Perish, "sfx/sfx-perish.ogg"),
            Pair(SoundSample.Jump, "sfx/sfx-jump.ogg"),
            Pair(SoundSample.Hurt, "sfx/sfx-hurt.ogg"),
            Pair(SoundSample.HurtLow, "sfx/sfx-hurt-low.ogg")
    )

    init {
        if(enabled) {
            enable()
        }
    }

    fun enable(value: Boolean) {
        if(value) {
            enable()
        } else {
            disable()
        }
    }

    fun enable() {
        if (enabled) {
            return
        }
        sounds += soundPathMap
                .mapValues { Gdx.audio.newSound(Gdx.files.internal(it.value)) }

        enabled = true
    }

    fun dispose() {
        if (!enabled) {
            return
        }

        sounds.forEach {
            it.value.stop()
            it.value.dispose()
        }
        sounds.clear()
        enabled = false
    }

    fun disable() = dispose()

    fun playSoundSample(soundSample: SoundSample) {
        val sound = sounds[soundSample]
        sound?.stop()
        sound?.play()
    }
}