package pl.touk.liero.utils

import com.badlogic.gdx.math.MathUtils

/**
 * Rozszerzenia typów numerycznych
 */

fun Int.isEven(): Boolean = (this.and(1) == 0)
fun Int.isOdd(): Boolean = (this.and(1) != 0)
fun Long.isEven(): Boolean = (this.and(1) == 0L)
fun Long.isOdd(): Boolean = (this.and(1) != 0L)

fun Int.hex() = Integer.toHexString(this)