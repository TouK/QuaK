package pl.touk.liero.screen

import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import ktx.scene2d.button
import ktx.scene2d.label
import ktx.scene2d.table
import pl.touk.liero.Ctx
import pl.touk.liero.gdx.onClicked
import pl.touk.liero.system.SoundSystem

class GameScreen(ctx: Ctx) : UiScreen(ctx) {
    private lateinit var pause: Button
    private lateinit var leftFooter: Label
    private lateinit var rightFooter: Label

    override val root = table {
        setFillParent(true)
        table {
            label("left header", "small").cell(align = Align.topLeft)
            add().expandX()
            pause = button("pause", ctx.skin) {cell ->
                cell.size(ctx.params.smallButtonSize)
                        .expandX().fillX().align(Align.right)
                onClicked {
                    ctx.sound.playSoundSample(SoundSystem.SoundSample.Select)
                    ctx.uiEvents += UiEvent.Pause
                }
            }
        }.cell(expandX = true, fillX = true)

        row()
        add().expand()
        row()
        table {
            leftFooter = label("frags: 0", "small").cell(align = Align.topLeft)
            add().expandX()
            rightFooter = label("frags: 0", "small").cell(align = Align.topLeft)
        }.cell(expandX = true, fillX = true)
    }

    override fun render(delta: Float) {
        update()
        ctx.stage.act(delta)
        ctx.stage.draw()
    }

    override fun show() {
        pause.color.set(ctx.params.colorHud)
        super.show()
    }

    fun update() {
        leftFooter.setText("frags: " + ctx.leftFrags)
        rightFooter.setText("frags: " + ctx.rightFrags)
    }
}