package com.marcin.panklex

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3

abstract class Object(val objectType : String)
{
    abstract fun getOccupiedPositions(positions : MutableList<Vector3>)
    abstract fun getPresentPositions(positions : MutableList<Vector3>)

    abstract fun getTiles(
        tiles : Tiles, spaceLayerTiles : MutableMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)

    abstract fun getSideTransparency(spaceSideTransparency : MutableMap<Direction3d, Boolean>, spacePosition : Vector3)

    abstract fun canStoreEntity(spacePosition : Vector3) : Boolean

    abstract fun isGround(spacePosition : Vector3) : Boolean

    abstract fun getMoves(moveList : MutableList<Move>, spacePosition : Vector3, room : Room)

    abstract fun getActions(actionArray : Array<Action?>, spacePosition : Vector3)
}
