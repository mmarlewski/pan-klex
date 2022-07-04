package com.marcin.panklex

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.Vector3

class KlexMap(val room : KlexRoom, val tiles : KlexTiles)
{
    val maps = mutableListOf<TiledMap>()
    val renderers = mutableListOf<KlexIsometricTiledMapRenderer>()

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
        for (f in 0 until maxFloors)
        {
            maps.add(TiledMap())
            renderers.add(KlexIsometricTiledMapRenderer(maps[f], 1f, f))

            val layers = mutableListOf<TiledMapTileLayer>()
            layers.add(TiledMapTileLayer(maxWidth, maxHeight, 32, 16).apply { name = BlockSide.Below.name })
            layers.add(TiledMapTileLayer(maxWidth, maxHeight, 32, 16).apply { name = BlockSide.Left.name })
            layers.add(TiledMapTileLayer(maxWidth, maxHeight, 32, 16).apply { name = BlockSide.Up.name })
            layers.add(TiledMapTileLayer(maxWidth, maxHeight, 32, 16).apply { name = BlockSide.Down.name })
            layers.add(TiledMapTileLayer(maxWidth, maxHeight, 32, 16).apply { name = BlockSide.Right.name })
            layers.add(TiledMapTileLayer(maxWidth, maxHeight, 32, 16).apply { name = BlockSide.Above.name })

            for (l in 0 until layers.size)
            {
                for (j in 0 until maxHeight)
                {
                    for (i in 0 until maxWidth)
                    {
                        val cell = TiledMapTileLayer.Cell()
                        layers[l].setCell(i, j, cell)
                    }
                }
            }

            for (l in 0 until layers.size)
            {
                maps[f].layers.add(layers[l])
            }
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

        for (f in 0 until maxFloors)
        {
            for (l in 0 until maps[f].layers.size())
            {
                for (j in 0 until room.height)
                {
                    for (i in 0 until room.width)
                    {
                        val layer = (maps[f].layers[l] as TiledMapTileLayer)

                        val cell = when (direction)
                        {
                            MapDirection.Up    -> layer.getCell(i, j)
                            MapDirection.Right -> layer.getCell(j, room.width - 1 - i)
                            MapDirection.Down  -> layer.getCell(room.width - 1 - i, room.height - 1 - j)
                            MapDirection.Left  -> layer.getCell(room.height - 1 - j, i)
                        }

                        val block = room.getBlock(i, j, f)

                        if (block != null)
                        {
                            val levelSide = enumValueOf<BlockSide>(maps[f].layers[l].name)

                            val isBorder = when (levelSide)
                            {
                                BlockSide.Right -> block.isRightBorder
                                BlockSide.Left  -> block.isLeftBorder
                                BlockSide.Up    -> block.isUpBorder
                                BlockSide.Down  -> block.isDownBorder
                                BlockSide.Above -> block.isAboveBorder
                                BlockSide.Below -> block.isBelowBorder
                            }

                            /////

                            val isLeftBorder = when (direction)
                            {
                                MapDirection.Up    -> block.isLeftBorder
                                MapDirection.Right -> block.isDownBorder
                                MapDirection.Down  -> block.isRightBorder
                                MapDirection.Left  -> block.isUpBorder
                            }
                            val isUpBorder = when (direction)
                            {
                                MapDirection.Up    -> block.isUpBorder
                                MapDirection.Right -> block.isLeftBorder
                                MapDirection.Down  -> block.isDownBorder
                                MapDirection.Left  -> block.isRightBorder
                            }
                            val isBelowBorder = when (direction)
                            {
                                MapDirection.Up    -> block.isBelowBorder
                                MapDirection.Right -> block.isBelowBorder
                                MapDirection.Down  -> block.isBelowBorder
                                MapDirection.Left  -> block.isBelowBorder
                            }

                            /////

                            val isLeftBorderAbove = when (direction)
                            {
                                MapDirection.Up    -> block.isLeftBorderAbove
                                MapDirection.Right -> block.isDownBorderAbove
                                MapDirection.Down  -> block.isRightBorderAbove
                                MapDirection.Left  -> block.isUpBorderAbove
                            }
                            val isLeftBorderUp = when (direction)
                            {
                                MapDirection.Up    -> block.isLeftBorderUp
                                MapDirection.Right -> block.isDownBorderLeft
                                MapDirection.Down  -> block.isRightBorderDown
                                MapDirection.Left  -> block.isUpBorderRight
                            }
                            val isLeftBorderBelow = when (direction)
                            {
                                MapDirection.Up    -> block.isLeftBorderBelow
                                MapDirection.Right -> block.isDownBorderBelow
                                MapDirection.Down  -> block.isRightBorderBelow
                                MapDirection.Left  -> block.isUpBorderBelow
                            }
                            val isLeftBorderDown = when (direction)
                            {
                                MapDirection.Up    -> block.isLeftBorderDown
                                MapDirection.Right -> block.isDownBorderRight
                                MapDirection.Down  -> block.isRightBorderUp
                                MapDirection.Left  -> block.isUpBorderLeft
                            }

                            /////

                            val isUpBorderAbove = when (direction)
                            {
                                MapDirection.Up    -> block.isUpBorderAbove
                                MapDirection.Right -> block.isLeftBorderAbove
                                MapDirection.Down  -> block.isDownBorderAbove
                                MapDirection.Left  -> block.isRightBorderAbove
                            }
                            val isUpBorderRight = when (direction)
                            {
                                MapDirection.Up    -> block.isUpBorderRight
                                MapDirection.Right -> block.isLeftBorderUp
                                MapDirection.Down  -> block.isDownBorderLeft
                                MapDirection.Left  -> block.isRightBorderDown
                            }
                            val isUpBorderBelow = when (direction)
                            {
                                MapDirection.Up    -> block.isUpBorderBelow
                                MapDirection.Right -> block.isLeftBorderBelow
                                MapDirection.Down  -> block.isDownBorderBelow
                                MapDirection.Left  -> block.isRightBorderBelow
                            }
                            val isUpBorderLeft = when (direction)
                            {
                                MapDirection.Up    -> block.isUpBorderLeft
                                MapDirection.Right -> block.isLeftBorderDown
                                MapDirection.Down  -> block.isDownBorderRight
                                MapDirection.Left  -> block.isRightBorderUp
                            }

                            /////

                            val isBelowBorderUp = when (direction)
                            {
                                MapDirection.Up    -> block.isBelowBorderUp
                                MapDirection.Right -> block.isBelowBorderLeft
                                MapDirection.Down  -> block.isBelowBorderDown
                                MapDirection.Left  -> block.isBelowBorderRight
                            }
                            val isBelowBorderRight = when (direction)
                            {
                                MapDirection.Up    -> block.isBelowBorderRight
                                MapDirection.Right -> block.isBelowBorderUp
                                MapDirection.Down  -> block.isBelowBorderLeft
                                MapDirection.Left  -> block.isBelowBorderDown
                            }
                            val isBelowBorderDown = when (direction)
                            {
                                MapDirection.Up    -> block.isBelowBorderDown
                                MapDirection.Right -> block.isBelowBorderRight
                                MapDirection.Down  -> block.isBelowBorderUp
                                MapDirection.Left  -> block.isBelowBorderLeft
                            }
                            val isBelowBorderLeft = when (direction)
                            {
                                MapDirection.Up    -> block.isBelowBorderLeft
                                MapDirection.Right -> block.isBelowBorderDown
                                MapDirection.Down  -> block.isBelowBorderRight
                                MapDirection.Left  -> block.isBelowBorderUp
                            }

                            /////

                            val mapSide = when (levelSide)
                            {
                                BlockSide.Right -> when (direction)
                                {
                                    MapDirection.Up    -> BlockSide.Right
                                    MapDirection.Right -> BlockSide.Down
                                    MapDirection.Down  -> BlockSide.Left
                                    MapDirection.Left  -> BlockSide.Up
                                }
                                BlockSide.Left  -> when (direction)
                                {
                                    MapDirection.Up    -> BlockSide.Left
                                    MapDirection.Right -> BlockSide.Up
                                    MapDirection.Down  -> BlockSide.Right
                                    MapDirection.Left  -> BlockSide.Down
                                }
                                BlockSide.Up    -> when (direction)
                                {
                                    MapDirection.Up    -> BlockSide.Up
                                    MapDirection.Right -> BlockSide.Right
                                    MapDirection.Down  -> BlockSide.Down
                                    MapDirection.Left  -> BlockSide.Left
                                }
                                BlockSide.Down  -> when (direction)
                                {
                                    MapDirection.Up    -> BlockSide.Down
                                    MapDirection.Right -> BlockSide.Left
                                    MapDirection.Down  -> BlockSide.Up
                                    MapDirection.Left  -> BlockSide.Right
                                }
                                BlockSide.Above -> BlockSide.Above
                                BlockSide.Below -> BlockSide.Below
                            }

                            /////

                            cell.tile = when (mapSide)
                            {
                                BlockSide.Right -> if (!isBorder) null else tiles.rightBorder
                                BlockSide.Down  -> if (!isBorder) null else tiles.downBorder
                                BlockSide.Above -> if (!isBorder) null else tiles.aboveBorder
                                BlockSide.Left  -> if (!isBorder || !isLeftBorder) null
                                else when
                                {
                                    isLeftBorderAbove && isLeftBorderUp && isLeftBorderBelow && isLeftBorderDown     -> tiles.leftBorderAboveUpBelowDown
                                    isLeftBorderAbove && isLeftBorderUp && isLeftBorderBelow && isLeftBorderDown     -> tiles.leftBorderAboveUpBelow
                                    isLeftBorderAbove && isLeftBorderUp && !isLeftBorderBelow && isLeftBorderDown    -> tiles.leftBorderAboveUpDown
                                    isLeftBorderAbove && isLeftBorderUp && !isLeftBorderBelow && !isLeftBorderDown   -> tiles.leftBorderAboveUp
                                    isLeftBorderAbove && !isLeftBorderUp && isLeftBorderBelow && isLeftBorderDown    -> tiles.leftBorderAboveBelowDown
                                    isLeftBorderAbove && !isLeftBorderUp && isLeftBorderBelow && !isLeftBorderDown   -> tiles.leftBorderAboveBelow
                                    isLeftBorderAbove && !isLeftBorderUp && !isLeftBorderBelow && isLeftBorderDown   -> tiles.leftBorderAboveDown
                                    isLeftBorderAbove && !isLeftBorderUp && !isLeftBorderBelow && !isLeftBorderDown  -> tiles.leftBorderAbove
                                    !isLeftBorderAbove && isLeftBorderUp && isLeftBorderBelow && isLeftBorderDown    -> tiles.leftBorderUpBelowDown
                                    !isLeftBorderAbove && isLeftBorderUp && isLeftBorderBelow && !isLeftBorderDown   -> tiles.leftBorderUpBelow
                                    !isLeftBorderAbove && isLeftBorderUp && !isLeftBorderBelow && isLeftBorderDown   -> tiles.leftBorderUpDown
                                    !isLeftBorderAbove && isLeftBorderUp && !isLeftBorderBelow && !isLeftBorderDown  -> tiles.leftBorderUp
                                    !isLeftBorderAbove && !isLeftBorderUp && isLeftBorderBelow && isLeftBorderDown   -> tiles.leftBorderBelowDown
                                    !isLeftBorderAbove && !isLeftBorderUp && isLeftBorderBelow && !isLeftBorderDown  -> tiles.leftBorderBelow
                                    !isLeftBorderAbove && !isLeftBorderUp && !isLeftBorderBelow && isLeftBorderDown  -> tiles.leftBorderDown
                                    !isLeftBorderAbove && !isLeftBorderUp && !isLeftBorderBelow && !isLeftBorderDown -> tiles.leftBorder
                                    else                                                                             -> null
                                }
                                BlockSide.Up    -> if (!isBorder || !isUpBorder) null
                                else when
                                {
                                    isUpBorderAbove && isUpBorderRight && isUpBorderBelow && isUpBorderLeft     -> tiles.upBorderAboveRightBelowLeft
                                    isUpBorderAbove && isUpBorderRight && isUpBorderBelow && !isUpBorderLeft    -> tiles.upBorderAboveRightBelow
                                    isUpBorderAbove && isUpBorderRight && !isUpBorderBelow && isUpBorderLeft    -> tiles.upBorderAboveRightLeft
                                    isUpBorderAbove && isUpBorderRight && !isUpBorderBelow && !isUpBorderLeft   -> tiles.upBorderAboveRight
                                    isUpBorderAbove && !isUpBorderRight && isUpBorderBelow && isUpBorderLeft    -> tiles.upBorderAboveBelowLeft
                                    isUpBorderAbove && !isUpBorderRight && isUpBorderBelow && !isUpBorderLeft   -> tiles.upBorderAboveBelow
                                    isUpBorderAbove && !isUpBorderRight && !isUpBorderBelow && isUpBorderLeft   -> tiles.upBorderAboveLeft
                                    isUpBorderAbove && !isUpBorderRight && !isUpBorderBelow && !isUpBorderLeft  -> tiles.upBorderAbove
                                    !isUpBorderAbove && isUpBorderRight && isUpBorderBelow && isUpBorderLeft    -> tiles.upBorderRightBelowLeft
                                    !isUpBorderAbove && isUpBorderRight && isUpBorderBelow && !isUpBorderLeft   -> tiles.upBorderRightBelow
                                    !isUpBorderAbove && isUpBorderRight && !isUpBorderBelow && isUpBorderLeft   -> tiles.upBorderRightLeft
                                    !isUpBorderAbove && isUpBorderRight && !isUpBorderBelow && !isUpBorderLeft  -> tiles.upBorderRight
                                    !isUpBorderAbove && !isUpBorderRight && isUpBorderBelow && isUpBorderLeft   -> tiles.upBorderBelowLeft
                                    !isUpBorderAbove && !isUpBorderRight && isUpBorderBelow && !isUpBorderLeft  -> tiles.upBorderBelow
                                    !isUpBorderAbove && !isUpBorderRight && !isUpBorderBelow && isUpBorderLeft  -> tiles.upBorderLeft
                                    !isUpBorderAbove && !isUpBorderRight && !isUpBorderBelow && !isUpBorderLeft -> tiles.upBorder
                                    else                                                                        -> null
                                }
                                BlockSide.Below -> if (!isBorder || !isBelowBorder) null
                                else when
                                {
                                    isBelowBorderUp && isBelowBorderRight && isBelowBorderDown && isBelowBorderLeft     -> tiles.belowBorderUpRightDownLeft
                                    isBelowBorderUp && isBelowBorderRight && isBelowBorderDown && !isBelowBorderLeft    -> tiles.belowBorderUpRightDown
                                    isBelowBorderUp && isBelowBorderRight && !isBelowBorderDown && isBelowBorderLeft    -> tiles.belowBorderUpRightLeft
                                    isBelowBorderUp && isBelowBorderRight && !isBelowBorderDown && !isBelowBorderLeft   -> tiles.belowBorderUpRight
                                    isBelowBorderUp && !isBelowBorderRight && isBelowBorderDown && isBelowBorderLeft    -> tiles.belowBorderUpDownLeft
                                    isBelowBorderUp && !isBelowBorderRight && isBelowBorderDown && !isBelowBorderLeft   -> tiles.belowBorderUpDown
                                    isBelowBorderUp && !isBelowBorderRight && !isBelowBorderDown && isBelowBorderLeft   -> tiles.belowBorderUpLeft
                                    isBelowBorderUp && !isBelowBorderRight && !isBelowBorderDown && !isBelowBorderLeft  -> tiles.belowBorderUp
                                    !isBelowBorderUp && isBelowBorderRight && isBelowBorderDown && isBelowBorderLeft    -> tiles.belowBorderRightDownLeft
                                    !isBelowBorderUp && isBelowBorderRight && isBelowBorderDown && !isBelowBorderLeft   -> tiles.belowBorderRightDown
                                    !isBelowBorderUp && isBelowBorderRight && !isBelowBorderDown && isBelowBorderLeft   -> tiles.belowBorderRightLeft
                                    !isBelowBorderUp && isBelowBorderRight && !isBelowBorderDown && !isBelowBorderLeft  -> tiles.belowBorderRight
                                    !isBelowBorderUp && !isBelowBorderRight && isBelowBorderDown && isBelowBorderLeft   -> tiles.belowBorderDownLeft
                                    !isBelowBorderUp && !isBelowBorderRight && isBelowBorderDown && !isBelowBorderLeft  -> tiles.belowBorderDown
                                    !isBelowBorderUp && !isBelowBorderRight && !isBelowBorderDown && isBelowBorderLeft  -> tiles.belowBorderLeft
                                    !isBelowBorderUp && !isBelowBorderRight && !isBelowBorderDown && !isBelowBorderLeft -> tiles.belowBorder
                                    else                                                                                -> null
                                }
                            }
                        }
                    }
                }
            }
        }
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

        val map = maps[selectPosition.z.toInt()]

        (map.layers[BlockSide.Right.name] as TiledMapTileLayer).getCell(
            selectPosition.x.toInt(),
            selectPosition.y.toInt()
        ).tile = tiles.rightSelect
        (map.layers[BlockSide.Left.name] as TiledMapTileLayer).getCell(
            selectPosition.x.toInt(),
            selectPosition.y.toInt()
        ).tile = tiles.leftSelect
        (map.layers[BlockSide.Up.name] as TiledMapTileLayer).getCell(
            selectPosition.x.toInt(),
            selectPosition.y.toInt()
        ).tile = tiles.upSelect
        (map.layers[BlockSide.Down.name] as TiledMapTileLayer).getCell(
            selectPosition.x.toInt(),
            selectPosition.y.toInt()
        ).tile = tiles.downSelect
        (map.layers[BlockSide.Above.name] as TiledMapTileLayer).getCell(
            selectPosition.x.toInt(),
            selectPosition.y.toInt()
        ).tile = tiles.aboveSelect
        (map.layers[BlockSide.Below.name] as TiledMapTileLayer).getCell(
            selectPosition.x.toInt(),
            selectPosition.y.toInt()
        ).tile = tiles.belowSelect
    }

    fun info(position : Vector3)
    {
        val block = room.getBlock(position)

        if (block != null)
        {
            Gdx.app.log("--", "--")

            Gdx.app.log("r", "${block.isRightBorder}")
            Gdx.app.log("r u", "${block.isRightBorderUp}")
            Gdx.app.log("r d", "${block.isRightBorderDown}")
            Gdx.app.log("r a", "${block.isRightBorderAbove}")
            Gdx.app.log("r b", "${block.isRightBorderBelow}")

            Gdx.app.log("--", "--")

            Gdx.app.log("l", "${block.isLeftBorder}")
            Gdx.app.log("l u", "${block.isLeftBorderUp}")
            Gdx.app.log("l d", "${block.isLeftBorderDown}")
            Gdx.app.log("l a", "${block.isLeftBorderAbove}")
            Gdx.app.log("l b", "${block.isLeftBorderBelow}")

            Gdx.app.log("--", "--")

            Gdx.app.log("u", "${block.isUpBorder}")
            Gdx.app.log("u r", "${block.isUpBorderRight}")
            Gdx.app.log("u l", "${block.isUpBorderLeft}")
            Gdx.app.log("u a", "${block.isUpBorderAbove}")
            Gdx.app.log("u b", "${block.isUpBorderBelow}")

            Gdx.app.log("--", "--")

            Gdx.app.log("d", "${block.isDownBorder}")
            Gdx.app.log("d r", "${block.isDownBorderRight}")
            Gdx.app.log("d l", "${block.isDownBorderLeft}")
            Gdx.app.log("d a", "${block.isDownBorderAbove}")
            Gdx.app.log("d b", "${block.isDownBorderBelow}")

            Gdx.app.log("--", "--")

            Gdx.app.log("a", "${block.isAboveBorder}")
            Gdx.app.log("a r", "${block.isAboveBorderRight}")
            Gdx.app.log("a l", "${block.isAboveBorderLeft}")
            Gdx.app.log("a u", "${block.isAboveBorderUp}")
            Gdx.app.log("a d", "${block.isAboveBorderDown}")

            Gdx.app.log("--", "--")

            Gdx.app.log("b", "${block.isBelowBorder}")
            Gdx.app.log("b r", "${block.isBelowBorderRight}")
            Gdx.app.log("b l", "${block.isBelowBorderLeft}")
            Gdx.app.log("b u", "${block.isBelowBorderUp}")
            Gdx.app.log("b d", "${block.isBelowBorderDown}")

            Gdx.app.log("--", "--")
        }
    }

    fun clearMap()
    {
        for (m in maps)
        {
            for (l in m.layers)
            {
                for (j in 0 until maxHeight)
                {
                    for (i in 0 until maxWidth)
                    {
                        (l as TiledMapTileLayer).getCell(i, j).tile = null
                    }
                }
            }
        }
    }
}
