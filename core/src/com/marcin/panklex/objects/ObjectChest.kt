package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class ObjectChest(val chestPosition : Vector3, val chestDirection : Direction2d) : Object("chest")
{
    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        positions.add(chestPosition)
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        positions.add(chestPosition)
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : HashMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val relativeChestDirection = objectiveToRelativeDirection2d(chestDirection, mapDirection)

        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = null
        spaceLayerTiles[SpaceLayer.SideUp] = null
        spaceLayerTiles[SpaceLayer.Behind] = tiles.chestSideBehind
        spaceLayerTiles[SpaceLayer.Before] = tiles.chestSideBefore
        spaceLayerTiles[SpaceLayer.SideDown] = when (relativeChestDirection)
        {
            Direction2d.Down -> tiles.chestFrontDown
            Direction2d.Up -> tiles.chestBackDown
            else             -> tiles.chestSideDown
        }
        spaceLayerTiles[SpaceLayer.SideRight] = when (relativeChestDirection)
        {
            Direction2d.Right -> tiles.chestFrontRight
            Direction2d.Left -> tiles.chestBackRight
            else              -> tiles.chestSideRight
        }
        spaceLayerTiles[SpaceLayer.SideAbove] = tiles.chestSideAbove
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
