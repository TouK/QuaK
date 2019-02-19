package pl.touk.liero.gdx

import com.badlogic.gdx.math.MathUtils
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3

fun Vector3.set(vec: Vector2) {
    this.set(vec.x, vec.y, 0f)
}

/**
 * Random vector inside given circle
 */
fun Vector2.rnd(radius: Float): Vector2 {
    val rad = MathUtils.random(radius)
    val phi = MathUtils.random(MathUtils.PI2)
    return set(rad * MathUtils.cos(phi), rad * MathUtils.sin(phi))
}

/**
 * Random vector inside given [-size, size] square
 */
fun Vector2.rndSquare(size: Float): Vector2 =
     set(MathUtils.random(-size, size), MathUtils.random(-size, size))

/**
 * Random vector inside given rectangle
 */
fun Vector2.rndRectangle(width: Float, height: Float): Vector2 =
        set(MathUtils.random(-width, width), MathUtils.random(-height, height))

/**
 * Rotate left (counter-clockwise) by 90 degrees
 */
fun Vector2.rotateLeft(): Vector2 {
    val x = this.x
    this.x = -y
    y = x
    return this
}

/**
 * Rotate left (clockwise) by 90 degrees
 */
fun Vector2.rotateRight(): Vector2 {
    val x = this.x
    this.x = y
    y = -x
    return this
}

/**
 * Get angle in radians in box2d "system", where up (0, 1) is 0, left (-1, 0) is pi/2 (90 degrees)
 *
 * Box2d is different from what would be expected, where 0 degrees is right direction (1, 0)
 */
fun Vector2.boxAngRad(): Float = Math.atan2(-this.x.toDouble(), this.y.toDouble()).toFloat()

operator fun Vector2.component1(): Float = this.x

operator fun Vector2.component2(): Float = this.y