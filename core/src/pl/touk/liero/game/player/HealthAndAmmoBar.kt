package pl.touk.liero.game.player

import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.physics.box2d.Body
import pl.touk.liero.Ctx
import pl.touk.liero.ecs.Entity
import pl.touk.liero.ecs.SpriteRenderScript
import pl.touk.liero.ecs.body
import pl.touk.liero.ecs.energy
import kotlin.math.cos
import kotlin.math.sin

class HealthAndAmmoBar(ctx: Ctx, private val playerState: PlayerState, val weaponBody: Body) : SpriteRenderScript {

    private val healthTexture = ctx.gameAtlas.findRegion("health")
    private val ammoTexture = ctx.gameAtlas.findRegion("ammo")
    private val crosshair = ctx.gameAtlas.findRegion("celownik")

    override fun render(self: Entity, batch: SpriteBatch, timeStepSec: Float) {
        val pos = self[body].position
        val health = self[energy].energy / self[energy].total
        val ammo = playerState.currentWeapon.percentageCooldown()
        batch.draw(healthTexture, pos.x - 0.5f, pos.y + 1.0f, health, 0.2f)
        batch.draw(ammoTexture, pos.x - 0.5f, pos.y + 0.8f, ammo, 0.2f)

        val angle = weaponBody.angle
        val r = 2.5f

        batch.draw(crosshair, pos.x + (r * cos(angle)) - 0.25f, pos.y + (r * sin(angle))- 0.25f, 0.5f, 0.5f)
    }

}
