package pl.touk.liero

interface Gun {
    fun update(timeStepSec: Float)
    fun shoot(ammoChange: Int): Boolean
}
