package pl.touk.liero.game

import javax.naming.OperationNotSupportedException

interface PlayerControl {
    var xAxis: Float
    var fire: Boolean
    var left: Boolean
    var right: Boolean
    var fireJustPressed: Boolean

    fun clear()
}

open class PlayerControlSmooth: PlayerControl {
    override var xAxis: Float = 0f
    override var fire: Boolean = false
    override var fireJustPressed: Boolean = false

    override var left: Boolean
        get() = xAxis < 0.5f
        set(_) {}

    override var right: Boolean
        get() = xAxis > 0.5f
        set(_) {}

    override fun clear() {
        xAxis = 0f
        fire = false
        fireJustPressed = false
    }
}

class PlayerButtonControl : PlayerControl {
    override var fire = false
    override var left = false
    override var right = false
    override var fireJustPressed = false

    override var xAxis: Float
      get() = if (left) -1f else 0f + if (right) 1f else 0f
      set(_) { throw OperationNotSupportedException("Can only change throught left, right properties") }

    override fun clear() {
        left = false
        right = false
        fire = false
        fireJustPressed = false
    }
}