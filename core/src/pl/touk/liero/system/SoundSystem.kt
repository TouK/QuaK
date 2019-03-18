package pl.touk.liero.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.audio.Sound

class SoundSystem(enabled: Boolean) {
    enum class SoundSample {
        Quack1, Quack2, DuckExclaim, DuckHowl, DuckLong, DuckMed, DuckQuestion, DuckShort,
        Select, Back, Shoot1, Shoot2, Shoot3, Pew, Perish, Jump, Hurt, HurtLow,
        NesPew, NoAmmo, PewLong, PewLow,
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
            Pair(SoundSample.HurtLow, "sfx/sfx-hurt-low.ogg"),
            Pair(SoundSample.DuckExclaim, "sfx/AV_duck_exclaim.ogg"),
            Pair(SoundSample.DuckHowl, "sfx/AV_duck_howl.ogg"),
            Pair(SoundSample.DuckLong, "sfx/AV_duck_long.ogg"),
            Pair(SoundSample.DuckMed, "sfx/AV_duck_med.ogg"),
            Pair(SoundSample.DuckQuestion, "sfx/AV_duck_question.ogg"),
            Pair(SoundSample.DuckShort, "sfx/AV_duck_short.ogg"),
            Pair(SoundSample.NoAmmo, "sfx/sfx-no-ammo.ogg"),
            Pair(SoundSample.PewLong, "sfx/sfx-pew-long.ogg"),
            Pair(SoundSample.PewLow, "sfx/sfx-pew-low.ogg"),
            Pair(SoundSample.NesPew, "sfx/sfx-nes-pew.ogg")
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