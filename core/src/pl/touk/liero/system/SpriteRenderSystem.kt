package pl.touk.liero.system

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import pl.touk.liero.ecs.*
import pl.touk.liero.utils.rad_deg

class SpriteRenderSystem(engine: Engine<Entity>,
                         private val batch: SpriteBatch,
                         private val camera: Camera) : System {

    val family = engine.family(body, texture)
    val family2 = engine.family(parent, texture)
    val family3 = engine.family(spriteRender)
    val family4 = engine.family(pos, texture)

    override fun update(timeStepSec: Float) {
        batch.projectionMatrix.set(camera.combined)
        batch.begin()
        family.foreach { body, texture ->
            draw(body, texture)
        }
        family2.foreach { parent, texture ->
            draw(parent.parent.get(body), texture)
        }
        family3.foreach { ent, renderScript ->
            renderScript.render(ent, batch, timeStepSec)
        }
        family4.foreach { pos, texture ->
            draw(pos, texture)
        }
        batch.end()
    }

    fun draw(pos: Vector2, texture: Texture) {
        batch.setColor(texture.color)
        val region = TextureRegion(texture.texture)
        if(texture.flipY) {
            region.flip(false, true)
        }
        batch.draw(region,
                pos.x - texture.width / 2, pos.y - texture.height / 2,
                texture.width / 2, texture.height / 2,
                texture.width, texture.height,
                texture.scaleX, texture.scaleY,
                texture.angleDeg)
    }

    fun draw(body: Body, texture: Texture) {
        batch.setColor(texture.color)
        val pos = body.position.add(texture.pos)
        val angle = body.angle.rad_deg()
        val region = TextureRegion(texture.texture)
        if(texture.flipY) {
            region.flip(false, true)
        }
        batch.draw(region,
                pos.x - texture.width / 2, pos.y - texture.height / 2,
                texture.width / 2, texture.height / 2,
                texture.width, texture.height,
                texture.scaleX, texture.scaleY,
                angle + texture.angleDeg)
    }
}