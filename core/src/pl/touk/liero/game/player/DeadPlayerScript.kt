package pl.touk.liero.game.player

import com.badlogic.gdx.graphics.g2d.Animation
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.utils.Array
import pl.touk.liero.Ctx
import pl.touk.liero.ecs.Entity
import pl.touk.liero.ecs.texture
import pl.touk.liero.script.Script

class DeadPlayerScript(ctx: Ctx) : Script {

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
        walkFrames.add(ctx.gameAtlas.findRegion("blobDead0"))
        walkFrames.add(ctx.gameAtlas.findRegion("blobDead1"))
        walkFrames.add(ctx.gameAtlas.findRegion("blobDead2"))
        walkFrames.add(ctx.gameAtlas.findRegion("blobDead3"))
        walkFrames.add(ctx.gameAtlas.findRegion("blobDead4"))
        walkFrames.add(ctx.gameAtlas.findRegion("blobDead5"))
        walkFrames.add(ctx.gameAtlas.findRegion("blobDead6"))
        walkFrames.add(ctx.gameAtlas.findRegion("blobDead7"))
        walkFrames.add(ctx.gameAtlas.findRegion("blobDead8"))
        return Animation(0.3f, walkFrames, Animation.PlayMode.NORMAL)
    }
}
