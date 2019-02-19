package pl.touk.liero.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align
import pl.touk.liero.ecs.text
import pl.touk.liero.ecs.Engine
import pl.touk.liero.ecs.Entity
import pl.touk.liero.ecs.System
import pl.touk.liero.gdx.project
import pl.touk.liero.gdx.unproject

class TextSystem(engine: Engine<Entity>,
                 private val batch: SpriteBatch,
                 private val worldCamera: Camera,
                 private val hudCamera: Camera) : System {

    val family = engine.family(text)

    override fun update(timeStepSec: Float) {
        batch.projectionMatrix.set(hudCamera.combined)
        batch.enableBlending()
        batch.begin()

        family.foreach { ent, text ->
            if (!ent.dead) {
                val screenPos = worldCamera.project(text.pos)
                val pos = hudCamera.unproject(screenPos)
                text.bitmapFont.color = text.color
                text.bitmapFont.draw(batch, text.text, pos.x - Gdx.graphics.width.toFloat() / 2f, pos.y, Gdx.graphics.width.toFloat(), Align.center, false)
            }
        }
        batch.end()
    }
}