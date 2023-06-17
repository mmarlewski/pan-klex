package com.marcin.panklex

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.moves.MoveFall
import com.marcin.panklex.moves.MoveGround
import com.marcin.panklex.moves.MoveJumpDist2
import com.marcin.panklex.moves.MoveJumpDist3

class Space(val position : Vector3)
{
    // entities and objects

    var entityOccupying : Entity? = null
    var objectOccupying : Object? = null
    var objectsPresent = mutableListOf<Object>()

    // sides and layers

    val layerTiles = mutableMapOf<SpaceLayer, TiledMapTile?>().apply {
        for (layer in SpaceLayer.values())
            this[layer] = null
    }
    val sideTransparency = mutableMapOf<Direction3d, Boolean>().apply {
        for (direction in Direction3d.values())
            this[direction] = true
    }
    val sideVisibility = mutableMapOf<Direction3d, Boolean>().apply {
        for (direction in Direction3d.values())
            this[direction] = false
    }

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

    val groundMoves = mutableMapOf<Dist1Conn, MoveGround>().apply {
        for (direction in Dist1Conn.values())
            this[direction] = MoveGround(position, getPositionDist1Conn(position, direction))
    }
    val fallMove = MoveFall(position, Vector3(position.x, position.y, position.z - 1))
    val jump2Moves = mutableMapOf<Dist2Conn, MoveJumpDist2>().apply {
        for (direction in Dist2Conn.values())
            this[direction] = MoveJumpDist2(position, direction)
    }
    val jump3Moves = mutableMapOf<Dist3Conn, MoveJumpDist3>().apply {
        for (direction in Dist3Conn.values())
            this[direction] = MoveJumpDist3(position, direction)
    }

    // pathfinding

    var isOnPath = false
    var move : Move? = null
    var parentSpace : Space? = null
    var parentMove : Move? = null
    var globalCost = 0
    var localCost = 0

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

                if (room.entityPlayer.hasPlayerUpgrade(PlayerUpgrade.SpringLeg))
                {
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
