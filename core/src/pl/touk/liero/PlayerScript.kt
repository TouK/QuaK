package pl.touk.liero

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.MathUtils.PI
import com.badlogic.gdx.math.MathUtils.random
import com.badlogic.gdx.physics.box2d.Body
import ktx.math.vec2
import pl.touk.liero.ecs.Entity
import pl.touk.liero.ecs.body
import pl.touk.liero.ecs.texture
import pl.touk.liero.entity.entity
import pl.touk.liero.game.PlayerControl
import pl.touk.liero.game.cat_ground
import pl.touk.liero.game.gun.Gun
import pl.touk.liero.game.projectile.fireBazooka
import pl.touk.liero.script.LifeTimeScript
import pl.touk.liero.script.Script
import pl.touk.liero.system.SoundSystem
import pl.touk.liero.utils.queryRectangle
import pl.touk.liero.utils.then

class PlayerScript(val ctx: Ctx,
                   val control: PlayerControl,
                   val gun: Gun,
                   val weapon: Body,
                   val animation: Animation<TextureRegion>,
                   val idleAnimation: Animation<TextureRegion>) : Script {


    var movingAnimationTime: Float = 0f
    var idleAnimationTime: Float = 0f
    var isRight = true

    override fun update(me: Entity, timeStepSec: Float) {
        val myBody = me[body]
        val myTexture = me[texture]

        gun.update(timeStepSec)

        control.fireJustPressed.then {

            ctx.engine.entity {
                text("kwa", myBody.position, Color.WHITE, ctx.smallFont)
                script(LifeTimeScript(1f))
            }
            if (gun.shoot(1)) {
                val quack1Or2 = random.nextInt(2)

                fireBazooka(ctx, me[body].position, vec2(1f, 0f).rotateRad(weapon.angle))

                when (quack1Or2) {
                    0 -> ctx.sound.playSoundSample(SoundSystem.SoundSample.Quack1)
                    1 -> ctx.sound.playSoundSample(SoundSystem.SoundSample.Quack2)
                }
            } else {
                ctx.sound.playSoundSample(SoundSystem.SoundSample.Hurt)
            }
        }

        // regulator proporcjonalny (PID bez ID) do prędkości x
        val v = MathUtils.clamp(control.xAxis, -1f, 1f) * ctx.params.playerSpeed
        val f = (v - myBody.linearVelocity.x) * ctx.params.playerPidProportional * myBody.mass
        MathUtils.clamp(f, -ctx.params.playerMaxForce, ctx.params.playerMaxForce)
        myBody.applyForceToCenter(f, 0f, true)

        if ((control.left && isRight) || (control.right && !isRight)) {
            myTexture.flipX()
            val weaponEntity = weapon.userData as Entity
            weaponEntity[texture].flipY()
            isRight = !isRight
            weapon.setTransform(weapon.position, PI - weapon.angle)
        }
        val direction = if (isRight) -1 else 1

        control.up then {
            if (weapon.angle < ctx.params.weaponUpperAngle || MathUtils.PI - weapon.angle < ctx.params.weaponUpperAngle) {
                weapon.angularVelocity = ctx.params.weaponRotationSpeed * control.yAxis * direction
            }
        }

        control.down then {
            if (weapon.angle > ctx.params.weaponLowerAngle && MathUtils.PI - weapon.angle > ctx.params.weaponLowerAngle) {
                weapon.angularVelocity = ctx.params.weaponRotationSpeed * control.yAxis * direction
            }
        }

        control.jumpJustPressed.then {
            val ground = ctx.world.queryRectangle(myBody.position.sub(0f, ctx.params.playerSize / 2), ctx.params.playerSize, 0.2f, cat_ground)
            if (ground != null) {
                myBody.setLinearVelocity(myBody.linearVelocity.x, ctx.params.playerJumpSpeed)
                myBody.gravityScale = ctx.params.playerGravityScaleInAir
            }
        }
        if (!control.jump || myBody.linearVelocity.y <= 0f) {
            myBody.gravityScale = ctx.params.playerGravityScale
        }

        renderMovement(me, timeStepSec)
    }

    private fun renderMovement(me: Entity, timeStepSec: Float) {

        if (kotlin.math.abs(me[body].linearVelocity.x) > ctx.params.idleVelocityLimit) {
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

    override fun beforeDestroy(me: Entity) {
        weapon.fixtureList.forEach { it.isSensor = false }
    }
}
