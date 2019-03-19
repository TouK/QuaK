package pl.touk.liero.game.projectile

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array
import pl.touk.liero.Ctx
import pl.touk.liero.ecs.Entity
import pl.touk.liero.ecs.texture
import pl.touk.liero.script.Script

class ExplosionScript(val ctx: Ctx) : Script {

    var liveTime: Float = 0f
    var animation = createAnimation(ctx)

    override fun update(me: Entity, timeStepSec: Float) {
        liveTime += timeStepSec
        val textureRegion = animation.getKeyFrame(liveTime)
        me[texture].texture = textureRegion
        return
    }

    private fun createAnimation(ctx: Ctx): Animation<TextureRegion> {
        val walkFrames: Array<TextureRegion> = Array()
        walkFrames.add(ctx.gameAtlas.findRegion("frame0000"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0001"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0002"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0003"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0004"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0005"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0006"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0007"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0008"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0009"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0010"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0011"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0012"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0013"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0014"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0015"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0016"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0017"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0018"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0019"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0020"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0021"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0022"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0023"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0024"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0025"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0026"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0027"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0028"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0029"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0030"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0031"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0032"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0033"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0034"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0035"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0036"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0037"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0038"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0039"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0040"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0041"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0042"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0043"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0044"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0045"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0046"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0047"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0048"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0049"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0050"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0051"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0052"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0053"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0054"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0055"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0056"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0057"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0058"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0059"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0060"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0061"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0062"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0063"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0064"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0065"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0066"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0067"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0068"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0069"))
        walkFrames.add(ctx.gameAtlas.findRegion("frame0070"))
        return Animation(0.025f, walkFrames, Animation.PlayMode.NORMAL)
    }
}
