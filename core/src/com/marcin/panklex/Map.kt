package com.marcin.panklex

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.math.Vector3

class Map(val room : Room, val tiles : Tiles)
{
    // dimensions

    var mapWidth = 0
    val mapMaxWidth = 100
    var mapHeight = 0
    val mapMaxHeight = 100
    var mapFloors = 0
    val mapMaxFloors = 10

    // other

    val maps = mutableListOf<TiledMap>()
    val renderers = mutableListOf<CustomIsometricTiledMapRenderer>()
    var mapDirection = Direction2d.Up

    fun createMap()
    {
        Gdx.app.log("map", "creating map...")

        for (k in 0 until mapMaxFloors)
        {
            maps.add(TiledMap())
            renderers.add(CustomIsometricTiledMapRenderer(maps[k], 1f, k))

            val layers = mutableListOf<TiledMapTileLayer>()

            layers.add(TiledMapTileLayer(mapMaxWidth, mapMaxHeight, tileLength, tileLengthHalf).apply {
                name = SpaceLayer.SelectBack.name
            })
            layers.add(TiledMapTileLayer(mapMaxWidth, mapMaxHeight, tileLength, tileLengthHalf).apply {
                name = SpaceLayer.LinesBelow.name
            })
            layers.add(TiledMapTileLayer(mapMaxWidth, mapMaxHeight, tileLength, tileLengthHalf).apply {
                name = SpaceLayer.SideBelow.name
            })
            layers.add(TiledMapTileLayer(mapMaxWidth, mapMaxHeight, tileLength, tileLengthHalf).apply {
                name = SpaceLayer.LinesLeft.name
            })
            layers.add(TiledMapTileLayer(mapMaxWidth, mapMaxHeight, tileLength, tileLengthHalf).apply {
                name = SpaceLayer.SideLeft.name
            })
            layers.add(TiledMapTileLayer(mapMaxWidth, mapMaxHeight, tileLength, tileLengthHalf).apply {
                name = SpaceLayer.LinesUp.name
            })
            layers.add(TiledMapTileLayer(mapMaxWidth, mapMaxHeight, tileLength, tileLengthHalf).apply {
                name = SpaceLayer.SideUp.name
            })
            layers.add(TiledMapTileLayer(mapMaxWidth, mapMaxHeight, tileLength, tileLengthHalf).apply {
                name = SpaceLayer.Behind.name
            })
            layers.add(TiledMapTileLayer(mapMaxWidth, mapMaxHeight, tileLength, tileLengthHalf).apply {
                name = SpaceLayer.EntityWhole.name
            })
            layers.add(TiledMapTileLayer(mapMaxWidth, mapMaxHeight, tileLength, tileLengthHalf).apply {
                name = SpaceLayer.Before.name
            })
            layers.add(TiledMapTileLayer(mapMaxWidth, mapMaxHeight, tileLength, tileLengthHalf).apply {
                name = SpaceLayer.SideDown.name
            })
            layers.add(TiledMapTileLayer(mapMaxWidth, mapMaxHeight, tileLength, tileLengthHalf).apply {
                name = SpaceLayer.SideRight.name
            })
            layers.add(TiledMapTileLayer(mapMaxWidth, mapMaxHeight, tileLength, tileLengthHalf).apply {
                name = SpaceLayer.SideAbove.name
            })
            layers.add(TiledMapTileLayer(mapMaxWidth, mapMaxHeight, tileLength, tileLengthHalf).apply {
                name = SpaceLayer.SelectFront.name
            })
            layers.add(TiledMapTileLayer(mapMaxWidth, mapMaxHeight, tileLength, tileLengthHalf).apply {
                name = SpaceLayer.EntityOutline.name
            })

            for (j in 0 until mapMaxHeight)
            {
                for (i in 0 until mapMaxWidth)
                {
                    for (layer in layers)
                    {
                        val cell = TiledMapTileLayer.Cell()
                        layer.setCell(i, j, cell)
                    }
                }
            }

            layers.forEach(maps[k].layers::add)
        }

        Gdx.app.log("map", "created map")
    }

    fun updateMap()
    {
        //Gdx.app.log("map", "updating map...")

        // clear

        for (k in 0 until mapMaxFloors)
        {
            for (j in 0 until mapMaxHeight)
            {
                for (i in 0 until mapMaxWidth)
                {
                    for (layer in maps[k].layers)
                    {
                        (layer as TiledMapTileLayer).getCell(i, j).tile = null
                    }
                }
            }
        }

        // new dimensions

        mapWidth = when (mapDirection)
        {
            Direction2d.Up, Direction2d.Down    -> room.roomWidth
            Direction2d.Right, Direction2d.Left -> room.roomHeight
        }

        mapHeight = when (mapDirection)
        {
            Direction2d.Up, Direction2d.Down    -> room.roomHeight
            Direction2d.Right, Direction2d.Left -> room.roomWidth
        }

        mapFloors = room.roomFloors

        // new tiles

        val spaceObjectivePosition = Vector3()
        val spaceRelativePosition = Vector3()

        for (k in 0 until room.roomFloors)
        {
            for (j in 0 until room.roomHeight)
            {
                for (i in 0 until room.roomWidth)
                {
                    spaceObjectivePosition.set(i.toFloat(), j.toFloat(), k.toFloat())
                    getRelativeMapPosition(spaceObjectivePosition, spaceRelativePosition)

                    val space = room.getSpace(spaceObjectivePosition)

                    if (space != null)
                    {
                        // tiles

                        for (layer in SpaceLayer.values())
                        {
                            val mapLayer = maps[k].layers[layer.name] as TiledMapTileLayer

                            val cell = mapLayer.getCell(spaceRelativePosition.x.toInt(), spaceRelativePosition.y.toInt())

                            val relativeSide = spaceLayerToDirection3d(layer)

                            val objectiveSide = when (relativeSide)
                            {
                                null -> null
                                else -> relativeToObjectiveDirection3d(relativeSide, mapDirection)
                            }

                            var tile = space.layerTiles[layer]

                            if (space.isOnBorder && (layer == SpaceLayer.Behind || layer == SpaceLayer.Before))
                            {
                                tile = null
                            }

                            if (space.sideVisibility[objectiveSide] == false)
                            {
                                tile = null
                            }

                            cell.tile = tile
                        }
                    }
                }
            }
        }

        //Gdx.app.log("map", "updated map")
    }

    fun changeMapDirection(newDirection : Direction2d)
    {
        mapDirection = newDirection
    }

    fun changeSelection(levelPosition : Vector3)
    {
        val space = room.getSpace(levelPosition)

        if (space != null)
        {
            space.layerTiles[SpaceLayer.SelectBack] = tiles.selectBack
            space.layerTiles[SpaceLayer.SelectFront] = tiles.selectFront
        }
    }

    fun getObjectiveMapPosition(relativeMapPosition : Vector3, objectiveMapPosition : Vector3)
    {
        val x = relativeMapPosition.x
        val y = relativeMapPosition.y
        val z = relativeMapPosition.z

        when (mapDirection)
        {
            Direction2d.Up    -> objectiveMapPosition.set(x, y, z)
            Direction2d.Right -> objectiveMapPosition.set(y, mapWidth - 1 - x, z)
            Direction2d.Down  -> objectiveMapPosition.set(mapWidth - 1 - x, mapHeight - 1 - y, z)
            Direction2d.Left  -> objectiveMapPosition.set(mapHeight - 1 - y, x, z)
        }
    }

    fun getRelativeMapPosition(objectiveMapPosition : Vector3, relativeMapPosition : Vector3)
    {
        val x = objectiveMapPosition.x
        val y = objectiveMapPosition.y
        val z = objectiveMapPosition.z

        when (mapDirection)
        {
            Direction2d.Up    -> relativeMapPosition.set(x, y, z)
            Direction2d.Right -> relativeMapPosition.set(room.roomHeight - 1 - y, x, z)
            Direction2d.Down  -> relativeMapPosition.set(room.roomWidth - 1 - x, mapHeight - 1 - y, z)
            Direction2d.Left  -> relativeMapPosition.set(y, room.roomWidth - 1 - x, z)
        }
    }

    fun getLevelPosition(objectiveMapPosition : Vector3, levelPosition : Vector3)
    {
        levelPosition.set(
            room.roomWidthStart + objectiveMapPosition.x, room.roomHeightStart + objectiveMapPosition.y, room.roomFloorStart + objectiveMapPosition.z)
    }

    fun info(levelPosition : Vector3)
    {
        val space = room.getSpace(levelPosition)

        if (space != null)
        {
            Gdx.app.log("--", "--")

            Gdx.app.log("position", space.position.toString())
            Gdx.app.log("objectOccupying", space.objectOccupying?.objectType ?: "--")
            Gdx.app.log("entityOccupying", space.entityOccupying?.entityType ?: "--")

            Gdx.app.log("isOnBorder", "${space.isOnBorder}")
            Gdx.app.log("isWithinBorder", "${space.isWithinBorder}")

            Gdx.app.log("sideTransparency b", "${space.sideTransparency[Direction3d.Below]}")
            Gdx.app.log("sideTransparency l", "${space.sideTransparency[Direction3d.Left]}")
            Gdx.app.log("sideTransparency u", "${space.sideTransparency[Direction3d.Up]}")
            Gdx.app.log("sideTransparency d", "${space.sideTransparency[Direction3d.Down]}")
            Gdx.app.log("sideTransparency r", "${space.sideTransparency[Direction3d.Right]}")
            Gdx.app.log("sideTransparency a", "${space.sideTransparency[Direction3d.Above]}")

            Gdx.app.log("--", "--")
        }
    }
}
