package com.marcin.panklex

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.objects.LadderPart
import kotlin.math.abs

abstract class Move(val moveStart : Vector3, val moveEnd : Vector3)
{
    abstract fun getMoveTiles(
        tiles : Tiles, spaceLayerTiles : HashMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)

    abstract fun getCost() : Int

    abstract fun getPositions(positionList : MutableList<Vector3>)
}

class GroundMove(val startPosition : Vector3, val endPosition : Vector3) : Move(startPosition, endPosition)
{
    val moveDirection = getDist1Conn(startPosition, endPosition)

    override fun getMoveTiles(
        tiles : Tiles, spaceLayerTiles : HashMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        if (spacePosition == startPosition)
        {
            val relativeMoveDirection = objectiveToRelativeDist1Conn(moveDirection, mapDirection)

            spaceLayerTiles[SpaceLayer.MoveWhole] = when (relativeMoveDirection)
            {
                Dist1Conn.Up        -> tiles.groundMoveUpWhole
                Dist1Conn.UpRight   -> tiles.groundMoveUpRightWhole
                Dist1Conn.Right     -> tiles.groundMoveRightWhole
                Dist1Conn.DownRight -> tiles.groundMoveDownRightWhole
                Dist1Conn.Down      -> tiles.groundMoveDownWhole
                Dist1Conn.DownLeft  -> tiles.groundMoveDownLeftWhole
                Dist1Conn.Left      -> tiles.groundMoveLeftWhole
                Dist1Conn.UpLeft    -> tiles.groundMoveUpLeftWhole
                Dist1Conn.Center    -> null
            }
            spaceLayerTiles[SpaceLayer.MoveOutline] = when (relativeMoveDirection)
            {
                Dist1Conn.Up        -> tiles.groundMoveUpOutline
                Dist1Conn.UpRight   -> tiles.groundMoveUpRightOutline
                Dist1Conn.Right     -> tiles.groundMoveRightOutline
                Dist1Conn.DownRight -> tiles.groundMoveDownRightOutline
                Dist1Conn.Down      -> tiles.groundMoveDownOutline
                Dist1Conn.DownLeft  -> tiles.groundMoveDownLeftOutline
                Dist1Conn.Left      -> tiles.groundMoveLeftOutline
                Dist1Conn.UpLeft    -> tiles.groundMoveUpLeftOutline
                Dist1Conn.Center    -> null
            }
        }
    }

    override fun getCost() : Int
    {
        return 1
    }

    override fun getPositions(positionList : MutableList<Vector3>)
    {
        positionList.add(startPosition)
    }
}

class FallMove(val startPosition : Vector3, val endPosition : Vector3) : Move(startPosition, endPosition)
{
    override fun getMoveTiles(
        tiles : Tiles, spaceLayerTiles : HashMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        if (spacePosition == startPosition)
        {
            spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.fallMoveWhole
            spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.fallMoveOutline
        }
    }

    override fun getCost() : Int
    {
        return 2
    }

    override fun getPositions(positionList : MutableList<Vector3>)
    {
        positionList.add(startPosition)
    }
}

class Jump2Move(val startPosition : Vector3, val dist2Direction : Dist2Conn) :
    Move(startPosition, getPositionDist2Conn(startPosition, dist2Direction))
{
    val dist1Direction = dist2ConnToDist1Conn(dist2Direction)

    val dist2Position = getPositionDist2Conn(startPosition, dist2Direction)
    val dist1Position = getPositionDist1Conn(startPosition, dist1Direction)

    override fun getMoveTiles(
        tiles : Tiles, spaceLayerTiles : HashMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val relativeDist1Direction = objectiveToRelativeDist1Conn(dist1Direction, mapDirection)

        when (spacePosition)
        {
            startPosition, dist1Position ->
            {
                spaceLayerTiles[SpaceLayer.MoveWhole] = when (relativeDist1Direction)
                {
                    Dist1Conn.Up        -> tiles.jumpMoveUpWhole
                    Dist1Conn.UpRight   -> tiles.jumpMoveUpRightWhole
                    Dist1Conn.Right     -> tiles.jumpMoveRightWhole
                    Dist1Conn.DownRight -> tiles.jumpMoveDownRightWhole
                    Dist1Conn.Down      -> tiles.jumpMoveDownWhole
                    Dist1Conn.DownLeft  -> tiles.jumpMoveDownLeftWhole
                    Dist1Conn.Left      -> tiles.jumpMoveLeftWhole
                    Dist1Conn.UpLeft    -> tiles.jumpMoveUpLeftWhole
                    Dist1Conn.Center    -> null
                }
                spaceLayerTiles[SpaceLayer.MoveOutline] = when (relativeDist1Direction)
                {
                    Dist1Conn.Up        -> tiles.jumpMoveUpOutline
                    Dist1Conn.UpRight   -> tiles.jumpMoveUpRightOutline
                    Dist1Conn.Right     -> tiles.jumpMoveRightOutline
                    Dist1Conn.DownRight -> tiles.jumpMoveDownRightOutline
                    Dist1Conn.Down      -> tiles.jumpMoveDownOutline
                    Dist1Conn.DownLeft  -> tiles.jumpMoveDownLeftOutline
                    Dist1Conn.Left      -> tiles.jumpMoveLeftOutline
                    Dist1Conn.UpLeft    -> tiles.jumpMoveUpLeftOutline
                    Dist1Conn.Center    -> null
                }
            }
        }
    }

    override fun getCost() : Int
    {
        return 3
    }

    override fun getPositions(positionList : MutableList<Vector3>)
    {
        positionList.add(startPosition)
        positionList.add(dist1Position)
    }
}

class Jump3Move(val startPosition : Vector3, val dist3Direction : Dist3Conn) :
    Move(startPosition, getPositionDist3Conn(startPosition, dist3Direction))
{
    val dist2Direction = dist3ConnToDist2Conn(dist3Direction)
    val dist1Direction = dist3ConnToDist1Conn(dist3Direction)

    val dist3Position = getPositionDist3Conn(startPosition, dist3Direction)
    val dist2Position = getPositionDist2Conn(startPosition, dist2Direction)
    val dist1Position = getPositionDist1Conn(startPosition, dist1Direction)

    override fun getMoveTiles(
        tiles : Tiles, spaceLayerTiles : HashMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val relativeDist1Direction = objectiveToRelativeDist1Conn(dist1Direction, mapDirection)
        val relativeDist2Direction = objectiveToRelativeDist1Conn(dist2ConnToDist1Conn(dist2Direction), mapDirection)

        when (spacePosition)
        {
            startPosition, dist1Position ->
            {
                spaceLayerTiles[SpaceLayer.MoveWhole] = when (relativeDist1Direction)
                {
                    Dist1Conn.Up        -> tiles.jumpMoveUpWhole
                    Dist1Conn.UpRight   -> tiles.jumpMoveUpRightWhole
                    Dist1Conn.Right     -> tiles.jumpMoveRightWhole
                    Dist1Conn.DownRight -> tiles.jumpMoveDownRightWhole
                    Dist1Conn.Down      -> tiles.jumpMoveDownWhole
                    Dist1Conn.DownLeft  -> tiles.jumpMoveDownLeftWhole
                    Dist1Conn.Left      -> tiles.jumpMoveLeftWhole
                    Dist1Conn.UpLeft    -> tiles.jumpMoveUpLeftWhole
                    Dist1Conn.Center    -> null
                }
                spaceLayerTiles[SpaceLayer.MoveOutline] = when (relativeDist1Direction)
                {
                    Dist1Conn.Up        -> tiles.jumpMoveUpOutline
                    Dist1Conn.UpRight   -> tiles.jumpMoveUpRightOutline
                    Dist1Conn.Right     -> tiles.jumpMoveRightOutline
                    Dist1Conn.DownRight -> tiles.jumpMoveDownRightOutline
                    Dist1Conn.Down      -> tiles.jumpMoveDownOutline
                    Dist1Conn.DownLeft  -> tiles.jumpMoveDownLeftOutline
                    Dist1Conn.Left      -> tiles.jumpMoveLeftOutline
                    Dist1Conn.UpLeft    -> tiles.jumpMoveUpLeftOutline
                    Dist1Conn.Center    -> null
                }
            }
            dist2Position                ->
            {
                spaceLayerTiles[SpaceLayer.MoveWhole] = when (relativeDist2Direction)
                {
                    Dist1Conn.Up        -> tiles.jumpMoveUpWhole
                    Dist1Conn.UpRight   -> tiles.jumpMoveUpRightWhole
                    Dist1Conn.Right     -> tiles.jumpMoveRightWhole
                    Dist1Conn.DownRight -> tiles.jumpMoveDownRightWhole
                    Dist1Conn.Down      -> tiles.jumpMoveDownWhole
                    Dist1Conn.DownLeft  -> tiles.jumpMoveDownLeftWhole
                    Dist1Conn.Left      -> tiles.jumpMoveLeftWhole
                    Dist1Conn.UpLeft    -> tiles.jumpMoveUpLeftWhole
                    Dist1Conn.Center    -> null
                }
                spaceLayerTiles[SpaceLayer.MoveOutline] = when (relativeDist2Direction)
                {
                    Dist1Conn.Up        -> tiles.jumpMoveUpOutline
                    Dist1Conn.UpRight   -> tiles.jumpMoveUpRightOutline
                    Dist1Conn.Right     -> tiles.jumpMoveRightOutline
                    Dist1Conn.DownRight -> tiles.jumpMoveDownRightOutline
                    Dist1Conn.Down      -> tiles.jumpMoveDownOutline
                    Dist1Conn.DownLeft  -> tiles.jumpMoveDownLeftOutline
                    Dist1Conn.Left      -> tiles.jumpMoveLeftOutline
                    Dist1Conn.UpLeft    -> tiles.jumpMoveUpLeftOutline
                    Dist1Conn.Center    -> null
                }
            }
        }
    }

    override fun getCost() : Int
    {
        return 5
    }

    override fun getPositions(positionList : MutableList<Vector3>)
    {
        positionList.add(startPosition)
        positionList.add(dist1Position)
        positionList.add(dist2Position)
    }
}

class ThroughTeleporterFieldMove(val startPosition : Vector3, val endPosition : Vector3) : Move(startPosition, endPosition)
{
    val moveDirection = getDist1Conn(startPosition, endPosition)

    override fun getMoveTiles(
        tiles : Tiles, spaceLayerTiles : HashMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        if (spacePosition == startPosition)
        {
            val relativeMoveDirection = objectiveToRelativeDist1Conn(moveDirection, mapDirection)

            spaceLayerTiles[SpaceLayer.MoveWhole] = null
            spaceLayerTiles[SpaceLayer.MoveOutline] = when (relativeMoveDirection)
            {
                Dist1Conn.Up        -> tiles.teleporterFieldMoveUp
                Dist1Conn.UpRight   -> tiles.teleporterFieldMoveUpRight
                Dist1Conn.Right     -> tiles.teleporterFieldMoveRight
                Dist1Conn.DownRight -> tiles.teleporterFieldMoveDownRight
                Dist1Conn.Down      -> tiles.teleporterFieldMoveDown
                Dist1Conn.DownLeft  -> tiles.teleporterFieldMoveDownLeft
                Dist1Conn.Left      -> tiles.teleporterFieldMoveLeft
                Dist1Conn.UpLeft    -> tiles.teleporterFieldMoveUpLeft
                Dist1Conn.Center    -> null
            }
        }
    }

    override fun getCost() : Int
    {
        return 1
    }

    override fun getPositions(positionList : MutableList<Vector3>)
    {
        positionList.add(startPosition)
    }
}

class StairsMove(
    val centerPosition : Vector3, val stairsDirection : Direction2d, val startPosition : Vector3,
    val endPosition : Vector3) : Move(startPosition, endPosition)
{
    override fun getMoveTiles(
        tiles : Tiles, spaceLayerTiles : HashMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val directionZ = getDirectionZ(startPosition, endPosition)
        val relativeStairsDirection = objectiveToRelativeDirection2d(stairsDirection, mapDirection)

        when (spacePosition)
        {
            centerPosition -> when (directionZ)
            {
                DirectionZ.Below -> when (relativeStairsDirection)
                {
                    Direction2d.Up    ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.stairsMoveUpBelowWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.stairsMoveUpBelowOutline
                    }
                    Direction2d.Right ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.stairsMoveRightBelowWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.stairsMoveRightBelowOutline
                    }
                    Direction2d.Down  ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.stairsMoveDownBelowWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.stairsMoveDownBelowOutline
                    }
                    Direction2d.Left  ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.stairsMoveLeftBelowWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.stairsMoveLeftBelowOutline
                    }
                }
                DirectionZ.Above -> when (relativeStairsDirection)
                {
                    Direction2d.Up    ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.stairsMoveUpAboveWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.stairsMoveUpAboveOutline
                    }
                    Direction2d.Right ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.stairsMoveRightAboveWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.stairsMoveRightAboveOutline
                    }
                    Direction2d.Down  ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.stairsMoveDownAboveWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.stairsMoveDownAboveOutline
                    }
                    Direction2d.Left  ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.stairsMoveLeftAboveWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.stairsMoveLeftAboveOutline
                    }
                }
                null             -> Unit
            }
        }
    }

    override fun getCost() : Int
    {
        return 1
    }

    override fun getPositions(positionList : MutableList<Vector3>)
    {
        positionList.add(centerPosition)
    }
}

class LadderMove(
    val startLadderPart : LadderPart, val endLadderPart : LadderPart, val endDirection : Dist1Conn,
    val ladderDirection : Direction2d) :
    Move(startLadderPart.ladderPartPositions[Dist1Conn.Center]!!, endLadderPart.ladderPartPositions[endDirection]!!)
{
    val moveDirectionZ = getDirectionZ(startLadderPart.centerPosition, endLadderPart.centerPosition)
    val moveDistance = abs(startLadderPart.centerPosition.z - endLadderPart.centerPosition.z).toInt()
    val ladderPositions = mutableListOf<Vector3>()

    init
    {
        ladderPositions.add(startLadderPart.centerPosition)

        for (i in 1 until moveDistance)
        {
            when (moveDirectionZ)
            {
                DirectionZ.Above -> ladderPositions.add(
                    Vector3(
                        startLadderPart.centerPosition.x, startLadderPart.centerPosition.y,
                        startLadderPart.centerPosition.z + i))
                DirectionZ.Below -> ladderPositions.add(
                    Vector3(
                        startLadderPart.centerPosition.x, startLadderPart.centerPosition.y,
                        startLadderPart.centerPosition.z - i))
                null             -> Unit
            }
        }

        ladderPositions.add(endLadderPart.centerPosition)
    }

    override fun getMoveTiles(
        tiles : Tiles, spaceLayerTiles : HashMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val relativeLadderDirection = objectiveToRelativeDirection2d(ladderDirection, mapDirection)
        val relativeEndDirection = objectiveToRelativeDist1Conn(endDirection, mapDirection)

        when (spacePosition)
        {
            endLadderPart.centerPosition -> when (relativeEndDirection)
            {
                Dist1Conn.Up        ->
                {
                    spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.groundMoveUpWhole
                    spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.groundMoveUpOutline
                }
                Dist1Conn.UpRight   ->
                {
                    spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.groundMoveUpRightWhole
                    spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.groundMoveUpRightOutline
                }
                Dist1Conn.Right     ->
                {
                    spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.groundMoveRightWhole
                    spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.groundMoveRightOutline
                }
                Dist1Conn.DownRight ->
                {
                    spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.groundMoveDownRightWhole
                    spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.groundMoveDownRightOutline
                }
                Dist1Conn.Down      ->
                {
                    spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.groundMoveDownWhole
                    spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.groundMoveDownOutline
                }
                Dist1Conn.DownLeft  ->
                {
                    spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.groundMoveDownLeftWhole
                    spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.groundMoveDownLeftOutline
                }
                Dist1Conn.Left      ->
                {
                    spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.groundMoveLeftWhole
                    spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.groundMoveLeftOutline
                }
                Dist1Conn.UpLeft    ->
                {
                    spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.groundMoveUpLeftWhole
                    spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.groundMoveUpLeftOutline
                }
                Dist1Conn.Center    -> Unit
            }
            in ladderPositions           -> when (moveDirectionZ)
            {
                DirectionZ.Above -> when (relativeLadderDirection)
                {
                    Direction2d.Up    ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.ladderMoveUpAboveWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.ladderMoveUpAboveOutline
                    }
                    Direction2d.Right ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.ladderMoveRightAboveWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.ladderMoveRightAboveOutline
                    }
                    Direction2d.Down  ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.ladderMoveDownAboveWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.ladderMoveDownAboveOutline
                    }
                    Direction2d.Left  ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.ladderMoveLeftAboveWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.ladderMoveLeftAboveOutline
                    }
                }
                DirectionZ.Below -> when (relativeLadderDirection)
                {
                    Direction2d.Up    ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.ladderMoveUpBelowWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.ladderMoveUpBelowOutline
                    }
                    Direction2d.Right ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.ladderMoveRightBelowWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.ladderMoveRightBelowOutline
                    }
                    Direction2d.Down  ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.ladderMoveDownBelowWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.ladderMoveDownBelowOutline
                    }
                    Direction2d.Left  ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.ladderMoveLeftBelowWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.ladderMoveLeftBelowOutline
                    }
                }
                null             -> when (relativeEndDirection)
                {
                    Dist1Conn.Up        ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.groundMoveUpWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.groundMoveUpOutline
                    }
                    Dist1Conn.UpRight   ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.groundMoveUpRightWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.groundMoveUpRightOutline
                    }
                    Dist1Conn.Right     ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.groundMoveRightWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.groundMoveRightOutline
                    }
                    Dist1Conn.DownRight ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.groundMoveDownRightWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.groundMoveDownRightOutline
                    }
                    Dist1Conn.Down      ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.groundMoveDownWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.groundMoveDownOutline
                    }
                    Dist1Conn.DownLeft  ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.groundMoveDownLeftWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.groundMoveDownLeftOutline
                    }
                    Dist1Conn.Left      ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.groundMoveLeftWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.groundMoveLeftOutline
                    }
                    Dist1Conn.UpLeft    ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.groundMoveUpLeftWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.groundMoveUpLeftOutline
                    }
                    Dist1Conn.Center    -> Unit
                }
            }
        }
    }

    override fun getCost() : Int
    {
        var cost = 1 * moveDistance
        if (endDirection != Dist1Conn.Center) cost += 1
        return cost
    }

    override fun getPositions(positionList : MutableList<Vector3>)
    {
        positionList.addAll(ladderPositions)
    }
}
