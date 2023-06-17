package com.marcin.panklex.moves

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*
import com.marcin.panklex.objects.ObjectLadderLike

class MoveLadder(val moveLadderStart : Vector3, val moveLadderEnd : Vector3, val objectLadderLike : ObjectLadderLike) :
    MoveLadderLike(moveLadderStart, moveLadderEnd, objectLadderLike)
{
    override fun getMoveTiles(
        tiles : Tiles, spaceLayerTiles : MutableMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val relativeLadderDirection = objectiveToRelativeDirection2d(objectLadderLike.ladderLikeDirection, mapDirection)
        val objectiveEndDirection = getDist1Conn(moveLadderStart, moveLadderEnd)
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
}
