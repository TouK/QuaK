package pl.touk.liero.system

import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.controllers.mappings.Xbox
import pl.touk.liero.Ctx
import pl.touk.liero.ecs.System
import pl.touk.liero.game.PlayerControl
import pl.touk.liero.screen.UiEvent

class JoystickInputSystem(private val ctx: Ctx,
                          private val playerControl: PlayerControl,
                          private val jump: Int,
                          private val fire: Int,
                          private val changeWeapon: Int,
                          private val changeWeaponBackwards: Int,
                          private val controller: Controller) : System {

    var startPressed = false

    override fun update(timeStepSec: Float) {
        playerControl.fireJustPressed = !playerControl.fire && controller.getButton(fire)
        playerControl.fire = controller.getButton(fire)
        playerControl.jumpJustPressed = !playerControl.jump && controller.getButton(jump)
        playerControl.jump = controller.getButton(jump)
        playerControl.changeWeaponJustPressed = !playerControl.changeWeapon && controller.getButton(changeWeapon)
        playerControl.changeWeapon = controller.getButton(changeWeapon)
        playerControl.changeWeaponJustPressedBackwards = !playerControl.changeWeaponBackwards && controller.getButton(changeWeaponBackwards)
        playerControl.changeWeaponBackwards = controller.getButton(changeWeaponBackwards)
        playerControl.xAxis = controller.getAxis(Xbox.L_STICK_HORIZONTAL_AXIS)
        playerControl.yAxis = controller.getAxis(Xbox.L_STICK_VERTICAL_AXIS)

        if(controller.getButton(Xbox.START) && !startPressed) {
            ctx.uiEvents += UiEvent.StartPressed
        }
        startPressed = controller.getButton(Xbox.START)
    }
}