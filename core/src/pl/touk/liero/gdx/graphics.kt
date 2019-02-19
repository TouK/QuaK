package pl.touk.liero.gdx

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Graphics

// Konwersja pixele -> centymetry
fun px2cmX(px: Float) = px / Gdx.graphics.ppcX
fun px2cmY(px: Float) = px / Gdx.graphics.ppcY

// Konwersja przy użyciu średniej wartości dla osi X i Y (zazwyczaj jest drobna różnica w osi x i y, ale niewielka)
fun px2cm(px: Float) = px / (Gdx.graphics.ppcX + Gdx.graphics.ppcY) / 2f

/** Krótszy bok ekranu (zazwyczaj width, ale dla pewności) */
fun Graphics.shorter() = Math.min(Gdx.graphics.height, Gdx.graphics.width)