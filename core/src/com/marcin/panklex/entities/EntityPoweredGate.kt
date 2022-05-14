package com.marcin.panklex.entities

import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.Action
import com.marcin.panklex.BaseEntity
import com.marcin.panklex.Direction
import com.marcin.panklex.screens.ScreenGame

class EntityPoweredGate : BaseEntity("entity PoweredGate")
{
    var firstPartPosition = Vector3()
    var firstPartDirection = Direction.None
    var isFirstPartPowered = false
    var secondPartPosition = Vector3()
    var secondPartDirection = Direction.None
    var isSecondPartPowered = false
    var gatePosition = Vector3()
    var isGateVertical = false
    var isGateOpen = false

    override fun getPositions() : List<Vector3>
    {
        val list = mutableListOf(firstPartPosition, secondPartPosition)

        if (isFirstPartPowered || isSecondPartPowered) list.add(gatePosition)

        return list
    }

    override fun isTraversable(position : Vector3) : Boolean
    {
        return when (position)
        {
            firstPartPosition, secondPartPosition -> false
            gatePosition                          -> !(isFirstPartPowered || isSecondPartPowered)
            else                                  -> true
        }
    }

    override fun isActionPossible(position : Vector3, action : Action) : Boolean
    {
        return when (action)
        {
            Action.Bomb -> true
            Action.Cell ->
            {
                when (position)
                {
                    firstPartPosition, secondPartPosition -> true
                    else                                  -> false
                }
            }
            Action.Hand ->
            {
                when (position)
                {
                    firstPartPosition, secondPartPosition -> true
                    else                                  -> false
                }
            }
            else        -> false
        }
    }

    override fun onAction(position : Vector3, action : Action, screenGame : ScreenGame)
    {
        when (action)
        {
            Action.Bomb ->
            {
                when (position)
                {
                    gatePosition ->
                    {
                        isFirstPartPowered = false
                        isSecondPartPowered = false
                    }
                    else         ->
                    {
                        screenGame.level.entities.remove(this)
                    }
                }
            }
            Action.Cell ->
            {
                when (position)
                {
                    firstPartPosition  -> if (!isFirstPartPowered && screenGame.cellCount > 0)
                    {
                        isFirstPartPowered = true
                        screenGame.cellCount--
                    }
                    secondPartPosition -> if (!isSecondPartPowered && screenGame.cellCount > 0)
                    {
                        isSecondPartPowered = true
                        screenGame.cellCount--
                    }
                    else               ->
                    {
                    }
                }
            }
            Action.Hand ->
            {
                when (position)
                {
                    firstPartPosition  -> if (isFirstPartPowered)
                    {
                        isFirstPartPowered = false
                        screenGame.cellCount++
                    }
                    secondPartPosition -> if (isSecondPartPowered)
                    {
                        isSecondPartPowered = false
                        screenGame.cellCount++
                    }
                    else               ->
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
