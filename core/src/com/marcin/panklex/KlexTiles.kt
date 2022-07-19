package com.marcin.panklex

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile

class KlexTiles(assetManager : AssetManager)
{
    val tileset = TextureRegion(assetManager.get<Texture>("iso.png")).split(32, 32)

    val rightSelect = tileset[0][0]
    val leftSelect = tileset[0][1]
    val upSelect = tileset[1][0]
    val downSelect = tileset[1][1]
    val aboveSelect = tileset[2][0]
    val belowSelect = tileset[2][1]

    val rightBorder = tileset[0][2]
    val downBorder = tileset[1][2]
    val aboveBorder = tileset[2][2]

    val leftBorderAbove = tileset[0][3]
    val leftBorderUp = tileset[0][4]
    val leftBorderBelow = tileset[0][5]
    val leftBorderDown = tileset[0][6]

    val upBorderAbove = tileset[1][3]
    val upBorderRight = tileset[1][4]
    val upBorderBelow = tileset[1][5]
    val upBorderLeft = tileset[1][6]

    val belowBorderUp = tileset[2][3]
    val belowBorderRight = tileset[2][4]
    val belowBorderDown = tileset[2][5]
    val belowBorderLeft = tileset[2][6]
}
