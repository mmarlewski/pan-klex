package com.marcin.panklex

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.Vector3

class KlexMap(val room : KlexRoom, val tiles : KlexTiles)
{
    val tiledMap = TiledMap()
    val renderer = KlexIsometricTiledMapRenderer(tiledMap, 1f)

    var width = 0
    val maxWidth = 100
    var height = 0
    val maxHeight = 100
    val maxFloors = 10
    var direction = MapDirection.Up
    var selectMapPosition = Vector3()
    var selectRoomPosition = Vector3()

    fun createMap()
    {
        val layers = mutableListOf<TiledMapTileLayer>()

        for (k in 0 until maxFloors)
        {
            layers.add(TiledMapTileLayer(maxWidth, maxHeight, 32, 16).apply { name = k.toString() })
        }

        for (k in 0 until maxFloors)
        {
            for (j in 0 until maxHeight)
            {
                for (i in 0 until maxWidth)
                {
                    val cell = TiledMapTileLayer.Cell()
                    layers[k].setCell(i, j, cell)
                }
            }
        }

        for (l in layers)
        {
            tiledMap.layers.add(l)
        }
    }

    fun updateMap()
    {
        width = when (direction)
        {
            MapDirection.Up, MapDirection.Down    -> room.width
            MapDirection.Right, MapDirection.Left -> room.height
        }

        height = when (direction)
        {
            MapDirection.Up, MapDirection.Down    -> room.height
            MapDirection.Right, MapDirection.Left -> room.width
        }

        for (k in 0 until room.floors)
        {
            for (j in 0 until room.height)
            {
                for (i in 0 until room.width)
                {
                    val cell = when (direction)
                    {
                        MapDirection.Up    -> getCell(i, j, k)
                        MapDirection.Right -> getCell(j, room.width - 1 - i, k)
                        MapDirection.Down  -> getCell(room.width - 1 - i, room.height - 1 - j, k)
                        MapDirection.Left  -> getCell(room.height - 1 - j, i, k)
                    }

                    val block = room.getBlock(i, j, k)

                    cell.tile = if (block != null)
                    {
                        if (block.isNotEmpty())
                        {
                            if (block.isBorder)
                            {
                                when (direction)
                                {
                                    MapDirection.Up    -> when
                                    {
                                        block.isBorderAbove && block.isBorderDown && block.isBorderRight   -> tiles.borderAboveLeftRight
                                        !block.isBorderAbove && block.isBorderDown && block.isBorderRight  -> tiles.borderLeftRight
                                        block.isBorderAbove && !block.isBorderDown && block.isBorderRight  -> tiles.borderAboveRight
                                        block.isBorderAbove && block.isBorderDown && !block.isBorderRight  -> tiles.borderAboveLeft
                                        !block.isBorderAbove && !block.isBorderDown && block.isBorderRight -> tiles.borderRight
                                        block.isBorderAbove && !block.isBorderDown && !block.isBorderRight -> tiles.borderAbove
                                        !block.isBorderAbove && block.isBorderDown && !block.isBorderRight -> tiles.borderLeft
                                        else                                                               -> null
                                    }
                                    MapDirection.Right -> when
                                    {
                                        block.isBorderAbove && block.isBorderRight && block.isBorderUp   -> tiles.borderAboveLeftRight
                                        !block.isBorderAbove && block.isBorderRight && block.isBorderUp  -> tiles.borderLeftRight
                                        block.isBorderAbove && !block.isBorderRight && block.isBorderUp  -> tiles.borderAboveRight
                                        block.isBorderAbove && block.isBorderRight && !block.isBorderUp  -> tiles.borderAboveLeft
                                        !block.isBorderAbove && !block.isBorderRight && block.isBorderUp -> tiles.borderRight
                                        block.isBorderAbove && !block.isBorderRight && !block.isBorderUp -> tiles.borderAbove
                                        !block.isBorderAbove && block.isBorderRight && !block.isBorderUp -> tiles.borderLeft
                                        else                                                             -> null
                                    }
                                    MapDirection.Down  -> when
                                    {
                                        block.isBorderAbove && block.isBorderUp && block.isBorderLeft   -> tiles.borderAboveLeftRight
                                        !block.isBorderAbove && block.isBorderUp && block.isBorderLeft  -> tiles.borderLeftRight
                                        block.isBorderAbove && !block.isBorderUp && block.isBorderLeft  -> tiles.borderAboveRight
                                        block.isBorderAbove && block.isBorderUp && !block.isBorderLeft  -> tiles.borderAboveLeft
                                        !block.isBorderAbove && !block.isBorderUp && block.isBorderLeft -> tiles.borderRight
                                        block.isBorderAbove && !block.isBorderUp && !block.isBorderLeft -> tiles.borderAbove
                                        !block.isBorderAbove && block.isBorderUp && !block.isBorderLeft -> tiles.borderLeft
                                        else                                                            -> null
                                    }
                                    MapDirection.Left  -> when
                                    {
                                        block.isBorderAbove && block.isBorderLeft && block.isBorderDown   -> tiles.borderAboveLeftRight
                                        !block.isBorderAbove && block.isBorderLeft && block.isBorderDown  -> tiles.borderLeftRight
                                        block.isBorderAbove && !block.isBorderLeft && block.isBorderDown  -> tiles.borderAboveRight
                                        block.isBorderAbove && block.isBorderLeft && !block.isBorderDown  -> tiles.borderAboveLeft
                                        !block.isBorderAbove && !block.isBorderLeft && block.isBorderDown -> tiles.borderRight
                                        block.isBorderAbove && !block.isBorderLeft && !block.isBorderDown -> tiles.borderAbove
                                        !block.isBorderAbove && block.isBorderLeft && !block.isBorderDown -> tiles.borderLeft
                                        else                                                              -> null
                                    }
                                }
                            }
                            else tiles.blockUnselected
                        }
                        else null
                    }
                    else null
                }
            }
        }
    }

    fun getCell(xMap : Int, yMap : Int, zMap : Int) : TiledMapTileLayer.Cell
    {
        return (tiledMap.layers[zMap.toString()] as TiledMapTileLayer).getCell(xMap, yMap)
    }

    fun getCell(mapPosition : Vector3) : TiledMapTileLayer.Cell
    {
        return getCell(mapPosition.x.toInt(), mapPosition.y.toInt(), mapPosition.z.toInt())
    }

    fun getRoomPosition(xMap : Int, yMap : Int, zMap : Int, roomPosition : Vector3)
    {
        when (direction)
        {
            MapDirection.Up    -> roomPosition.set(xMap.toFloat(), yMap.toFloat(), zMap.toFloat())
            MapDirection.Right -> roomPosition.set((height - yMap - 1).toFloat(), xMap.toFloat(), zMap.toFloat())
            MapDirection.Down  -> roomPosition.set(
                (width - xMap - 1).toFloat(),
                (height - yMap - 1).toFloat(),
                zMap.toFloat()
            )
            MapDirection.Left  -> roomPosition.set(yMap.toFloat(), (width - xMap - 1).toFloat(), zMap.toFloat())
        }
    }

    fun getRoomPosition(mapPosition : Vector3, roomPosition : Vector3)
    {
        getRoomPosition(mapPosition.x.toInt(), mapPosition.y.toInt(), mapPosition.z.toInt(), roomPosition)
    }

    fun changeMapDirection(newDirection : MapDirection)
    {
        direction = newDirection

        clearMap()
        updateMap()
    }

    fun changeMapDirectionRight()
    {
        when (direction)
        {
            MapDirection.Up    -> changeMapDirection(MapDirection.Right)
            MapDirection.Right -> changeMapDirection(MapDirection.Down)
            MapDirection.Down  -> changeMapDirection(MapDirection.Left)
            MapDirection.Left  -> changeMapDirection(MapDirection.Up)
        }
    }

    fun changeMapDirectionLeft()
    {
        when (direction)
        {
            MapDirection.Up    -> changeMapDirection(MapDirection.Left)
            MapDirection.Left  -> changeMapDirection(MapDirection.Down)
            MapDirection.Down  -> changeMapDirection(MapDirection.Right)
            MapDirection.Right -> changeMapDirection(MapDirection.Up)
        }
    }

    fun changeSelection(selectPosition : Vector3)
    {
        selectMapPosition.set(selectPosition)

        val cell = getCell(selectMapPosition)

        getRoomPosition(selectMapPosition, selectRoomPosition)

        //Gdx.app.log("map", "x ${selectMapPosition.x} y ${selectMapPosition.y} z ${selectMapPosition.z}")

        val block = room.getBlock(selectRoomPosition)

        cell.tile = if (block != null)
        {
            if (block.isNotEmpty()) tiles.blockSelected
            else null
        }
        else null
    }

    fun clearMap()
    {
        for (k in 0 until maxFloors)
        {
            for (j in 0 until maxHeight)
            {
                for (i in 0 until maxWidth)
                {
                    getCell(i, j, k).tile = null
                }
            }
        }
    }
}
