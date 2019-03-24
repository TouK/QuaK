package pl.touk.liero.game

import javax.naming.OperationNotSupportedException

interface PlayerControl {
    var xAxis: Float
    var yAxis: Float
    var fire: Boolean
    var jump: Boolean
    var left: Boolean
    var right: Boolean
    var up: Boolean
    var down: Boolean
    var fireJustPressed: Boolean
    var jumpJustPressed: Boolean
    var changeWeapon: Boolean
    var changeWeaponJustPressed: Boolean
    var changeWeaponBackwards: Boolean
    var changeWeaponJustPressedBackwards: Boolean

    fun clear()
}

open class PlayerControlSmooth: PlayerControl {
    override var xAxis: Float = 0f
    override var yAxis: Float = 0f
    override var fire: Boolean = false
    override var fireJustPressed: Boolean = false
    override var changeWeapon: Boolean = false
    override var changeWeaponJustPressed: Boolean = false
    override var changeWeaponBackwards: Boolean = false
    override var changeWeaponJustPressedBackwards: Boolean = false
    override var jump: Boolean = false
    override var jumpJustPressed: Boolean = false

    override var left: Boolean
        get() = xAxis < -0.5f
        set(_) {}

    override var right: Boolean
        get() = xAxis > 0.5f
        set(_) {}

    override var up: Boolean
        get() = yAxis < -0.5f
        set(_) {}

    override var down: Boolean
        get() = yAxis > 0.5f
        set(_) {}

    override fun clear() {
        xAxis = 0f
        yAxis = 0f
        fire = false
        jump = false
        fireJustPressed = false
        jumpJustPressed = false
        changeWeapon = false
        changeWeaponJustPressed = false
        changeWeaponBackwards = false
        changeWeaponJustPressedBackwards = false
    }
}

class PlayerButtonControl : PlayerControl {
    override var fire = false
    override var jump = false
    override var left = false
    override var right = false
    override var up = false
    override var down = false
    override var fireJustPressed = false
    override var jumpJustPressed = false
    override var changeWeapon = false
    override var changeWeaponJustPressed = false
    override var changeWeaponBackwards = false
    override var changeWeaponJustPressedBackwards = false

    override var xAxis: Float
      get() = if (left) -1f else 0f + if (right) 1f else 0f
      set(_) { throw OperationNotSupportedException("Can only change throught left, right properties") }

    override var yAxis: Float
        get() = if (up) -1f else 0f + if (down) 1f else 0f
        set(_) { throw OperationNotSupportedException("Can only change throught up, down properties") }

    override fun clear() {
        left = false
        right = false
        up = false
        down = false
        fire = false
        jump = false
        fireJustPressed = false
        jumpJustPressed = false
        changeWeapon = false
        changeWeaponJustPressed = false
        changeWeaponBackwards = false
        changeWeaponJustPressedBackwards = false
    }
}