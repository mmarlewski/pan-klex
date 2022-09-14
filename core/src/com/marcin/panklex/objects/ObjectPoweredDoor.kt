package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class ObjectPoweredDoor(val poweredDoorPosition : Vector3, val poweredDoorDirection : Direction2d) : Object("poweredDoor")
{
    var isPoweredDoorPowered = false

    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        positions.add(poweredDoorPosition)
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        positions.add(poweredDoorPosition)
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : HashMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val relativePoweredDoorDirection = objectiveToRelativeDirection2d(poweredDoorDirection, mapDirection)

        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = when (relativePoweredDoorDirection)
        {
            Direction2d.Left -> when (isPoweredDoorPowered)
            {
                true  -> tiles.poweredDoorPoweredLeft
                false -> tiles.poweredDoorUnpoweredLeft
            }
            else             -> null
        }
        spaceLayerTiles[SpaceLayer.SideUp] = when (relativePoweredDoorDirection)
        {
            Direction2d.Up -> when (isPoweredDoorPowered)
            {
                true  -> tiles.poweredDoorPoweredUp
                false -> tiles.poweredDoorUnpoweredUp
            }
            else           -> null
        }
        spaceLayerTiles[SpaceLayer.Behind] = tiles.poweredDoorBlankBehind
        spaceLayerTiles[SpaceLayer.Before] = tiles.poweredDoorBlankBefore
        spaceLayerTiles[SpaceLayer.SideDown] = when (relativePoweredDoorDirection)
        {
            Direction2d.Down -> when (isPoweredDoorPowered)
            {
                true  -> tiles.poweredDoorPoweredDown
                false -> tiles.poweredDoorUnpoweredDown
            }
            else             -> tiles.poweredDoorBlankDown
        }
        spaceLayerTiles[SpaceLayer.SideRight] = when (relativePoweredDoorDirection)
        {
            Direction2d.Right -> when (isPoweredDoorPowered)
            {
                true  -> tiles.poweredDoorPoweredRight
                false -> tiles.poweredDoorUnpoweredRight
            }
            else              -> tiles.poweredDoorBlankRight
        }
        spaceLayerTiles[SpaceLayer.SideAbove] = tiles.poweredDoorBlankAbove
    }

    override fun getSideTransparency(spaceSideTransparency : HashMap<Direction3d, Boolean>, spacePosition : Vector3)
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
}
