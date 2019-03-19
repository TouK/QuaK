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

fun fireFragzooka(ctx: Ctx, pos: Vector2, direction: Vector2) {
    ctx.engine.entity {
        body(ctx.world.body(BodyDef.BodyType.DynamicBody) {
            position.set(pos.add(direction.nor()))
            gravityScale = 1f
            linearDamping = 0f
            bullet = true
            linearVelocity.set(direction.scl(ctx.params.bazookasSpeed))
            val vec = Vector2(direction.nor()).scl(0.8f)
            position.set(pos.add(vec))
            circle(ctx.params.bazookasSize / 2) {
                filter {
                    categoryBits = cat_bulletRed
                    maskBits = mask_bulletRed
                }
            }
        })
        texture(ctx.gameAtlas.findRegion("kaczka-toukowa-sprites-01"), ctx.params.bazookasSize * 3, ctx.params.bazookasSize * 3)
        val projectileAnimation = createProjectileAnimation(ctx)
        script(FragzookaProjectileScript(projectileAnimation, ctx))
    }
}

fun fireFragments(ctx: Ctx, pos: Vector2) {
    for (i in 1..8) {
        val dir = Vector2.X.rotate(45f * i)
        ctx.engine.entity {
            body(ctx.world.body(BodyDef.BodyType.DynamicBody) {
                position.set(pos.add(dir.nor().scl(0.8f)))
                gravityScale = 1f
                linearDamping = 0f
                bullet = true
                linearVelocity.set(dir.scl(ctx.params.fragzookasSpeed))
                circle(ctx.params.fragzookasSize / 3) {
                    filter {
                        categoryBits = cat_bulletRed
                        maskBits = mask_bulletRed
                    }
                }
            })
            texture(ctx.gameAtlas.findRegion("kaczka-toukowa-sprites-01"), ctx.params.fragzookasSize, ctx.params.fragzookasSize)
            val projectileAnimation = createProjectileAnimation(ctx)
            script(FragzookaFragmentScript(ctx.params.fragzookaDamage, projectileAnimation, ctx))
        }

    }

}

class FragzookaProjectileScript(val projectileAnimation: Animation<TextureRegion>,
                                val ctx: Ctx): Script {

    var liveTime: Float = 0f

    override fun beginContact(me: Entity, other: Entity, contact: Contact) {
        me.dead = true
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

    private fun explosion(ctx: Ctx, pos: Vector2) {
        ctx.engine.entity {
            position(pos)
            lifeSpan(1750f, ctx.worldEngine.timeMs)
            texture(ctx.gameAtlas.findRegion("frame0000"), ctx.params.fragzookaRadius, ctx.params.fragzookaRadius)
            script(ExplosionScript(ctx))
        }
        ctx.sound.playSoundSample(SoundSystem.SoundSample.Explode)
        fireFragments(ctx, pos)
    }
}

class FragzookaFragmentScript(val hitPoints: Float,
                              val projectileAnimation: Animation<TextureRegion>,
                              val ctx: Ctx) : Script {

    var liveTime: Float = 0f

    override fun beginContact(me: Entity, other: Entity, contact: Contact) {
        if (me.dead) {
            return
        }
        me.dead = true
        if (other.contains(energy)) {
            other[energy].energy -= hitPoints
        }
        ctx.actions.schedule(0) { explosion(ctx, me[body].position) }
    }

    override fun update(me: Entity, timeStepSec: Float) {
        liveTime += timeStepSec
        val textureRegion = projectileAnimation.getKeyFrame(liveTime)
        me[texture].texture = textureRegion
        me[texture].angleDeg = me[body].linearVelocity.angle()
        return
    }

    private fun explosion(ctx: Ctx, pos:Vector2) {
        ctx.engine.entity {
            position(pos)
            lifeSpan(1750f, ctx.worldEngine.timeMs)
            texture(ctx.gameAtlas.findRegion("frame0000"), ctx.params.fragzookaRadius * 3, ctx.params.fragzookaRadius * 3)
            script(ExplosionScript(ctx))
        }
        ctx.world.querySquare(pos, ctx.params.bazookaRadius * 2) {fixture ->
            with(fixture.body.userData) {
                if (this != null && this is Entity && this.contains(energy) && this.contains(body)) {
                    val distance = this[body].position.sub(pos).len()
                    if (distance <= ctx.params.fragzookaRadius) {
                        //this[energy].energy -= ctx.params.bazookaExplosionDamage * (1 - distance / ctx.params.bazookaRadius)
                        this[energy].energy -= ctx.params.fragzookaDamage
                    }
                }
            }
            true
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