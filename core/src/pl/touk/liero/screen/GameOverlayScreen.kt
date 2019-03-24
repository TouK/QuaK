package pl.touk.liero.screen

import com.badlogic.gdx.scenes.scene2d.ui.Table
import ktx.scene2d.KTableWidget
import ktx.scene2d.label
import ktx.scene2d.table
import pl.touk.liero.Ctx

abstract class GameOverlayScreen(ctx: Ctx) : UiScreen(ctx) {
    private lateinit var backgroundTable: Table
    override val root = table {
        setFillParent(true)
        backgroundTable = table {
            defaults().pad(ctx.params.pad)

            background = skin.getDrawable("fill")
            setColor(ctx.params.colorLevelOverlay)

            label(getText())
            createStars(this)
            row()
            table {
                defaults().pad(ctx.params.pad)
                createButtons(this)
            }
        }.cell(expandX = true, fillX = true)
    }

    abstract fun createButtons(table: KTableWidget)

    open fun createStars(table: KTableWidget) {

    }

    open fun getText() = ""

    override fun render(delta: Float) {
        ctx.stage.act(delta)
        ctx.stage.draw()
    }

    override fun show() {
        super.show()
        backgroundTable.color = ctx.params.colorLevelOverlay
    }
}