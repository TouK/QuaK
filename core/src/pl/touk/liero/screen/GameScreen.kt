package pl.touk.liero.screen

import com.badlogic.gdx.scenes.scene2d.ui.Button
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.utils.Align
import ktx.scene2d.label
import ktx.scene2d.table
import pl.touk.liero.Ctx

class GameScreen(ctx: Ctx) : UiScreen(ctx) {
    private lateinit var pause: Button
    private lateinit var leftFooter: Label
    private lateinit var rightFooter: Label

    override val root = table {
        setFillParent(true)

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
        super.show()
    }

    fun update() {
        leftFooter.setText("frags: " + ctx.leftFrags.frags)
        rightFooter.setText("frags: " + ctx.rightFrags.frags)
    }
}