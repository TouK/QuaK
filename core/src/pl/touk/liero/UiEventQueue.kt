package pl.touk.liero

class UiEventQueue<T: Enum<T>> {
    private val events = mutableListOf<T>()

    fun add(event: T) { events.add(event) }
    operator fun plusAssign(event: T) = add(event)
    fun handle(block: (T) -> Unit) {
        for (e in events) {
            block(e)
        }
        events.clear()
    }
}