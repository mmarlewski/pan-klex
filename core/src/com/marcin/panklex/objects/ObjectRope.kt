package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*
import com.marcin.panklex.moves.MoveLadder
import com.marcin.panklex.moves.MoveLadderLike
import com.marcin.panklex.moves.MoveRope

class ObjectRope(val ropeOriginPosition : Vector3, var ropeDirection : Direction2d, var ropeLength : Int) :
    ObjectLadderLike(ropeOriginPosition, ropeDirection, ropeLength, "rope")
{
    val firstPosition = centerPositions[1]
    val firstPart = getLadderLikePart(firstPosition)
    val originPart = getLadderLikePart(ladderLikeOriginPosition)
    val lastPosition = centerPositions.last()
    val lastPart = getLadderLikePart(lastPosition)
    val isExtended = false

    init
    {
        createMoves()
    }

    override fun createMoves()
    {
        for (ladderLikePart in ladderLikeParts)
        {
            for (endPosition in endPositions)
            {
                ladderLikePart.centerMoves.add(MoveRope(ladderLikePart.centerPosition, endPosition, this))
            }
        }
    }

    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        positions.addAll(centerPositions)
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        positions.addAll(centerPositions)
        positions.addAll(endPositions)
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : MutableMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val relativeLadderDirection = objectiveToRelativeDirection2d(ropeDirection, mapDirection)

        spaceLayerTiles[SpaceLayer.Behind] = when (spacePosition)
        {
            ropeOriginPosition -> null
            firstPosition      -> when (relativeLadderDirection)
            {
                Direction2d.Up    -> tiles.ropeTopUp
                Direction2d.Right -> tiles.ropeTopRight
                Direction2d.Down  -> tiles.ropeTopDown
                Direction2d.Left  -> tiles.ropeTopLeft
            }
            lastPosition       -> tiles.ropeBottom
            in centerPositions -> tiles.ropeMiddle
            else               -> null
        }

        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = null
        spaceLayerTiles[SpaceLayer.SideUp] = null
        spaceLayerTiles[SpaceLayer.Before] = null
        spaceLayerTiles[SpaceLayer.SideDown] = null
        spaceLayerTiles[SpaceLayer.SideRight] = null
        spaceLayerTiles[SpaceLayer.SideAbove] = null
    }

    override fun getSideTransparency(spaceSideTransparency : MutableMap<Direction3d, Boolean>, spacePosition : Vector3)
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
        val ladderLikePart = getLadderLikePart(spacePosition)

        if (ladderLikePart != null)
        {
            moveList.addAll(ladderLikePart.centerMoves)
        }
    }

    override fun getActions(actionArray : Array<Action?>, spacePosition : Vector3)
    {
        //
    }
}
