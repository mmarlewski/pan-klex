package com.marcin.panklex

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3

class Space(val position : Vector3)
{
    // entities and objects

    var entityOccupying : Entity? = null
    var objectOccupying : Object? = null
    var objectsPresent = mutableListOf<Object>()

    // sides and layers

    val layerTiles = HashMap<SpaceLayer, TiledMapTile?>()
    val sideTransparency = HashMap<Direction3d, Boolean>()
    val sideVisibility = HashMap<Direction3d, Boolean>()

    // border

    var isOnBorder = false
    var isWithinBorder = false

    // spaces

    var spacesAbove = 0
    var spacesBelow = 0

    // ground

    var isGround = false
    var isOnGround = false

    // moves

    val moveList = mutableListOf<Move>()

    val groundMoves = mutableMapOf<Dist1Conn, GroundMove>()
    val fallMove = FallMove(position, Vector3(position.x, position.y, position.z - 1))
    val jump2Moves = mutableMapOf<Dist2Conn, Jump2Move>()
    val jump3Moves = mutableMapOf<Dist3Conn, Jump3Move>()

    // pathfinding

    var isOnPath = false
    var move : Move? = null
    var parentSpace : Space? = null
    var parentMove : Move? = null
    var globalCost = 0
    var localCost = 0

    init
    {
        for (layer in SpaceLayer.values())
        {
            layerTiles[layer] = null
        }

        for (side in Direction3d.values())
        {
            sideTransparency[side] = true
        }

        for (side in Direction3d.values())
        {
            sideVisibility[side] = false
        }

        for (direction in Dist1Conn.values())
        {
            groundMoves[direction] = GroundMove(position, getPositionDist1Conn(position, direction))
        }

        for (direction in Dist2Conn.values())
        {
            jump2Moves[direction] = Jump2Move(position, direction)
        }

        for (direction in Dist3Conn.values())
        {
            jump3Moves[direction] = Jump3Move(position, direction)
        }
    }

    fun isObjectOccupyingNullOrCanStoreEntity(position : Vector3) : Boolean
    {
        val objOccupying = objectOccupying

        return (objOccupying == null || objOccupying.canStoreEntity(position))
    }

    fun getMoves(room : Room) : List<Move>
    {
        moveList.clear()

        // space moves

        if (objectOccupying == null)
        {
            if (isOnGround)
            {
                for (direction in Dist1Conn.values())
                {
                    moveList.add(groundMoves[direction]!!)
                }

                for (direction in Dist2Conn.values())
                {
                    val move = jump2Moves[direction]!!
                    val dist1Space = room.getSpace(move.dist1Position)
                    val dist2Space = room.getSpace(move.dist2Position)

                    if (dist1Space?.isObjectOccupyingNullOrCanStoreEntity(position) != false)
                    {
                        if (dist2Space?.isObjectOccupyingNullOrCanStoreEntity(position) != false)
                        {
                            moveList.add(move)
                        }
                    }
                }

                for (direction in Dist3Conn.values())
                {
                    val move = jump3Moves[direction]!!
                    val dist1Space = room.getSpace(move.dist1Position)
                    val dist2Space = room.getSpace(move.dist2Position)
                    val dist3Space = room.getSpace(move.dist3Position)

                    if (dist1Space?.isObjectOccupyingNullOrCanStoreEntity(position) != false)
                    {
                        if (dist2Space?.isObjectOccupyingNullOrCanStoreEntity(position) != false)
                        {
                            if (dist3Space?.isObjectOccupyingNullOrCanStoreEntity(position) != false)
                            {
                                moveList.add(move)
                            }
                        }
                    }
                }
            }
            else if (spacesBelow > 0)
            {
                moveList.add(fallMove)
            }
        }

        //objects moves

        for (obj in objectsPresent)
        {
            obj.getMoves(moveList, position, room)
        }

        return moveList
    }
}
