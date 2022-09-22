package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*
import com.marcin.panklex.moves.MoveStairs

enum class StairsPosition
{
    Center, Left, DownLeft, Down, DownRight, Right, AboveCenter, AboveUp, AboveLeft, AboveRight
}

fun getStairsPosition(
    centerPosition : Vector3, stairsPosition : StairsPosition, stairsDirection : Direction2d) : Vector3
{
    return when (stairsPosition)
    {
        StairsPosition.Center      -> Vector3(centerPosition.x, centerPosition.y, centerPosition.z)
        StairsPosition.Left        -> when (stairsDirection)
        {
            Direction2d.Up    -> Vector3(centerPosition.x - 1, centerPosition.y, centerPosition.z)
            Direction2d.Right -> Vector3(centerPosition.x, centerPosition.y + 1, centerPosition.z)
            Direction2d.Down  -> Vector3(centerPosition.x + 1, centerPosition.y, centerPosition.z)
            Direction2d.Left  -> Vector3(centerPosition.x, centerPosition.y - 1, centerPosition.z)
        }
        StairsPosition.DownLeft    -> when (stairsDirection)
        {
            Direction2d.Up    -> Vector3(centerPosition.x - 1, centerPosition.y - 1, centerPosition.z)
            Direction2d.Right -> Vector3(centerPosition.x - 1, centerPosition.y + 1, centerPosition.z)
            Direction2d.Down  -> Vector3(centerPosition.x + 1, centerPosition.y + 1, centerPosition.z)
            Direction2d.Left  -> Vector3(centerPosition.x + 1, centerPosition.y - 1, centerPosition.z)
        }
        StairsPosition.Down        -> when (stairsDirection)
        {
            Direction2d.Up    -> Vector3(centerPosition.x, centerPosition.y - 1, centerPosition.z)
            Direction2d.Right -> Vector3(centerPosition.x - 1, centerPosition.y, centerPosition.z)
            Direction2d.Down  -> Vector3(centerPosition.x, centerPosition.y + 1, centerPosition.z)
            Direction2d.Left  -> Vector3(centerPosition.x + 1, centerPosition.y, centerPosition.z)
        }
        StairsPosition.DownRight   -> when (stairsDirection)
        {
            Direction2d.Up    -> Vector3(centerPosition.x + 1, centerPosition.y - 1, centerPosition.z)
            Direction2d.Right -> Vector3(centerPosition.x - 1, centerPosition.y - 1, centerPosition.z)
            Direction2d.Down  -> Vector3(centerPosition.x - 1, centerPosition.y + 1, centerPosition.z)
            Direction2d.Left  -> Vector3(centerPosition.x + 1, centerPosition.y + 1, centerPosition.z)
        }
        StairsPosition.Right       -> when (stairsDirection)
        {
            Direction2d.Up    -> Vector3(centerPosition.x + 1, centerPosition.y, centerPosition.z)
            Direction2d.Right -> Vector3(centerPosition.x, centerPosition.y - 1, centerPosition.z)
            Direction2d.Down  -> Vector3(centerPosition.x - 1, centerPosition.y, centerPosition.z)
            Direction2d.Left  -> Vector3(centerPosition.x, centerPosition.y + 1, centerPosition.z)
        }
        StairsPosition.AboveCenter -> Vector3(centerPosition.x, centerPosition.y, centerPosition.z + 1)
        StairsPosition.AboveUp     -> when (stairsDirection)
        {
            Direction2d.Up    -> Vector3(centerPosition.x, centerPosition.y + 1, centerPosition.z + 1)
            Direction2d.Right -> Vector3(centerPosition.x + 1, centerPosition.y, centerPosition.z + 1)
            Direction2d.Down  -> Vector3(centerPosition.x, centerPosition.y - 1, centerPosition.z + 1)
            Direction2d.Left  -> Vector3(centerPosition.x - 1, centerPosition.y, centerPosition.z + 1)
        }
        StairsPosition.AboveLeft   -> when (stairsDirection)
        {
            Direction2d.Up    -> Vector3(centerPosition.x - 1, centerPosition.y, centerPosition.z + 1)
            Direction2d.Right -> Vector3(centerPosition.x, centerPosition.y + 1, centerPosition.z + 1)
            Direction2d.Down  -> Vector3(centerPosition.x + 1, centerPosition.y, centerPosition.z + 1)
            Direction2d.Left  -> Vector3(centerPosition.x, centerPosition.y - 1, centerPosition.z + 1)
        }
        StairsPosition.AboveRight  -> when (stairsDirection)
        {
            Direction2d.Up    -> Vector3(centerPosition.x + 1, centerPosition.y, centerPosition.z + 1)
            Direction2d.Right -> Vector3(centerPosition.x, centerPosition.y - 1, centerPosition.z + 1)
            Direction2d.Down  -> Vector3(centerPosition.x - 1, centerPosition.y, centerPosition.z + 1)
            Direction2d.Left  -> Vector3(centerPosition.x, centerPosition.y + 1, centerPosition.z + 1)
        }
    }
}

class ObjectStairs(val stairsPosition : Vector3, var stairsDirection : Direction2d) : Object("stairs")
{
    val stairsPositions = mutableMapOf<StairsPosition, Vector3>().apply {
        for (position in StairsPosition.values())
            this[position] = getStairsPosition(stairsPosition, position, stairsDirection)
    }

    val centerMoves = mutableMapOf<StairsPosition, MoveStairs>().apply {
        for (position in StairsPosition.values())
            this[position] = MoveStairs(
                stairsPosition, stairsDirection, stairsPositions[StairsPosition.Center]!!, stairsPositions[position]!!)
    }

    val aboveMoves = mutableMapOf<StairsPosition, MoveStairs>().apply {
        for (position in StairsPosition.values())
            this[position] = MoveStairs(
                stairsPosition, stairsDirection, stairsPositions[StairsPosition.AboveCenter]!!, stairsPositions[position]!!)
    }

    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        positions.add(stairsPosition)
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        positions.add(stairsPositions[StairsPosition.Center]!!)
        positions.add(stairsPositions[StairsPosition.AboveCenter]!!)
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : MutableMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val relativeStairsDirection2d = objectiveToRelativeDirection2d(stairsDirection, mapDirection)

        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = null
        spaceLayerTiles[SpaceLayer.SideUp] = null
        spaceLayerTiles[SpaceLayer.Behind] = when (relativeStairsDirection2d)
        {
            Direction2d.Up    -> tiles.stairsUpBehind
            Direction2d.Right -> tiles.stairsRightBehind
            Direction2d.Down  -> tiles.stairsDownBehind
            Direction2d.Left  -> tiles.stairsLeftBehind
        }
        spaceLayerTiles[SpaceLayer.Before] = when (relativeStairsDirection2d)
        {
            Direction2d.Up    -> tiles.stairsUpBefore
            Direction2d.Right -> tiles.stairsRightBefore
            Direction2d.Down  -> tiles.stairsDownBefore
            Direction2d.Left  -> tiles.stairsLeftBefore
        }
        spaceLayerTiles[SpaceLayer.SideDown] = when (relativeStairsDirection2d)
        {
            Direction2d.Up    -> tiles.stairsUpDown
            Direction2d.Right -> tiles.stairsRightDown
            Direction2d.Down  -> tiles.stairsDownDown
            Direction2d.Left  -> tiles.stairsLeftDown
        }
        spaceLayerTiles[SpaceLayer.SideRight] = when (relativeStairsDirection2d)
        {
            Direction2d.Up    -> tiles.stairsUpRight
            Direction2d.Right -> tiles.stairsRightRight
            Direction2d.Down  -> tiles.stairsDownRight
            Direction2d.Left  -> tiles.stairsLeftRight
        }
        spaceLayerTiles[SpaceLayer.SideAbove] = when (relativeStairsDirection2d)
        {
            Direction2d.Up    -> tiles.stairsUpAbove
            Direction2d.Right -> tiles.stairsRightAbove
            Direction2d.Down  -> tiles.stairsDownAbove
            Direction2d.Left  -> tiles.stairsLeftAbove
        }
    }

    override fun getSideTransparency(spaceSideTransparency : MutableMap<Direction3d, Boolean>, spacePosition : Vector3)
    {
        spaceSideTransparency[Direction3d.Below] = false
        spaceSideTransparency[Direction3d.Left] = true
        spaceSideTransparency[Direction3d.Up] = true
        spaceSideTransparency[Direction3d.Down] = true
        spaceSideTransparency[Direction3d.Right] = true
        spaceSideTransparency[Direction3d.Above] = true

        spaceSideTransparency[direction2dToDirection3d(stairsDirection)] = false
    }

    override fun canStoreEntity(spacePosition : Vector3) : Boolean
    {
        return false
    }

    override fun isGround(spacePosition : Vector3) : Boolean
    {
        return false
    }

    override fun getMoves(moveList : MutableList<Move>, spacePosition : Vector3, room : Room)
    {
        when (spacePosition)
        {
            stairsPositions[StairsPosition.Center]      -> moveList.addAll(centerMoves.values)
            stairsPositions[StairsPosition.AboveCenter] -> moveList.addAll(aboveMoves.values)
        }
    }

    override fun getActions(actionArray : Array<Action?>, spacePosition : Vector3)
    {
        //
    }
}
