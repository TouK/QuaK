package pl.touk.liero.game.gun

interface Gun {
    fun update(timeStepSec: Float)
    fun shoot(ammoChange: Int): Boolean
    fun percentageCooldown(): Float
}
