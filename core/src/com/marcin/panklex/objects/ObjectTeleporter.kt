package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.ui.Cell
import com.marcin.panklex.*
import com.marcin.panklex.moves.MoveThroughTeleportField

class ObjectTeleporter(val teleporterPosition : Vector3) : Object("teleporter")
{
    lateinit var otherTeleporter : ObjectTeleporter
    var isPowered = false

    val fieldPosition = Vector3(teleporterPosition.x, teleporterPosition.y, teleporterPosition.z + 1)

    val fieldPositionUp = Vector3(fieldPosition.x, fieldPosition.y + 1, fieldPosition.z)
    val fieldPositionRight = Vector3(fieldPosition.x + 1, fieldPosition.y, fieldPosition.z)
    val fieldPositionDown = Vector3(fieldPosition.x, fieldPosition.y - 1, fieldPosition.z)
    val fieldPositionLeft = Vector3(fieldPosition.x - 1, fieldPosition.y, fieldPosition.z)

    val throughTeleporterFieldMoveUp = MoveThroughTeleportField(fieldPosition, fieldPositionUp)
    val throughTeleporterFieldMoveRight = MoveThroughTeleportField(fieldPosition, fieldPositionRight)
    val throughTeleporterFieldMoveDown = MoveThroughTeleportField(fieldPosition, fieldPositionDown)
    val throughTeleporterFieldMoveLeft = MoveThroughTeleportField(fieldPosition, fieldPositionLeft)

    val actionPowerTeleport = Action(
        "power teleport",
        "")
    {
        val objectTeleporter = it.mouseObject as ObjectTeleporter
        val entityPlayer = it.level.entityPlayer

        if (entityPlayer.getPlayerItem(PlayerItem.Cell) > 0)
        {
            entityPlayer.changePlayerItem(PlayerItem.Cell, -1)
            objectTeleporter.isPowered = true
            it.updateItemLabels()
        }
        it.room.updateObjectTiles(it.tiles, it.map.mapDirection)
        it.map.updateMap()
    }

    val actionUnpowerTeleport = Action(
        "unpower teleport",
        "")
    {
        val objectTeleporter = it.mouseObject as ObjectTeleporter
        val entityPlayer = it.level.entityPlayer

        entityPlayer.changePlayerItem(PlayerItem.Cell, 1)
        objectTeleporter.isPowered = false
        it.updateItemLabels()
        it.room.updateObjectTiles(it.tiles, it.map.mapDirection)
        it.map.updateMap()
    }

    val actionUseTeleport = Action(
        "use teleport",
        "teleport player")
    {
        val objectTeleporter = it.mouseObject as ObjectTeleporter
        val entityPlayer = it.level.entityPlayer

        entityPlayer.setPosition(otherTeleporter.fieldPosition)
        it.level.updateEntities()
        it.room.updateEntityTiles(it.tiles)
        it.map.updateMap()
    }

    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        positions.add(teleporterPosition)
        positions.add(fieldPosition)
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        positions.add(teleporterPosition)
        positions.add(fieldPosition)
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : MutableMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = null
        spaceLayerTiles[SpaceLayer.SideUp] = null
        spaceLayerTiles[SpaceLayer.Behind] = when (spacePosition)
        {
            fieldPosition -> if (isPowered) tiles.teleporterFieldBehind else null
            else          -> null
        }
        spaceLayerTiles[SpaceLayer.Before] = when (spacePosition)
        {
            fieldPosition -> if (isPowered) tiles.teleporterFieldBefore else null
            else          -> null
        }
        spaceLayerTiles[SpaceLayer.SideDown] = when (spacePosition)
        {
            teleporterPosition -> tiles.teleporterUnpoweredDown
            else               -> null
        }
        spaceLayerTiles[SpaceLayer.SideRight] = when (spacePosition)
        {
            teleporterPosition -> tiles.teleporterUnpoweredRight
            else               -> null
        }
        spaceLayerTiles[SpaceLayer.SideAbove] = when (spacePosition)
        {
            teleporterPosition -> if (isPowered) tiles.teleporterPoweredAbove else tiles.teleporterUnpoweredAbove
            else               -> null
        }
    }

    override fun getSideTransparency(spaceSideTransparency : MutableMap<Direction3d, Boolean>, spacePosition : Vector3)
    {
        when (spacePosition)
        {
            teleporterPosition ->
            {
                spaceSideTransparency[Direction3d.Below] = false
                spaceSideTransparency[Direction3d.Left] = false
                spaceSideTransparency[Direction3d.Up] = false
                spaceSideTransparency[Direction3d.Down] = false
                spaceSideTransparency[Direction3d.Right] = false
                spaceSideTransparency[Direction3d.Above] = false
            }
            fieldPosition      ->
            {
                spaceSideTransparency[Direction3d.Below] = true
                spaceSideTransparency[Direction3d.Left] = true
                spaceSideTransparency[Direction3d.Up] = true
                spaceSideTransparency[Direction3d.Down] = true
                spaceSideTransparency[Direction3d.Right] = true
                spaceSideTransparency[Direction3d.Above] = true
            }
        }
    }

    override fun canStoreEntity(spacePosition : Vector3) : Boolean
    {
        return when (spacePosition)
        {
            teleporterPosition -> false
            else               -> true
        }
    }

    override fun isGround(spacePosition : Vector3) : Boolean
    {
        return when (spacePosition)
        {
            teleporterPosition -> true
            else               -> false
        }
    }

    override fun getMoves(moveList : MutableList<Move>, spacePosition : Vector3, room : Room)
    {
        when (spacePosition)
        {
            fieldPosition ->
            {
                moveList.add(throughTeleporterFieldMoveUp)
                moveList.add(throughTeleporterFieldMoveRight)
                moveList.add(throughTeleporterFieldMoveDown)
                moveList.add(throughTeleporterFieldMoveLeft)
            }
        }
    }

    override fun getActions(actionArray : Array<Action?>, spacePosition : Vector3)
    {
        actionArray[0] = actionPowerTeleport.apply {
            actionDescription = if (isPowered) "( teleporter is powered )" else "( teleporter is not powered )"
            isActionPossible = !isPowered
        }

        actionArray[1] = actionUnpowerTeleport.apply {
            actionDescription = if (isPowered) "( teleporter is powered )" else "( teleporter is not powered )"
            isActionPossible = isPowered
        }

        actionArray[2] = actionUseTeleport.apply {
            actionDescription = if (isPowered) "( teleporter is powered )" else "( teleporter is not powered )"
            isActionPossible = (isPowered && otherTeleporter.isPowered)
        }
    }
}
