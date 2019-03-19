package pl.touk.liero.state

import pl.touk.liero.Ctx
import pl.touk.liero.scene.MenuScene
import pl.touk.liero.scene.WorldScene
import pl.touk.liero.screen.LevelScreen
import pl.touk.liero.screen.UiScreen
import pl.touk.liero.system.MusicSystem

class LevelSelectionState(ctx: Ctx) : State {
    override val scene: WorldScene = MenuScene(ctx)
    override val screen: UiScreen = LevelScreen(ctx)
    override fun musicTrack(): MusicSystem.MusicTrack = MusicSystem.MusicTrack.Preparations
}