package pl.touk.liero.utils

import com.badlogic.gdx.math.MathUtils
import ktx.math.vec2

fun rndBall(radius: Float) = vec2(MathUtils.random(radius), 0f).rotate(MathUtils.random(360f))

fun rndBox(w: Float, h: Float) = vec2(MathUtils.random(-w / 2, w / 2), MathUtils.random(-h / 2, h / 2))

/**
 * Int in [from, to] (both inclusive)
 */
fun rnd(from: Int, to: Int) = MathUtils.random(from, to)

/**
 * Random float between [from, to) (from - inclusive, to - exclusive)
 */
fun rnd(from: Float, to: Float) = MathUtils.random(from, to)

/**
 * Random float between [mean - range / 2, mean + range / 2)
 */
fun rndMeanRange(mean: Float, range: Float): Float {
    if (range < 0) throw IllegalArgumentException("Negative range value: $range")
    if (range == 0f) return mean
    return MathUtils.random(mean - range / 2, mean + range / 2)
}

/**
 * Random float between [0, to) (0 - inclusive, to - exclusive)
 */
fun rnd(to: Float) = MathUtils.random(to)

/**
 * Random int between [0, to] (both inclusive)
 */
fun rnd(to: Int) = MathUtils.random(to)
