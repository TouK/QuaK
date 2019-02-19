package pl.touk.liero.utils

import com.badlogic.gdx.math.MathUtils

fun Float.rad_deg(): Float = this * MathUtils.radiansToDegrees
fun Float.deg_rad(): Float = this * MathUtils.degreesToRadians
fun Int.deg_rad(): Float = this * MathUtils.degreesToRadians

/**
 * Clamp value to the [min, max] range (both inclusive)
 */
fun Int.clamp(min: Int, max: Int) = MathUtils.clamp(this, min, max)

/**
 * Clamp to [min, max] (both inclusive)
 */
fun Float.clamp(min: Float, max: Float) = MathUtils.clamp(this, min, max)