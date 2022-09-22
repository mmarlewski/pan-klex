package com.marcin.panklex

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.entities.EntityPlayer

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
    var entityPlayer = level.entityPlayer

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

                            if (current.sideTransparency[side] == true)
                            {
                                neighbour.sideVisibility[oppositeSide] = true
                            }

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

    fun updateEntityTiles(tiles : Tiles)
    {
        Gdx.app.log("room", "updating entity tiles...")

        for (space in roomSpaces)
        {
            space.layerTiles[SpaceLayer.EntityWhole] = null
            space.layerTiles[SpaceLayer.EntityOutline] = null

            val entityOccupying = space.entityOccupying

            entityOccupying?.getTiles(tiles, space.layerTiles)
        }

        Gdx.app.log("room", "updated entity tiles")
    }

    fun updateObjectTiles(tiles : Tiles, mapDirection : Direction2d)
    {
        Gdx.app.log("room", "updating object tiles...")

        for (space in roomSpaces)
        {
            space.layerTiles[SpaceLayer.SideBelow] = null
            space.layerTiles[SpaceLayer.SideLeft] = null
            space.layerTiles[SpaceLayer.SideUp] = null
            space.layerTiles[SpaceLayer.Behind] = null
            space.layerTiles[SpaceLayer.Before] = null
            space.layerTiles[SpaceLayer.SideDown] = null
            space.layerTiles[SpaceLayer.SideRight] = null
            space.layerTiles[SpaceLayer.SideAbove] = null

            val objectOccupying = space.objectOccupying

            objectOccupying?.getTiles(tiles, space.layerTiles, space.position, mapDirection)
        }

        Gdx.app.log("room", "updated object tiles")
    }

    fun updateLinesTiles(tiles : Tiles, mapDirection : Direction2d)
    {
        Gdx.app.log("room", "updating lines tiles...")

        for (space in roomSpaces)
        {
            space.layerTiles[SpaceLayer.LinesBelow] = null
            space.layerTiles[SpaceLayer.LinesLeft] = null
            space.layerTiles[SpaceLayer.LinesUp] = null

            val objectOccupying = space.objectOccupying

            if (objectOccupying != null)
            {
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

        Gdx.app.log("room", "updated lines tiles")
    }

    fun updateMoveTiles(tiles : Tiles, mapDirection : Direction2d)
    {
        Gdx.app.log("room", "updating move tiles...")

        for (space in roomSpaces)
        {
            space.layerTiles[SpaceLayer.MoveWhole] = null
            space.layerTiles[SpaceLayer.MoveOutline] = null

            if (space.isOnPath)
            {
                space.move?.getMoveTiles(tiles, space.layerTiles, space.position, mapDirection)
            }
        }

        Gdx.app.log("room", "updated move tiles")
    }

    fun updateSelectTiles(
        tiles : Tiles, selectPosition : Vector3, isMoving : Boolean, isPathFound : Boolean, isMoveAccessible : Boolean)
    {
        //Gdx.app.log("room", "updating select tiles...")

        for (space in roomSpaces)
        {
            space.layerTiles[SpaceLayer.SelectBack] = null
            space.layerTiles[SpaceLayer.SelectFront] = null

            if (space.position == selectPosition)
            {
                when (isMoving)
                {
                    true  -> when (isPathFound)
                    {
                        true  -> when (isMoveAccessible)
                        {
                            true  ->
                            {
                                space.layerTiles[SpaceLayer.SelectBack] = tiles.selectPathFoundBack
                                space.layerTiles[SpaceLayer.SelectFront] = tiles.selectPathFoundFront
                            }
                            false ->
                            {
                                space.layerTiles[SpaceLayer.SelectBack] = tiles.selectInaccessibleBack
                                space.layerTiles[SpaceLayer.SelectFront] = tiles.selectInaccessibleFront
                            }
                        }
                        false ->
                        {
                            space.layerTiles[SpaceLayer.SelectBack] = tiles.selectPathNotFoundBack
                            space.layerTiles[SpaceLayer.SelectFront] = tiles.selectPathNotFoundFront
                        }
                    }
                    false ->
                    {
                        space.layerTiles[SpaceLayer.SelectBack] = tiles.selectNotMovingBack
                        space.layerTiles[SpaceLayer.SelectFront] = tiles.selectNotMovingFront
                    }
                }
            }
        }

        //Gdx.app.log("room", "updated select tiles")
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
