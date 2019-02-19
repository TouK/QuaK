package pl.touk.liero.screen

import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.utils.Align
import pl.touk.liero.gdx.onClicked
import pl.touk.liero.Ctx
import ktx.scene2d.button
import ktx.scene2d.label
import ktx.scene2d.table

class GameScreen(ctx: Ctx) : UiScreen(ctx) {
    private lateinit var pause: Button

    override val root = table {
        setFillParent(true)
        table {
            label("left header", "small").cell(align = Align.topLeft)
            add().expandX()
            pause = button("pause", ctx.skin) {cell ->
                cell.size(ctx.params.smallButtonSize)
                        .expandX().fillX().align(Align.right)
                onClicked {
                    ctx.uiEvents += UiEvent.Pause
                }
            }
        }.cell(expandX = true, fillX = true)

        row()
        add().expand()
        row()
        table {
            label("left footer", "small").cell(align = Align.topLeft)
            add().expandX()
            label("right footer", "small").cell(align = Align.topLeft)
        }.cell(expandX = true, fillX = true)
    }

    override fun render(delta: Float) {
        ctx.stage.act(delta)
        ctx.stage.draw()
    }

    override fun show() {
        pause.color.set(ctx.params.colorHud)
        super.show()
    }
}