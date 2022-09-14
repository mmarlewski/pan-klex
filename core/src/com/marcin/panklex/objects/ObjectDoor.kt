package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class ObjectDoor(val doorPosition : Vector3, val doorDirection : Direction2d) : Object("door")
{
    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        positions.add(doorPosition)
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        positions.add(doorPosition)
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : HashMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val relativeDoorDirection = objectiveToRelativeDirection2d(doorDirection, mapDirection)

        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = when (relativeDoorDirection)
        {
            Direction2d.Left -> tiles.doorDoorLeft
            else             -> null
        }
        spaceLayerTiles[SpaceLayer.SideUp] = when (relativeDoorDirection)
        {
            Direction2d.Up -> tiles.doorDoorUp
            else           -> null
        }
        spaceLayerTiles[SpaceLayer.Behind] = tiles.doorBlankBehind
        spaceLayerTiles[SpaceLayer.Before] = tiles.doorBlankBefore
        spaceLayerTiles[SpaceLayer.SideDown] = when (relativeDoorDirection)
        {
            Direction2d.Down -> tiles.doorDoorDown
            else             -> tiles.doorBlankDown
        }
        spaceLayerTiles[SpaceLayer.SideRight] = when (relativeDoorDirection)
        {
            Direction2d.Right -> tiles.doorDoorRight
            else              -> tiles.doorBlankRight
        }
        spaceLayerTiles[SpaceLayer.SideAbove] = tiles.doorDoorAbove
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
