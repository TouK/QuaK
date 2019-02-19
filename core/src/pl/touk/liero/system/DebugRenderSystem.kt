package pl.touk.liero.system

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType.Line
import pl.touk.liero.ecs.Engine
import pl.touk.liero.ecs.Entity
import pl.touk.liero.ecs.System

class DebugRenderSystem(val engine: Engine<Entity>, val camera: Camera) : System {

    val renderer = ShapeRenderer()

    override fun update(timeStepSec: Float) {
        renderer.setProjectionMatrix(camera.combined)
        renderer.begin(Line)
        engine.ents.forEach { e ->
            e.scripts.forEach {
                it.debugDraw(e, renderer)
            }
        }
        renderer.end()
    }
}