package pl.touk.liero.game.weapon

import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import ktx.math.vec2
import pl.touk.liero.Ctx
import pl.touk.liero.ecs.Texture
import pl.touk.liero.game.projectile.fireBazooka

class Bazooka(val ctx: Ctx) : Weapon {
    var ammo: Int = 0
        get() = field
    val totalAmmo: Float = ctx.params.bazookaAmmo.toFloat()
    var lastUpdate: Float = 0f
    var cooldown: Float = ctx.params.bazookaCooldown
    val name: String = "BAZOOKA"

    override fun update(timeStepSec: Float) {
        if( cooldown > 0) {
            cooldown -= timeStepSec
            if(cooldown < 0) {
                cooldown = 0f
            }
        }


        if (lastUpdate > 1) {
            ammo = if (ammo < totalAmmo) ammo + 1 else ammo
            lastUpdate = 0f
        } else {
            lastUpdate += timeStepSec
        }

    }

    override fun preAttack(ammoChange: Int): Boolean {
        if (cooldown <= 0) {
            cooldown = ctx.params.bazookaCooldown

            if (ammo > 0) {
                ammo -= ammoChange
                return true
            } else
                return false
        }
        else
            return false
    }

    override fun attack(ctx: Ctx, pos: Vector2, direction: Vector2) {
        fireBazooka(ctx, pos, direction)
    }

    override val texture: Texture =
        Texture(TextureRegion(ctx.gameAtlas.findRegion("kaczkozooka")), 2.6f, 1f, vec2(0f, -0.3f))

    override fun percentageCooldown(): Float {
        return cooldown / ctx.params.bazookaCooldown
    }
}
