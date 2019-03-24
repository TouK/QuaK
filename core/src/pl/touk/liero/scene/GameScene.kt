package pl.touk.liero.scene

import com.badlogic.gdx.Gdx
import pl.touk.liero.Ctx
import pl.touk.liero.entity.entity
import pl.touk.liero.script.CameraScript

class GameScene(val ctx: Ctx) : WorldScene {

    override fun create() {
        ctx.level = ctx.levelLoader.load(ctx.currentLevel)
        ctx.level.start(ctx)
        ctx.cameraScript = CameraScript(ctx.worldCamera, ctx.level.width, ctx.level.height)
        ctx.engine.entity {
            script(ctx.cameraScript)
        }
        ctx.cameraScript.resize(Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    }

    override fun update(deltaTimeSec: Float) {
        ctx.worldEngine.update(deltaTimeSec)
    }

    override fun destroy() {
        ctx.clearWorld()
        ctx.level.dispose()
    }
}