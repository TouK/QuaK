package pl.touk.liero.game.joint

import com.badlogic.gdx.physics.box2d.Body
import com.badlogic.gdx.physics.box2d.JointDef
import com.badlogic.gdx.physics.box2d.joints.RevoluteJointDef
import pl.touk.liero.Ctx

fun createWeaponJoint(ctx: Ctx, playerBody: Body, weaponBody: Body): JointDef {
    val revoluteJointDef = RevoluteJointDef()
    revoluteJointDef.bodyA = playerBody
    revoluteJointDef.bodyB = weaponBody

    revoluteJointDef.localAnchorA.set(ctx.params.playerAnchor)
    revoluteJointDef.localAnchorB.set(ctx.params.weaponAnchor)

    revoluteJointDef.collideConnected = false

    revoluteJointDef.referenceAngle = 0f
    revoluteJointDef.enableLimit = true
    revoluteJointDef.lowerAngle = ctx.params.weaponLowerAngle
    revoluteJointDef.upperAngle = ctx.params.weaponUpperAngle

    return revoluteJointDef
}