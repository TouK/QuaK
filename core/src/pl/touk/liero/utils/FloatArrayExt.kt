package pl.touk.liero.utils

fun FloatArray.scl(scale: Float) = this.also {
    for(i in 0 until this.size) {
        this[i] *= scale
    }
}