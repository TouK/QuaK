package pl.touk.liero.entity

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Joint
import pl.touk.liero.ecs.*
import pl.touk.liero.script.Script

class EntityBuilder {
    val e = Entity()
    fun build(): Entity {
        return e
    }
    fun body(b: Body) {
        e.add(body, b)
        b.userData = e
    }
    fun energy(total: Float) {
        e.add(energy, Energy(total))
    }

    fun script(script: Script) {
        e.add(script)
    }

    fun texture(tex: TextureRegion,
                width: Float, height: Float,
                pos: Vector2 = Vector2(), angle: Float = 0f,
                scale: Float = 1f, color: Color = Color.WHITE) {
        e.add(texture, Texture(tex, width, height, pos, angle, scale, scale, color))
    }
    fun parent(entity: Entity) {
        e.add(parent, Parent(entity))
        if (!entity.contains(children)) {
            entity.add(children, Children())
        }
        entity.get(children).add(e)
    }

    fun child(entity: Entity) {
        entity.add(parent, Parent(e))
        if (!e.contains(children)) {
            e.add(children, Children())
        }
        e.get(children).add(entity)
    }

    fun lifeSpan(lifeSpan: Float, begin: Int){
        e.add(lifespan, LifeSpan(lifeSpan, begin))
    }

    fun text(txt: String, pos: Vector2, color: Color, font: BitmapFont) {
        e.add(text, Text(txt, Vector2(pos), font, color))
    }

    fun renderScript(spriteRenderScript: SpriteRenderScript) {
        e.add(spriteRender, spriteRenderScript)
    }
    fun position(vec: Vector2) {
        e.add(pos, Vector2(vec))
    }

    fun joint(jnt: Joint) {
        e.add(joint, jnt)
    }
}

fun Engine<Entity>.entity(init: EntityBuilder.() -> Unit): Entity {
    val builder = EntityBuilder()
    builder.init()
    val e = builder.build()
    this.add(e)
    return e
}