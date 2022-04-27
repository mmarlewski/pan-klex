package com.marcin.panklex.entities

import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.BaseEntity
import com.marcin.panklex.Direction

class EntityStairs : BaseEntity("entity Stairs")
{
    var position = Vector3()
    var direction = Direction.None
    var upperEnd = Vector3()
    var lowerEnd = Vector3()
}