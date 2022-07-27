package com.marcin.panklex

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.ScreenUtils

class KlexMap(val room : KlexRoom, val tiles : KlexTiles)
{
    // tile lengths

    val tileLength = 32
    val tileLengthHalf = tileLength / 2
    val tileLengthQuarter = tileLengthHalf / 2

    //important

    val maps = mutableListOf<TiledMap>()
    val renderers = mutableListOf<KlexIsometricTiledMapRenderer>()

    // dimensions

    var width = 0
    val maxWidth = 100
    var height = 0
    val maxHeight = 100
    val maxFloors = 10

    // other

    var direction = MapDirection.Up
    var selectMapPosition = Vector3()

    fun createMap()
    {
        for (k in 0 until maxFloors)
        {
            maps.add(TiledMap())
            renderers.add(KlexIsometricTiledMapRenderer(maps[k], 1f, k))

            val belowLayer = TiledMapTileLayer(maxWidth, maxHeight, tileLength, tileLengthHalf).apply { name = "below" }
            val leftLayer = TiledMapTileLayer(maxWidth, maxHeight, tileLength, tileLengthHalf).apply { name = "left" }
            val upLayer = TiledMapTileLayer(maxWidth, maxHeight, tileLength, tileLengthHalf).apply { name = "up" }
            val frontLayer = TiledMapTileLayer(maxWidth, maxHeight, tileLength, tileLengthHalf).apply { name = "front" }

            for (j in 0 until maxHeight)
            {
                for (i in 0 until maxWidth)
                {
                    val belowCell = TiledMapTileLayer.Cell()
                    val leftCell = TiledMapTileLayer.Cell()
                    val upCell = TiledMapTileLayer.Cell()
                    val frontCell = TiledMapTileLayer.Cell()
                    belowLayer.setCell(i, j, belowCell)
                    leftLayer.setCell(i, j, leftCell)
                    upLayer.setCell(i, j, upCell)
                    frontLayer.setCell(i, j, frontCell)
                }
            }

            maps[k].layers.add(belowLayer)
            maps[k].layers.add(leftLayer)
            maps[k].layers.add(upLayer)
            maps[k].layers.add(frontLayer)
        }
    }

    fun updateMap()
    {
        // clear

        for (k in 0 until maxFloors)
        {
            for (j in 0 until maxHeight)
            {
                for (i in 0 until maxWidth)
                {
                    for (layer in maps[k].layers)
                    {
                        (layer as TiledMapTileLayer).getCell(i, j).tile = null
                    }
                }
            }
        }

        // dimensions

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

        // new tiles

        for (k in 0 until maxFloors)
        {
            for (j in 0 until room.height)
            {
                for (i in 0 until room.width)
                {
                    val block = room.getBlock(i, j, k)

                    if (block != null)
                    {
                        /////

                        val isRightBorder = when (direction)
                        {
                            MapDirection.Up    -> block.isRightBorder
                            MapDirection.Right -> block.isUpBorder
                            MapDirection.Down  -> block.isLeftBorder
                            MapDirection.Left  -> block.isDownBorder
                        }
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
                        val isDownBorder = when (direction)
                        {
                            MapDirection.Up    -> block.isDownBorder
                            MapDirection.Right -> block.isRightBorder
                            MapDirection.Down  -> block.isUpBorder
                            MapDirection.Left  -> block.isLeftBorder
                        }
                        val isAboveBorder = block.isAboveBorder
                        val isBelowBorder = block.isBelowBorder

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

                        val belowLayer = maps[k].layers["below"] as TiledMapTileLayer
                        val leftLayer = maps[k].layers["left"] as TiledMapTileLayer
                        val upLayer = maps[k].layers["up"] as TiledMapTileLayer
                        val frontLayer = maps[k].layers["front"] as TiledMapTileLayer

                        val belowCell = when (direction)
                        {
                            MapDirection.Up    -> belowLayer.getCell(i, j)
                            MapDirection.Right -> belowLayer.getCell(j, room.width - 1 - i)
                            MapDirection.Down  -> belowLayer.getCell(room.width - 1 - i, room.height - 1 - j)
                            MapDirection.Left  -> belowLayer.getCell(room.height - 1 - j, i)
                        }

                        val leftCell = when (direction)
                        {
                            MapDirection.Up    -> leftLayer.getCell(i, j)
                            MapDirection.Right -> leftLayer.getCell(j, room.width - 1 - i)
                            MapDirection.Down  -> leftLayer.getCell(room.width - 1 - i, room.height - 1 - j)
                            MapDirection.Left  -> leftLayer.getCell(room.height - 1 - j, i)
                        }

                        val upCell = when (direction)
                        {
                            MapDirection.Up    -> upLayer.getCell(i, j)
                            MapDirection.Right -> upLayer.getCell(j, room.width - 1 - i)
                            MapDirection.Down  -> upLayer.getCell(room.width - 1 - i, room.height - 1 - j)
                            MapDirection.Left  -> upLayer.getCell(room.height - 1 - j, i)
                        }

                        val frontCell = when (direction)
                        {
                            MapDirection.Up    -> frontLayer.getCell(i, j)
                            MapDirection.Right -> frontLayer.getCell(j, room.width - 1 - i)
                            MapDirection.Down  -> frontLayer.getCell(room.width - 1 - i, room.height - 1 - j)
                            MapDirection.Left  -> frontLayer.getCell(room.height - 1 - j, i)
                        }

                        belowCell.tile = if (isBelowBorder) tiles.below(isBelowBorderUp, isBelowBorderRight, isBelowBorderDown, isBelowBorderLeft) else tiles.belowBorderTile
                        leftCell.tile = if (isLeftBorder) tiles.left(isLeftBorderAbove, isLeftBorderUp, isLeftBorderBelow, isLeftBorderDown) else tiles.leftBorderTile
                        upCell.tile = if (isUpBorder) tiles.up(isUpBorderAbove, isUpBorderRight, isUpBorderBelow, isUpBorderLeft) else tiles.upBorderTile
                        frontCell.tile = tiles.front(isDownBorder, isRightBorder, isAboveBorder)

                        /////
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
    }

    fun changeSelection(selectPosition : Vector3)
    {
        selectMapPosition.set(selectPosition)

        (maps[selectMapPosition.z.toInt()].layers["below"] as TiledMapTileLayer).getCell(selectMapPosition.x.toInt(), selectMapPosition.y.toInt()).tile = tiles.belowSelectTile
        (maps[selectMapPosition.z.toInt()].layers["left"] as TiledMapTileLayer).getCell(selectMapPosition.x.toInt(), selectMapPosition.y.toInt()).tile = tiles.leftSelectTile
        (maps[selectMapPosition.z.toInt()].layers["up"] as TiledMapTileLayer).getCell(selectMapPosition.x.toInt(), selectMapPosition.y.toInt()).tile = tiles.upSelectTile
        (maps[selectMapPosition.z.toInt()].layers["front"] as TiledMapTileLayer).getCell(selectMapPosition.x.toInt(), selectMapPosition.y.toInt()).tile = tiles.frontSelectTile
    }

    fun info(position : Vector3)
    {
        val block = room.getBlock(position)

        if (block != null)
        {
            Gdx.app.log("--", "--")

            Gdx.app.log("B", "${block.isBorder}")

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
}
