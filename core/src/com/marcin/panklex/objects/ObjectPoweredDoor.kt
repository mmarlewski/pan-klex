package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class ObjectPoweredDoor(val poweredDoorPosition : Vector3, val poweredDoorDirection : Direction2d) : Object("poweredDoor")
{
    val exitPosition = when (poweredDoorDirection)
    {
        Direction2d.Up    -> Vector3(poweredDoorPosition.x, poweredDoorPosition.y - 1, poweredDoorPosition.z)
        Direction2d.Right -> Vector3(poweredDoorPosition.x - 1, poweredDoorPosition.y, poweredDoorPosition.z)
        Direction2d.Down  -> Vector3(poweredDoorPosition.x, poweredDoorPosition.y + 1, poweredDoorPosition.z)
        Direction2d.Left  -> Vector3(poweredDoorPosition.x + 1, poweredDoorPosition.y, poweredDoorPosition.z)
    }
    var isPowered = false

    val actionPowerDoor = Action(
        "power door",
        "")
    {
        val objectPoweredDoor = it.mouseObject as ObjectPoweredDoor
        val entityPlayer = it.level.entityPlayer

        if (entityPlayer.getPlayerItem(PlayerItem.Cell) > 0)
        {
            entityPlayer.changePlayerItem(PlayerItem.Cell, -1)
            objectPoweredDoor.isPowered = true
            it.updateItemLabels()
        }
        it.room.updateObjectTiles(it.tiles, it.map.mapDirection)
        it.map.updateMap()
    }

    val actionUnpowerDoor = Action(
        "unpower door",
        "")
    {
        val objectPoweredDoor = it.mouseObject as ObjectPoweredDoor
        val entityPlayer = it.level.entityPlayer

        entityPlayer.changePlayerItem(PlayerItem.Cell, 1)
        objectPoweredDoor.isPowered = false
        it.updateItemLabels()
        it.room.updateObjectTiles(it.tiles, it.map.mapDirection)
        it.map.updateMap()
    }

    val actionUseDoor = Action(
        "use door",
        "change room")
    {
        val objectPoweredDoor = it.mouseObject as ObjectPoweredDoor
        val entityPlayer = it.level.entityPlayer

        if (isPowered)
        {
            entityPlayer.setPosition(exitPosition)
            it.level.updateEntities()
            it.level.updateObjects()
            it.level.updateSideTransparency()
            it.level.updateGround()
            it.level.updateSpacesAboveAndBelow()
            it.level.clearSideVisibility()
            it.level.clearBorder()
            it.room.updateRoom(it.level.entityPlayer.playerPosition)
            it.room.updateEntityTiles(it.tiles)
            it.room.updateObjectTiles(it.tiles, it.map.mapDirection)
            it.room.updateLinesTiles(it.tiles, it.map.mapDirection)
            it.map.updateMap()
        }
    }

    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        positions.add(poweredDoorPosition)
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        positions.add(poweredDoorPosition)
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : MutableMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val relativePoweredDoorDirection = objectiveToRelativeDirection2d(poweredDoorDirection, mapDirection)

        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = when (relativePoweredDoorDirection)
        {
            Direction2d.Left -> when (isPowered)
            {
                true  -> tiles.poweredDoorPoweredLeft
                false -> tiles.poweredDoorUnpoweredLeft
            }
            else             -> null
        }
        spaceLayerTiles[SpaceLayer.SideUp] = when (relativePoweredDoorDirection)
        {
            Direction2d.Up -> when (isPowered)
            {
                true  -> tiles.poweredDoorPoweredUp
                false -> tiles.poweredDoorUnpoweredUp
            }
            else           -> null
        }
        spaceLayerTiles[SpaceLayer.Behind] = tiles.poweredDoorBlankBehind
        spaceLayerTiles[SpaceLayer.Before] = tiles.poweredDoorBlankBefore
        spaceLayerTiles[SpaceLayer.SideDown] = when (relativePoweredDoorDirection)
        {
            Direction2d.Down -> when (isPowered)
            {
                true  -> tiles.poweredDoorPoweredDown
                false -> tiles.poweredDoorUnpoweredDown
            }
            else             -> tiles.poweredDoorBlankDown
        }
        spaceLayerTiles[SpaceLayer.SideRight] = when (relativePoweredDoorDirection)
        {
            Direction2d.Right -> when (isPowered)
            {
                true  -> tiles.poweredDoorPoweredRight
                false -> tiles.poweredDoorUnpoweredRight
            }
            else              -> tiles.poweredDoorBlankRight
        }
        spaceLayerTiles[SpaceLayer.SideAbove] = tiles.poweredDoorBlankAbove
    }

    override fun getSideTransparency(spaceSideTransparency : MutableMap<Direction3d, Boolean>, spacePosition : Vector3)
    {
        spaceSideTransparency[Direction3d.Below] = false
        spaceSideTransparency[Direction3d.Left] = false
        spaceSideTransparency[Direction3d.Up] = false
        spaceSideTransparency[Direction3d.Down] = false
        spaceSideTransparency[Direction3d.Right] = false
        spaceSideTransparency[Direction3d.Above] = false
    }

    override fun canStoreEntity(spacePosition : Vector3) : Boolean
    {
        return false
    }

    override fun isGround(spacePosition : Vector3) : Boolean
    {
        return true
    }

    override fun getMoves(moveList : MutableList<Move>, spacePosition : Vector3, room : Room)
    {
        //
    }

    override fun getActions(actionArray : Array<Action?>, spacePosition : Vector3)
    {
        actionArray[0] = actionPowerDoor.apply {
            actionDescription = if (isPowered) "door is powered" else "door is unpowered"
            isActionPossible = !isPowered
        }
        actionArray[1] = actionUnpowerDoor.apply {
            actionDescription = if (isPowered) "door is powered" else "door is unpowered"
            isActionPossible = isPowered
        }
        actionArray[2] = actionUseDoor.apply {
            isActionPossible = isPowered
        }
    }
}
