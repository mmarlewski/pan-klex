package com.marcin.panklex

import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.screens.ScreenGame

abstract class BaseEntity(val name : String)
{
    abstract fun getPositions() : List<Vector3>
    abstract fun isTraversable(position : Vector3) : Boolean
    abstract fun isActionPossible(position : Vector3, action : Action) : Boolean
    abstract fun onAction(position : Vector3, action : Action, screenGame : ScreenGame)
}
