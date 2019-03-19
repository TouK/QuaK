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

fun fireGun(ctx: Ctx, pos: Vector2, direction: Vector2) {
    ctx.engine.entity {
        body(ctx.world.body(BodyDef.BodyType.DynamicBody) {
            gravityScale = 0f
            linearDamping = 0f
            bullet = true
            linearVelocity.set(direction.scl(ctx.params.gunSpeed))
            val vec = Vector2(direction.nor()).scl(0.8f)
            position.set(pos.add(vec))
            angle = direction.angleRad()
            circle(ctx.params.gunSize / 2) {
                filter {
                    categoryBits = cat_bulletRed
                    maskBits = mask_bulletRed
                }
            }
        })
        texture(ctx.gameAtlas.findRegion("kaczka-toukowa-sprites-01"), ctx.params.gunSize, ctx.params.gunSize)
        val projectileAnimation = createProjectileAnimation(ctx)
        script(GunProjectileScript(ctx.params.gunDamage, projectileAnimation, ctx))
    }
}

class GunProjectileScript(val damage: Float,
                          val projectileAnimation: Animation<TextureRegion>,
                          val ctx: Ctx) : Script {

    var liveTime: Float = 0f

    override fun beginContact(me: Entity, other: Entity, contact: Contact) {
        me.dead = true
        if (other.contains(energy)) {
            other[energy].energy -= damage
        }
    }

    override fun update(me: Entity, timeStepSec: Float) {
        liveTime += timeStepSec
        val textureRegion = projectileAnimation.getKeyFrame(liveTime)
        me[texture].texture = textureRegion
        me[texture].angleDeg = me[body].linearVelocity.angle()
        return
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
