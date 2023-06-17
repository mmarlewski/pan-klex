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
    var entityPlayer = level.entityPlayer

    val selectPosition = Vector3()

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

            if (objectOccupying != null && space.isOnBorder)
            {
                var isAboveUpLine = false
                var isAboveRightLine = false
                var isAboveDownLine = false
                var isAboveLeftLine = false

                var isLeftAboveLine = false
                var isLeftUpLine = false
                var isLeftBelowLine = false
                var isLeftDownLine = false

                var isUpAboveLine = false
                var isUpRightLine = false
                var isUpBelowLine = false
                var isUpLeftLine = false

                var isDownAboveLine = false
                var isDownRightLine = false
                var isDownBelowLine = false
                var isDownLeftLine = false

                var isRightAboveLine = false
                var isRightUpLine = false
                var isRightBelowLine = false
                var isRightDownLine = false

                var isBelowUpLine = false
                var isBelowRightLine = false
                var isBelowDownLine = false
                var isBelowLeftLine = false

                val aboveSpace = getSpace(space.position, Direction3d.Above)
                val upSpace = getSpace(space.position, Direction3d.Up)
                val rightSpace = getSpace(space.position, Direction3d.Right)
                val downSpace = getSpace(space.position, Direction3d.Down)
                val leftSpace = getSpace(space.position, Direction3d.Left)
                val belowSpace = getSpace(space.position, Direction3d.Below)

                if (aboveSpace != null && !aboveSpace.isOnBorder)
                {
                    val aboveUpSpace = getSpace(aboveSpace.position, Direction3d.Up)
                    if (aboveUpSpace == null || aboveUpSpace.isOnBorder ||
                        (upSpace != null && !upSpace.isOnBorder)
                    ) isAboveUpLine = true

                    val aboveRightSpace = getSpace(aboveSpace.position, Direction3d.Right)
                    if (aboveRightSpace == null || aboveRightSpace.isOnBorder ||
                        (rightSpace != null && !rightSpace.isOnBorder)
                    ) isAboveRightLine = true

                    val aboveDownSpace = getSpace(aboveSpace.position, Direction3d.Down)
                    if (aboveDownSpace == null || aboveDownSpace.isOnBorder ||
                        (downSpace != null && !downSpace.isOnBorder)
                    ) isAboveDownLine = true

                    val aboveLeftSpace = getSpace(aboveSpace.position, Direction3d.Left)
                    if (aboveLeftSpace == null || aboveLeftSpace.isOnBorder ||
                        (leftSpace != null && !leftSpace.isOnBorder)
                    ) isAboveLeftLine = true
                }

                if (leftSpace != null && !leftSpace.isOnBorder)
                {
                    val leftAboveSpace = getSpace(leftSpace.position, Direction3d.Above)
                    if (leftAboveSpace == null || leftAboveSpace.isOnBorder ||
                        (aboveSpace != null && !aboveSpace.isOnBorder)
                    ) isLeftAboveLine = true

                    val leftUpSpace = getSpace(leftSpace.position, Direction3d.Up)
                    if (leftUpSpace == null || leftUpSpace.isOnBorder ||
                        (upSpace != null && !upSpace.isOnBorder)
                    ) isLeftUpLine = true

                    val leftBelowSpace = getSpace(leftSpace.position, Direction3d.Below)
                    if (leftBelowSpace == null || leftBelowSpace.isOnBorder ||
                        (belowSpace != null && !belowSpace.isOnBorder)
                    ) isLeftBelowLine = true

                    val leftDownSpace = getSpace(leftSpace.position, Direction3d.Down)
                    if (leftDownSpace == null || leftDownSpace.isOnBorder ||
                        (downSpace != null && !downSpace.isOnBorder)
                    ) isLeftDownLine = true
                }

                if (upSpace != null && !upSpace.isOnBorder)
                {
                    val upAboveSpace = getSpace(upSpace.position, Direction3d.Above)
                    if (upAboveSpace == null || upAboveSpace.isOnBorder ||
                        (aboveSpace != null && !aboveSpace.isOnBorder)
                    ) isUpAboveLine = true

                    val upRightSpace = getSpace(upSpace.position, Direction3d.Right)
                    if (upRightSpace == null || upRightSpace.isOnBorder ||
                        (rightSpace != null && !rightSpace.isOnBorder)
                    ) isUpRightLine = true

                    val upBelowSpace = getSpace(upSpace.position, Direction3d.Below)
                    if (upBelowSpace == null || upBelowSpace.isOnBorder ||
                        (belowSpace != null && !belowSpace.isOnBorder)
                    ) isUpBelowLine = true

                    val upLeftSpace = getSpace(upSpace.position, Direction3d.Left)
                    if (upLeftSpace == null || upLeftSpace.isOnBorder ||
                        (leftSpace != null && !leftSpace.isOnBorder)
                    ) isUpLeftLine = true
                }

                if (downSpace != null && !downSpace.isOnBorder)
                {
                    val downAboveSpace = getSpace(downSpace.position, Direction3d.Above)
                    if (downAboveSpace == null || downAboveSpace.isOnBorder ||
                        (aboveSpace != null && !aboveSpace.isOnBorder)
                    ) isDownAboveLine = true

                    val downRightSpace = getSpace(downSpace.position, Direction3d.Right)
                    if (downRightSpace == null || downRightSpace.isOnBorder ||
                        (rightSpace != null && !rightSpace.isOnBorder)
                    ) isDownRightLine = true

                    val downBelowSpace = getSpace(downSpace.position, Direction3d.Below)
                    if (downBelowSpace == null || downBelowSpace.isOnBorder ||
                        (belowSpace != null && !belowSpace.isOnBorder)
                    ) isDownBelowLine = true

                    val downLeftSpace = getSpace(downSpace.position, Direction3d.Left)
                    if (downLeftSpace == null || downLeftSpace.isOnBorder ||
                        (leftSpace != null && !leftSpace.isOnBorder)
                    ) isDownLeftLine = true
                }

                if (rightSpace != null && !rightSpace.isOnBorder)
                {
                    val rightAboveSpace = getSpace(rightSpace.position, Direction3d.Above)
                    if (rightAboveSpace == null || rightAboveSpace.isOnBorder ||
                        (aboveSpace != null && !aboveSpace.isOnBorder)
                    ) isRightAboveLine = true

                    val rightUpSpace = getSpace(rightSpace.position, Direction3d.Up)
                    if (rightUpSpace == null || rightUpSpace.isOnBorder ||
                        (upSpace != null && !upSpace.isGround)
                    ) isRightUpLine = true

                    val rightBelowSpace = getSpace(rightSpace.position, Direction3d.Below)
                    if (rightBelowSpace == null || rightBelowSpace.isOnBorder ||
                        (belowSpace != null && !belowSpace.isOnBorder)
                    ) isRightBelowLine = true

                    val rightDownSpace = getSpace(rightSpace.position, Direction3d.Down)
                    if (rightDownSpace == null || rightDownSpace.isOnBorder ||
                        (downSpace != null && !downSpace.isOnBorder)
                    ) isRightDownLine = true
                }

                if (belowSpace != null && !belowSpace.isOnBorder)
                {
                    val belowUpSpace = getSpace(belowSpace.position, Direction3d.Up)
                    if (belowUpSpace == null || belowUpSpace.isOnBorder ||
                        (upSpace != null && !upSpace.isGround)
                    ) isBelowUpLine = true

                    val belowRightSpace = getSpace(belowSpace.position, Direction3d.Right)
                    if (belowRightSpace == null || belowRightSpace.isOnBorder ||
                        (rightSpace != null && !rightSpace.isOnBorder)
                    ) isBelowRightLine = true

                    val belowDownSpace = getSpace(belowSpace.position, Direction3d.Down)
                    if (belowDownSpace == null || belowDownSpace.isOnBorder ||
                        (downSpace != null && !downSpace.isOnBorder)
                    ) isBelowDownLine = true

                    val belowLeftSpace = getSpace(belowSpace.position, Direction3d.Left)
                    if (belowLeftSpace == null || belowLeftSpace.isOnBorder ||
                        (leftSpace != null && !leftSpace.isOnBorder)
                    ) isBelowLeftLine = true
                }

                space.layerTiles[SpaceLayer.LinesLeft] = when (mapDirection)
                {
                    Direction2d.Up    -> tiles.getLeft(isLeftAboveLine, isLeftUpLine, isLeftBelowLine, isLeftDownLine)
                    Direction2d.Right -> tiles.getLeft(isUpAboveLine, isUpRightLine, isUpBelowLine, isUpLeftLine)
                    Direction2d.Down  -> tiles.getLeft(isRightAboveLine, isRightDownLine, isRightBelowLine, isRightUpLine)
                    Direction2d.Left  -> tiles.getLeft(isDownAboveLine, isDownLeftLine, isDownBelowLine, isDownRightLine)
                }

                space.layerTiles[SpaceLayer.LinesUp] = when (mapDirection)
                {
                    Direction2d.Up    -> tiles.getUp(isUpAboveLine, isUpRightLine, isUpBelowLine, isUpLeftLine)
                    Direction2d.Right -> tiles.getUp(isRightAboveLine, isRightDownLine, isRightBelowLine, isRightUpLine)
                    Direction2d.Down  -> tiles.getUp(isDownAboveLine, isDownLeftLine, isDownBelowLine, isDownRightLine)
                    Direction2d.Left  -> tiles.getUp(isLeftAboveLine, isLeftUpLine, isLeftBelowLine, isLeftDownLine)
                }

                space.layerTiles[SpaceLayer.LinesBelow] = when (mapDirection)
                {
                    Direction2d.Up    -> tiles.getBelow(isBelowUpLine, isBelowRightLine, isBelowDownLine, isBelowLeftLine)
                    Direction2d.Right -> tiles.getBelow(isBelowRightLine, isBelowDownLine, isBelowLeftLine, isBelowUpLine)
                    Direction2d.Down  -> tiles.getBelow(isBelowDownLine, isBelowLeftLine, isBelowUpLine, isBelowRightLine)
                    Direction2d.Left  -> tiles.getBelow(isBelowLeftLine, isBelowUpLine, isBelowRightLine, isBelowDownLine)
                }

                /*
                if (space.position.x == 29f && space.position.y == 21f && space.position.z == 7f)
                {
                    Gdx.app.log("lines above up", isAboveUpLine.toString())
                    Gdx.app.log("lines above right", isAboveRightLine.toString())
                    Gdx.app.log("lines above down", isAboveDownLine.toString())
                    Gdx.app.log("lines above left", isAboveUpLine.toString())

                    Gdx.app.log("lines left above", isLeftAboveLine.toString())
                    Gdx.app.log("lines left up", isLeftUpLine.toString())
                    Gdx.app.log("lines left below", isLeftBelowLine.toString())
                    Gdx.app.log("lines left down", isLeftDownLine.toString())

                    Gdx.app.log("lines up above", isUpAboveLine.toString())
                    Gdx.app.log("lines up right", isUpRightLine.toString())
                    Gdx.app.log("lines up below", isUpBelowLine.toString())
                    Gdx.app.log("lines up left", isUpLeftLine.toString())

                    Gdx.app.log("lines down above", isDownAboveLine.toString())
                    Gdx.app.log("lines down right", isDownRightLine.toString())
                    Gdx.app.log("lines down below", isDownBelowLine.toString())
                    Gdx.app.log("lines down left", isDownLeftLine.toString())

                    Gdx.app.log("lines right above", isRightAboveLine.toString())
                    Gdx.app.log("lines right up", isRightUpLine.toString())
                    Gdx.app.log("lines right below", isRightBelowLine.toString())
                    Gdx.app.log("lines right down", isRightDownLine.toString())

                    Gdx.app.log("lines below up", isBelowUpLine.toString())
                    Gdx.app.log("lines below right", isBelowRightLine.toString())
                    Gdx.app.log("lines below down", isBelowDownLine.toString())
                    Gdx.app.log("lines below left", isBelowLeftLine.toString())
                }
                 */
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

    fun updateLineTiles(tiles : Tiles, linePositions : MutableList<Vector3>)
    {
        Gdx.app.log("room", "updating line tiles...")

        for (space in roomSpaces)
        {
            if (space.position in linePositions)
            {
                space.layerTiles[SpaceLayer.LineBack] = tiles.lineBack
                space.layerTiles[SpaceLayer.LineFront] = tiles.lineFront
            }
            else
            {
                space.layerTiles[SpaceLayer.LineBack] = null
                space.layerTiles[SpaceLayer.LineFront] = null
            }
        }

        Gdx.app.log("room", "updated line tiles")
    }

    fun updateSelectTiles(
            tiles : Tiles, newSelectPosition : Vector3, isMoving : Boolean, isPathFound : Boolean, isMoveAccessible : Boolean
    )
    {
        //Gdx.app.log("room", "updating select tiles...")

        val selectSpace = getSpace(selectPosition)
        val newSelectSpace = getSpace(newSelectPosition)

        if (selectSpace != null)
        {
            selectSpace.layerTiles[SpaceLayer.SelectBack] = null
            selectSpace.layerTiles[SpaceLayer.SelectFront] = null
        }

        if (newSelectSpace != null)
        {
            when (isMoving)
            {
                true  -> when (isPathFound)
                {
                    true  -> when (isMoveAccessible)
                    {
                        true  ->
                        {
                            newSelectSpace.layerTiles[SpaceLayer.SelectBack] = tiles.selectPathFoundBack
                            newSelectSpace.layerTiles[SpaceLayer.SelectFront] = tiles.selectPathFoundFront
                        }
                        false ->
                        {
                            newSelectSpace.layerTiles[SpaceLayer.SelectBack] = tiles.selectInaccessibleBack
                            newSelectSpace.layerTiles[SpaceLayer.SelectFront] = tiles.selectInaccessibleFront
                        }
                    }
                    false ->
                    {
                        newSelectSpace.layerTiles[SpaceLayer.SelectBack] = tiles.selectPathNotFoundBack
                        newSelectSpace.layerTiles[SpaceLayer.SelectFront] = tiles.selectPathNotFoundFront
                    }
                }
                false ->
                {
                    newSelectSpace.layerTiles[SpaceLayer.SelectBack] = tiles.selectNotMovingBack
                    newSelectSpace.layerTiles[SpaceLayer.SelectFront] = tiles.selectNotMovingFront
                }
            }
        }

        selectPosition.set(newSelectPosition)

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
