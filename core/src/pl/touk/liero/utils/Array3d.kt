package pl.touk.liero.utils

class FloatArray3d(xc: Int, val yc: Int, val zc: Int) {
    val array = FloatArray(xc * yc * zc)
    operator fun get(x: Int, y: Int, z: Int): Float =
        array[x*yc*zc + y*zc + z]

    operator fun set(x: Int, y: Int, z: Int, value: Float) {
        array[x*yc*zc + y*zc + z] = value
    }
}

class IntArray3d(xc: Int, val yc: Int, val zc: Int) {
    val array = IntArray(xc * yc * zc)
    operator fun get(x: Int, y: Int, z: Int): Int =
            array[x*yc*zc + y*zc + z]

    operator fun set(x: Int, y: Int, z: Int, value: Int) {
        array[x*yc*zc + y*zc + z] = value
    }
}