package pl.touk.liero.game.projectile

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Contact
import com.badlogic.gdx.utils.Array
import ktx.box2d.body
import ktx.box2d.filter
import pl.touk.liero.Ctx
import pl.touk.liero.ecs.Entity
import pl.touk.liero.ecs.body
import pl.touk.liero.ecs.energy
import pl.touk.liero.ecs.texture
import pl.touk.liero.entity.entity
import pl.touk.liero.game.cat_bulletRed
import pl.touk.liero.game.mask_bulletRed
import pl.touk.liero.script.Script
import pl.touk.liero.utils.querySquare

fun fireBazooka(ctx: Ctx, pos: Vector2, direction: Vector2) {
    ctx.engine.entity {
        body(ctx.world.body(BodyDef.BodyType.DynamicBody) {
            gravityScale = 0f
            linearDamping = 0f
            bullet = true
            linearVelocity.set(direction.scl(ctx.params.bazookasSpeed))
            val vec = Vector2(direction.nor()).scl(0.8f)
            position.set(pos.add(vec))
            circle(ctx.params.bazookasSize) {
                filter {
                    categoryBits = cat_bulletRed
                    maskBits = mask_bulletRed
                }
            }
        })
        texture(ctx.gameAtlas.findRegion("projectile0"), ctx.params.bazookasSize*6f, ctx.params.bazookasSize*5f)
        val projectileAnimation = createProjectileAnimation(ctx)
        script(BazookaProjectileScript(ctx.params.bazookaDirectDamage, projectileAnimation, ctx))
    }
}

class BazookaProjectileScript(val hitPoints: Float,
                              val projectileAnimation: Animation<TextureRegion>,
                              val ctx: Ctx) : Script {

    var liveTime: Float = 0f

    override fun beginContact(me: Entity, other: Entity, contact: Contact) {
        me.dead = true
        if (other.contains(energy)) {
            other[energy].energy -= hitPoints
        }
        ctx.actions.schedule(0) { explosion(ctx, me[body].position) }
    }

    override fun beforeDestroy(me: Entity) {
        ctx.worldCamera.shake()
        super.beforeDestroy(me)
    }

    override fun update(me: Entity, timeStepSec: Float) {
        liveTime += timeStepSec
        val textureRegion = projectileAnimation.getKeyFrame(liveTime)
        me[texture].texture = textureRegion
        me[texture].angleDeg = me[body].linearVelocity.angle()
        return
    }
}

fun explosion(ctx: Ctx, pos:Vector2) {
    ctx.engine.entity {
        position(pos)
        lifeSpan(0.5f, ctx.worldEngine.timeMs)
        texture(ctx.gameAtlas.findRegion("explosion"), ctx.params.bazookaRadius * 2, ctx.params.bazookaRadius * 2)
    }
    ctx.world.querySquare(pos, ctx.params.bazookaRadius * 2) {fixture ->
        with(fixture.body.userData) {
            if (this != null && this is Entity && this.contains(energy) && this.contains(body)) {
                val distance = this[body].position.sub(pos).len()
                if (distance <= ctx.params.bazookaRadius) {
                    //this[energy].energy -= ctx.params.bazookaExplosionDamage * (1 - distance / ctx.params.bazookaRadius)
                    this[energy].energy -= ctx.params.bazookaExplosionDamage
                }
            }
        }
        true
    }
}

private fun createProjectileAnimation(ctx: Ctx): Animation<TextureRegion> {
    val walkFrames: Array<TextureRegion> = Array()
    walkFrames.add(ctx.gameAtlas.findRegion("projectile0"))
    walkFrames.add(ctx.gameAtlas.findRegion("projectile1"))
    walkFrames.add(ctx.gameAtlas.findRegion("projectile2"))
    walkFrames.add(ctx.gameAtlas.findRegion("projectile3"))
    return Animation(0.025f, walkFrames, Animation.PlayMode.LOOP)
}
