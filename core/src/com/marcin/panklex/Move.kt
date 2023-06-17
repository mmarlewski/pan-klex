package com.marcin.panklex

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3

abstract class Move(val moveStart : Vector3, val moveEnd : Vector3)
{
    abstract fun getMoveTiles(
        tiles : Tiles, spaceLayerTiles : MutableMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)

    abstract fun getCost() : Int

    abstract fun getPositions(positionList : MutableList<Vector3>)
}
