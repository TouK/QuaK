package pl.touk.liero.ecs

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.Joint
import ktx.collections.GdxArray

class Energy(var total: Float,
             var energy: Float = total) {
    operator fun minusAssign(amount: Float) {
        energy = Math.max(0f, energy - amount)
    }
}

class Parent(val parent: Entity)

class Children {
    val children = GdxArray<Entity>()
    fun add(e: Entity) {
        children.add(e)
    }

    fun remove(e: Entity) {
        children.removeValue(e, true)
    }
}

class Texture(val texture: TextureRegion,
              val width: Float, val height: Float,
              val pos: Vector2, var angleDeg: Float = 0f,
              var scaleX: Float = 1f, var scaleY: Float = 1f,
              color: Color = Color.WHITE) {
    val color = Color(color)
}

class Text(var text: String,
           val pos: Vector2,
           val bitmapFont: BitmapFont,
           color: Color) {
    val color = Color(color)
}

class LifeSpan(var lifeSpan: Float,
               var begin: Int)

interface SpriteRenderScript {
    fun render(self: Entity, batch: SpriteBatch, timeStepSec: Float)
}

private var i = 0
val body = ComponentTag<Body>(i++)
val pos = ComponentTag<Vector2>(i++)
val energy = ComponentTag<Energy>(i++)
val texture = ComponentTag<Texture>(i++)
val parent = ComponentTag<Parent>(i++)
val children = ComponentTag<Children>(i++)
val spriteRender = ComponentTag<SpriteRenderScript>(i++)
val text = ComponentTag<Text>(i++)
val lifespan = ComponentTag<LifeSpan>(i++)
val joint = ComponentTag<Joint>(i++)
