package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class ObjectHalfColumn(val halfColumnPosition : Vector3, var halfColumnDirection : Direction2d) : Object("halfColumn")
{
    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        positions.add(halfColumnPosition)
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        positions.add(halfColumnPosition)
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : MutableMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val relativeHalfColumnDirection = objectiveToRelativeDirection2d(halfColumnDirection, mapDirection)

        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = null
        spaceLayerTiles[SpaceLayer.SideUp] = null
        spaceLayerTiles[SpaceLayer.Behind] = when (relativeHalfColumnDirection)
        {
            Direction2d.Up    -> tiles.halfColumnUpBehind
            Direction2d.Right -> tiles.halfColumnRightBehind
            Direction2d.Down  -> tiles.halfColumnDownBehind
            Direction2d.Left  -> tiles.halfColumnLeftBehind
        }
        spaceLayerTiles[SpaceLayer.Before] = when (relativeHalfColumnDirection)
        {
            Direction2d.Up    -> tiles.halfColumnUpBefore
            Direction2d.Right -> tiles.halfColumnRightBefore
            Direction2d.Down  -> tiles.halfColumnDownBefore
            Direction2d.Left  -> tiles.halfColumnLeftBefore
        }
        spaceLayerTiles[SpaceLayer.SideDown] = when (relativeHalfColumnDirection)
        {
            Direction2d.Up    -> tiles.halfColumnUpDown
            Direction2d.Right -> tiles.halfColumnRightDown
            Direction2d.Down  -> tiles.halfColumnDownDown
            Direction2d.Left  -> tiles.halfColumnLeftDown
        }
        spaceLayerTiles[SpaceLayer.SideRight] = when (relativeHalfColumnDirection)
        {
            Direction2d.Up    -> tiles.halfColumnUpRight
            Direction2d.Right -> tiles.halfColumnRightRight
            Direction2d.Down  -> tiles.halfColumnDownRight
            Direction2d.Left  -> tiles.halfColumnLeftRight
        }
        spaceLayerTiles[SpaceLayer.SideAbove] = when (relativeHalfColumnDirection)
        {
            Direction2d.Up    -> tiles.halfColumnUpAbove
            Direction2d.Right -> tiles.halfColumnRightAbove
            Direction2d.Down  -> tiles.halfColumnDownAbove
            Direction2d.Left  -> tiles.halfColumnLeftAbove
        }
    }

    override fun getSideTransparency(spaceSideTransparency : MutableMap<Direction3d, Boolean>, spacePosition : Vector3)
    {
        spaceSideTransparency[Direction3d.Below] = true
        spaceSideTransparency[Direction3d.Left] = true
        spaceSideTransparency[Direction3d.Up] = true
        spaceSideTransparency[Direction3d.Down] = true
        spaceSideTransparency[Direction3d.Right] = true
        spaceSideTransparency[Direction3d.Above] = true

        spaceSideTransparency[direction2dToDirection3d(oppositeDirection2d(halfColumnDirection))] = false
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
        //
    }
}
