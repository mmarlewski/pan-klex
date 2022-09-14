package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class ObjectTeleporter(val teleporterPosition : Vector3) : Object("teleporter")
{
    val fieldPosition = Vector3(teleporterPosition.x, teleporterPosition.y, teleporterPosition.z + 1)

    val fieldPositionUp = Vector3(fieldPosition.x, fieldPosition.y + 1, fieldPosition.z)
    val fieldPositionRight = Vector3(fieldPosition.x + 1, fieldPosition.y, fieldPosition.z)
    val fieldPositionDown = Vector3(fieldPosition.x, fieldPosition.y - 1, fieldPosition.z)
    val fieldPositionLeft = Vector3(fieldPosition.x - 1, fieldPosition.y, fieldPosition.z)

    val throughTeleporterFieldMoveUp = ThroughTeleporterFieldMove(fieldPosition, fieldPositionUp)
    val throughTeleporterFieldMoveRight = ThroughTeleporterFieldMove(fieldPosition, fieldPositionRight)
    val throughTeleporterFieldMoveDown = ThroughTeleporterFieldMove(fieldPosition, fieldPositionDown)
    val throughTeleporterFieldMoveLeft = ThroughTeleporterFieldMove(fieldPosition, fieldPositionLeft)

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
        tiles : Tiles, spaceLayerTiles : HashMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = null
        spaceLayerTiles[SpaceLayer.SideUp] = null
        spaceLayerTiles[SpaceLayer.Behind] = when (spacePosition)
        {
            fieldPosition -> tiles.teleporterFieldBehind
            else          -> null
        }
        spaceLayerTiles[SpaceLayer.Before] = when (spacePosition)
        {
            fieldPosition -> tiles.teleporterFieldBefore
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
            teleporterPosition -> tiles.teleporterPoweredAbove
            else               -> null
        }
    }

    override fun getSideTransparency(spaceSideTransparency : HashMap<Direction3d, Boolean>, spacePosition : Vector3)
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
}
