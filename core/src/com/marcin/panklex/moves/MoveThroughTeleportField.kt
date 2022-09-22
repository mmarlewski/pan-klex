package com.marcin.panklex.moves

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class MoveThroughTeleportField(val startPosition : Vector3, val endPosition : Vector3) : Move(startPosition, endPosition)
{
    val moveDirection = getDist1Conn(startPosition, endPosition)

    override fun getMoveTiles(
        tiles : Tiles, spaceLayerTiles : MutableMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
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
