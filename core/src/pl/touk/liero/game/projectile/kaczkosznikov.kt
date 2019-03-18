package pl.touk.liero.game.projectile

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Contact
import ktx.box2d.body
import ktx.box2d.filter
import pl.touk.liero.Ctx
import pl.touk.liero.ecs.Entity
import pl.touk.liero.ecs.energy
import pl.touk.liero.entity.entity
import pl.touk.liero.game.cat_bulletRed
import pl.touk.liero.game.mask_bulletRed
import pl.touk.liero.script.Script

fun fireKaczkosznikov(ctx: Ctx, position: Vector2, direction: Vector2) {
    ctx.engine.entity {
        body(ctx.world.body(BodyDef.BodyType.DynamicBody) {
            position.set(1f, 1f)
            gravityScale = 0f
            linearDamping = 0f
            linearVelocity.set(direction.scl(ctx.params.kaczkosznikovSpeed))
            circle(ctx.params.kaczkosznikovSize) {
                filter {
                    categoryBits = cat_bulletRed
                    maskBits = mask_bulletRed
                }
            }
        })
        texture(ctx.gameAtlas.findRegion("kaczkosznikov"), ctx.params.kaczkosznikovSize, ctx.params.kaczkosznikovSize)
        script(BazookaScript(ctx.params.kaczkosznikovDamage))
    }
}

class KaczkosznikovScript(val damage: Float) : Script {
    override fun beginContact(me: Entity, other: Entity, contact: Contact) {
        me.dead = true
        if (other.contains(energy)) {
            other[energy].energy -= damage
        }
    }
}
