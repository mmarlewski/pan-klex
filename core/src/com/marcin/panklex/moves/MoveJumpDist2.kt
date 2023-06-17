package com.marcin.panklex.moves

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class MoveJumpDist2(val startPosition : Vector3, val dist2Direction : Dist2Conn) :
    Move(startPosition, getPositionDist2Conn(startPosition, dist2Direction))
{
    val dist1Direction = dist2ConnToDist1Conn(dist2Direction)

    val dist2Position = getPositionDist2Conn(startPosition, dist2Direction)
    val dist1Position = getPositionDist1Conn(startPosition, dist1Direction)

    override fun getMoveTiles(
        tiles : Tiles, spaceLayerTiles : MutableMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val relativeDist1Direction = objectiveToRelativeDist1Conn(dist1Direction, mapDirection)

        when (spacePosition)
        {
            startPosition, dist1Position ->
            {
                spaceLayerTiles[SpaceLayer.MoveWhole] = when (relativeDist1Direction)
                {
                    Dist1Conn.Up        -> tiles.dist2JumpMoveUpWhole
                    Dist1Conn.UpRight   -> tiles.dist2JumpMoveUpRightWhole
                    Dist1Conn.Right     -> tiles.dist2JumpMoveRightWhole
                    Dist1Conn.DownRight -> tiles.dist2JumpMoveDownRightWhole
                    Dist1Conn.Down      -> tiles.dist2JumpMoveDownWhole
                    Dist1Conn.DownLeft  -> tiles.dist2JumpMoveDownLeftWhole
                    Dist1Conn.Left      -> tiles.dist2JumpMoveLeftWhole
                    Dist1Conn.UpLeft    -> tiles.dist2JumpMoveUpLeftWhole
                    Dist1Conn.Center    -> null
                }
                spaceLayerTiles[SpaceLayer.MoveOutline] = when (relativeDist1Direction)
                {
                    Dist1Conn.Up        -> tiles.dist2JumpMoveUpOutline
                    Dist1Conn.UpRight   -> tiles.dist2JumpMoveUpRightOutline
                    Dist1Conn.Right     -> tiles.dist2JumpMoveRightOutline
                    Dist1Conn.DownRight -> tiles.dist2JumpMoveDownRightOutline
                    Dist1Conn.Down      -> tiles.dist2JumpMoveDownOutline
                    Dist1Conn.DownLeft  -> tiles.dist2JumpMoveDownLeftOutline
                    Dist1Conn.Left      -> tiles.dist2JumpMoveLeftOutline
                    Dist1Conn.UpLeft    -> tiles.dist2JumpMoveUpLeftOutline
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
