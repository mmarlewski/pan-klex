package com.marcin.panklex.entities

import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.BaseEntity

class EntityVendingMachine : BaseEntity("entity VendingMachine")
{
    var position = Vector3()
    var isBroken = true
    var pickaxes = 0
    var bombs = 0
    var energyCells = 0
}