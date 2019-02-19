package pl.touk.liero.level

import com.badlogic.gdx.graphics.Color
import pl.touk.liero.Ctx
import pl.touk.liero.game.createPlayer
import pl.touk.liero.utils.overwrite

class Level1 : Level {
    override var width = 32f
    override var height = 18f

    override fun start(ctx: Ctx) {
        ctx.params.overwrite(LevelParams().also {
            it.playerColor = Color.BLACK
        })
        createBounds(ctx, width, height)
        createPlayer(ctx, 20f, 20f)
    }

    override fun dispose() {

    }
}
