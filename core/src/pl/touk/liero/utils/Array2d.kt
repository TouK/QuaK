package pl.touk.liero.utils

/**
 * Dwuwymiarowe tablice prymitywnych typów
 * W kotlinie IntArray != Array<Int> więc trzeba każdą implementować z osobna
 */

class FloatArray2d(val w: Int, val h: Int) {
    val array = FloatArray(w * h)
    operator fun get(x: Int, y: Int): Float =
            array[x*h + y]

    operator fun set(x: Int, y: Int, value: Float) {
        array[x*h + y] = value
    }

    fun size() = array.size
}

open class IntArray2d(val w: Int, val h: Int, defaultValue: Int = 0) {

    val array = IntArray(w * h) { defaultValue }
    operator fun get(x: Int, y: Int): Int =
            array[x*h + y]

    operator fun set(x: Int, y: Int, value: Int) {
        array[x*h + y] = value
    }

    operator fun set(x: Int, y: Int, value: UInt) {
        array[x*h + y] = value.toInt()
    }

    fun size() = array.size

    fun forEach(block: (x: Int, y: Int, value: Int) -> Unit) {
        for (x in 0 until w) {
            for (y in 0 until h) {
                block(x, y, get(x, y))
            }
        }
    }

    fun forEach(block: (x: Int, y: Int) -> Unit) {
        for (x in 0 until w) {
            for (y in 0 until h) {
                block(x, y)
            }
        }
    }

    fun dumpTxt(): String {
        val sb = StringBuilder()
        for (y in 0 until h) {
            for (x in 0 until w) {
                sb.append(get(x, y).toChar())
            }
            if(y < h-1)
                sb.append("\n")
        }
        return sb.toString()
    }

    fun dumpTxt(dict: Map<Int, Char>): String {
        val sb = StringBuilder()
        for (y in 0 until h) {
            for (x in 0 until w) {
                sb.append(dict[get(x, y)] ?: '?')
            }
            if(y < h-1)
                sb.append("\n")
        }
        return sb.toString()
    }

    override fun equals(other: Any?): Boolean {
        if(other != null && other is IntArray2d && other.w == w && other.h == h) {
            for(x in 0 until w) {
                for(y in 0 until h) {
                    if (this[x, y] != other[x, y]) {
                        return false
                    }
                }
            }
            return true
        }
        return false
    }
}

open class BooleanArray2d(val w: Int, val h: Int) {
    val array = BooleanArray(w * h)
    open operator fun get(x: Int, y: Int): Boolean =
            array[x*h + y]

    open operator fun set(x: Int, y: Int, value: Boolean) {
        array[x*h + y] = value
    }

    fun forEach(block: (x: Int, y: Int, value: Boolean) -> Unit) {
        for (x in 0 until w) {
            for (y in 0 until h) {
                block(x, y, get(x, y))
            }
        }
    }

    fun forEachSet(block: (x: Int, y: Int) -> Unit) {
        forEach { x, y, value ->
            if (value) {
                block(x, y)
            }
        }
    }

    fun size() = array.size
}

class BitMap(w: Int, h: Int) : BooleanArray2d(w, h) {

    var count = 0

    override fun set(x: Int, y: Int, value: Boolean) {
        if (!get(x, y) && value) {
            count++
            array[x*h + y] = value
        } else if(get(x, y) && !value) {
            count--
            array[x*h + y] = value
        }
    }

    fun print() {
        println("-".repeat(w + 2))
        for (y in ((h-1) downTo 0)) {
            print('|')
            for (x in 0 until w) {
                if (get(x, y)) {
                    print('#')
                } else {
                    print(' ')
                }
            }
            println('|')
        }
        println("-".repeat(w + 2))
    }

    fun fillRatio() = count.toFloat() / size()

    fun paste(bitmap: BitMap, offsetX: Int, offsetY: Int) {
        for(x in 0 until bitmap.w) {
            for(y in 0 until bitmap.h) {
                safeSet(offsetX + x, offsetY + y, bitmap[x, y])
            }
        }
    }

    private fun safeSet(x: Int, y: Int, value: Boolean) {
        if (x >= 0 && x < w && y > 0 && y < h) {
            this[x, y] = value
        }
    }

    fun countNeighbours(x: Int, y: Int): Int {
        var count = 0
        for (xi in x-1..x+1) {
            for (yi in y-1..y+1) {
                if (get(xi, yi))
                    count++
            }
        }
        return count
    }

    fun filter(): BitMap {
        forEach { x, y, value ->
            if (x > 0 && x < w - 1 && y > 0 && y < h - 1) {
                val neighbours = countNeighbours(x, y)
                if (neighbours > 3) {
                    set(x, y, true)
                } else if (neighbours < 2) {
                    //bitmap.set(x, y, false)
                }
            }
        }
        return this
    }

    companion object {
        fun fromString(str: String): BitMap {
            val lines = str.lines()//.map { line -> line.map { char -> char != ' ' } }
            val w = lines.maxBy { it.length }!!.length
            val h = lines.size
            return BitMap(w, h).also {
                lines.forEachIndexed { y, line ->
                    line.forEachIndexed { x, char ->
                        if(char != ' ') {
                            it[x, y] = true
                        }
                    }
                }
            }
        }
    }
}