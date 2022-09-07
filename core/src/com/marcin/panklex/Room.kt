package com.marcin.panklex

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3

class Room(val level : Level)
{
    var isRoomCreated = false
    var roomWidthStart = 0
    var roomWidthEnd = 0
    var roomWidth = 0
    var roomHeightStart = 0
    var roomHeightEnd = 0
    var roomHeight = 0
    var roomFloorStart = 0
    var roomFloorEnd = 0
    var roomFloors = 0
    var roomSpaces = mutableListOf<Space>()

    fun updateRoom(startingPosition : Vector3)
    {
        Gdx.app.log("room", "updating room...")

        roomWidthStart = 0
        roomWidthEnd = 0
        roomWidth = 0
        roomHeightStart = 0
        roomHeightEnd = 0
        roomHeight = 0
        roomFloorStart = 0
        roomFloorEnd = 0
        roomFloors = 0
        roomSpaces.clear()

        var isCreatingRoomPossible = false

        val startingSpace = level.getSpace(startingPosition)

        if (startingSpace != null)
        {
            val objectOccupyingStartingSpace = startingSpace.objectOccupying

            if (objectOccupyingStartingSpace == null)
            {
                isCreatingRoomPossible = true
            }
        }

        if (!isCreatingRoomPossible) isRoomCreated = false
        else
        {
            startingSpace!!

            ///// init

            isRoomCreated = true

            roomWidthStart = startingSpace.position.x.toInt()
            roomWidthEnd = startingSpace.position.x.toInt()
            roomWidth = 0
            roomHeightStart = startingSpace.position.y.toInt()
            roomHeightEnd = startingSpace.position.y.toInt()
            roomHeight = 0
            roomFloorStart = startingSpace.position.z.toInt()
            roomFloorEnd = startingSpace.position.z.toInt()
            roomFloors = 0

            ///// gather spaces within borders

            val queue = mutableListOf<Space>()
            val checked = mutableListOf<Space>()
            val border = mutableListOf<Space>()
            val notBorder = mutableListOf<Space>()

            queue.add(startingSpace)

            while (queue.isNotEmpty())
            {
                // current space

                var current = queue.firstOrNull { space -> space !in border }
                if (current == null) current = queue.first()
                queue.remove(current)
                checked.add(current)

                // adjust boundaries

                if (current.position.x < roomWidthStart) roomWidthStart = current.position.x.toInt()
                if (current.position.x > roomWidthEnd) roomWidthEnd = current.position.x.toInt()
                if (current.position.y < roomHeightStart) roomHeightStart = current.position.y.toInt()
                if (current.position.y > roomHeightEnd) roomHeightEnd = current.position.y.toInt()
                if (current.position.z < roomFloorStart) roomFloorStart = current.position.z.toInt()
                if (current.position.z > roomFloorEnd) roomFloorEnd = current.position.z.toInt()

                // get new spaces

                if (current !in border)
                {
                    for (side in Direction3d.values())
                    {
                        val neighbour = level.getSpace(current.position, side)

                        if (neighbour != null)
                        {
                            // side visibility

                            val oppositeSide = oppositeDirection3d(side)

                            neighbour.sideVisibility[oppositeSide] = true

                            // on border

                            when (neighbour.sideTransparency[oppositeSide])
                            {
                                true  ->
                                {
                                    if (neighbour in border)
                                    {
                                        border.remove(neighbour)
                                    }

                                    if (neighbour !in notBorder)
                                    {
                                        notBorder.add(neighbour)
                                    }
                                }
                                false ->
                                {
                                    if (neighbour !in border && neighbour !in notBorder)
                                    {
                                        border.add(neighbour)
                                    }
                                }
                            }

                            // new spaces

                            if (neighbour !in queue && neighbour !in checked)
                            {
                                queue.add(neighbour)
                            }
                        }
                    }
                }
            }

            ///// determine border

            for (space in checked)
            {
                if (space in border)
                {
                    space.isOnBorder = true
                }
                else
                {
                    space.isWithinBorder = true
                }
            }

            ///// build room

            roomWidth = roomWidthEnd - roomWidthStart + 1
            roomHeight = roomHeightEnd - roomHeightStart + 1
            roomFloors = roomFloorEnd - roomFloorStart + 1

            for (s in checked)
            {
                roomSpaces.add(s)
            }
        }

        Gdx.app.log("room", "updated room")
    }

    fun updateTiles(tiles : Tiles, mapDirection : Direction2d)
    {
        //Gdx.app.log("room","updating tiles...")

        for (space in roomSpaces)
        {
            // clear

            for (layer in SpaceLayer.values())
            {
                space.layerTiles[layer] = null
            }

            // EntityWhole, EntityOutline

            val entityOccupying = space.entityOccupying

            if (entityOccupying != null)
            {
                entityOccupying.getTiles(tiles, space.layerTiles)
            }

            val objectOccupying = space.objectOccupying

            if (objectOccupying != null)
            {
                // sides, Behind, Before

                objectOccupying.getTiles(tiles, space.layerTiles, space.position, mapDirection)

                // lines

                if (space.isOnBorder)
                {
                    var isAboveLine = false
                    var isUpLine = false
                    var isRightLine = false
                    var isDownLine = false
                    var isLeftLine = false
                    var isBelowLine = false

                    val aboveSpace = getSpace(space.position, Direction3d.Above)
                    val upSpace = getSpace(space.position, Direction3d.Up)
                    val rightSpace = getSpace(space.position, Direction3d.Right)
                    val downSpace = getSpace(space.position, Direction3d.Down)
                    val leftSpace = getSpace(space.position, Direction3d.Left)
                    val belowSpace = getSpace(space.position, Direction3d.Below)

                    if (aboveSpace == null || !aboveSpace.isOnBorder) isAboveLine = true
                    if (upSpace == null || !upSpace.isOnBorder) isUpLine = true
                    if (rightSpace == null || !rightSpace.isOnBorder) isRightLine = true
                    if (downSpace == null || !downSpace.isOnBorder) isDownLine = true
                    if (leftSpace == null || !leftSpace.isOnBorder) isLeftLine = true
                    if (belowSpace == null || !belowSpace.isOnBorder) isBelowLine = true

                    space.layerTiles[SpaceLayer.LinesBelow] = when (mapDirection)
                    {
                        Direction2d.Up    -> tiles.getBelow(isUpLine, isRightLine, isDownLine, isLeftLine)
                        Direction2d.Right -> tiles.getBelow(isRightLine, isDownLine, isLeftLine, isUpLine)
                        Direction2d.Down  -> tiles.getBelow(isDownLine, isLeftLine, isUpLine, isRightLine)
                        Direction2d.Left  -> tiles.getBelow(isLeftLine, isUpLine, isRightLine, isDownLine)
                    }

                    space.layerTiles[SpaceLayer.LinesLeft] = when (mapDirection)
                    {
                        Direction2d.Up    -> tiles.getLeft(isAboveLine, isUpLine, isBelowLine, isDownLine)
                        Direction2d.Right -> tiles.getLeft(isAboveLine, isRightLine, isBelowLine, isLeftLine)
                        Direction2d.Down  -> tiles.getLeft(isAboveLine, isDownLine, isBelowLine, isUpLine)
                        Direction2d.Left  -> tiles.getLeft(isAboveLine, isLeftLine, isBelowLine, isRightLine)
                    }

                    space.layerTiles[SpaceLayer.LinesUp] = when (mapDirection)
                    {
                        Direction2d.Up    -> tiles.getUp(isAboveLine, isRightLine, isBelowLine, isLeftLine)
                        Direction2d.Right -> tiles.getUp(isAboveLine, isDownLine, isBelowLine, isUpLine)
                        Direction2d.Down  -> tiles.getUp(isAboveLine, isLeftLine, isBelowLine, isRightLine)
                        Direction2d.Left  -> tiles.getUp(isAboveLine, isUpLine, isBelowLine, isDownLine)
                    }
                }
            }
        }

        //Gdx.app.log("room","updated tiles")
    }

    fun getSpace(x : Int, y : Int, z : Int) : Space?
    {
        return roomSpaces.firstOrNull { s -> s.position.x.toInt() == x && s.position.y.toInt() == y && s.position.z.toInt() == z }
    }

    fun getSpace(roomPosition : Vector3) : Space?
    {
        return getSpace(roomPosition.x.toInt(), roomPosition.y.toInt(), roomPosition.z.toInt())
    }

    fun getSpace(roomPosition : Vector3, direction : Direction3d) : Space?
    {
        return when (direction)
        {
            Direction3d.Right -> getSpace(roomPosition.x.toInt() + 1, roomPosition.y.toInt(), roomPosition.z.toInt())
            Direction3d.Left  -> getSpace(roomPosition.x.toInt() - 1, roomPosition.y.toInt(), roomPosition.z.toInt())
            Direction3d.Up    -> getSpace(roomPosition.x.toInt(), roomPosition.y.toInt() + 1, roomPosition.z.toInt())
            Direction3d.Down  -> getSpace(roomPosition.x.toInt(), roomPosition.y.toInt() - 1, roomPosition.z.toInt())
            Direction3d.Above -> getSpace(roomPosition.x.toInt(), roomPosition.y.toInt(), roomPosition.z.toInt() + 1)
            Direction3d.Below -> getSpace(roomPosition.x.toInt(), roomPosition.y.toInt(), roomPosition.z.toInt() - 1)
        }
    }
}
