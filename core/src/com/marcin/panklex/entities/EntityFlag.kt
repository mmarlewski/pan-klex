package com.marcin.panklex.entities

import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.Action
import com.marcin.panklex.BaseEntity
import com.marcin.panklex.screens.ScreenGame

class EntityFlag : BaseEntity("entity Flag")
{
    var flagPosition = Vector3()

    override fun getPositions() : List<Vector3>
    {
        return listOf(flagPosition)
    }

    override fun isTraversable(position : Vector3) : Boolean
    {
        return when (position)
        {
            flagPosition -> false
            else         -> true
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
                screenGame.level.entities.remove(this)
            }
            Action.Walk ->
            {
                screenGame.game.changeScreen(screenGame.game.screenEndGame)
            }
            else        ->
            {
            }
        }
    }
}
