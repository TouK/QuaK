package pl.touk.liero.game.weapon

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import ktx.math.vec2
import pl.touk.liero.Ctx
import pl.touk.liero.ecs.Texture
import pl.touk.liero.game.projectile.fireFragzooka
import pl.touk.liero.game.projectile.fireGrenade
import pl.touk.liero.system.SoundSystem

class Grenade(val ctx: Ctx) : Weapon {
    var cooldown: Float = ctx.params.grenadeCooldown
    val name: String = "GRENADE"

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
            cooldown = ctx.params.grenadeCooldown
            return true
        }
        else
            return false
    }

    override fun attack(ctx: Ctx, pos: Vector2, direction: Vector2) {
        fireGrenade(ctx, pos, direction)
    }

    override val texture: Texture =
            Texture(TextureRegion(ctx.gameAtlas.findRegion("kaczka-toukowa-sprites-01")), 2.6f, 1f, vec2(0f, -0.3f), scaleX = 0.5f, scaleY = 0.5f)

    override fun percentageCooldown(): Float {
        return cooldown / ctx.params.grenadeCooldown
    }
}
