package pl.touk.liero.screen

import com.badlogic.gdx.Gdx
import ktx.scene2d.button
import ktx.scene2d.table
import pl.touk.liero.Ctx
import pl.touk.liero.gdx.onClicked
import pl.touk.liero.system.SoundSystem

class MenuScreen(ctx: Ctx) : UiScreen(ctx) {
    override val root = table{
        setFillParent(true)
        defaults().pad(ctx.params.pad)

        table {
            button("play") { cell ->
                onClicked {
                    ctx.sound.playSoundSample(SoundSystem.SoundSample.Select)
                    ctx.uiEvents += UiEvent.Play
                }
                cell.size(ctx.params.buttonSize)
            }
        }
        row()
        table {
            button("back") { cell ->
                onClicked {
                    ctx.sound.playSoundSample(SoundSystem.SoundSample.Back)
                    Gdx.app.exit()
                }
                cell.size(ctx.params.buttonSize)
            }
            add().expandX
            button("music") { cell ->
                isChecked = ctx.prefs.music
                cell.size(ctx.params.buttonSize)
                onClicked {
                    ctx.sound.playSoundSample(SoundSystem.SoundSample.Select)
                    ctx.prefs.music = isChecked
                    ctx.music.enable(isChecked)
                }
            }
            button("sound") { cell ->
                isChecked = ctx.prefs.sound
                cell.size(ctx.params.buttonSize)
                onClicked {
                    ctx.sound.playSoundSample(SoundSystem.SoundSample.Select)
                    ctx.prefs.sound = isChecked
                    ctx.sound.enable(isChecked)
                }
            }
        }
    }

    override fun onEnter() {
        ctx.sound.playSoundSample(SoundSystem.SoundSample.Select)
        ctx.uiEvents += UiEvent.Play
    }
}