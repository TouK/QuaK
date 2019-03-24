package pl.touk.liero.system

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.BodyDef
import ktx.box2d.body
import ktx.box2d.filter
import ktx.math.minus
import ktx.math.vec2
import pl.touk.liero.Ctx
import pl.touk.liero.ecs.Entity
import pl.touk.liero.ecs.body
import pl.touk.liero.entity.entity
import pl.touk.liero.game.cat_blood
import pl.touk.liero.game.mask_blood
import pl.touk.liero.script.Script
import kotlin.random.Random

class BloodScript(val ctx: Ctx) : Script {
    private var timer = 0f
    private val random = Random(ctx.worldEngine.timeMs)

//    override fun update(me: Entity, timeStepSec: Float) {
////        timer += timeStepSec
////        val energy = me[energy]
////
////        if (energy.energy < energy.total * ctx.params.largeBloodThreshold) {
////            if (timer >= ctx.params.bloodCooldown) {
////                hemorrhage(10, ctx, me[body].position)
////            }
////        } else if (energy.energy < energy.total * ctx.params.mediumBloodThreshold) {
////            if (timer >= ctx.params.bloodCooldown) {
////                hemorrhage(6, ctx, me[body].position)
////            }
////        } else if (energy.energy < energy.total) {
////            if (timer >= ctx.params.bloodCooldown) {
////                hemorrhage(3, ctx, me[body].position)
////            }
////        }
////
////        if (timer >= ctx.params.bloodCooldown)
////            timer = 0f
//    }

    override fun beforeDestroy(me: Entity) {
        bloodSplatter(200, ctx, me[body].position)
    }

    fun bloodSplatter(count: Int, ctx: Ctx, pos: Vector2) {
        for (i in 1..count) {

            val dir = Vector2(random.nextDouble(-0.5, 0.5).toFloat(), random.nextDouble(-2.0, 2.0).toFloat()).nor()

            ctx.engine.entity {
                body(ctx.world.body(BodyDef.BodyType.DynamicBody) {
                    position.set(pos)
                    gravityScale = 1f
                    linearDamping = 0f
                    linearVelocity.set(dir.scl(random.nextDouble(0.05, ctx.params.bloodSpeed.toDouble()).toFloat()))
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

    fun hemorrhage(count: Int, ctx: Ctx, pos: Vector2) {
        for (i in 1..count) {
            ctx.actions.schedule((i - 1) * 0.125f) {
                ctx.engine.entity {
                    body(ctx.world.body(BodyDef.BodyType.DynamicBody) {
                        position.set(pos.minus(vec2(0.3f, 0.3f)))
                        gravityScale = 1f
                        linearDamping = 0f
                        circle(ctx.params.bloodSize / 2 * 0.6f) {
                            friction = 0.9f
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
    }
}
