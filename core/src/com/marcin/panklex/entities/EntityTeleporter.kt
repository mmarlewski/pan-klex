package com.marcin.panklex.entities

import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.marcin.panklex.Action
import com.marcin.panklex.BaseEntity
import com.marcin.panklex.screens.ScreenGame

class EntityTeleporter : BaseEntity("entity Teleporter")
{
    var firstTelePosition = Vector3()
    var firstTeleExitPosition = Vector3()
    var isFirstTelePowered = false
    var secondTelePosition = Vector3()
    var secondTeleExitPosition = Vector3()
    var isSecondTelePowered = false

    override fun getPositions() : List<Vector3>
    {
        return listOf(firstTelePosition, firstTeleExitPosition, secondTelePosition, secondTeleExitPosition)
    }

    override fun isTraversable(position : Vector3) : Boolean
    {
        return when (position)
        {
            firstTelePosition, secondTelePosition -> false
            else                                  -> true
        }
    }

    override fun isActionPossible(position : Vector3, action : Action) : Boolean
    {
        return when (action)
        {
            Action.Bomb, Action.Cell, Action.Hand, Action.Walk -> true
            else                                               -> false
        }
    }

    override fun onAction(position : Vector3, action : Action, screenGame : ScreenGame)
    {
        when (action)
        {
            Action.Bomb ->
            {
                screenGame.level.entities.remove(this)
                screenGame.bombCount--
            }
            Action.Cell ->
            {
                when (position)
                {
                    firstTelePosition  -> if (!isFirstTelePowered && screenGame.cellCount > 0)
                    {
                        isFirstTelePowered = true
                        screenGame.cellCount--
                    }
                    secondTelePosition -> if (!isSecondTelePowered && screenGame.cellCount > 0)
                    {
                        isSecondTelePowered = true
                        screenGame.cellCount--
                    }
                }
            }
            Action.Hand ->
            {
                when (position)
                {
                    firstTelePosition  -> if (isFirstTelePowered)
                    {
                        isFirstTelePowered = false
                        screenGame.cellCount++
                    }
                    secondTelePosition -> if (isSecondTelePowered)
                    {
                        isSecondTelePowered = false
                        screenGame.cellCount++
                    }
                }
            }
            Action.Walk ->
            {
                when (position)
                {
                    firstTelePosition  -> if (isFirstTelePowered && isSecondTelePowered)
                    {
                        screenGame.changeHearts(-1)
                        screenGame.changePlayerPosition(secondTeleExitPosition)
                        screenGame.changeLevel(screenGame.playerPosition.z.toInt())
                    }
                    secondTelePosition -> if (isFirstTelePowered && isSecondTelePowered)
                    {
                        screenGame.changeHearts(-1)
                        screenGame.changePlayerPosition(firstTeleExitPosition)
                        screenGame.changeLevel(screenGame.playerPosition.z.toInt())
                    }
                }
            }
            else        ->
            {
            }
        }
    }
}
