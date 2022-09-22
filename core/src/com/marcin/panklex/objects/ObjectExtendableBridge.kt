package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class ObjectExtendableBridge(val originPosition : Vector3, var bridgeDirection : Direction2d, var bridgeLength : Int) :
    Object("extendableBridge")
{
    val extensionPositions = mutableListOf<Vector3>()
    var isExtended = false

    val actionExtendBridge = Action(
        "extend bridge",
        "")
    {
        val objectExtendableBridge = it.mouseObject as ObjectExtendableBridge
        objectExtendableBridge.isExtended = true
        it.level.updateObjects()
        it.level.updateSideTransparency()
        it.level.updateGround()
        it.level.updateSpacesAboveAndBelow()
        it.room.updateObjectTiles(it.tiles, it.map.mapDirection)
        it.map.updateMap()
    }

    val actionFoldBridge = Action(
        "fold bridge",
        "")
    {
        val objectExtendableBridge = it.mouseObject as ObjectExtendableBridge
        objectExtendableBridge.isExtended = false
        it.level.updateObjects()
        it.level.updateSideTransparency()
        it.level.updateGround()
        it.level.updateSpacesAboveAndBelow()
        it.room.updateObjectTiles(it.tiles, it.map.mapDirection)
        it.map.updateMap()
    }

    init
    {
        for (i in 1 until bridgeLength)
        {
            when (bridgeDirection)
            {
                Direction2d.Up    -> extensionPositions.add(
                    Vector3(originPosition.x, originPosition.y + i, originPosition.z))
                Direction2d.Right -> extensionPositions.add(
                    Vector3(originPosition.x + i, originPosition.y, originPosition.z))
                Direction2d.Down  -> extensionPositions.add(
                    Vector3(originPosition.x, originPosition.y - i, originPosition.z))
                Direction2d.Left  -> extensionPositions.add(
                    Vector3(originPosition.x - i, originPosition.y, originPosition.z))
            }
        }
    }

    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        positions.add(originPosition)
        if (isExtended)
        {
            positions.addAll(extensionPositions)
        }
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        positions.add(originPosition)
        if (isExtended)
        {
            positions.addAll(extensionPositions)
        }
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : MutableMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val relativeLadderDirection = objectiveToRelativeDirection2d(bridgeDirection, mapDirection)

        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = null
        spaceLayerTiles[SpaceLayer.SideUp] = null
        spaceLayerTiles[SpaceLayer.Behind] = null
        spaceLayerTiles[SpaceLayer.Before] = tiles.extendableBridgeOriginUpBefore
        spaceLayerTiles[SpaceLayer.SideDown] = when (spacePosition)
        {
            originPosition        -> when (relativeLadderDirection)
            {
                Direction2d.Up, Direction2d.Down    -> tiles.extendableBridgeOriginUpDown
                Direction2d.Right, Direction2d.Left -> tiles.extendableBridgeOriginRightDown
            }
            in extensionPositions -> when (relativeLadderDirection)
            {
                Direction2d.Up, Direction2d.Down    -> tiles.extendableBridgeExtensionUpDown
                Direction2d.Right, Direction2d.Left -> tiles.extendableBridgeExtensionRightDown
            }
            else                  -> null
        }
        spaceLayerTiles[SpaceLayer.SideRight] = when (spacePosition)
        {
            originPosition        -> when (relativeLadderDirection)
            {
                Direction2d.Up, Direction2d.Down    -> tiles.extendableBridgeOriginUpRight
                Direction2d.Right, Direction2d.Left -> tiles.extendableBridgeOriginRightRight
            }
            in extensionPositions -> when (relativeLadderDirection)
            {
                Direction2d.Up, Direction2d.Down    -> tiles.extendableBridgeExtensionUpRight
                Direction2d.Right, Direction2d.Left -> tiles.extendableBridgeExtensionRightRight
            }
            else                  -> null
        }
        spaceLayerTiles[SpaceLayer.SideAbove] = when (spacePosition)
        {
            originPosition        -> when (relativeLadderDirection)
            {
                Direction2d.Up, Direction2d.Down    -> tiles.extendableBridgeOriginUpAbove
                Direction2d.Right, Direction2d.Left -> tiles.extendableBridgeOriginRightAbove
            }
            in extensionPositions -> when (relativeLadderDirection)
            {
                Direction2d.Up, Direction2d.Down    -> tiles.extendableBridgeExtensionUpAbove
                Direction2d.Right, Direction2d.Left -> tiles.extendableBridgeExtensionRightAbove
            }
            else                  -> null
        }
    }

    override fun getSideTransparency(spaceSideTransparency : MutableMap<Direction3d, Boolean>, spacePosition : Vector3)
    {
        spaceSideTransparency[Direction3d.Below] = true
        spaceSideTransparency[Direction3d.Left] = true
        spaceSideTransparency[Direction3d.Up] = true
        spaceSideTransparency[Direction3d.Down] = true
        spaceSideTransparency[Direction3d.Right] = true
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
        if (spacePosition == originPosition)
        {
            actionArray[0] = actionExtendBridge.apply {
                actionDescription = if (isExtended) "( bridge is extended )" else "( bridge is folded )"
                isActionPossible = !isExtended
            }

            actionArray[1] = actionFoldBridge.apply {
                actionDescription = if (isExtended) "( bridge is extended )" else "( bridge is folded )"
                isActionPossible = isExtended
            }
        }
    }
}
