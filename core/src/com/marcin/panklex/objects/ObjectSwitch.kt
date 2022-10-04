package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class ObjectSwitch(
        val switchPosition : Vector3, var switchDirection : Direction2d, val actionOn : Action, val actionOff : Action
) : Object("switch")
{
    var isOn = false

    val actionManualSwitch = Action(
            "switch manually",
            "( no arrow or upgrade )"
    )
    {
        val entityPlayer = it.level.entityPlayer

        if (arePositionsNextToEachOther(entityPlayer.playerPosition, this.switchPosition))
        {
            when (isOn)
            {
                true  -> actionOn.performAction(it)
                false -> actionOff.performAction(it)
            }
            isOn = !isOn
        }
    }

    val actionArrowSwitch = Action(
            "switch with arrow",
            "( -1 pointing arrow )"
    )
    {
        val entityPlayer = it.level.entityPlayer

        if (entityPlayer.getPlayerItem(PlayerItem.PointingArrow) > 0)
        {
            it.lineFinding.findLine(entityPlayer.playerPosition, this.switchPosition)
            if (it.lineFinding.isLineFound && !it.lineFinding.isLineObstructed)
            {
                when (isOn)
                {
                    true  -> actionOn.performAction(it)
                    false -> actionOff.performAction(it)
                }
                isOn = !isOn

                entityPlayer.changePlayerItem(PlayerItem.PointingArrow, -1)
                it.updateItemLabels()
            }
        }
    }

    val actionUpgradeSwitch = Action(
            "switch with ext. hand",
            "( ext. hand required )"
    )
    {
        val entityPlayer = it.level.entityPlayer

        if (entityPlayer.hasPlayerUpgrade(PlayerUpgrade.ExtendedArm))
        {
            it.lineFinding.findLine(entityPlayer.playerPosition, this.switchPosition)
            if (it.lineFinding.isLineFound && !it.lineFinding.isLineObstructed)
            {
                when (isOn)
                {
                    true  -> actionOn.performAction(it)
                    false -> actionOff.performAction(it)
                }
                isOn = !isOn
            }
        }
    }

    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        positions.add(switchPosition)
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        positions.add(switchPosition)
    }

    override fun getTiles(
            tiles : Tiles, spaceLayerTiles : MutableMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
            mapDirection : Direction2d
    )
    {
        val vendingMachineRelativeDirection = objectiveToRelativeDirection2d(switchDirection, mapDirection)

        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = when (vendingMachineRelativeDirection)
        {
            Direction2d.Left -> tiles.switchButtonLeft
            else             -> null
        }
        spaceLayerTiles[SpaceLayer.SideUp] = when (vendingMachineRelativeDirection)
        {
            Direction2d.Up -> tiles.switchButtonUp
            else           -> null
        }
        spaceLayerTiles[SpaceLayer.SideDown] = when (vendingMachineRelativeDirection)
        {
            Direction2d.Down -> tiles.switchButtonDown
            else             -> tiles.switchBlankDown
        }
        spaceLayerTiles[SpaceLayer.SideRight] = when (vendingMachineRelativeDirection)
        {
            Direction2d.Right -> tiles.switchButtonRight
            else              -> tiles.switchBlankRight
        }
        spaceLayerTiles[SpaceLayer.SideAbove] = tiles.switchBlankAbove
    }

    override fun getSideTransparency(spaceSideTransparency : MutableMap<Direction3d, Boolean>, spacePosition : Vector3)
    {
        spaceSideTransparency[Direction3d.Below] = false
        spaceSideTransparency[Direction3d.Left] = false
        spaceSideTransparency[Direction3d.Up] = false
        spaceSideTransparency[Direction3d.Down] = false
        spaceSideTransparency[Direction3d.Right] = false
        spaceSideTransparency[Direction3d.Above] = false
    }

    override fun canStoreEntity(spacePosition : Vector3) : Boolean
    {
        return false
    }

    override fun isGround(spacePosition : Vector3) : Boolean
    {
        return true
    }

    override fun getMoves(moveList : MutableList<Move>, spacePosition : Vector3, room : Room)
    {
        //
    }

    override fun getActions(actionArray : Array<Action?>, spacePosition : Vector3)
    {
        actionArray[0] = actionManualSwitch.apply {
            isActionPossible = true
        }
        actionArray[1] = actionArrowSwitch.apply {
            isActionPossible = true
        }
        actionArray[2] = actionUpgradeSwitch.apply {
            isActionPossible = true
        }
    }
}
