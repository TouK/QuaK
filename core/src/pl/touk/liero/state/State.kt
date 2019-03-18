package pl.touk.liero.state

import pl.touk.liero.scene.WorldScene
import pl.touk.liero.screen.UiScreen
import pl.touk.liero.system.MusicSystem

interface State {
    val screen: UiScreen
    val scene: WorldScene
    val musicTrack: MusicSystem.MusicTrack
}