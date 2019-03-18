package pl.touk.liero

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.MathUtils.random
import com.badlogic.gdx.math.Vector2
import ktx.math.vec2
import pl.touk.liero.ecs.Entity
import pl.touk.liero.ecs.body
import pl.touk.liero.ecs.joint
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
                   val animation: Animation<TextureRegion>,
                   val idleAnimation: Animation<TextureRegion>) : Script {


    var movingAnimationTime: Float = 0f
    var idleAnimationTime: Float = 0f

    override fun update(me: Entity, timeStepSec: Float) {
        val myBody = me[body]
        val weapon = me[joint].bodyB
        val myTexture = me[texture]

        gun.update(timeStepSec)

        control.fireJustPressed.then {

            ctx.engine.entity {
                text("kwa", myBody.position, Color.WHITE, ctx.smallFont)
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
        val f = (v - myBody.linearVelocity.x) * ctx.params.playerPidProportional * myBody.mass
        MathUtils.clamp(f, -ctx.params.playerMaxForce, ctx.params.playerMaxForce)
        myBody.applyForceToCenter(f, 0f, true)

        control.left.then {
            myTexture.flipY = true
            if(myBody.angle == 0f) {
                myBody.setTransform(myBody.position, -MathUtils.PI)
                val newWeaponAngle = Vector2(-weapon.transform.orientation.x, weapon.transform.orientation.y).angleRad()
                weapon.setTransform(weapon.position, newWeaponAngle)
            }
        }
        control.right.then {
            myTexture.flipY = false
            if(myBody.angle == -MathUtils.PI) {
                myBody.setTransform(myBody.position, 0f)
                val newWeaponAngle = Vector2(-weapon.transform.orientation.x, weapon.transform.orientation.y).angleRad()
                weapon.setTransform(weapon.position, newWeaponAngle)
            }
        }
        control.up then {
            if(myBody.angle == -MathUtils.PI) {
                weapon.angularVelocity = -ctx.params.weaponRotationSpeed
            } else {
                weapon.angularVelocity = ctx.params.weaponRotationSpeed
            }
        }
        control.down then {
            if(myBody.angle == -MathUtils.PI) {
                weapon.angularVelocity = ctx.params.weaponRotationSpeed
            } else {
                weapon.angularVelocity = -ctx.params.weaponRotationSpeed
            }
        }
        control.jumpJustPressed.then {
            val ground = ctx.world.queryRectangle(myBody.position.sub(0f, ctx.params.playerSize / 2), ctx.params.playerSize, 0.2f, cat_ground)
            if(ground != null) {
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

        if(kotlin.math.abs(me[body].linearVelocity.x) > ctx.params.idleVelocityLimit) {
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
