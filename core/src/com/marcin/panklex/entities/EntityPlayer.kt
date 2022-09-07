package com.marcin.panklex.entities

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.Entity
import com.marcin.panklex.SpaceLayer
import com.marcin.panklex.Tiles

class EntityPlayer(var playerPosition : Vector3) : Entity("player")
{
    override fun getOccupiedPosition() : Vector3
    {
        return playerPosition
    }

    override fun getTiles(tiles : Tiles, spaceLayerTiles : HashMap<SpaceLayer, TiledMapTile?>)
    {
        spaceLayerTiles[SpaceLayer.EntityWhole] = tiles.playerWhole
        spaceLayerTiles[SpaceLayer.EntityOutline] = tiles.playerOutline
    }
}
