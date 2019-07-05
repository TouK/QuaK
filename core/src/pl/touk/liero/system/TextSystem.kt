package pl.touk.liero.system

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.utils.Align
import ktx.math.vec2
import pl.touk.liero.ecs.*
import pl.touk.liero.gdx.project
import pl.touk.liero.gdx.unproject

class TextSystem(engine: Engine<Entity>,
                 private val batch: SpriteBatch,
                 private val worldCamera: Camera,
                 private val hudCamera: Camera) : System {

    private val family = engine.family(text)
    private val worldPos = vec2()

    override fun update(timeStepSec: Float) {
        batch.projectionMatrix.set(hudCamera.combined)
        batch.enableBlending()
        batch.begin()

        family.foreach { ent, text ->
            if (!ent.dead) {
                worldPos.set(0f, 0f)
                if (ent.contains(body)) {
                    worldPos.add(ent[body].position)
                }
                worldPos.add(text.pos)
                val screenPos = worldCamera.project(worldPos)
                val pos = hudCamera.unproject(screenPos)
                text.bitmapFont.color = text.color
                text.bitmapFont.draw(batch, text.text, pos.x - Gdx.graphics.width.toFloat() / 2f, pos.y, Gdx.graphics.width.toFloat(), Align.center, false)
            }
        }
        batch.end()
    }
}