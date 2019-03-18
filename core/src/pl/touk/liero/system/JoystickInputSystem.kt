package pl.touk.liero.system

import com.badlogic.gdx.controllers.Controller
import com.badlogic.gdx.controllers.mappings.Xbox
import pl.touk.liero.ecs.System
import pl.touk.liero.game.PlayerControl

class JoystickInputSystem(private val playerControl: PlayerControl,
                          private val fire: Int,
                          private val controller: Controller) : System {

    override fun update(timeStepSec: Float) {
        playerControl.fireJustPressed = !playerControl.fire && controller.getButton(fire)
        playerControl.fire = controller.getButton(fire)
        playerControl.xAxis = controller.getAxis(Xbox.L_STICK_HORIZONTAL_AXIS)
        playerControl.yAxis = controller.getAxis(Xbox.L_STICK_VERTICAL_AXIS)
    }
}