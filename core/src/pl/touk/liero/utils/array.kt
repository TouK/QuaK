package pl.touk.liero.utils

fun FloatArray.repeat(n: Int): FloatArray {
    val result = FloatArray(this.size * n)
    for (i in 0 until n) {
        System.arraycopy(this, 0, result, i * this.size, this.size)
    }
    return result
}

fun FloatArray.repeat(n: Int, mutator: (FloatArray) -> Unit): FloatArray {
    val result = FloatArray(this.size * n)
    for (i in 0 until n) {
        mutator(this)
        System.arraycopy(this, 0, result, i * this.size, this.size)
    }
    return result
}

fun FloatArray.repeat(n: Int, mutator: (FloatArray, Int) -> Unit): FloatArray {
    val result = FloatArray(this.size * n)
    for (i in 0 until n) {
        mutator(this, i)
        System.arraycopy(this, 0, result, i * this.size, this.size)
    }
    return result
}

fun ShortArray.repeat(n: Int): ShortArray {
    val result = ShortArray(this.size * n)
    for (i in 0 until n) {
        System.arraycopy(this, 0, result, i * this.size, this.size)
    }
    return result
}

fun ShortArray.repeat(n: Int,mutator: (ShortArray) -> Unit): ShortArray {
    val result = ShortArray(this.size * n)
    for (i in 0 until n) {
        mutator(this)
        System.arraycopy(this, 0, result, i * this.size, this.size)
    }
    return result
}

fun ShortArray.repeat(n: Int, mutator: (ShortArray, Int) -> Unit): ShortArray {
    val result = ShortArray(this.size * n)
    for (i in 0 until n) {
        mutator(this, i)
        System.arraycopy(this, 0, result, i * this.size, this.size)
    }
    return result
}