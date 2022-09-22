package com.marcin.panklex.moves

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*
import com.marcin.panklex.objects.ObjectLadderLike

class MoveRope(val moveRopeStart : Vector3, val moveRopeEnd : Vector3, val objectRopeLike : ObjectLadderLike) :
    MoveLadderLike(moveRopeStart, moveRopeEnd, objectRopeLike)
{
    override fun getMoveTiles(
        tiles : Tiles, spaceLayerTiles : MutableMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val relativeRopeDirection = objectiveToRelativeDirection2d(objectRopeLike.ladderLikeDirection, mapDirection)
        val objectiveEndDirection = getDist1Conn(moveRopeStart, moveRopeEnd)
        val relativeEndDirection = objectiveToRelativeDist1Conn(objectiveEndDirection, mapDirection)

        when (spacePosition)
        {
            endCenterPosition  -> when (relativeEndDirection)
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
            in centerPositions -> when (moveDirection)
            {
                DirectionZ.Above ->
                {
                    spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.ropeMoveAboveWhole
                    spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.ropeMoveAboveOutline
                }
                DirectionZ.Below ->
                {
                    spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.ropeMoveBelowWhole
                    spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.ropeMoveBelowOutline
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
}
