package pl.touk.liero.scene

interface WorldScene {
    fun create()
    fun update(deltaTimeSec: Float)
    fun destroy()
}