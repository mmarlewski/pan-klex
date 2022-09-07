package com.marcin.panklex

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3

class Space(val position : Vector3)
{
    var entityOccupying : Entity? = null
    var objectOccupying : Object? = null
    var objectsPresent = mutableListOf<Object>()

    val layerTiles = HashMap<SpaceLayer, TiledMapTile?>()
    val sideTransparency = HashMap<Direction3d, Boolean>()
    val sideVisibility = HashMap<Direction3d, Boolean>()

    var isOnBorder = false
    var isWithinBorder = false

    init
    {
        for (layer in SpaceLayer.values())
        {
            layerTiles[layer] = null
        }

        for (side in Direction3d.values())
        {
            sideTransparency[side] = true
        }

        for (side in Direction3d.values())
        {
            sideVisibility[side] = false
        }
    }
}
