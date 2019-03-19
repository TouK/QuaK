package pl.touk.liero.game.gun

import pl.touk.liero.Ctx
import pl.touk.liero.game.gun.Gun

class Bazooka(val ctx: Ctx) : Gun {

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

    override fun shoot(ammoChange: Int): Boolean {
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

    override fun percentageCooldown(): Float {
        return cooldown / ctx.params.bazookaCooldown
    }
}
