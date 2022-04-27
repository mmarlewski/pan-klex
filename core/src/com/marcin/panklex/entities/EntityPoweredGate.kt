package com.marcin.panklex.entities

import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.BaseEntity
import com.marcin.panklex.Direction

class EntityPoweredGate : BaseEntity("entity PoweredGate")
{
    var firstPartPosition = Vector3()
    var firstPartDirection = Direction.None
    var isFirstPartPowered = false
    var secondPartPosition = Vector3()
    var secondPartDirection = Direction.None
    var isSecondPartPowered = false
    var gapePosition = Vector3()
    var isGateOpen = false
}