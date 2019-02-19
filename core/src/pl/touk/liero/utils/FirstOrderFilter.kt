package pl.touk.liero.utils

/**
 * tauSec - czas po jakim filtr osiągnie 66% target value
 *
 * Możemy ustawić target, i przy każdym kroku podawać tylko czas, lub jeśli target (input) często się zmienia,
 * podawać go zawsze na wejściu.
 *
 *
 * <pre>
 *     volume = FirstOrderFilter(1f, 0f, 1f)
 *     ...
 *     music.setVol(colume.updateAndGet(deltaTime))
 *     ...
 *     // volume down
 *     volume.target = 0.5f
 * </pre>
 */
class FirstOrderFilter(val tauSec: Float, initialState: Float = 0f, initialTarget: Float = 0f) {
    var state = initialState
    var target = initialTarget

    fun update(deltaSec: Float) {
        state += (target - state) * deltaSec / tauSec
    }

    fun updateAndGet(deltaSec: Float): Float {
        update(deltaSec)
        return state
    }

    fun update(target: Float, deltaSec: Float): Float {
        this.target = target
        state += (target - state) * deltaSec / tauSec
        return state
    }

    fun value() = state
}
