package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

enum class ElevatorShaftType
{
    Empty, EmptyBroken, Door, DoorBroken, Screen, ScreenBroken
}

class ObjectElevator(
    originPosition : Vector3, val height : Int, val directions : List<Direction2d>,
    val types : List<ElevatorShaftType>) : Object("elevator")
{
    val shaftPositions = mutableListOf<Vector3>()
    val shaftDirections = mutableMapOf<Vector3, Direction2d>()
    val shaftTypes = mutableMapOf<Vector3, ElevatorShaftType>()

    init
    {
        shaftPositions.add(originPosition)
        shaftDirections[originPosition] = directions[0]
        shaftTypes[originPosition] = types[0]

        for (i in 1 until height)
        {
            shaftPositions.add(Vector3(originPosition.x, originPosition.y, originPosition.z - i))
            shaftDirections[shaftPositions[i]] = directions[i]
            shaftTypes[shaftPositions[i]] = types[i]
        }
    }

    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        positions.addAll(shaftPositions)
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        positions.addAll(shaftPositions)
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : HashMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val relativeShaftDirection = objectiveToRelativeDirection2d(shaftDirections[spacePosition]!!, mapDirection)

        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = null
        spaceLayerTiles[SpaceLayer.SideUp] = null
        spaceLayerTiles[SpaceLayer.Behind] = null
        spaceLayerTiles[SpaceLayer.Before] = null
        spaceLayerTiles[SpaceLayer.SideDown] = when (relativeShaftDirection)
        {
            Direction2d.Down -> when (shaftTypes[spacePosition])
            {
                ElevatorShaftType.Empty        -> tiles.elevatorEmptyDown
                ElevatorShaftType.EmptyBroken  -> tiles.elevatorEmptyBrokenDown
                ElevatorShaftType.Door         -> tiles.elevatorDoorDown
                ElevatorShaftType.DoorBroken   -> tiles.elevatorDoorBrokenDown
                ElevatorShaftType.Screen       -> tiles.elevatorScreenBlankDown
                ElevatorShaftType.ScreenBroken -> tiles.elevatorScreenBrokenDown
                else                           -> null
            }
            else             -> when (shaftTypes[spacePosition])
            {
                ElevatorShaftType.Empty, ElevatorShaftType.Door, ElevatorShaftType.Screen -> tiles.elevatorEmptyDown
                else                                                                      -> tiles.elevatorEmptyBrokenDown
            }
        }
        spaceLayerTiles[SpaceLayer.SideRight] = when (relativeShaftDirection)
        {
            Direction2d.Right -> when (shaftTypes[spacePosition])
            {
                ElevatorShaftType.Empty        -> tiles.elevatorEmptyRight
                ElevatorShaftType.EmptyBroken  -> tiles.elevatorEmptyBrokenRight
                ElevatorShaftType.Door         -> tiles.elevatorDoorRight
                ElevatorShaftType.DoorBroken   -> tiles.elevatorDoorBrokenRight
                ElevatorShaftType.Screen       -> tiles.elevatorScreenBlankRight
                ElevatorShaftType.ScreenBroken -> tiles.elevatorScreenBrokenRight
                else                           -> null
            }
            else              -> when (shaftTypes[spacePosition])
            {
                ElevatorShaftType.Empty, ElevatorShaftType.Door, ElevatorShaftType.Screen -> tiles.elevatorEmptyRight
                else                                                                      -> tiles.elevatorEmptyBrokenRight
            }
        }
        spaceLayerTiles[SpaceLayer.SideAbove] = null
    }

    override fun getSideTransparency(spaceSideTransparency : HashMap<Direction3d, Boolean>, spacePosition : Vector3)
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
        return false
    }

    override fun isGround(spacePosition : Vector3) : Boolean
    {
        return false
    }

    override fun getMoves(moveList : MutableList<Move>, spacePosition : Vector3, room : Room)
    {
        //
    }
}
