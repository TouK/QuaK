package pl.touk.liero.game

interface Clock {
    var timeMs: Int
    val timeSec: Float
        get() = timeMs / 1000f
}