package pl.touk.liero.gdx

import com.badlogic.gdx.graphics.Camera
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

private val v3 = Vector3()
private val v2 = Vector2()

/**
 * Metoda unproject w klasie Camera jest dobra do przejścia z touch coordinates na world coordinates,
 * ale nie przy przejściu np. z world coordinates -> screen coordinates -> HUD coordinates
 * Tutaj mamy wyraźne rozróżnienie unprojectTouch / unproject
 */

fun Camera.unprojectTouch(touchX: Int, touchY: Int): Vector2 {
    v3.set(touchX.toFloat(), touchY.toFloat(), 0f)
    this.unproject(v3)
    v2.set(v3.x, v3.y)
    return v2
}

fun Camera.unprojectTouch(touch: Vector2): Vector2 {
    v3.set(touch.x, touch.y, 0f)
    this.unproject(v3)
    v2.set(v3.x, v3.y)
    return v2
}

fun Camera.unproject(x: Float, y: Float): Vector2 {
    v3.set(x, y, 0f)
    v3.prj(invProjectionView)
    v2.set(v3.x, v3.y)
    return v2
}

fun Camera.unproject(pos: Vector2): Vector2 {
    v3.set(pos.x, pos.y, 0f)
    v3.prj(invProjectionView)
    v2.set(v3.x, v3.y)
    return v2
}

fun Camera.project(x: Float, y: Float): Vector2 {
    v3.set(x, y, 0f)
    v3.prj(combined)
    v2.set(v3.x, v3.y)
    return v2
}

fun Camera.project(pos: Vector2): Vector2 {
    v3.set(pos.x, pos.y, 0f)
    v3.prj(combined)
    v2.set(v3.x, v3.y)
    return v2
}