package pl.touk.liero.game.weapon

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import ktx.math.vec2
import pl.touk.liero.Ctx
import pl.touk.liero.ecs.Texture
import pl.touk.liero.game.projectile.fireBazooka
import pl.touk.liero.system.SoundSystem

class Bazooka(val ctx: Ctx) : Weapon {

    var cooldown: Float = ctx.params.bazookaCooldown
    val name: String = "BAZOOKA"

    override fun update(timeStepSec: Float) {
        if( cooldown > 0) {
            cooldown -= timeStepSec
            if(cooldown < 0) {
                cooldown = 0f
            }
        }

    }

    override fun canAttack(): Boolean {
        if (cooldown <= 0) {
            cooldown = ctx.params.bazookaCooldown
            return true
        }
        else
            return false
    }

    override fun attack(ctx: Ctx, pos: Vector2, direction: Vector2) {
        val quack1Or2 = MathUtils.random.nextInt(2)
        when (quack1Or2) {
            0 -> ctx.sound.playSoundSample(SoundSystem.SoundSample.Quack1)
            1 -> ctx.sound.playSoundSample(SoundSystem.SoundSample.Quack2)
        }
        fireBazooka(ctx, pos, direction)
    }

    override val texture: Texture =
        Texture(TextureRegion(ctx.gameAtlas.findRegion("kaczkozooka")), 2.6f, 1f, vec2(-0.1f, -0.4f), scaleX = 0.5f, scaleY = 0.5f)

    override fun percentageCooldown(): Float {
        return cooldown / ctx.params.bazookaCooldown
    }
}
