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
    var cells = 0

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
            Action.Bomb, Action.Hand -> true
            Action.Cell              -> !isBroken
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
            Action.Cell ->
            {
                isBroken = true
            }
            Action.Hand ->
            {
                screenGame.game.vendingMachineScreen.vendingMachineEntity = this
                screenGame.game.changeScreen(screenGame.game.vendingMachineScreen)
            }
            else        ->
            {
            }
        }
    }
}
