package com.marcin.panklex.moves

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.Direction2d
import com.marcin.panklex.Move
import com.marcin.panklex.SpaceLayer
import com.marcin.panklex.Tiles

class MoveFall(val startPosition : Vector3, val endPosition : Vector3) : Move(startPosition, endPosition)
{
    override fun getMoveTiles(
        tiles : Tiles, spaceLayerTiles : MutableMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
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
