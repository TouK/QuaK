package pl.touk.liero.level

import com.badlogic.gdx.graphics.Color
import pl.touk.liero.Ctx
import pl.touk.liero.game.player.createPlayer
import pl.touk.liero.game.weapon.createWeapon
import pl.touk.liero.utils.overwrite

class Level2 : Level {
    override var width = 32f
    override var height = 18f

    override fun start(ctx: Ctx) {
        ctx.params.overwrite(LevelParams().also {
            it.playerColor = Color.BLACK
        })
        val weapon1 = createWeapon(ctx, 0f, 0f)
        val weapon2 = createWeapon(ctx, 0f, 0f)

        createBounds(ctx, width, height)
        createPlayer(ctx, width * 0.2f, 2f, ctx.keyboardPlayerControl, weapon1)
        createPlayer(ctx, width * 0.8f, 2f, ctx.joystickPlayerControl, weapon2)
    }

    override fun dispose() {

    }
}
