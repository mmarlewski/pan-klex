package com.marcin.panklex.moves

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class MoveJumpDist3(val startPosition : Vector3, val dist3Direction : Dist3Conn) :
    Move(startPosition, getPositionDist3Conn(startPosition, dist3Direction))
{
    val dist2Direction = dist3ConnToDist2Conn(dist3Direction)
    val dist1Direction = dist3ConnToDist1Conn(dist3Direction)

    val dist3Position = getPositionDist3Conn(startPosition, dist3Direction)
    val dist2Position = getPositionDist2Conn(startPosition, dist2Direction)
    val dist1Position = getPositionDist1Conn(startPosition, dist1Direction)

    override fun getMoveTiles(
        tiles : Tiles, spaceLayerTiles : MutableMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
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
                    Dist1Conn.Up        -> tiles.dist3JumpMoveUpWhole
                    Dist1Conn.UpRight   -> tiles.dist3JumpMoveUpRightWhole
                    Dist1Conn.Right     -> tiles.dist3JumpMoveRightWhole
                    Dist1Conn.DownRight -> tiles.dist3JumpMoveDownRightWhole
                    Dist1Conn.Down      -> tiles.dist3JumpMoveDownWhole
                    Dist1Conn.DownLeft  -> tiles.dist3JumpMoveDownLeftWhole
                    Dist1Conn.Left      -> tiles.dist3JumpMoveLeftWhole
                    Dist1Conn.UpLeft    -> tiles.dist3JumpMoveUpLeftWhole
                    Dist1Conn.Center    -> null
                }
                spaceLayerTiles[SpaceLayer.MoveOutline] = when (relativeDist1Direction)
                {
                    Dist1Conn.Up        -> tiles.dist3JumpMoveUpOutline
                    Dist1Conn.UpRight   -> tiles.dist3JumpMoveUpRightOutline
                    Dist1Conn.Right     -> tiles.dist3JumpMoveRightOutline
                    Dist1Conn.DownRight -> tiles.dist3JumpMoveDownRightOutline
                    Dist1Conn.Down      -> tiles.dist3JumpMoveDownOutline
                    Dist1Conn.DownLeft  -> tiles.dist3JumpMoveDownLeftOutline
                    Dist1Conn.Left      -> tiles.dist3JumpMoveLeftOutline
                    Dist1Conn.UpLeft    -> tiles.dist3JumpMoveUpLeftOutline
                    Dist1Conn.Center    -> null
                }
            }
            dist2Position                ->
            {
                spaceLayerTiles[SpaceLayer.MoveWhole] = when (relativeDist2Direction)
                {
                    Dist1Conn.Up        -> tiles.dist3JumpMoveUpWhole
                    Dist1Conn.UpRight   -> tiles.dist3JumpMoveUpRightWhole
                    Dist1Conn.Right     -> tiles.dist3JumpMoveRightWhole
                    Dist1Conn.DownRight -> tiles.dist3JumpMoveDownRightWhole
                    Dist1Conn.Down      -> tiles.dist3JumpMoveDownWhole
                    Dist1Conn.DownLeft  -> tiles.dist3JumpMoveDownLeftWhole
                    Dist1Conn.Left      -> tiles.dist3JumpMoveLeftWhole
                    Dist1Conn.UpLeft    -> tiles.dist3JumpMoveUpLeftWhole
                    Dist1Conn.Center    -> null
                }
                spaceLayerTiles[SpaceLayer.MoveOutline] = when (relativeDist2Direction)
                {
                    Dist1Conn.Up        -> tiles.dist3JumpMoveUpOutline
                    Dist1Conn.UpRight   -> tiles.dist3JumpMoveUpRightOutline
                    Dist1Conn.Right     -> tiles.dist3JumpMoveRightOutline
                    Dist1Conn.DownRight -> tiles.dist3JumpMoveDownRightOutline
                    Dist1Conn.Down      -> tiles.dist3JumpMoveDownOutline
                    Dist1Conn.DownLeft  -> tiles.dist3JumpMoveDownLeftOutline
                    Dist1Conn.Left      -> tiles.dist3JumpMoveLeftOutline
                    Dist1Conn.UpLeft    -> tiles.dist3JumpMoveUpLeftOutline
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
