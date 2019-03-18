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

fun swingSword(ctx: Ctx, pos: Vector2, direction: Vector2) {
    ctx.engine.entity {
        body(ctx.world.body(BodyDef.BodyType.DynamicBody) {
            val vec = Vector2(direction.nor()).scl(1.5f)
            position.set(pos.add(vec))
            gravityScale = 0f
            linearDamping = 0f
            circle(ctx.params.swordRange) {
                filter {
                    categoryBits = cat_bulletRed
                    maskBits = mask_bulletRed
                }
            }
        })
        texture(ctx.gameAtlas.findRegion("sword"), ctx.params.swordRange, ctx.params.swordRange)
        script(SwordScript(ctx.params.swordDamage))
        lifeSpan(ctx.params.swordLifeSpan, ctx.worldEngine.timeMs)
    }
}

class SwordScript(val damage: Float) : Script {
    override fun beginContact(me: Entity, other: Entity, contact: Contact) {
        me.dead = true
        if (other.contains(energy)) {
            other[energy].energy -= damage
        }
    }
}
