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
        val holeLayer = TiledMapTileLayer(maxMapWidth, maxMapHeight, tileWidth, tileHeight).apply { name = MapLayer.Hole.name }
        val baseLayer = TiledMapTileLayer(maxMapWidth, maxMapHeight, tileWidth, tileHeight).apply { name = MapLayer.Base.name }
        val blockLayer = TiledMapTileLayer(maxMapWidth, maxMapHeight, tileWidth, tileHeight).apply { name = MapLayer.Block.name }
        val wallLayer = TiledMapTileLayer(maxMapWidth, maxMapHeight, tileWidth, tileHeight).apply { name = MapLayer.Wall.name }
        val entityLayer = TiledMapTileLayer(maxMapWidth, maxMapHeight, tileWidth, tileHeight).apply { name = MapLayer.Entity.name }
        val coverLayer = TiledMapTileLayer(maxMapWidth, maxMapHeight, tileWidth, tileHeight).apply { name = MapLayer.Cover.name }
        val actionLayer = TiledMapTileLayer(maxMapWidth, maxMapHeight, tileWidth, tileHeight).apply { name = MapLayer.Action.name }

        for (i in 0..maxMapWidth - 1)
        {
            for (j in 0..maxMapHeight - 1)
            {
                val holeCell = TiledMapTileLayer.Cell()
                val baseCell = TiledMapTileLayer.Cell()
                val blockCell = TiledMapTileLayer.Cell()
                val wallCell = TiledMapTileLayer.Cell()
                val entityCell = TiledMapTileLayer.Cell()
                val coverCell = TiledMapTileLayer.Cell()
                val actionCell = TiledMapTileLayer.Cell()

                holeLayer.setCell(i, j, holeCell)
                baseLayer.setCell(i, j, baseCell)
                blockLayer.setCell(i, j, blockCell)
                wallLayer.setCell(i, j, wallCell)
                entityLayer.setCell(i, j, entityCell)
                coverLayer.setCell(i, j, coverCell)
                actionLayer.setCell(i, j, actionCell)
            }
        }

        map.layers.add(holeLayer)
        map.layers.add(baseLayer)
        map.layers.add(blockLayer)
        map.layers.add(wallLayer)
        map.layers.add(entityLayer)
        map.layers.add(coverLayer)
        map.layers.add(actionLayer)
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

    fun clearLayer(layer: String)
    {
        for (i in 0..maxMapWidth - 1)
        {
            for (j in 0..maxMapHeight - 1)
            {
                (map.layers[layer] as TiledMapTileLayer).getCell(i, j).tile = null
            }
        }
    }
}
