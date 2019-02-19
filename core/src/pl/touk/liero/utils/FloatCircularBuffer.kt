package pl.touk.liero.utils

/**
 * Circular buffer of primitive float values, constant capacity.
 *
 * FIFO queue behavior with push() and pop() operations
 */
class FloatCircularBuffer(capacity: Int) {
    val array: FloatArray
    var head: Int = 0
    var tail: Int = 0
    var size: Int = 0

    init {
        assert(capacity > 0)
        array = FloatArray(capacity)
        head = 0
        tail = 0
        size = 0
    }

    /**
     * Push given value to the end (tail) of the queue (if queue is full, the head will be overwritten)
     */
    fun push(value: Float) {
        array[tail] = value
        moveTail()
        if (isFull()) {
            moveHead()
        } else {
            size++
        }
    }

    /**
     * Remove and return the value from the front (head) of the queue
     */
    fun pop(): Float {
        if (size > 0) {
            val value = array[head]
            moveHead()
            size--
            return value
        } else {
            throw IndexOutOfBoundsException("Array empty, size: " + size)
        }
    }

    /**
     * Return (but don't remove) the value from the front (head) of the queue
     */
    fun peek(): Float {
        if (size > 0) {
            return array[head]
        } else {
            throw IndexOutOfBoundsException("Array empty, size: " + size)
        }
    }

    /**
     * Get value at given position
     */
    operator fun get(index: Int): Float {
        var i = index
        i += head
        if (i >= array.size) {
            i -= array.size
        }
        return array[i]
    }

    /**
     * Set value at given position
     */
    operator fun set(index: Int, value: Float) {
        var i = index
        i += head
        if (i >= array.size) {
            i -= array.size
        }
        array[i] = value
    }

    /**
     * Get number of elements in the queue
     */
    fun size(): Int = size

    /**
     * Get total capacity of the queue
     */
    fun capacity(): Int = array.size

    /**
     * If number of elements in the buffer equals capacity
     */
    fun  isFull(): Boolean = size == capacity()

    /**
     * Remove all elements
     */
    fun clear() {
        head = 0
        tail = 0
        size = 0
    }

    private fun moveTail() {
        tail++
        if (tail >= array.size) {
            tail = 0
        }
    }

    private fun moveHead() {
        head++
        if (head >= array.size) {
            head = 0
        }
    }
}
