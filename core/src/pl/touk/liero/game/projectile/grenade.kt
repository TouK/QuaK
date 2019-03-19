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
        texture(ctx.gameAtlas.findRegion("kaczka-toukowa-sprites-01"), ctx.params.grenadeSize, ctx.params.grenadeSize)
        lifeSpan(3000f, ctx.worldEngine.timeMs)
        val projectileAnimation = createProjectileAnimation(ctx)
        script(GrenadeScript(projectileAnimation, ctx))
    }
}

class GrenadeScript(val projectileAnimation: Animation<TextureRegion>,
                    val ctx: Ctx) : Script {

    var liveTime: Float = 0f

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

        explosion(ctx, me[body].position)

        super.beforeDestroy(me)
    }

    override fun update(me: Entity, timeStepSec: Float) {
        liveTime += timeStepSec
        val textureRegion = projectileAnimation.getKeyFrame(liveTime)
        me[texture].texture = textureRegion
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

private fun createProjectileAnimation(ctx: Ctx): Animation<TextureRegion> {
    val walkFrames: Array<TextureRegion> = Array()
    walkFrames.add(ctx.gameAtlas.findRegion("kaczka-toukowa-sprites-01"))
    walkFrames.add(ctx.gameAtlas.findRegion("kaczka-toukowa-sprites-02"))
    walkFrames.add(ctx.gameAtlas.findRegion("kaczka-toukowa-sprites-03"))
    walkFrames.add(ctx.gameAtlas.findRegion("kaczka-toukowa-sprites-04"))
    walkFrames.add(ctx.gameAtlas.findRegion("kaczka-toukowa-sprites-05"))
    walkFrames.add(ctx.gameAtlas.findRegion("kaczka-toukowa-sprites-06"))
    walkFrames.add(ctx.gameAtlas.findRegion("kaczka-toukowa-sprites-07"))
    walkFrames.add(ctx.gameAtlas.findRegion("kaczka-toukowa-sprites-08"))
    return Animation(0.025f, walkFrames, Animation.PlayMode.LOOP)
}