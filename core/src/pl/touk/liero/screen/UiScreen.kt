package pl.touk.liero.screen

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.ScreenAdapter
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.EventListener
import com.badlogic.gdx.scenes.scene2d.InputEvent
import ktx.scene2d.KTableWidget
import pl.touk.liero.Ctx
import pl.touk.liero.gdx.invalidateAll
import pl.touk.liero.system.SoundSystem

abstract class UiScreen(internal val ctx: Ctx) : ScreenAdapter(), EventListener {
    abstract val root: KTableWidget

    override fun resize(width: Int, height: Int) {
        root.setSize(Gdx.graphics.width.toFloat(), Gdx.graphics.height * 0.4f)
        root.invalidateAll()
        root.layout()
    }

    override fun show() {
        ctx.stage.addActor(root)
        ctx.stage.addListener(this)
    }

    override fun render(delta: Float) {
        ctx.stage.act(delta)
        ctx.stage.draw()
    }

    override fun hide() {
        ctx.stage.removeListener(this)
        root.remove()
    }

    override fun handle(event: Event?): Boolean {
        if (event is InputEvent && event.type == InputEvent.Type.keyDown) {
            return handleKey(event.keyCode)
        }
        return false
    }

    open fun handleKey(keyCode: Int): Boolean {
        when(keyCode) {
            Input.Keys.ENTER -> {
                onEnter()
                return true
            }
            Input.Keys.ESCAPE -> {
                ctx.sound.playSoundSample(SoundSystem.SoundSample.Back)
                ctx.uiEvents += UiEvent.Back
                return true
            }
            else -> return false
        }
    }

    open fun onEnter() {

    }
}