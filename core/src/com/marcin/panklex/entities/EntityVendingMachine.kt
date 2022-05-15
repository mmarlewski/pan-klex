package com.marcin.panklex.entities

import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.Action
import com.marcin.panklex.BaseEntity
import com.marcin.panklex.screens.ScreenGame

class EntityVendingMachine : BaseEntity("entity VendingMachine")
{
    var vendingMachinePosition = Vector3()
    var isBroken = false
    var pickaxes = 0
    var bombs = 0
    var energyCells = 0

    override fun getPositions() : List<Vector3>
    {
        return listOf(vendingMachinePosition)
    }

    override fun isTraversable(position : Vector3) : Boolean
    {
        return when (position)
        {
            vendingMachinePosition -> false
            else                   -> true
        }
    }

    override fun isActionPossible(position : Vector3, action : Action) : Boolean
    {
        return when (action)
        {
            Action.Bomb              -> true
            Action.Coin, Action.Cell -> !isBroken
            Action.Hand              -> isBroken
            else                     -> false
        }
    }

    override fun onAction(position : Vector3, action : Action, screenGame : ScreenGame)
    {
        when (action)
        {
            Action.Bomb ->
            {
                screenGame.level.entities.remove(this)
            }
            Action.Coin ->
            {
                if (!isBroken)
                {
                    if (screenGame.coinCount > 0)
                    {
                        screenGame.coinCount--

                        screenGame.pickaxeCount++
                        screenGame.bombCount++
                        screenGame.cellCount++
                    }
                }
            }
            Action.Cell ->
            {
                isBroken = true
            }
            Action.Hand ->
            {
                if (isBroken)
                {
                    screenGame.pickaxeCount++
                    screenGame.bombCount++
                    screenGame.cellCount++
                }
            }
            else        ->
            {
            }
        }
    }
}
