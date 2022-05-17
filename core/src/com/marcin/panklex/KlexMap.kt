package com.marcin.panklex

import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer
import com.badlogic.gdx.utils.Disposable

class KlexMap(val maxMapWidth : Int, val maxMapHeight : Int, val tileWidth : Int, val tileHeight : Int) : Disposable
{
    val map = TiledMap()
    val renderer = OrthogonalTiledMapRenderer(map, 1f)

    var mapWidth = 0
    var mapHeight = 0

    val width get() = mapWidth * tileWidth
    val height get() = mapHeight * tileHeight

    init
    {
        val pitLayer =
            TiledMapTileLayer(maxMapWidth, maxMapHeight, tileWidth, tileHeight).apply { name = MapLayer.Pit.name }
        val holeLayer =
            TiledMapTileLayer(maxMapWidth, maxMapHeight, tileWidth, tileHeight).apply { name = MapLayer.Hole.name }
        val baseLayer =
            TiledMapTileLayer(maxMapWidth, maxMapHeight, tileWidth, tileHeight).apply { name = MapLayer.Base.name }
        val blockLayer =
            TiledMapTileLayer(maxMapWidth, maxMapHeight, tileWidth, tileHeight).apply { name = MapLayer.Block.name }
        val wallLayer =
            TiledMapTileLayer(maxMapWidth, maxMapHeight, tileWidth, tileHeight).apply { name = MapLayer.Wall.name }
        val entityLayer =
            TiledMapTileLayer(maxMapWidth, maxMapHeight, tileWidth, tileHeight).apply { name = MapLayer.Entity.name }
        val playerLayer =
            TiledMapTileLayer(maxMapWidth, maxMapHeight, tileWidth, tileHeight).apply { name = MapLayer.Player.name }
        val coverLayer =
            TiledMapTileLayer(maxMapWidth, maxMapHeight, tileWidth, tileHeight).apply { name = MapLayer.Cover.name }

        for (i in 0 until maxMapWidth)
        {
            for (j in 0 until maxMapHeight)
            {
                val pitCell = TiledMapTileLayer.Cell()
                val holeCell = TiledMapTileLayer.Cell()
                val baseCell = TiledMapTileLayer.Cell()
                val blockCell = TiledMapTileLayer.Cell()
                val wallCell = TiledMapTileLayer.Cell()
                val entityCell = TiledMapTileLayer.Cell()
                val playerCell = TiledMapTileLayer.Cell()
                val coverCell = TiledMapTileLayer.Cell()

                pitLayer.setCell(i, j, pitCell)
                holeLayer.setCell(i, j, holeCell)
                baseLayer.setCell(i, j, baseCell)
                blockLayer.setCell(i, j, blockCell)
                wallLayer.setCell(i, j, wallCell)
                entityLayer.setCell(i, j, entityCell)
                playerLayer.setCell(i, j, playerCell)
                coverLayer.setCell(i, j, coverCell)
            }
        }

        map.layers.add(pitLayer)
        map.layers.add(holeLayer)
        map.layers.add(baseLayer)
        map.layers.add(blockLayer)
        map.layers.add(wallLayer)
        map.layers.add(entityLayer)
        map.layers.add(playerLayer)
        map.layers.add(coverLayer)
    }

    fun setDimensions(width : Int, height : Int)
    {
        mapWidth = width
        mapHeight = height
    }

    fun setTile(layer : MapLayer, x : Int, y : Int, tile : TiledMapTile)
    {
        (map.layers[layer.name] as TiledMapTileLayer).getCell(x, y).tile = tile
    }

    fun clearLayer(layer : MapLayer)
    {
        for (i in 0 until maxMapWidth)
        {
            for (j in 0 until maxMapHeight)
            {
                (map.layers[layer.name] as TiledMapTileLayer).getCell(i, j).tile = null
            }
        }
    }

    override fun dispose()
    {
        map.dispose()
        renderer.dispose()
    }
}
