package pl.touk.liero.state

import pl.touk.liero.Ctx
import pl.touk.liero.scene.EmptyScene
import pl.touk.liero.scene.WorldScene
import pl.touk.liero.screen.MenuScreen
import pl.touk.liero.screen.UiScreen
import pl.touk.liero.system.MusicSystem

class MenuState(ctx: Ctx) : State {
    override val scene: WorldScene = EmptyScene()
    override val screen: UiScreen = MenuScreen(ctx)
    override val musicTrack: MusicSystem.MusicTrack = MusicSystem.MusicTrack.Preparations
}