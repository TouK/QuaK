package pl.touk.liero

import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import ktx.math.vec2
import com.badlogic.gdx.math.MathUtils.random
import pl.touk.liero.ecs.Entity
import pl.touk.liero.ecs.body
import pl.touk.liero.ecs.joint
import pl.touk.liero.ecs.texture
import pl.touk.liero.entity.entity
import pl.touk.liero.script.Script
import pl.touk.liero.game.PlayerControl
import pl.touk.liero.game.gun.Gun
import pl.touk.liero.game.cat_ground
import pl.touk.liero.game.projectile.fireBazooka
import pl.touk.liero.gdx.ifJustPressed
import pl.touk.liero.script.LifeTimeScript
import pl.touk.liero.system.SoundSystem
import pl.touk.liero.utils.queryRectangle
import pl.touk.liero.utils.then

class PlayerScript(val ctx: Ctx,
                   val control: PlayerControl,
                   val gun: Gun,
                   val animation: Animation<TextureRegion>,
                   val idleAnimation: Animation<TextureRegion>) : Script {

    var gunAngleDeg = 0f
    val pidProportional = 10f
    val maxForce = 80f
    var movingAnimationTime: Float = 0f
    var idleAnimationTime: Float = 0f

    override fun update(me: Entity, timeStepSec: Float) {
        val b = me[body]
        val weapon = me[joint].bodyB
        gun.update(timeStepSec)
        control.fireJustPressed.then {
            ctx.engine.entity {
                text("kwa", b.position, Color.WHITE, ctx.smallFont)
                script(LifeTimeScript(1f))
            }
            if(gun.shoot(1)) {
                val quack1Or2 = random.nextInt(2)
                fireBazooka(ctx, me[body].position, vec2(1f, 0f).rotateRad(weapon.angle))
                when(quack1Or2) {
                    0 -> ctx.sound.playSoundSample(SoundSystem.SoundSample.Quack1)
                    1 -> ctx.sound.playSoundSample(SoundSystem.SoundSample.Quack2)
                }
            } else {
                ctx.sound.playSoundSample(SoundSystem.SoundSample.Hurt)
            }
        }

        val v = MathUtils.clamp(control.xAxis, -1f, 1f) * ctx.params.playerSpeed
        val f = (v - b.linearVelocity.x) * pidProportional * b.mass
        MathUtils.clamp(f, -maxForce, maxForce)
        b.applyForceToCenter(f, 0f, true)

        control.left.then {
            b.setLinearVelocity(-ctx.params.playerSpeed, b.linearVelocity.y)
        }
        control.right.then {
            b.setLinearVelocity(ctx.params.playerSpeed, b.linearVelocity.y)
        }
        control.up then {
            weapon.angularVelocity = ctx.params.weaponRotationSpeed
        }
        control.down then {
            weapon.angularVelocity = -ctx.params.weaponRotationSpeed
        }
        // todo: podpiąć do control.jumpJustPressed, jak będzie gotowe
        Input.Keys.ENTER.ifJustPressed {
            val ground = ctx.world.queryRectangle(b.position.sub(0f, ctx.params.playerSize / 2), ctx.params.playerSize, 0.2f, cat_ground)
            if(ground != null) {
                b.setLinearVelocity(b.linearVelocity.x, ctx.params.playerJumpSpeed)
            }
        }

        renderMovement(me, timeStepSec)
    }

    private fun renderMovement(me: Entity, timeStepSec: Float) {

        if(me[body].linearVelocity.x > 0) {
            idleAnimationTime = 0f
            movingAnimationTime += timeStepSec
            val textureRegion = animation.getKeyFrame(movingAnimationTime)
            me[texture].texture = textureRegion
            return
        }
        if(me[body].linearVelocity.x < 0) {
            movingAnimationTime += timeStepSec
            idleAnimationTime = 0f
            val textureRegion = animation.getKeyFrame(movingAnimationTime)
            me[texture].texture = textureRegion
            return
        }
        movingAnimationTime = 0f
        idleAnimationTime += timeStepSec
        val textureRegion = idleAnimation.getKeyFrame(idleAnimationTime)
        me[texture].texture = textureRegion
    }
}
