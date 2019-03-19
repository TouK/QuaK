package pl.touk.liero

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.MathUtils.PI
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import ktx.box2d.body
import ktx.box2d.filter
import ktx.math.vec2
import pl.touk.liero.ecs.*
import pl.touk.liero.entity.entity
import pl.touk.liero.game.*
import pl.touk.liero.game.player.DeadPlayerScript
import pl.touk.liero.game.player.PlayerState
import pl.touk.liero.game.player.createPlayer
import pl.touk.liero.script.LifeTimeScript
import pl.touk.liero.script.Script
import pl.touk.liero.system.SoundSystem
import pl.touk.liero.utils.queryRectangle
import pl.touk.liero.utils.then
import kotlin.random.Random

class PlayerScript(val ctx: Ctx,
                   val control: PlayerControl,
                   val playerState: PlayerState,
                   val animation: Animation<TextureRegion>,
                   val idleAnimation: Animation<TextureRegion>,
                   val hurtAnimation: Animation<TextureRegion>,
                   val team: String) : Script {


    var movingAnimationTime: Float = 0f
    var idleAnimationTime: Float = 0f
    var hurtAnimationTime: Float = 0f
    var isRight = true
    var hurt = false
    var lastEnergy: Float = 0f
    val _random = Random(ctx.worldEngine.timeMs)
    var bleeds = false
    var hasDoubleJump = false
    fun weaponBody(me: Entity) = me[joint].bodyB

    override fun update(me: Entity, timeStepSec: Float) {
        val myBody = me[body]
        val myTexture = me[texture]
        val weapon = weaponBody(me)

        playerState.currentWeapon.update(timeStepSec)

        control.fire.then {

            ctx.engine.entity {
                script(LifeTimeScript(1f))
            }
            if (playerState.currentWeapon.canAttack()) {
                playerState.currentWeapon.attack(ctx, me[body].position, vec2(1f, 0f).rotateRad(weapon.angle))
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

        if (control.up) {
            if (weapon.angle < ctx.params.weaponUpperAngle || MathUtils.PI - weapon.angle < ctx.params.weaponUpperAngle) {
                weapon.angularVelocity = ctx.params.weaponRotationSpeed * control.yAxis * direction
            }
        } else if (control.down) {
            if (weapon.angle > ctx.params.weaponLowerAngle && MathUtils.PI - weapon.angle > ctx.params.weaponLowerAngle) {
                weapon.angularVelocity = ctx.params.weaponRotationSpeed * control.yAxis * direction
            }
        } else {
            weapon.angularVelocity = 0f
        }

        val onGround = checkOnGround(myBody)

        control.jumpJustPressed.then {
            if (onGround || hasDoubleJump) {
                myBody.setLinearVelocity(myBody.linearVelocity.x, ctx.params.playerJumpSpeed)
                myBody.gravityScale = ctx.params.playerGravityScaleInAir
            }
        }
        if (!control.jump || myBody.linearVelocity.y <= 0f) {
            myBody.gravityScale = ctx.params.playerGravityScale
        }
        if (onGround) {
            hasDoubleJump = true
        }

        control.changeWeaponJustPressed then {
            val currWeaponIndex = playerState.weapons.indexOf(playerState.currentWeapon)
            val nextWeaponIndex = (currWeaponIndex + 1) % playerState.weapons.size
            playerState.currentWeapon = playerState.weapons[nextWeaponIndex]
            val weaponEntity = weaponBody(me).userData as Entity
            weaponEntity[texture] = playerState.currentWeapon.texture.copy()
            if (!isRight) {
                weaponEntity[texture].flipY()
            }
        }

        checkIfHurt(me)
        renderMovement(me, timeStepSec)
    }

    private fun checkOnGround(myBody: Body): Boolean {
        val ground = ctx.world.queryRectangle(myBody.position.sub(0f, ctx.params.playerSize / 2), ctx.params.playerSize, 0.2f, cat_ground)
        return  ground != null
    }

    private fun renderMovement(me: Entity, timeStepSec: Float) {

        if (hurt) {
            hurtAnimationTime += timeStepSec

            if (!bleeds) {
                bloodSplatter(15, ctx, me[body].position)
                bleeds = true
            }
            val textureRegion = hurtAnimation.getKeyFrame(hurtAnimationTime)
            me[texture].texture = textureRegion
            checkIfShouldStopHurt()
            return
        }
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

    private fun checkIfHurt(me: Entity) {
        if(lastEnergy > me[energy].energy) {
            ctx.sound.playSoundSample(SoundSystem.SoundSample.DuckHowl)
            hurt = true
        }
        lastEnergy = me[energy].energy
    }

    fun bloodSplatter(count: Int, ctx: Ctx, pos: Vector2) {
        for (i in 1..count) {

            val dir = Vector2(_random.nextDouble(-0.5, 0.5).toFloat(), _random.nextDouble(-2.0, 2.0).toFloat()).nor()

            ctx.engine.entity {
                body(ctx.world.body(BodyDef.BodyType.DynamicBody) {
                    position.set(pos)
                    gravityScale = 1f
                    linearDamping = 0f
                    linearVelocity.set(dir.scl(_random.nextDouble(0.05, ctx.params.bloodSpeed.toDouble()).toFloat()))
                    circle(ctx.params.bloodSize / 2 * 0.6f) {
                        isSensor = true
                        filter {
                            categoryBits = cat_blood
                            maskBits = mask_blood
                        }
                    }
                })
                lifeSpan(ctx.params.bloodLifeSpan, ctx.worldEngine.timeMs)
                texture(ctx.gameAtlas.findRegion("blood"), ctx.params.bloodSize, ctx.params.bloodSize)
            }
        }
    }

    private fun checkIfShouldStopHurt() {
        if (hurtAnimationTime > ctx.params.hurtAnimationTime) {
            hurt = false
            bleeds = false
            hurtAnimationTime = 0f
        }
    }

    override fun beforeDestroy(me: Entity) {
        ctx.sound.playSoundSample(SoundSystem.SoundSample.DuckLong)
        weaponBody(me).fixtureList.forEach { it.isSensor = false }
        creatDeadBody(me)
        respawn()
    }

    private fun respawn() {
        if(team == "left") {
            ctx.rightFrags++
            if(ctx.rightFrags < 5) {
                createPlayer(ctx, ctx.level.width * 0.2f, 4f, ctx.leftPlayerControl, "left")
            }
        } else {
            ctx.leftFrags++
            if(ctx.leftFrags < 5) {
                ctx.actions.schedule(0.5f) {
                    createPlayer(ctx, ctx.level.width * 0.8f, 4f, ctx.rightPlayerControl, "right")
                }
            }
        }
    }

    private fun creatDeadBody(me: Entity) {
        val playerBody = ctx.world.body(BodyDef.BodyType.DynamicBody) {
            position.set(me[body].position.x,me[body].position.y)
            linearDamping = 0f
            fixedRotation = true
            gravityScale = ctx.params.playerGravityScale
            box(width = 0.2f, height = 0.2f) {
                density = 1f
                restitution = 0f
                friction = 0.8f
                filter {
                    categoryBits = cat_red
                    maskBits = cat_ground
                }
            }
        }

        ctx.engine.entity {
            body(playerBody)
            texture(ctx.gameAtlas.findRegion("blobDead0"), ctx.params.playerSize, ctx.params.playerSize, scale = 1.6f)
            script(DeadPlayerScript(ctx))
        }
    }
}

class RotateScript(val omegaDegPerSec: Float) : Script {
    override fun update(me: Entity, timeStepSec: Float) {
        me[texture].angleDeg += timeStepSec * omegaDegPerSec
    }
}
