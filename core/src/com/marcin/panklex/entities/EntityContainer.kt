package com.marcin.panklex.entities

import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.Action
import com.marcin.panklex.BaseEntity
import com.marcin.panklex.screens.ScreenGame

class EntityContainer : BaseEntity("entity Container")
{
    var containerPosition = Vector3()
    var pickaxes = 0
    var bombs = 0
    var cells = 0
    var coins = 0

    override fun getPositions() : List<Vector3>
    {
        return listOf(containerPosition)
    }

    override fun isTraversable(position : Vector3) : Boolean
    {
        return when (position)
        {
            containerPosition -> false
            else              -> true
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
                screenGame.level.entities.remove(this)
                screenGame.bombCount--
            }
            Action.Hand ->
            {
                screenGame.game.containerScreen.containerEntity = this
                screenGame.game.changeScreen(screenGame.game.containerScreen)
            }
            else        ->
            {
            }
        }
    }
}
