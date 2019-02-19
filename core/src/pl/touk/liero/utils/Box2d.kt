package pl.touk.liero.utils

import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.physics.box2d.Fixture
import com.badlogic.gdx.physics.box2d.QueryCallback
import com.badlogic.gdx.physics.box2d.World
import kotlin.experimental.and

private object categoryCheck : QueryCallback {
    var categoryMask: Short = 0
    var result: Boolean = false

    override fun reportFixture(fixture: Fixture): Boolean {
        if (fixture.filterData.categoryBits and categoryMask != 0.toShort()) {
            result = true
            return false    // terminate
        }
        return true
    }
}

private object myQueryCallback : QueryCallback {

    var foundFixture: Fixture? = null

    override fun reportFixture(fixture: Fixture?): Boolean {
        if(fixture != null) {
            foundFixture = fixture
            return false
        }
        return true
    }
}

/**
 * Szukaj obiektu w danym kwadracie, którego kategoria (categoryBits) pokrywa się z maską.
 * Zwraca pierwszy obiekt który znajdzie
 */
fun World.querySquare(pos: Vector2, size: Float, categotyMask: Short): Boolean {
    categoryCheck.categoryMask = categotyMask
    categoryCheck.result = false
    this.QueryAABB(categoryCheck, pos.x - size / 2, pos.y - size / 2, pos.x + size / 2, pos.y + size / 2)
    return categoryCheck.result
}

/**
 * Szukaj obiektu w danym kwadracie, zwraca pierwszy obiekt który znajdzie
 */
fun World.querySquare(pos: Vector2, size: Float): Fixture? {
    myQueryCallback.foundFixture = null
    this.QueryAABB(myQueryCallback, pos.x - size / 2, pos.y - size / 2, pos.x + size / 2, pos.y + size / 2)
    return myQueryCallback.foundFixture
}

/**
 * Łatwiejszy dostęp do Fixture.filterData.maskBits, ważny jest setter, który poprawnie zmieni wartość
 */
var Fixture.categoryBits: Short
    get() {
        return this.filterData.categoryBits
    }
    set(newCatBits) {
        val filterData = this.filterData
        filterData.categoryBits = newCatBits
        this.filterData = filterData
    }

/**
 * Łatwiejszy dostęp do Fixture.filterData.maskBits, ważny jest setter, który poprawnie zmieni wartość
 */
var Fixture.maskBits: Short
    get() {
        return this.filterData.maskBits
    }
    set(newMaskBits) {
        val filterData = this.filterData
        filterData.maskBits = newMaskBits
        this.filterData = filterData
    }