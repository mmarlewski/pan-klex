package com.marcin.panklex.moves

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class MoveGround(val startPosition : Vector3, val endPosition : Vector3) : Move(startPosition, endPosition)
{
    val moveDirection = getDist1Conn(startPosition, endPosition)

    override fun getMoveTiles(
        tiles : Tiles, spaceLayerTiles : MutableMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
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
