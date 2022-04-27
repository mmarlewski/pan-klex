package com.marcin.panklex.entities

import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.BaseEntity

class EntityContainer : BaseEntity("entity Container")
{
    var position = Vector3()
    var pickaxes = 0
    var bombs = 0
    var energyCells = 0
    var coins = 0
}