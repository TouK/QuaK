package pl.touk.liero.level

class LevelLoader {
    fun load(levelNr: Int) : Level =
            when(levelNr) {
                0 -> MenuIdleLevel()
                1 -> Level1()
                2 -> Level2()
                else -> Level1()
            }
}