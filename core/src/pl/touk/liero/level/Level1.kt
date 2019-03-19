package pl.touk.liero.level

import com.badlogic.gdx.graphics.Color
import pl.touk.liero.Ctx
import pl.touk.liero.game.player.createPlayer
import pl.touk.liero.game.weapon.createWeapon
import pl.touk.liero.utils.overwrite

class Level1 : Level {
    override var width = 32f
    override var height = 18f

    override fun start(ctx: Ctx) {
        ctx.params.overwrite(LevelParams().also {
            it.playerColor = Color.BLACK
        })
        val loader = LevelMapLoader(ctx).also {
            it.loadMap("forest")
        }
        width = loader.width
        height = loader.height

        createBounds(ctx, width, height)
        createPlayer(ctx, width * 0.2f, 2f, ctx.leftPlayerControl)
        createPlayer(ctx, width * 0.8f, 2f, ctx.rightPlayerControl)
    }

    override fun dispose() {

    }
}
