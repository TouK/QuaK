package pl.touk.liero.game.projectile

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Contact
import ktx.box2d.body
import ktx.box2d.filter
import ktx.math.vec2
import pl.touk.liero.Ctx
import pl.touk.liero.ecs.Entity
import pl.touk.liero.ecs.energy
import pl.touk.liero.entity.entity
import pl.touk.liero.game.cat_bulletRed
import pl.touk.liero.game.mask_bulletRed
import pl.touk.liero.script.Script

fun fireFragments(ctx: Ctx, pos: Vector2, direction: Vector2) {
    for (i in 1..8) {
        val dir = direction.rotate(40f*i)

        ctx.engine.entity {
            body(ctx.world.body(BodyDef.BodyType.DynamicBody) {
                position.set(pos.add(dir.nor()))
                gravityScale = 1f
                linearDamping = 0f
                bullet = true
                linearVelocity.set(dir.scl(ctx.params.fragzookasSpeed))
                circle(ctx.params.fragzookasSize) {
                    filter {
                        categoryBits = cat_bulletRed
                        maskBits = mask_bulletRed
                    }
                }
            })
            texture(ctx.gameAtlas.findRegion("fragzooka"), ctx.params.fragzookasSize, ctx.params.fragzookasSize)
            script(FragmentScript(ctx.params.fragzookaDamage))
        }
    }
}

class FragmentScript(val hitPoints: Float) : Script {
    override fun beginContact(me: Entity, other: Entity, contact: Contact) {
        me.dead = true
        if (other.contains(energy)) {
            other[energy].energy -= hitPoints
        }
    }
}
