package pl.touk.liero.system

import com.badlogic.gdx.Input.Keys
import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer
import com.badlogic.gdx.physics.box2d.World
import pl.touk.liero.ecs.System
import pl.touk.liero.gdx.justPressed

class WorldRenderSystem(val renderer: Box2DDebugRenderer, val world: World, val worldCamera: Camera) : System {
    var render = true

    override fun update(timeStepSec: Float) {
        if (Keys.F12.justPressed()) {
            render = !render
        }
        if (render) {
            renderer.render(world, worldCamera.combined)
        }
    }
}