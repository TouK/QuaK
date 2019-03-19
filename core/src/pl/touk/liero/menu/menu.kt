package pl.touk.liero.menu

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.utils.Array
import pl.touk.liero.Ctx
import pl.touk.liero.ecs.Entity
import pl.touk.liero.ecs.texture
import pl.touk.liero.entity.entity
import pl.touk.liero.script.Script

fun createMenu(ctx: Ctx, w: Float, h: Float) {
    ctx.engine.entity {
        position(Vector2(w/2, h/2))
        texture(ctx.menuAtlas.findRegion("quak_logo1"), w, h)
        script(MenuScript(ctx))
    }
}

class MenuScript(val ctx: Ctx) : Script {

    var animation = createMenuAnimation(ctx)
    var liveTime = 0f

    override fun update(me: Entity, timeStepSec: Float) {
        liveTime += timeStepSec
        val textureRegion = animation.getKeyFrame(liveTime)
        me[texture].texture = textureRegion
        return
    }

    private fun createMenuAnimation(ctx: Ctx): Animation<TextureRegion> {
        val menuFrames: Array<TextureRegion> = Array()
        menuFrames.add(ctx.menuAtlas.findRegion("quak_logo1"))
        menuFrames.add(ctx.menuAtlas.findRegion("quak_logo2"))
        menuFrames.add(ctx.menuAtlas.findRegion("quak_logo3"))
        menuFrames.add(ctx.menuAtlas.findRegion("quak_logo4"))
        menuFrames.add(ctx.menuAtlas.findRegion("quak_logo5"))
        menuFrames.add(ctx.menuAtlas.findRegion("quak_logo6"))
        return Animation(0.05f, menuFrames, Animation.PlayMode.LOOP)
    }
}