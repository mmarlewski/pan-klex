package com.marcin.panklex.entities

import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.BaseEntity

class EntityTeleporter : BaseEntity("entity Teleporter")
{
    var firstTelePosition = Vector3()
    var firstTeleExitPosition = Vector3()
    var isFirstTelePowered = false
    var secondTelePosition = Vector3()
    var secondTeleExitPosition = Vector3()
    var isSecondTelePowered = false
}