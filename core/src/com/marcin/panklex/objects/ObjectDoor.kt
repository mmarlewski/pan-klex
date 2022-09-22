package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class ObjectDoor(val doorPosition : Vector3, val doorDirection : Direction2d) : Object("door")
{
    val exitPosition = when (doorDirection)
    {
        Direction2d.Up    -> Vector3(doorPosition.x, doorPosition.y - 1, doorPosition.z)
        Direction2d.Right -> Vector3(doorPosition.x - 1, doorPosition.y, doorPosition.z)
        Direction2d.Down  -> Vector3(doorPosition.x, doorPosition.y + 1, doorPosition.z)
        Direction2d.Left  -> Vector3(doorPosition.x + 1, doorPosition.y, doorPosition.z)
    }

    val actionUseDoor = Action(
        "use door",
        "change room")
    {
        val entityPlayer = it.level.entityPlayer

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
        println(entityPlayer.playerPosition)
    }

    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        positions.add(doorPosition)
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        positions.add(doorPosition)
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : MutableMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val relativeDoorDirection = objectiveToRelativeDirection2d(doorDirection, mapDirection)

        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = when (relativeDoorDirection)
        {
            Direction2d.Left -> tiles.doorDoorLeft
            else             -> null
        }
        spaceLayerTiles[SpaceLayer.SideUp] = when (relativeDoorDirection)
        {
            Direction2d.Up -> tiles.doorDoorUp
            else           -> null
        }
        spaceLayerTiles[SpaceLayer.Behind] = tiles.doorBlankBehind
        spaceLayerTiles[SpaceLayer.Before] = tiles.doorBlankBefore
        spaceLayerTiles[SpaceLayer.SideDown] = when (relativeDoorDirection)
        {
            Direction2d.Down -> tiles.doorDoorDown
            else             -> tiles.doorBlankDown
        }
        spaceLayerTiles[SpaceLayer.SideRight] = when (relativeDoorDirection)
        {
            Direction2d.Right -> tiles.doorDoorRight
            else              -> tiles.doorBlankRight
        }
        spaceLayerTiles[SpaceLayer.SideAbove] = tiles.doorDoorAbove
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
        actionArray[0] = actionUseDoor.apply { isActionPossible = true }
    }
}
