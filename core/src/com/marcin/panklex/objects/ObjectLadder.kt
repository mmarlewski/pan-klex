package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class LadderPart(val centerPosition : Vector3, val ladder : ObjectLadder)
{
    val ladderPartPositions = mutableMapOf<Dist1Conn, Vector3>().apply {
        for (direction in Dist1Conn.values())
            this[direction] = getPositionDist1Conn(centerPosition, direction)
    }

    val centerMoves = mutableListOf<LadderMove>()

    fun addMoves()
    {
        for (ladderPart in ladder.ladderParts)
        {
            for (direction in Dist1Conn.values())
            {
                if (!(ladderPart.centerPosition == ladder.originPosition && dist1ConnToDirection2d(
                        direction) != ladder.ladderDirection))
                {
                    centerMoves.add(LadderMove(this, ladderPart, direction, ladder.ladderDirection))
                }
            }
        }
    }
}

class ObjectLadder(val originPosition : Vector3, var ladderDirection : Direction2d, var ladderLength : Int) :
    Object("ladder")
{
    val ladderParts = mutableListOf<LadderPart>()

    init
    {
        ladderParts.add(LadderPart(originPosition, this))

        for (i in 1 until ladderLength)
        {
            ladderParts.add(
                LadderPart(Vector3(originPosition.x, originPosition.y, originPosition.z - i), this))
        }

        for (ladderPart in ladderParts)
        {
            ladderPart.addMoves()
        }
    }

    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        for (ladderPart in ladderParts)
        {
            if (ladderPart.centerPosition != originPosition)
            {
                positions.add(ladderPart.centerPosition)
            }
        }
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        for (ladderPart in ladderParts)
        {
            for (direction in Dist1Conn.values())
            {
                positions.add(ladderPart.ladderPartPositions[direction]!!)
            }
        }
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : HashMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val relativeLadderDirection = objectiveToRelativeDirection2d(ladderDirection, mapDirection)

        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = null
        spaceLayerTiles[SpaceLayer.SideUp] = null
        spaceLayerTiles[SpaceLayer.Behind] = when (relativeLadderDirection)
        {
            Direction2d.Left -> tiles.ladderLeft
            Direction2d.Up   -> tiles.ladderUp
            else             -> null
        }
        spaceLayerTiles[SpaceLayer.Before] = when (relativeLadderDirection)
        {
            Direction2d.Down -> tiles.ladderDown
            Direction2d.Right -> tiles.ladderRight
            else -> null
        }
        spaceLayerTiles[SpaceLayer.SideDown] = null
        spaceLayerTiles[SpaceLayer.SideRight] = null
        spaceLayerTiles[SpaceLayer.SideAbove] = null
    }

    override fun getSideTransparency(spaceSideTransparency : HashMap<Direction3d, Boolean>, spacePosition : Vector3)
    {
        spaceSideTransparency[Direction3d.Below] = true
        spaceSideTransparency[Direction3d.Left] = true
        spaceSideTransparency[Direction3d.Up] = true
        spaceSideTransparency[Direction3d.Down] = true
        spaceSideTransparency[Direction3d.Right] = true
        spaceSideTransparency[Direction3d.Above] = true
    }

    override fun canStoreEntity(spacePosition : Vector3) : Boolean
    {
        return true
    }

    override fun isGround(spacePosition : Vector3) : Boolean
    {
        return false
    }

    override fun getMoves(moveList : MutableList<Move>, spacePosition : Vector3, room : Room)
    {
        for (ladderPart in ladderParts)
        {
            if (spacePosition == ladderPart.centerPosition)
            {
                moveList.addAll(ladderPart.centerMoves)
            }
        }
    }
}
