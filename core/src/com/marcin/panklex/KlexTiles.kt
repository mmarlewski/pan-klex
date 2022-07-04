package com.marcin.panklex

import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile

class KlexTiles(assetManager : AssetManager)
{
    val tileset = TextureRegion(assetManager.get<Texture>("iso.png")).split(32, 32)

    val rightSelect = StaticTiledMapTile(tileset[0][0])
    val leftSelect = StaticTiledMapTile(tileset[0][1])
    val upSelect = StaticTiledMapTile(tileset[1][0])
    val downSelect = StaticTiledMapTile(tileset[1][1])
    val aboveSelect = StaticTiledMapTile(tileset[2][0])
    val belowSelect = StaticTiledMapTile(tileset[2][1])

    val rightBorder = StaticTiledMapTile(tileset[0][2])
    val downBorder = StaticTiledMapTile(tileset[1][2])
    val aboveBorder = StaticTiledMapTile(tileset[2][2])

    val leftBorderAboveUpBelowDown = StaticTiledMapTile(tileset[0][3])
    val leftBorderAboveUpBelow = StaticTiledMapTile(tileset[0][4])
    val leftBorderAboveUpDown = StaticTiledMapTile(tileset[0][5])
    val leftBorderAboveUp = StaticTiledMapTile(tileset[0][6])
    val leftBorderAboveBelowDown = StaticTiledMapTile(tileset[0][7])
    val leftBorderAboveBelow = StaticTiledMapTile(tileset[0][8])
    val leftBorderAboveDown = StaticTiledMapTile(tileset[0][9])
    val leftBorderAbove = StaticTiledMapTile(tileset[0][10])
    val leftBorderUpBelowDown = StaticTiledMapTile(tileset[0][11])
    val leftBorderUpBelow = StaticTiledMapTile(tileset[0][12])
    val leftBorderUpDown = StaticTiledMapTile(tileset[0][13])
    val leftBorderUp = StaticTiledMapTile(tileset[0][14])
    val leftBorderBelowDown = StaticTiledMapTile(tileset[0][15])
    val leftBorderBelow = StaticTiledMapTile(tileset[0][16])
    val leftBorderDown = StaticTiledMapTile(tileset[0][17])
    val leftBorder = StaticTiledMapTile(tileset[0][18])

    val upBorderAboveRightBelowLeft = StaticTiledMapTile(tileset[1][3])
    val upBorderAboveRightBelow = StaticTiledMapTile(tileset[1][4])
    val upBorderAboveRightLeft = StaticTiledMapTile(tileset[1][5])
    val upBorderAboveRight = StaticTiledMapTile(tileset[1][6])
    val upBorderAboveBelowLeft = StaticTiledMapTile(tileset[1][7])
    val upBorderAboveBelow = StaticTiledMapTile(tileset[1][8])
    val upBorderAboveLeft = StaticTiledMapTile(tileset[1][9])
    val upBorderAbove = StaticTiledMapTile(tileset[1][10])
    val upBorderRightBelowLeft = StaticTiledMapTile(tileset[1][11])
    val upBorderRightBelow = StaticTiledMapTile(tileset[1][12])
    val upBorderRightLeft = StaticTiledMapTile(tileset[1][13])
    val upBorderRight = StaticTiledMapTile(tileset[1][14])
    val upBorderBelowLeft = StaticTiledMapTile(tileset[1][15])
    val upBorderBelow = StaticTiledMapTile(tileset[1][16])
    val upBorderLeft = StaticTiledMapTile(tileset[1][17])
    val upBorder = StaticTiledMapTile(tileset[1][18])

    val belowBorderUpRightDownLeft = StaticTiledMapTile(tileset[2][3])
    val belowBorderUpRightDown = StaticTiledMapTile(tileset[2][4])
    val belowBorderUpRightLeft = StaticTiledMapTile(tileset[2][5])
    val belowBorderUpRight = StaticTiledMapTile(tileset[2][6])
    val belowBorderUpDownLeft = StaticTiledMapTile(tileset[2][7])
    val belowBorderUpDown = StaticTiledMapTile(tileset[2][8])
    val belowBorderUpLeft = StaticTiledMapTile(tileset[2][9])
    val belowBorderUp = StaticTiledMapTile(tileset[2][10])
    val belowBorderRightDownLeft = StaticTiledMapTile(tileset[2][11])
    val belowBorderRightDown = StaticTiledMapTile(tileset[2][12])
    val belowBorderRightLeft = StaticTiledMapTile(tileset[2][13])
    val belowBorderRight = StaticTiledMapTile(tileset[2][14])
    val belowBorderDownLeft = StaticTiledMapTile(tileset[2][15])
    val belowBorderDown = StaticTiledMapTile(tileset[2][16])
    val belowBorderLeft = StaticTiledMapTile(tileset[2][17])
    val belowBorder = StaticTiledMapTile(tileset[2][18])
}
