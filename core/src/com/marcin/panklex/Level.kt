package com.marcin.panklex

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.entities.EntityPlayer
import com.marcin.panklex.objects.*

class Level
{
    var levelName = ""
    var levelWidth = 0
    var levelHeight = 0
    var levelFloors = 0
    var levelSpaceArray = arrayOf<Array<Array<Space>>>()
    var roomSpaces = mutableListOf<Space>()
    var objects = mutableListOf<Object>()
    var entities = mutableListOf<Entity>()
    var entityPlayer : EntityPlayer? = null

    fun createLevel()
    {
        Gdx.app.log("level", "creating level...")

        // main

        levelName = "manually created"
        levelWidth = 5
        levelHeight = 7
        levelFloors = 5

        // spaces

        for (k in 0 until levelFloors)
        {
            var rowHeight = arrayOf<Array<Space>>()

            for (j in 0 until levelHeight)
            {
                var rowWidth = arrayOf<Space>()

                for (i in 0 until levelWidth)
                {
                    val space = Space(Vector3(i.toFloat(), j.toFloat(), k.toFloat()))
                    rowWidth += space
                    roomSpaces.add(space)
                }

                rowHeight += rowWidth
            }

            levelSpaceArray += rowHeight
        }

        // objects

        for (i in 0 until levelWidth)
        {
            for (j in 0 until levelHeight)
            {
                objects.add(ObjectVendingMachine(Vector3(i.toFloat(), j.toFloat(), 0f), Direction2d.Down))
                objects.add(
                    ObjectVendingMachine(Vector3(i.toFloat(), j.toFloat(), (levelFloors - 1).toFloat()), Direction2d.Down))
            }
        }
        for (k in 1 until levelFloors - 1)
        {
            objects.add(ObjectVendingMachine(Vector3(0f, 0f, k.toFloat()), Direction2d.Down))
            objects.add(ObjectVendingMachine(Vector3(1f, 0f, k.toFloat()), Direction2d.Down))
            objects.add(ObjectVendingMachine(Vector3(2f, 0f, k.toFloat()), Direction2d.Down))
            objects.add(ObjectVendingMachine(Vector3(3f, 0f, k.toFloat()), Direction2d.Down))
            objects.add(ObjectVendingMachine(Vector3(4f, 0f, k.toFloat()), Direction2d.Down))

            objects.add(ObjectVendingMachine(Vector3(4f, 1f, k.toFloat()), Direction2d.Down))
            objects.add(ObjectHalfColumn(Vector3(4f, 2f, k.toFloat()), Direction2d.Right))
            objects.add(ObjectVendingMachine(Vector3(4f, 3f, k.toFloat()), Direction2d.Down))
            objects.add(ObjectBlock(Vector3(4f, 4f, k.toFloat())))
            objects.add(ObjectVendingMachine(Vector3(4f, 5f, k.toFloat()), Direction2d.Down))

            objects.add(ObjectVendingMachine(Vector3(4f, 6f, k.toFloat()), Direction2d.Down))
            objects.add(ObjectVendingMachine(Vector3(3f, 6f, k.toFloat()), Direction2d.Down))
            objects.add(ObjectVendingMachine(Vector3(2f, 6f, k.toFloat()), Direction2d.Down))
            objects.add(ObjectVendingMachine(Vector3(1f, 6f, k.toFloat()), Direction2d.Down))
            objects.add(ObjectVendingMachine(Vector3(0f, 6f, k.toFloat()), Direction2d.Down))

            objects.add(ObjectVendingMachine(Vector3(0f, 5f, k.toFloat()), Direction2d.Down))
            objects.add(ObjectVendingMachine(Vector3(0f, 4f, k.toFloat()), Direction2d.Down))
            objects.add(ObjectVendingMachine(Vector3(0f, 3f, k.toFloat()), Direction2d.Down))
            objects.add(ObjectVendingMachine(Vector3(0f, 2f, k.toFloat()), Direction2d.Down))
            objects.add(ObjectVendingMachine(Vector3(0f, 1f, k.toFloat()), Direction2d.Down))
        }

        objects.add(ObjectExtendableBridge(Vector3(1f, 2f, 1f), Direction2d.Right, 3))
        objects.add(ObjectHalfArch(Vector3(1f, 4f, 1f), Direction2d.Left))
        objects.add(ObjectBridge(Vector3(2f, 4f, 1f), Direction2d.Right))
        objects.add(ObjectHalfColumn(Vector3(3f, 4f, 1f), Direction2d.Right))
        objects.add(ObjectArch(Vector3(2f, 4f, 2f), Direction2d.Right))
        objects.add(ObjectColumn(Vector3(2f, 4f, 3f)))
        objects.add(ObjectExtendableLadder(Vector3(1f, 3f, 3f), Direction2d.Left, 3))
        objects.add(ObjectLadder(Vector3(1f, 5f, 3f), Direction2d.Left, 3))
        objects.add(
            ObjectElevator(
                Vector3(2f, 5f, 3f), 3, listOf(Direction2d.Left, Direction2d.Right, Direction2d.Right),
                listOf(ElevatorShaftType.Door, ElevatorShaftType.Screen, ElevatorShaftType.ScreenBroken)))

        objects.add(ObjectDoor(Vector3(0f, 2f, 2f), Direction2d.Right))
        objects.add(ObjectPoweredDoor(Vector3(0f, 4f, 2f), Direction2d.Right))
        objects.add(ObjectTeleporter(Vector3(3f, 1f, 1f)))
        objects.add(ObjectSwitch(Vector3(3f, 1f, 3f), Direction2d.Left))
        objects.add(ObjectStairs(Vector3(2f, 1f, 1f), Direction2d.Left))

        // entities

        entityPlayer = EntityPlayer(Vector3(2f, 4f, 3f))
        entities.add(entityPlayer!!)

        Gdx.app.log("level", "created level")
    }

    fun changePlayerPosition(newPosition : Vector3)
    {
        entityPlayer?.playerPosition?.set(newPosition)
    }

    fun updateEntities()
    {
        Gdx.app.log("level", "updating entities...")

        for (space in roomSpaces)
        {
            space.entityOccupying = null
        }

        for (entity in entities)
        {
            getSpace(entity.getOccupiedPosition())?.entityOccupying = entity
        }

        Gdx.app.log("level", "updated entities")
    }

    fun updateObjects()
    {
        Gdx.app.log("level", "updating objects...")

        for (space in roomSpaces)
        {
            space.objectOccupying = null
            space.objectsPresent.clear()
        }

        for (obj in objects)
        {
            val occupiedPositions = mutableListOf<Vector3>()
            val presentPositions = mutableListOf<Vector3>()

            obj.getOccupiedPositions(occupiedPositions)
            obj.getPresentPositions(presentPositions)

            for (p in occupiedPositions)
            {
                getSpace(p)?.objectOccupying = obj
            }

            for (p in presentPositions)
            {
                getSpace(p)?.objectsPresent?.add(obj)
            }
        }

        Gdx.app.log("level", "updated objects")
    }

    fun updateSideTransparency()
    {
        Gdx.app.log("level", "updating sideTransparency...")

        for (space in roomSpaces)
        {
            val objectOccupying = space.objectOccupying

            if (objectOccupying != null)
            {
                objectOccupying.getSideTransparency(space.sideTransparency, space.position)
            }
            else
            {
                for (side in Direction3d.values())
                {
                    space.sideTransparency[side] = true
                }
            }
        }

        Gdx.app.log("level", "updated sideTransparency")
    }

    fun clearBorder()
    {
        Gdx.app.log("level", "clearing border...")

        for (space in roomSpaces)
        {
            space.isOnBorder = false
            space.isWithinBorder = false
        }

        Gdx.app.log("level", "cleared border")
    }

    fun clearSideVisibility()
    {
        Gdx.app.log("level", "clearing sideVisibility...")

        for (space in roomSpaces)
        {
            for (side in Direction3d.values())
            {
                space.sideVisibility[side] = false
            }
        }

        Gdx.app.log("level", "cleared sideVisibility")
    }

    fun getSpace(x : Int, y : Int, z : Int) : Space?
    {
        return if (x in 0 until levelWidth && y in 0 until levelHeight && z in 0 until levelFloors) levelSpaceArray[z][y][x] else null
    }

    fun getSpace(position : Vector3) : Space?
    {
        return getSpace(position.x.toInt(), position.y.toInt(), position.z.toInt())
    }

    fun getSpace(position : Vector3, direction : Direction3d) : Space?
    {
        return when (direction)
        {
            Direction3d.Right -> getSpace(position.x.toInt() + 1, position.y.toInt(), position.z.toInt())
            Direction3d.Left  -> getSpace(position.x.toInt() - 1, position.y.toInt(), position.z.toInt())
            Direction3d.Up    -> getSpace(position.x.toInt(), position.y.toInt() + 1, position.z.toInt())
            Direction3d.Down  -> getSpace(position.x.toInt(), position.y.toInt() - 1, position.z.toInt())
            Direction3d.Above -> getSpace(position.x.toInt(), position.y.toInt(), position.z.toInt() + 1)
            Direction3d.Below -> getSpace(position.x.toInt(), position.y.toInt(), position.z.toInt() - 1)
        }
    }
}
