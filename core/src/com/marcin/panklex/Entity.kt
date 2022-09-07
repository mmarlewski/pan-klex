package com.marcin.panklex

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3

abstract class Entity(val entityType : String)
{
    abstract fun getOccupiedPosition() : Vector3
    abstract fun getTiles(tiles : Tiles, spaceLayerTiles : HashMap<SpaceLayer, TiledMapTile?>)
}
