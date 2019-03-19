package pl.touk.liero.system

import pl.touk.liero.ecs.System
import pl.touk.liero.game.PlayerControl
import pl.touk.liero.gdx.justPressed
import pl.touk.liero.gdx.pressed

class InputSystem(private val playerControl: PlayerControl,
                  private val left: Int,
                  private val right: Int,
                  private val up: Int,
                  private val down: Int,
                  private val jump: Int,
                  private val changeWeapon: Int,
                  private val fire: Int) : System {

    override fun update(timeStepSec: Float) {
        playerControl.fire = fire.pressed()
        playerControl.jump = jump.pressed()
        playerControl.changeWeapon = changeWeapon.pressed()
        playerControl.jumpJustPressed = jump.justPressed()
        playerControl.fireJustPressed = fire.justPressed()
        playerControl.changeWeaponJustPressed = changeWeapon.justPressed()
        playerControl.left = left.pressed()
        playerControl.right = right.pressed()
        playerControl.up = up.pressed()
        playerControl.down = down.pressed()
    }
}