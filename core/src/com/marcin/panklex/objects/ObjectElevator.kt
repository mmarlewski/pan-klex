package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*
import com.marcin.panklex.moves.MoveThroughElevator

enum class ElevatorPartType
{
    Empty, EmptyBroken, Door, DoorBroken, Screen, ScreenBroken
}

class ElevatorPart(val partPosition : Vector3, val partDirection : Direction2d, var partType : ElevatorPartType)
{
    val partPositionUp = Vector3(partPosition.x, partPosition.y + 1, partPosition.z)
    val partPositionRight = Vector3(partPosition.x + 1, partPosition.y, partPosition.z)
    val partPositionDown = Vector3(partPosition.x, partPosition.y - 1, partPosition.z)
    val partPositionLeft = Vector3(partPosition.x - 1, partPosition.y, partPosition.z)

    val throughElevatorMoveUp = MoveThroughElevator(partPosition, partPositionUp)
    val throughElevatorMoveRight = MoveThroughElevator(partPosition, partPositionRight)
    val throughTElevatorMoveDown = MoveThroughElevator(partPosition, partPositionDown)
    val throughElevatorMoveLeft = MoveThroughElevator(partPosition, partPositionLeft)
}

class ElevatorFloor(val elevatorPart : ElevatorPart, val floorName : String, val floorNumber : Int)
{
    var isAccessible : Boolean = false
}

class ObjectElevator(val elevatorParts : List<ElevatorPart>, val elevatorFloors : List<ElevatorFloor>) : Object("elevator")
{
    var currentElevatorFloor = elevatorFloors[0]

    val actionCallElevator = Action(
        "call elevator",
        "( change current floor )")
    {
        val levelPosition = it.levelMousePosition
        val objectElevator = it.mouseObject as ObjectElevator
        val elevatorPart = objectElevator.getElevatorPart(levelPosition)
        val elevatorFloor = objectElevator.getElevatorFloor(levelPosition)
        val entityPlayer = it.level.entityPlayer

        objectElevator.updateFloorsAccessibility()
        if (elevatorFloor != null && elevatorFloor.isAccessible)
        {
            objectElevator.changeCurrentFloor(elevatorFloor)
        }
        it.room.updateObjectTiles(it.tiles, it.map.mapDirection)
        it.map.updateMap()
    }

    val actionSwitchToElevatorScreen = Action(
        "use elevator",
        "( change screen )")
    {
        val levelPosition = it.levelMousePosition
        val objectElevator = it.mouseObject as ObjectElevator
        val elevatorPart = objectElevator.getElevatorPart(levelPosition)
        val elevatorFloor = objectElevator.getElevatorFloor(levelPosition)
        val entityPlayer = it.level.entityPlayer

        objectElevator.updateFloorsAccessibility()
        if (elevatorFloor != null && objectElevator.currentElevatorFloor == elevatorFloor)
        {
            it.game.screenElevator.setElevatorAndPlayer(objectElevator, entityPlayer)
            it.game.screenElevator.updateWidgets()
            it.game.changeScreen(it.game.screenElevator)
        }
    }

    val actionDestroyElevatorPart = Action(
        "destroy elevator part",
        "+1 gear")
    {
        val levelPosition = it.levelMousePosition
        val objectElevator = it.mouseObject as ObjectElevator
        val elevatorPart = objectElevator.getElevatorPart(levelPosition)
        val entityPlayer = it.level.entityPlayer

        if (elevatorPart != null)
        {
            elevatorPart.partType = when (elevatorPart.partType)
            {
                ElevatorPartType.Empty        -> ElevatorPartType.EmptyBroken
                ElevatorPartType.EmptyBroken  -> ElevatorPartType.EmptyBroken
                ElevatorPartType.Door         -> ElevatorPartType.DoorBroken
                ElevatorPartType.DoorBroken   -> ElevatorPartType.DoorBroken
                ElevatorPartType.Screen       -> ElevatorPartType.ScreenBroken
                ElevatorPartType.ScreenBroken -> ElevatorPartType.ScreenBroken
            }
        }
        entityPlayer.changePlayerItem(PlayerItem.Gear, 1)
        objectElevator.updateFloorsAccessibility()
        it.updateItemLabels()
        it.room.updateObjectTiles(it.tiles, it.map.mapDirection)
        it.map.updateMap()
    }

    val actionFixElevatorPart = Action(
        "fix elevator part",
        "-1 gear")
    {
        val levelPosition = it.levelMousePosition
        val objectElevator = it.mouseObject as ObjectElevator
        val elevatorPart = objectElevator.getElevatorPart(levelPosition)
        val entityPlayer = it.level.entityPlayer

        if (elevatorPart != null)
        {
            elevatorPart.partType = when (elevatorPart.partType)
            {
                ElevatorPartType.Empty        -> ElevatorPartType.Empty
                ElevatorPartType.EmptyBroken  -> ElevatorPartType.Empty
                ElevatorPartType.Door         -> ElevatorPartType.Door
                ElevatorPartType.DoorBroken   -> ElevatorPartType.Door
                ElevatorPartType.Screen       -> ElevatorPartType.Screen
                ElevatorPartType.ScreenBroken -> ElevatorPartType.Screen
            }
        }
        entityPlayer.changePlayerItem(PlayerItem.Gear, -1)
        objectElevator.updateFloorsAccessibility()
        it.updateItemLabels()
        it.room.updateObjectTiles(it.tiles, it.map.mapDirection)
        it.map.updateMap()
    }

    fun getElevatorPart(position : Vector3) : ElevatorPart?
    {
        var elevatorPart : ElevatorPart? = null
        for (part in elevatorParts)
        {
            if (part.partPosition == position)
            {
                elevatorPart = part
            }
        }
        return elevatorPart
    }

    fun getElevatorFloor(position : Vector3) : ElevatorFloor?
    {
        var elevatorFloor : ElevatorFloor? = null
        for (floor in elevatorFloors)
        {
            if (floor.elevatorPart.partPosition == position)
            {
                elevatorFloor = floor
            }
        }
        return elevatorFloor
    }

    fun getElevatorFloor(number : Int) : ElevatorFloor?
    {
        var elevatorFloor : ElevatorFloor? = null
        for (floor in elevatorFloors)
        {
            if (floor.floorNumber == number)
            {
                elevatorFloor = floor
            }
        }
        return elevatorFloor
    }

    fun changeCurrentFloor(elevatorFloor : ElevatorFloor)
    {
        currentElevatorFloor = elevatorFloor
    }

    fun updateFloorsAccessibility()
    {
        var highestFloorNumber = currentElevatorFloor.floorNumber
        var lowestFloorNumber = currentElevatorFloor.floorNumber
        for (elevatorFloor in elevatorFloors)
        {
            if (elevatorFloor.floorNumber < lowestFloorNumber)
            {
                lowestFloorNumber = elevatorFloor.floorNumber
            }
            if (elevatorFloor.floorNumber > highestFloorNumber)
            {
                highestFloorNumber = elevatorFloor.floorNumber
            }
        }

        var startchecking = false
        var isPathAccessible = true

        for (part in elevatorParts)
        {
            val position = part.partPosition
            val floor = getElevatorFloor(position)

            if (floor == currentElevatorFloor)
            {
                startchecking = true
            }

            if (startchecking)
            {
                if (!isPathAccessible)
                {
                    floor?.isAccessible = false
                }
                else
                {
                    when (part.partType)
                    {
                        ElevatorPartType.Empty, ElevatorPartType.Door, ElevatorPartType.Screen                   ->
                        {
                            floor?.isAccessible = true
                        }
                        ElevatorPartType.EmptyBroken, ElevatorPartType.DoorBroken, ElevatorPartType.ScreenBroken ->
                        {
                            floor?.isAccessible = false
                            isPathAccessible = false
                        }
                    }
                }
            }
        }

        startchecking = false
        isPathAccessible = true

        for (part in elevatorParts.reversed())
        {
            val position = part.partPosition
            val floor = getElevatorFloor(position)

            if (floor == currentElevatorFloor)
            {
                startchecking = true
            }

            if (startchecking)
            {
                if (!isPathAccessible)
                {
                    floor?.isAccessible = false
                }
                else
                {
                    when (part.partType)
                    {
                        ElevatorPartType.Empty, ElevatorPartType.Door, ElevatorPartType.Screen                   ->
                        {
                            floor?.isAccessible = true
                        }
                        ElevatorPartType.EmptyBroken, ElevatorPartType.DoorBroken, ElevatorPartType.ScreenBroken ->
                        {
                            floor?.isAccessible = false
                            isPathAccessible = false
                        }
                    }
                }
            }
        }
    }

    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        for (part in elevatorParts)
        {
            positions.add(part.partPosition)
        }
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        for (part in elevatorParts)
        {
            positions.add(part.partPosition)
        }
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : MutableMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val elevatorPart = getElevatorPart(spacePosition)
        val partDirection = elevatorPart?.partDirection
        val partType = elevatorPart?.partType
        val relativePartDirection =
            if (partDirection != null) objectiveToRelativeDirection2d(partDirection, mapDirection) else null

        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = tiles.elevatorLeft
        spaceLayerTiles[SpaceLayer.SideUp] = tiles.elevatorUp
        spaceLayerTiles[SpaceLayer.Behind] = null
        spaceLayerTiles[SpaceLayer.Before] = null
        spaceLayerTiles[SpaceLayer.SideDown] = when (relativePartDirection)
        {
            Direction2d.Down -> when (partType)
            {
                ElevatorPartType.Empty        -> tiles.elevatorEmptyDown
                ElevatorPartType.EmptyBroken  -> tiles.elevatorEmptyBrokenDown
                ElevatorPartType.Door         -> tiles.elevatorDoorDown
                ElevatorPartType.DoorBroken   -> tiles.elevatorDoorBrokenDown
                ElevatorPartType.Screen       -> when (currentElevatorFloor.floorNumber)
                {
                    1    -> tiles.elevatorScreen1Down
                    2    -> tiles.elevatorScreen2Down
                    3    -> tiles.elevatorScreen3Down
                    4    -> tiles.elevatorScreen4Down
                    5    -> tiles.elevatorScreen5Down
                    6    -> tiles.elevatorScreen6Down
                    7    -> tiles.elevatorScreen7Down
                    8    -> tiles.elevatorScreen8Down
                    9    -> tiles.elevatorScreen9Down
                    else -> tiles.elevatorScreenBlankDown
                }
                ElevatorPartType.ScreenBroken -> tiles.elevatorScreenBrokenDown
                else                          -> null
            }
            else             -> when (partType)
            {
                ElevatorPartType.Empty, ElevatorPartType.Door, ElevatorPartType.Screen -> tiles.elevatorEmptyDown
                else                                                                   -> tiles.elevatorEmptyBrokenDown
            }
        }
        spaceLayerTiles[SpaceLayer.SideRight] = when (relativePartDirection)
        {
            Direction2d.Right -> when (partType)
            {
                ElevatorPartType.Empty        -> tiles.elevatorEmptyRight
                ElevatorPartType.EmptyBroken  -> tiles.elevatorEmptyBrokenRight
                ElevatorPartType.Door         -> tiles.elevatorDoorRight
                ElevatorPartType.DoorBroken   -> tiles.elevatorDoorBrokenRight
                ElevatorPartType.Screen       -> when (currentElevatorFloor.floorNumber)
                {
                    1    -> tiles.elevatorScreen1Right
                    2    -> tiles.elevatorScreen2Right
                    3    -> tiles.elevatorScreen3Right
                    4    -> tiles.elevatorScreen4Right
                    5    -> tiles.elevatorScreen5Right
                    6    -> tiles.elevatorScreen6Right
                    7    -> tiles.elevatorScreen7Right
                    8    -> tiles.elevatorScreen8Right
                    9    -> tiles.elevatorScreen9Right
                    else -> tiles.elevatorScreenBlankRight
                }
                ElevatorPartType.ScreenBroken -> tiles.elevatorScreenBrokenRight
                else                          -> null
            }
            else              -> when (partType)
            {
                ElevatorPartType.Empty, ElevatorPartType.Door, ElevatorPartType.Screen -> tiles.elevatorEmptyRight
                else                                                                   -> tiles.elevatorEmptyBrokenRight
            }
        }
        spaceLayerTiles[SpaceLayer.SideAbove] = null
    }

    override fun getSideTransparency(spaceSideTransparency : MutableMap<Direction3d, Boolean>, spacePosition : Vector3)
    {
        spaceSideTransparency[Direction3d.Below] = true
        spaceSideTransparency[Direction3d.Left] = false
        spaceSideTransparency[Direction3d.Up] = false
        spaceSideTransparency[Direction3d.Down] = false
        spaceSideTransparency[Direction3d.Right] = false
        spaceSideTransparency[Direction3d.Above] = true
    }

    override fun canStoreEntity(spacePosition : Vector3) : Boolean
    {
        return true
    }

    override fun isGround(spacePosition : Vector3) : Boolean
    {
        return false
    }

    override fun getMoves(moveList : MutableList<Move>, spacePosition : Vector3, room : Room)
    {
        for (part in elevatorParts)
        {
            if (spacePosition==part.partPosition)
            {
                moveList.add(part.throughElevatorMoveUp)
                moveList.add(part.throughElevatorMoveRight)
                moveList.add(part.throughTElevatorMoveDown)
                moveList.add(part.throughElevatorMoveLeft)
            }
        }
    }

    override fun getActions(actionArray : Array<Action?>, spacePosition : Vector3)
    {
        updateFloorsAccessibility()
        val elevatorPart = getElevatorPart(spacePosition)
        val elevatorFloor = getElevatorFloor(spacePosition)

        if (elevatorPart != null)
        {
            actionArray[0] = actionCallElevator.apply {
                isActionPossible = (elevatorFloor != null && elevatorFloor.isAccessible)
            }
            actionArray[1] = actionSwitchToElevatorScreen.apply {
                isActionPossible = (currentElevatorFloor == elevatorFloor)
            }
            actionArray[2] = actionDestroyElevatorPart.apply {
                isActionPossible = when (elevatorPart.partType)
                {
                    ElevatorPartType.Empty, ElevatorPartType.Door, ElevatorPartType.Screen                   -> true
                    ElevatorPartType.EmptyBroken, ElevatorPartType.DoorBroken, ElevatorPartType.ScreenBroken -> false
                }
            }
            actionArray[3] = actionFixElevatorPart.apply {
                isActionPossible = when (elevatorPart.partType)
                {
                    ElevatorPartType.Empty, ElevatorPartType.Door, ElevatorPartType.Screen                   -> false
                    ElevatorPartType.EmptyBroken, ElevatorPartType.DoorBroken, ElevatorPartType.ScreenBroken -> true
                }
            }
        }
    }
}
