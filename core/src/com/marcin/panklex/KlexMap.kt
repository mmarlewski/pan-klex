package com.marcin.panklex

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer

class KlexMap(val maxMapWidth: Int, val maxMapHeight: Int, val tileWidth: Int, val tileHeight: Int)
{
    val map = TiledMap()
    val renderer = OrthogonalTiledMapRenderer(map, 1f)

    var mapWidth = 0
    var mapHeight = 0

    fun width() = mapWidth * tileWidth
    fun height() = mapHeight * tileHeight

    init
    {
        val holeLayer = TiledMapTileLayer(maxMapWidth, maxMapHeight, tileWidth, tileHeight).apply { name = "hole" }
        val baseLayer = TiledMapTileLayer(maxMapWidth, maxMapHeight, tileWidth, tileHeight).apply { name = "base" }
        val blockLayer = TiledMapTileLayer(maxMapWidth, maxMapHeight, tileWidth, tileHeight).apply { name = "block" }
        val wallLayer = TiledMapTileLayer(maxMapWidth, maxMapHeight, tileWidth, tileHeight).apply { name = "wall" }
        val selectLayer = TiledMapTileLayer(maxMapWidth, maxMapHeight, tileWidth, tileHeight).apply { name = "select" }
        val entityLayer = TiledMapTileLayer(maxMapWidth, maxMapHeight, tileWidth, tileHeight).apply { name = "entity" }
        val coverLayer = TiledMapTileLayer(maxMapWidth, maxMapHeight, tileWidth, tileHeight).apply { name = "cover" }

        for (i in 0..maxMapWidth - 1)
        {
            for (j in 0..maxMapHeight - 1)
            {
                val holeCell = TiledMapTileLayer.Cell()
                val baseCell = TiledMapTileLayer.Cell()
                val blockCell = TiledMapTileLayer.Cell()
                val wallCell = TiledMapTileLayer.Cell()
                val selectCell = TiledMapTileLayer.Cell()
                val entityCell = TiledMapTileLayer.Cell()
                val coverCell = TiledMapTileLayer.Cell()

                holeLayer.setCell(i, j, holeCell)
                baseLayer.setCell(i, j, baseCell)
                blockLayer.setCell(i, j, blockCell)
                wallLayer.setCell(i, j, wallCell)
                selectLayer.setCell(i, j, selectCell)
                entityLayer.setCell(i, j, entityCell)
                coverLayer.setCell(i, j, coverCell)
            }
        }

        map.layers.add(holeLayer)
        map.layers.add(baseLayer)
        map.layers.add(blockLayer)
        map.layers.add(wallLayer)
        map.layers.add(selectLayer)
        map.layers.add(entityLayer)
        map.layers.add(coverLayer)
    }

    fun setDimensions(width: Int, height: Int)
    {
        mapWidth = width
        mapHeight = height
    }

    fun setTile(layer: String, x: Int, y: Int, tile: TiledMapTile)
    {
        (map.layers[layer] as TiledMapTileLayer).getCell(x, y).tile = tile
    }
}
