package pl.touk.liero.gdx

import com.badlogic.gdx.InputAdapter
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.BodyDef
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.World
import com.badlogic.gdx.physics.box2d.joints.MouseJoint
import ktx.box2d.KtxQueryCallback
import ktx.box2d.body
import ktx.box2d.mouseJointWith
import ktx.box2d.query

/**
 * Umożliwia łapanie obiektów box2d za pomocą prawego klawisza myszy, wsytarczy dodać do input processora:
 * <code>
 * val mouseJointer = MouseJointer(ctx.worldCamera, ctx.world)
 *
 * override fun show() {
 *     WrapCtx.mux.addProcessor(mouseJointer)
 * }
 *
 *  override fun hide() {
 *      WrapCtx.mux.removeProcessor(mouseJointer)
 *  }
 * </pre>
 */
class MouseJointer(val worldCamera: Camera, val world: World) : InputAdapter(), KtxQueryCallback {
    private val baseBody = world.body(BodyDef.BodyType.StaticBody) {}
    private var mouseTargetBody: Body? = null
    private var mouseJoint: MouseJoint? = null

    override fun invoke(fixture: Fixture): Boolean {
        mouseTargetBody = fixture.body
        return false
    }

    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (button == 1) {
            val pos = worldCamera.unprojectTouch(screenX, screenY)

            if (mouseJoint == null) {
                mouseTargetBody = null
                world.query(pos.x - 0.1f, pos.y - 0.1f, pos.x + 0.1f, pos.y + 0.1f, this)
                if (mouseTargetBody != null) {
                    println("Mamy target body, tworzymy mouse joint")
                    mouseJoint = baseBody.mouseJointWith(mouseTargetBody!!) {
                        target.set(pos)
                        maxForce = 1000.0f * mouseTargetBody!!.getMass()
                    }
                    mouseTargetBody!!.isAwake = true
                }
            } else {
                throw IllegalStateException("Mouse Joint powinien być przerwany")
            }
        }
        // input was processed, but let it propagate to other listeners (e.g. stage)
        return false
    }

    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        // Desktop: pointer zawsze jest = 0
        if (mouseJoint != null) {
            val pos = worldCamera.unprojectTouch(screenX, screenY)
            mouseJoint!!.setTarget(pos)
        }
        // input was processed, but let it propagate to other listeners (e.g. stage)
        return false
    }

    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        if (button == 1 && mouseJoint != null) {
            println("Destroy joint")
            world.destroyJoint(mouseJoint)
            mouseJoint = null
            mouseTargetBody = null
        }
        // input was processed, but let it propagate to other listeners (e.g. stage)
        return false
    }

}