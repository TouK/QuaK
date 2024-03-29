package pl.touk.liero.screen

import com.badlogic.gdx.Input
import ktx.scene2d.label
import ktx.scene2d.table
import ktx.scene2d.textButton
import pl.touk.liero.Ctx
import pl.touk.liero.gdx.ifJustPressed
import pl.touk.liero.gdx.onClicked
import pl.touk.liero.system.SoundSystem

class LevelScreen(ctx: Ctx) : UiScreen(ctx) {
    override val root = table {
        setFillParent(true)
        defaults().pad(ctx.params.pad)

        label("Level Screen")
        row()
        textButton("Level 1") {
            onClicked {
                ctx.sound.playSoundSample(SoundSystem.SoundSample.Select)
                ctx.currentLevel = 1
                ctx.uiEvents += UiEvent.Play
            }
        }.cell(expandX = true)
        row()
        textButton("Level 2") {
            onClicked {
                ctx.sound.playSoundSample(SoundSystem.SoundSample.Select)
                ctx.currentLevel = 2
                ctx.uiEvents += UiEvent.Play
            }
        }.cell(expandX = true)
    }

    override fun render(delta: Float) {
        super.render(delta)
        Input.Keys.NUM_1.ifJustPressed {
            ctx.currentLevel = 1
            ctx.uiEvents += UiEvent.Play
        }
        Input.Keys.NUM_2.ifJustPressed {
            ctx.currentLevel = 2
            ctx.uiEvents += UiEvent.Play
        }
    }

    override fun onEnter() {
        ctx.uiEvents += UiEvent.Play
    }
}
