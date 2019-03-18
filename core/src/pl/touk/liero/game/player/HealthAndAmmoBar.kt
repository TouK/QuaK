package pl.touk.liero.game.player

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import pl.touk.liero.Ctx
import pl.touk.liero.ecs.Entity
import pl.touk.liero.ecs.SpriteRenderScript
import pl.touk.liero.ecs.body
import pl.touk.liero.ecs.energy

class HealthAndAmmoBar(ctx: Ctx, private val currentGun: Bazooka) : SpriteRenderScript {

    private val healthTexture = ctx.gameAtlas.findRegion("health")
    private val ammoTexture = ctx.gameAtlas.findRegion("ammo")

    override fun render(self: Entity, batch: SpriteBatch, timeStepSec: Float) {
        val pos = self[body].position
        val health = self[energy].energy / self[energy].total
        val ammo = currentGun.ammo / currentGun.totalAmmo
        batch.draw(healthTexture, pos.x - 0.5f, pos.y + 1.0f, health, 0.2f)
        batch.draw(ammoTexture, pos.x - 0.5f, pos.y + 0.8f, ammo, 0.2f)
    }

}
