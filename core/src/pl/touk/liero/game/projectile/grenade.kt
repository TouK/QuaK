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
import pl.touk.liero.system.SoundSystem
import pl.touk.liero.utils.querySquare
import kotlin.math.exp

fun fireGrenade(ctx: Ctx, pos: Vector2, direction: Vector2) {
    ctx.sound.playSoundSample(SoundSystem.SoundSample.QuackCounter)

    ctx.engine.entity {
        body(ctx.world.body(BodyDef.BodyType.DynamicBody) {
            position.set(pos.add(direction.nor()))
            gravityScale = 2f
            linearDamping = 0f
            bullet = true
            linearVelocity.set(direction.scl(ctx.params.grenadeSpeed))
            val vec = Vector2(direction.nor()).scl(0.8f)
            position.set(pos.add(vec))
            circle(ctx.params.grenadeSize / 4) {
                restitution = 0.8f
                filter {
                    categoryBits = cat_bulletRed
                    maskBits = mask_bulletRed
                }
            }
        })
        texture(ctx.gameAtlas.findRegion("kaczkogranat2"), ctx.params.grenadeSize, ctx.params.grenadeSize)
        lifeSpan(3000f, ctx.worldEngine.timeMs)
        script(GrenadeScript(ctx))
    }
}

class GrenadeScript(val ctx: Ctx) : Script {

    override fun beginContact(me: Entity, other: Entity, contact: Contact) {
        ctx.actions.schedule(0) { playBounceSound() }
        super.beginContact(me, other, contact)
    }

    private fun playBounceSound() {
        ctx.sound.playSoundSample(SoundSystem.SoundSample.Bounce)
    }

    override fun beforeDestroy(me: Entity) {
        ctx.worldCamera.shake()

        ctx.world.querySquare(me[body].position, ctx.params.grenadeRadius * 2) {fixture ->
            with(fixture.body.userData) {
                if (this != null && this is Entity && this.contains(energy) && this.contains(body)) {
                    val distance = this[body].position.sub(me[body].position).len()
                    if (distance <= ctx.params.grenadeRadius) {
                        this[energy].energy -= ctx.params.grenadeDamage
                    }
                }
            }
            true
        }

        ctx.actions.schedule(0) { explosion(ctx, me[body].position) }

        super.beforeDestroy(me)
    }

    override fun update(me: Entity, timeStepSec: Float) {
        me[texture].angleDeg = me[body].linearVelocity.angle()
        return
    }

    private fun explosion(ctx: Ctx, pos: Vector2) {
        ctx.engine.entity {
            position(pos)
            lifeSpan(1750f, ctx.worldEngine.timeMs)
            texture(ctx.gameAtlas.findRegion("frame0000"), ctx.params.grenadeRadius * 2.5f, ctx.params.grenadeRadius*2.5f)
            script(ExplosionScript(ctx))
        }
        ctx.sound.playSoundSample(SoundSystem.SoundSample.Explode)
    }
}