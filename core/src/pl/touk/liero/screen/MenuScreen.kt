package pl.touk.liero.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.utils.Scaling
import pl.touk.liero.gdx.onClicked
import pl.touk.liero.Ctx
import ktx.scene2d.button
import ktx.scene2d.image
import ktx.scene2d.table

class MenuScreen(ctx: Ctx) : UiScreen(ctx) {
    override val root = table{
        setFillParent(true)
        defaults().pad(ctx.params.pad)

        image("logo") { cell ->
            setScaling(Scaling.fit)
            cell.width(ctx.params.logoWidth)
            cell.expand()
        }
        row()
        table {
            button("play") { cell ->
                onClicked {
                    ctx.uiEvents += UiEvent.Play
                }
                cell.size(ctx.params.buttonSize)
            }
        }
        row()
        table {
            button("back") { cell ->
                onClicked {
                    Gdx.app.exit()
                }
                cell.size(ctx.params.buttonSize)
            }
            add().expandX
            button("music") { cell ->
                isChecked = ctx.prefs.music
                cell.size(ctx.params.buttonSize)
                onClicked {
                    ctx.prefs.music = isChecked
                    ctx.music.enable(isChecked)
                }
            }
            button("sound") { cell ->
                isChecked = ctx.prefs.sound
                cell.size(ctx.params.buttonSize)
                onClicked {
                    ctx.prefs.sound = isChecked
                }
            }
        }
    }

    override fun onEnter() {
        ctx.uiEvents += UiEvent.Play
    }
}