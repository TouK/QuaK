package pl.touk.liero.script

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector2
import pl.touk.liero.script.Script
import pl.touk.liero.ecs.Entity
import pl.touk.liero.gdx.set
import ktx.math.vec2

class CameraScript(val camera: Camera, val worldWidth: Float, val worldHeight: Float) : Script {
    // Gdzie celuje oś kamery w układzie świata
    val worldPosition = vec2(worldWidth / 2, worldHeight / 2)

    val deltaPos = vec2()
    val vel = vec2()

    val drag = 10f
    val spring = 100f

    val tmp = vec2()


    override fun update(me: Entity, timeStepSec: Float) {
        vel.x -= vel.x * drag * timeStepSec + deltaPos.x * spring * timeStepSec
        vel.y -= vel.y * drag * timeStepSec + deltaPos.y * spring * timeStepSec

        deltaPos.x += vel.x * timeStepSec
        deltaPos.y += vel.y * timeStepSec

        tmp.set(worldPosition).add(deltaPos)
        camera.position.set(tmp)
        camera.update()
    }

    fun resize(width: Float, height: Float) {
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