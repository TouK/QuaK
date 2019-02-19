package pl.touk.liero.ecs

import com.badlogic.gdx.utils.Pool
import java.util.*

class Actions {

    private object ActionOrder : Comparator<Action> {
        override fun compare(o1: Action, o2: Action): Int {
            return o1.time.compareTo(o2.time)
        }
    }

    private class Action(
            var time: Int,
            var action: () -> Unit,
            var interval: (() -> Int)? = null) : Pool.Poolable {

        override fun reset() {
            time = 0
            action = {}
            interval = null
        }
    }

    private val queue = PriorityQueue<Action>(30, ActionOrder)
    private val pool = object : Pool<Action>() {
        override fun newObject() = Action(0, {}, null)
    }
    private var currentTime: Int = 0

    fun schedule(delay: Int, action: () -> Unit) {
        queue.add(createAction(currentTime + delay, action))
    }

    fun schedule(delay: Float, action: () -> Unit) {
        schedule((delay * 1000).toInt(), action)
    }

    fun every(interval: Int, action: () -> Unit) {
        queue.add(createAction(currentTime + interval, action) { interval })
    }

    fun every(interval: Float, action: () -> Unit) {
        every((interval * 1000).toInt(), action)
    }

    fun update(currentTime: Int) {
        this.currentTime = currentTime
        while (queue.peek() != null && queue.peek().time <= currentTime) {
            val action = queue.poll()
            action.action()
            val repeat = action.interval
            if(repeat != null) {
                action.time = currentTime + repeat()
                queue.add(action)
            }
        }
    }

    fun reset() {
        queue.clear()
        currentTime = 0
    }

    private fun createAction(time: Int, action: () -> Unit, interval: (() -> Int)? = null) =
            pool.obtain().also {
                it.time = time
                it.action = action
                it.interval = interval
            }
}