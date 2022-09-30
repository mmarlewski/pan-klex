package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*
import com.marcin.panklex.moves.MoveLadder
import com.marcin.panklex.moves.MoveLadderLike

class ObjectLadder(val ladderOriginPosition : Vector3, var ladderDirection : Direction2d, var ladderLength : Int) :
        ObjectLadderLike(ladderOriginPosition, ladderDirection, ladderLength, "ladder")
{
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
                ladderLikePart.centerMoves.add(MoveLadder(ladderLikePart.centerPosition, endPosition, this))
            }
        }
    }

    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        for (position in centerPositions)
        {
            if (position != ladderOriginPosition)
            {
                positions.add(position)
            }
        }
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
        when (spacePosition)
        {
            ladderOriginPosition -> Unit
            else                 ->
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
                    Direction2d.Down  -> tiles.ladderDown
                    Direction2d.Right -> tiles.ladderRight
                    else              -> null
                }
                spaceLayerTiles[SpaceLayer.SideDown] = null
                spaceLayerTiles[SpaceLayer.SideRight] = null
                spaceLayerTiles[SpaceLayer.SideAbove] = null
            }
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
