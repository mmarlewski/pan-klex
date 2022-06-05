package com.marcin.panklex

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile

class KlexTiles(val assetManager : AssetManager)
{
    val tileset = TextureRegion(assetManager.get<Texture>("iso.png")).split(32, 32)
    val blockUnselected = StaticTiledMapTile(tileset[0][0])
    val blockSelected = StaticTiledMapTile(tileset[1][0])
    val emptyUnselected = StaticTiledMapTile(tileset[2][0])
    val emptySelected = StaticTiledMapTile(tileset[3][0])
    //val upUnselected = StaticTiledMapTile(tileset[0][1])
}
