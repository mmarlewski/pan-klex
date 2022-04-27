package com.marcin.panklex.entities

import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.BaseEntity

class EntityElevator:BaseEntity("entity Elevator")
{
    var positions= mutableListOf<Vector3>()
    var exitPositions= mutableListOf<Vector3>()
    var floorNames= mutableListOf<Vector3>()

}