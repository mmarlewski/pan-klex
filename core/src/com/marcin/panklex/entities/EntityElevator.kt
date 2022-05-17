package com.marcin.panklex.entities

import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.Action
import com.marcin.panklex.BaseEntity
import com.marcin.panklex.screens.ScreenGame

class EntityElevator : BaseEntity("entity Elevator")
{
    var elevatorPositions = mutableListOf<Vector3>()
    var exitPositions = mutableListOf<Vector3>()
    var floorNames = mutableListOf<String>()

    override fun getPositions() : List<Vector3>
    {
        return listOf(*elevatorPositions.toTypedArray(), *exitPositions.toTypedArray())
    }

    override fun isTraversable(position : Vector3) : Boolean
    {
        return when (position)
        {
            in elevatorPositions -> false
            else                 -> true
        }
    }

    override fun isActionPossible(position : Vector3, action : Action) : Boolean
    {
        return when (action)
        {
            Action.Bomb, Action.Hand -> true
            else                     -> false
        }
    }

    override fun onAction(position : Vector3, action : Action, screenGame : ScreenGame)
    {
        when (action)
        {
            Action.Bomb ->
            {
                // would have looked differently if not for ConcurrentModificationException

                var index = 0

                for (p in elevatorPositions)
                {
                    if (p == position) index = elevatorPositions.indexOf(p)
                }

                elevatorPositions.removeAt(index)
                exitPositions.removeAt(index)
                floorNames.removeAt(index)

                screenGame.bombCount--
            }
            Action.Hand ->
            {
                screenGame.game.elevatorScreen.elevatorEntity = this
                screenGame.game.changeScreen(screenGame.game.elevatorScreen)
            }
            else        ->
            {
            }
        }
    }
}
