package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class ObjectArch(val archPosition : Vector3, var archDirection : Direction2d) : Object("arch")
{
    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        positions.add(archPosition)
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        positions.add(archPosition)
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : HashMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val relativeArchPosition = objectiveToRelativeDirection2d(archDirection, mapDirection)

        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = null
        spaceLayerTiles[SpaceLayer.SideUp] = null
        spaceLayerTiles[SpaceLayer.Behind] = when (relativeArchPosition)
        {
            Direction2d.Left, Direction2d.Right -> tiles.archRightBehind
            Direction2d.Up, Direction2d.Down    -> tiles.archUpBehind
        }
        spaceLayerTiles[SpaceLayer.Before] = when (relativeArchPosition)
        {
            Direction2d.Left, Direction2d.Right -> tiles.archRightBefore
            Direction2d.Up, Direction2d.Down    -> tiles.archUpBefore
        }
        spaceLayerTiles[SpaceLayer.SideDown] = when (relativeArchPosition)
        {
            Direction2d.Left, Direction2d.Right -> tiles.archRightDown
            Direction2d.Up, Direction2d.Down    -> tiles.archUpDown
        }
        spaceLayerTiles[SpaceLayer.SideRight] = when (relativeArchPosition)
        {
            Direction2d.Left, Direction2d.Right -> tiles.archRightRight
            Direction2d.Up, Direction2d.Down    -> tiles.archUpRight
        }
        spaceLayerTiles[SpaceLayer.SideAbove] = when (relativeArchPosition)
        {
            Direction2d.Left, Direction2d.Right -> tiles.archRightAbove
            Direction2d.Up, Direction2d.Down    -> tiles.archUpAbove
        }
    }

    override fun getSideTransparency(spaceSideTransparency : HashMap<Direction3d, Boolean>, spacePosition : Vector3)
    {
        spaceSideTransparency[Direction3d.Below] = true
        spaceSideTransparency[Direction3d.Left] = when (archDirection)
        {
            Direction2d.Left, Direction2d.Right -> false
            Direction2d.Up, Direction2d.Down    -> true
        }
        spaceSideTransparency[Direction3d.Up] = when (archDirection)
        {
            Direction2d.Left, Direction2d.Right -> true
            Direction2d.Up, Direction2d.Down    -> false
        }
        spaceSideTransparency[Direction3d.Down] = when (archDirection)
        {
            Direction2d.Left, Direction2d.Right -> true
            Direction2d.Up, Direction2d.Down    -> false
        }
        spaceSideTransparency[Direction3d.Right] = when (archDirection)
        {
            Direction2d.Left, Direction2d.Right -> false
            Direction2d.Up, Direction2d.Down    -> true
        }
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
