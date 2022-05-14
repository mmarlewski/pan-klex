package com.marcin.panklex.entities

import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.Action
import com.marcin.panklex.BaseEntity
import com.marcin.panklex.Direction
import com.marcin.panklex.screens.ScreenGame

class EntityStairs : BaseEntity("entity Stairs")
{
    var stairsPosition = Vector3()
    var direction = Direction.None
    var upperEnd = Vector3()
    var lowerEnd = Vector3()

    override fun getPositions() : List<Vector3>
    {
        return listOf(stairsPosition, upperEnd, lowerEnd)
    }

    override fun isTraversable(position : Vector3) : Boolean
    {
        return when (position)
        {
            stairsPosition -> false
            else           -> true
        }
    }

    override fun isActionPossible(position : Vector3, action : Action) : Boolean
    {
        return when (action)
        {
            Action.Bomb, Action.Walk -> true
            else                     -> false
        }
    }

    override fun onAction(position : Vector3, action : Action, screenGame : ScreenGame)
    {
        when (action)
        {
            Action.Bomb ->
            {
                if (position == stairsPosition)
                {
                    screenGame.level.entities.remove(this)
                }
            }
            Action.Walk ->
            {
                when (position)
                {
                    stairsPosition ->
                    {
                        screenGame.changePlayerPosition(upperEnd)
                        screenGame.changeLevel(screenGame.playerPosition.z.toInt())
                    }
                    upperEnd       ->
                    {
                        screenGame.changePlayerPosition(lowerEnd)
                        screenGame.changeLevel(screenGame.playerPosition.z.toInt())
                    }
                    else           ->
                    {
                    }
                }
            }
            else        ->
            {
            }
        }
    }
}
