package com.marcin.panklex.moves

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*
import com.marcin.panklex.objects.ObjectExtendableLadder
import com.marcin.panklex.objects.ObjectLadderLike

class MoveExtendableLadder(
    val moveLadderStart : Vector3, val moveLadderEnd : Vector3, val objectLadderLike : ObjectLadderLike) :
    MoveLadderLike(moveLadderStart, moveLadderEnd, objectLadderLike)
{
    val objectExtendableLadder = objectLadderLike as ObjectExtendableLadder
    val firstPosition = objectExtendableLadder.firstPosition

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
            firstPosition      -> when (moveDirection)
            {
                DirectionZ.Above -> when (relativeLadderDirection)
                {
                    Direction2d.Up    ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.extendableLadderMoveOriginUpAboveWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.extendableLadderMoveOriginUpAboveOutline
                    }
                    Direction2d.Right ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.extendableLadderMoveOriginRightAboveWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.extendableLadderMoveOriginRightAboveOutline
                    }
                    Direction2d.Down  ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.extendableLadderMoveOriginDownAboveWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.extendableLadderMoveOriginDownAboveOutline
                    }
                    Direction2d.Left  ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.extendableLadderMoveOriginLeftAboveWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.extendableLadderMoveOriginLeftAboveOutline
                    }
                }
                DirectionZ.Below -> when (relativeLadderDirection)
                {
                    Direction2d.Up    ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.extendableLadderMoveOriginUpBelowWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.extendableLadderMoveOriginUpBelowOutline
                    }
                    Direction2d.Right ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.extendableLadderMoveOriginRightBelowWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.extendableLadderMoveOriginRightBelowOutline
                    }
                    Direction2d.Down  ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.extendableLadderMoveOriginDownBelowWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.extendableLadderMoveOriginDownBelowOutline
                    }
                    Direction2d.Left  ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.extendableLadderMoveOriginLeftBelowWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.extendableLadderMoveOriginLeftBelowOutline
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
            in centerPositions -> if (objectExtendableLadder.isExtended) when (moveDirection)
            {
                DirectionZ.Above -> when (relativeLadderDirection)
                {
                    Direction2d.Up    ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.extendableLadderMoveLadderUpAboveWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.extendableLadderMoveLadderUpAboveOutline
                    }
                    Direction2d.Right ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.extendableLadderMoveLadderRightAboveWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.extendableLadderMoveLadderRightAboveOutline
                    }
                    Direction2d.Down  ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.extendableLadderMoveLadderDownAboveWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.extendableLadderMoveLadderDownAboveOutline
                    }
                    Direction2d.Left  ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.extendableLadderMoveLadderLeftAboveWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.extendableLadderMoveLadderLeftAboveOutline
                    }
                }
                DirectionZ.Below -> when (relativeLadderDirection)
                {
                    Direction2d.Up    ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.extendableLadderMoveLadderUpBelowWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.extendableLadderMoveLadderUpBelowOutline
                    }
                    Direction2d.Right ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.extendableLadderMoveLadderRightBelowWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.extendableLadderMoveLadderRightBelowOutline
                    }
                    Direction2d.Down  ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.extendableLadderMoveLadderDownBelowWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.extendableLadderMoveLadderDownBelowOutline
                    }
                    Direction2d.Left  ->
                    {
                        spaceLayerTiles[SpaceLayer.MoveWhole] = tiles.extendableLadderMoveLadderLeftBelowWhole
                        spaceLayerTiles[SpaceLayer.MoveOutline] = tiles.extendableLadderMoveLadderLeftBelowOutline
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
