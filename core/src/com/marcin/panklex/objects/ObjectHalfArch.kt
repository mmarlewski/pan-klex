package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class ObjectHalfArch(val halfArchPosition : Vector3, var halfArchDirection : Direction2d) : Object("halfArch")
{
    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        positions.add(halfArchPosition)
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        positions.add(halfArchPosition)
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : MutableMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val relativeHalfArchDirection = objectiveToRelativeDirection2d(halfArchDirection, mapDirection)

        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = null
        spaceLayerTiles[SpaceLayer.SideUp] = null
        spaceLayerTiles[SpaceLayer.Behind] = when (relativeHalfArchDirection)
        {
            Direction2d.Left  -> tiles.halfArchLeftBehind
            Direction2d.Right -> tiles.halfArchRightBehind
            Direction2d.Up    -> tiles.halfArchUpBehind
            Direction2d.Down  -> tiles.halfArchDownBehind
        }
        spaceLayerTiles[SpaceLayer.Before] = when (relativeHalfArchDirection)
        {
            Direction2d.Left  -> tiles.halfArchLeftBefore
            Direction2d.Right -> tiles.halfArchRightBefore
            Direction2d.Up    -> tiles.halfArchUpBefore
            Direction2d.Down  -> tiles.halfArchDownBefore
        }
        spaceLayerTiles[SpaceLayer.SideDown] = when (relativeHalfArchDirection)
        {
            Direction2d.Left  -> tiles.halfArchLeftDown
            Direction2d.Right -> tiles.halfArchRightDown
            Direction2d.Up    -> tiles.halfArchUpDown
            Direction2d.Down  -> tiles.halfArchDownDown
        }
        spaceLayerTiles[SpaceLayer.SideRight] = when (relativeHalfArchDirection)
        {
            Direction2d.Left  -> tiles.halfArchLeftRight
            Direction2d.Right -> tiles.halfArchRightRight
            Direction2d.Up    -> tiles.halfArchUpRight
            Direction2d.Down  -> tiles.halfArchDownRight
        }
        spaceLayerTiles[SpaceLayer.SideAbove] = when (relativeHalfArchDirection)
        {
            Direction2d.Left  -> tiles.halfArchLeftAbove
            Direction2d.Right -> tiles.halfArchRightAbove
            Direction2d.Up    -> tiles.halfArchUpAbove
            Direction2d.Down  -> tiles.halfArchDownAbove
        }
    }

    override fun getSideTransparency(spaceSideTransparency : MutableMap<Direction3d, Boolean>, spacePosition : Vector3)
    {
        spaceSideTransparency[Direction3d.Below] = true
        spaceSideTransparency[Direction3d.Left] = true
        spaceSideTransparency[Direction3d.Up] = true
        spaceSideTransparency[Direction3d.Down] = true
        spaceSideTransparency[Direction3d.Right] = true
        spaceSideTransparency[Direction3d.Above] = false

        spaceSideTransparency[direction2dToDirection3d(halfArchDirection)] = false
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
