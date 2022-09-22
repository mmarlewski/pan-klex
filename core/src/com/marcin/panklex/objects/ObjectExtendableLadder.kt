package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*
import com.marcin.panklex.moves.MoveExtendableLadder
import com.marcin.panklex.moves.MoveLadder

class ObjectExtendableLadder(val ladderOriginPosition : Vector3, var ladderDirection : Direction2d, var ladderLength : Int) :
    ObjectLadderLike(ladderOriginPosition, ladderDirection, ladderLength, "extendable ladder")
{
    val firstPosition = centerPositions[1]
    val firstPart = getLadderLikePart(firstPosition)
    val originPart = getLadderLikePart(ladderLikeOriginPosition)
    var isExtended = false

    init
    {
        createMoves()
    }

    val actionExtendLadder = Action(
        "extend ladder",
        "")
    {
        val objectExtendableLadder = it.mouseObject as ObjectExtendableLadder
        objectExtendableLadder.isExtended = true
        it.level.updateObjects()
        it.room.updateObjectTiles(it.tiles, it.map.mapDirection)
        it.map.updateMap()
    }

    val actionFoldLadder = Action(
        "fold ladder",
        "")
    {
        val objectExtendableLadder = it.mouseObject as ObjectExtendableLadder
        objectExtendableLadder.isExtended = false
        it.level.updateObjects()
        it.room.updateObjectTiles(it.tiles, it.map.mapDirection)
        it.map.updateMap()
    }

    override fun createMoves()
    {
        for (ladderLikePart in ladderLikeParts)
        {
            for (endPosition in endPositions)
            {
                ladderLikePart.centerMoves.add(MoveExtendableLadder(ladderLikePart.centerPosition, endPosition, this))
            }
        }
    }

    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        for (ladderLikePart in ladderLikeParts)
        {
            if (!(!isExtended && ladderLikePart.centerPosition != ladderOriginPosition && ladderLikePart.centerPosition != firstPosition))
            {
                positions.add(ladderLikePart.centerPosition)
            }
        }
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        for (ladderLikePart in ladderLikeParts)
        {
            if (!(!isExtended && ladderLikePart.centerPosition != ladderOriginPosition && ladderLikePart.centerPosition != firstPosition))
            {
                positions.add(ladderLikePart.centerPosition)
                positions.addAll(ladderLikePart.ladderPartEndPositions.values)
            }
        }
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : MutableMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val relativeLadderDirection = objectiveToRelativeDirection2d(ladderDirection, mapDirection)

        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = null
        spaceLayerTiles[SpaceLayer.SideUp] = null
        spaceLayerTiles[SpaceLayer.Behind] = when (spacePosition)
        {
            ladderOriginPosition -> null
            firstPosition        -> when (relativeLadderDirection)
            {
                Direction2d.Up    -> tiles.extendableLadderUpBehind
                Direction2d.Right -> tiles.extendableLadderRightBehind
                Direction2d.Down  -> tiles.extendableLadderDownBehind
                Direction2d.Left  -> tiles.extendableLadderLeftBehind
            }
            in centerPositions   -> if (isExtended) when (relativeLadderDirection)
            {
                Direction2d.Left -> tiles.extendableLadderLadderLeft
                Direction2d.Up   -> tiles.extendableLadderLadderUp
                else             -> null
            }
            else null
            else                 -> null
        }
        spaceLayerTiles[SpaceLayer.Before] = when (spacePosition)
        {
            ladderOriginPosition -> null
            firstPosition        -> when (relativeLadderDirection)
            {
                Direction2d.Up    -> tiles.extendableLadderUpBefore
                Direction2d.Right -> tiles.extendableLadderRightBefore
                Direction2d.Down  -> tiles.extendableLadderDownBefore
                Direction2d.Left  -> tiles.extendableLadderLeftBefore
            }
            in centerPositions   -> if (isExtended) when (relativeLadderDirection)
            {
                Direction2d.Down  -> tiles.extendableLadderLadderDown
                Direction2d.Right -> tiles.extendableLadderLadderRight
                else              -> null
            }
            else null
            else                 -> null
        }
        spaceLayerTiles[SpaceLayer.SideDown] = when (spacePosition)
        {
            ladderOriginPosition -> null
            firstPosition        -> when (relativeLadderDirection)
            {
                Direction2d.Up    -> tiles.extendableLadderUpDown
                Direction2d.Right -> tiles.extendableLadderRightDown
                Direction2d.Down  -> tiles.extendableLadderDownDown
                Direction2d.Left  -> tiles.extendableLadderLeftDown
            }
            else                 -> null
        }
        spaceLayerTiles[SpaceLayer.SideRight] = when (spacePosition)
        {
            ladderOriginPosition -> null
            firstPosition        -> when (relativeLadderDirection)
            {
                Direction2d.Up    -> tiles.extendableLadderUpRight
                Direction2d.Right -> tiles.extendableLadderRightRight
                Direction2d.Down  -> tiles.extendableLadderDownRight
                Direction2d.Left  -> tiles.extendableLadderLeftRight
            }
            else                 -> null
        }
        spaceLayerTiles[SpaceLayer.SideAbove] = when (spacePosition)
        {
            ladderOriginPosition -> null
            firstPosition        -> when (relativeLadderDirection)
            {
                Direction2d.Up    -> tiles.extendableLadderUpAbove
                Direction2d.Right -> tiles.extendableLadderRightAbove
                Direction2d.Down  -> tiles.extendableLadderDownAbove
                Direction2d.Left  -> tiles.extendableLadderLeftAbove
            }
            else                 -> null
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
        return when (spacePosition)
        {
            ladderOriginPosition, firstPosition -> true
            in centerPositions                  -> isExtended
            else                                -> false
        }
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
            if (!isExtended)
            {
                if (ladderLikePart.centerPosition == ladderOriginPosition && ladderLikePart.centerPosition == firstPosition)
                {
                    for (move in ladderLikePart.centerMoves)
                    {
                        if (move.endLadderLikeMove in originPart!!.ladderPartEndPositions.values ||
                            move.endLadderLikeMove in firstPart!!.ladderPartEndPositions.values)
                        {
                            moveList.add(move)
                        }
                    }
                }
            }
            else
            {
                for (move in ladderLikePart.centerMoves)
                {
                    moveList.add(move)
                }
            }
        }
    }

    override fun getActions(actionArray : Array<Action?>, spacePosition : Vector3)
    {
        if (spacePosition == firstPosition)
        {
            actionArray[0] = actionExtendLadder.apply {
                actionDescription = if (isExtended) "( ladder is extended )" else "( ladder is folded )"
                isActionPossible = !isExtended
            }

            actionArray[1] = actionFoldLadder.apply {
                actionDescription = if (isExtended) "( ladder is extended )" else "( ladder is folded )"
                isActionPossible = isExtended
            }
        }
    }
}
