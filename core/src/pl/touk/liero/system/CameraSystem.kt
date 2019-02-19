package pl.touk.liero.system

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector2
import pl.touk.liero.ecs.System
import pl.touk.liero.gdx.set
import ktx.math.vec2

class CameraSystem(val camera: Camera, val worldWidth: Float, val worldHeight: Float) : System {
    // Gdzie celuje oś kamery w układzie świata
    val worldPosition = vec2()

    val deltaPos = vec2()
    val vel = vec2()

    val drag = 10f
    val spring = 100f

    val tmp = vec2()

    override fun update(timeStepSec: Float) {
        vel.x -= vel.x * drag * timeStepSec + deltaPos.x * spring * timeStepSec
        vel.y -= vel.y * drag * timeStepSec + deltaPos.y * spring * timeStepSec

        deltaPos.x += vel.x * timeStepSec
        deltaPos.y += vel.y * timeStepSec

        tmp.set(worldPosition).add(deltaPos)
        camera.position.set(tmp)
        camera.update()
    }

    fun resize(width: Int, height: Int) {
        val xScale = worldWidth  / width
        val yScale = worldHeight  / height
        if (yScale < xScale) {
            camera.viewportWidth = width * xScale
            camera.viewportHeight = height * xScale
        } else {
            camera.viewportWidth = width * yScale
            camera.viewportHeight = height * yScale
        }

        tmp.set(worldPosition).add(deltaPos)
        camera.position.set(tmp)

        camera.update()
    }

    fun shake(delta: Vector2) {
        deltaPos.set(delta)
    }
}