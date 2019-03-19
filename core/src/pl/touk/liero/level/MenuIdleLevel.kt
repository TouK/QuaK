package pl.touk.liero.level

import pl.touk.liero.Ctx
import pl.touk.liero.menu.createMenu

class MenuIdleLevel : Level {
    override var width = 32f
    override var height = 18f

    override fun start(ctx: Ctx) {
        createMenu(ctx, width, height)
    }

    override fun dispose() {

    }
}