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
    var levelSpaces = mutableListOf<Space>()
    var objects = mutableListOf<Object>()
    var entities = mutableListOf<Entity>()
    var entityPlayer = EntityPlayer()

    fun createLevel()
    {
        Gdx.app.log("level", "creating level...")

        if (true)
        {
            // main

            levelName = "map"
            levelWidth = 7
            levelHeight = 10
            levelFloors = 7

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
                        levelSpaces.add(space)
                    }

                    rowHeight += rowWidth
                }

                levelSpaceArray += rowHeight
            }

            // walls

            for (i in 0 until levelWidth)
            {
                for (j in 0 until levelHeight)
                {
                    objects.add(ObjectBlock(Vector3(i.toFloat(), j.toFloat(), 0f)))
                    objects.add(ObjectBlock(Vector3(i.toFloat(), j.toFloat(), (levelFloors - 1).toFloat())))
                }
            }
            for (k in 1 until levelFloors - 1)
            {
                objects.add(ObjectBlock(Vector3(0f, 0f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(1f, 0f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(2f, 0f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(3f, 0f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(4f, 0f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(5f, 0f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(6f, 1f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(6f, 2f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(6f, 3f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(6f, 4f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(6f, 5f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(6f, 6f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(6f, 7f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(6f, 8f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(6f, 9f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(6f, 9f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(5f, 9f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(4f, 9f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(3f, 9f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(2f, 9f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(1f, 9f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(0f, 9f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(0f, 8f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(0f, 7f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(0f, 6f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(0f, 5f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(0f, 4f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(0f, 3f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(0f, 2f, k.toFloat())))
                objects.add(ObjectBlock(Vector3(0f, 1f, k.toFloat())))
            }

            // objects

            val firstTeleporter = ObjectTeleporter(Vector3(3f, 7f, 3f))
            val secondTeleporter = ObjectTeleporter(Vector3(1f, 1f, 4f))

            firstTeleporter.otherTeleporter = secondTeleporter
            secondTeleporter.otherTeleporter = firstTeleporter

            objects.add(firstTeleporter)
            objects.add(secondTeleporter)

            val chest = ObjectChest(Vector3(4f, 4f, 1f), Direction2d.Right).apply {
                changeChestItem(PlayerItem.Cell, 3)
                changeChestItem(PlayerItem.Coin, 5)
                changeChestItem(PlayerItem.Pickaxe, 4)
                changeChestItem(PlayerItem.Gear, 2)
                changeChestItem(PlayerItem.RopeArrow, 1)
                changeChestItem(PlayerItem.PointingArrow, 1)
            }
            objects.add(chest)

            val vendingMachine = ObjectVendingMachine(Vector3(4f, 5f, 1f), Direction2d.Right).apply {
                changeVendingMachineItem(PlayerItem.Cell, 3)
                changeVendingMachineItem(PlayerItem.Pickaxe, 3)
                changeVendingMachineItem(PlayerItem.Gear, 3)
                changeVendingMachineItem(PlayerItem.RopeArrow, 3)
                changeVendingMachineItem(PlayerItem.PointingArrow, 3)
            }
            objects.add(vendingMachine)

            val objectStation = ObjectStation(Vector3(4f, 6f, 1f), PlayerUpgrade.ExtendedArm)

            objects.add(objectStation)

            val elevatorParts = listOf(
                    ElevatorPart(Vector3(4f, 3f, 1f), Direction2d.Right, ElevatorPartType.Door),
                    ElevatorPart(Vector3(4f, 3f, 2f), Direction2d.Right, ElevatorPartType.Empty),
                    ElevatorPart(Vector3(4f, 3f, 3f), Direction2d.Right, ElevatorPartType.Screen),
                    ElevatorPart(Vector3(4f, 3f, 4f), Direction2d.Left, ElevatorPartType.Door),
                    ElevatorPart(Vector3(4f, 3f, 5f), Direction2d.Right, ElevatorPartType.Door)
            )
            val elevatorFloors = listOf(
                    ElevatorFloor(elevatorParts[0], "first", 1),
                    ElevatorFloor(elevatorParts[3], "second", 2),
                    ElevatorFloor(elevatorParts[4], "third", 3)
            )
            val elevator = ObjectElevator(elevatorParts, elevatorFloors)
            elevator.changeCurrentFloor(elevatorFloors[0])
            objects.add(elevator)

            objects.add(ObjectRope(Vector3(4f, 2f, 4f), Direction2d.Left, 4))

            objects.add(ObjectVendingMachine(Vector3(1f, 3f, 1f), Direction2d.Down))

            objects.add(ObjectPoweredDoor(Vector3(2f, 3f, 1f), Direction2d.Up))

            objects.add(ObjectVendingMachine(Vector3(3f, 3f, 1f), Direction2d.Down))
            objects.add(ObjectVendingMachine(Vector3(3f, 2f, 1f), Direction2d.Down))

            objects.add(ObjectDoor(Vector3(3f, 1f, 1f), Direction2d.Left))

            objects.add(ObjectVendingMachine(Vector3(1f, 3f, 2f), Direction2d.Down))
            objects.add(ObjectVendingMachine(Vector3(2f, 3f, 2f), Direction2d.Down))
            objects.add(ObjectVendingMachine(Vector3(3f, 3f, 2f), Direction2d.Down))
            objects.add(ObjectVendingMachine(Vector3(3f, 2f, 2f), Direction2d.Down))
            objects.add(ObjectVendingMachine(Vector3(3f, 1f, 2f), Direction2d.Down))

            objects.add(ObjectVendingMachine(Vector3(1f, 1f, 3f), Direction2d.Down))
            objects.add(ObjectVendingMachine(Vector3(2f, 1f, 3f), Direction2d.Down))
            objects.add(ObjectVendingMachine(Vector3(3f, 1f, 3f), Direction2d.Down))
            objects.add(ObjectVendingMachine(Vector3(1f, 2f, 3f), Direction2d.Down))
            objects.add(ObjectVendingMachine(Vector3(2f, 2f, 3f), Direction2d.Down))
            objects.add(ObjectVendingMachine(Vector3(3f, 2f, 3f), Direction2d.Down))
            objects.add(ObjectVendingMachine(Vector3(1f, 3f, 3f), Direction2d.Down))
            objects.add(ObjectVendingMachine(Vector3(2f, 3f, 3f), Direction2d.Down))
            objects.add(ObjectVendingMachine(Vector3(3f, 3f, 3f), Direction2d.Down))

            val extendableBridge = ObjectExtendableBridge(Vector3(3f, 6f, 3f), Direction2d.Down, 3)

            objects.add(extendableBridge)

            objects.add(ObjectLadder(Vector3(3f, 8f, 5f), Direction2d.Up, 5))
            objects.add(ObjectLadder(Vector3(4f, 8f, 5f), Direction2d.Up, 5))

            objects.add(ObjectStairs(Vector3(2f, 8f, 3f), Direction2d.Right))
            objects.add(ObjectStairs(Vector3(1f, 8f, 2f), Direction2d.Up))
            objects.add(ObjectStairs(Vector3(1f, 7f, 1f), Direction2d.Up))
            objects.add(ObjectStairs(Vector3(2f, 7f, 1f), Direction2d.Left))

            objects.add(ObjectHalfArch(Vector3(5f, 8f, 3f), Direction2d.Up))
            objects.add(ObjectBridge(Vector3(5f, 7f, 3f), Direction2d.Up))
            objects.add(ObjectBridge(Vector3(5f, 6f, 3f), Direction2d.Up))
            objects.add(ObjectBridge(Vector3(5f, 5f, 3f), Direction2d.Up))
            objects.add(ObjectStairs(Vector3(5f, 5f, 4f), Direction2d.Down))
            objects.add(ObjectBridge(Vector3(5f, 4f, 4f), Direction2d.Up))
            objects.add(ObjectBridge(Vector3(5f, 3f, 4f), Direction2d.Up))
            objects.add(ObjectBridge(Vector3(5f, 2f, 4f), Direction2d.Up))
            objects.add(ObjectHalfArch(Vector3(5f, 1f, 4f), Direction2d.Down))

            val extendableLadder = ObjectExtendableLadder(Vector3(1f, 4f, 4f), Direction2d.Down, 4)

            objects.add(extendableLadder)

            objects.add(ObjectHalfColumn(Vector3(1f, 2f, 4f), Direction2d.Right))
            objects.add(ObjectColumn(Vector3(5f, 8f, 1f)))

            val objectSwitch = ObjectSwitch(
                    Vector3(0f, 4f, 4f), Direction2d.Right, extendableLadder.actionFoldLadder,
                    extendableLadder.actionExtendLadder
            )

            objects.add(objectSwitch)

            // player

            entityPlayer.setPosition(5f, 8f, 4f)
            //entityPlayer.changePlayerUpgrade(PlayerUpgrade.SpringLeg, true)
            entities.add(entityPlayer)
        }

        if (false)
        {
            // main

            levelName = "map"
            levelWidth = 50
            levelHeight = 50
            levelFloors = 10

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
                        levelSpaces.add(space)
                    }

                    rowHeight += rowWidth
                }

                levelSpaceArray += rowHeight
            }

            // global floor and ceiling

            for (i in 0 until levelWidth)
            {
                for (j in 0 until levelHeight)
                {
                    objects.add(ObjectBlock(Vector3(i.toFloat(), j.toFloat(), 0f)))
                    objects.add(ObjectBlock(Vector3(i.toFloat(), j.toFloat(), (levelFloors - 1).toFloat())))
                }
            }

            // main hall

            for (k in 1 until levelFloors - 1)
            {
                for (i in 18..31)
                {
                    objects.add(ObjectBlock(Vector3(i.toFloat(), 10f, k.toFloat())))
                }
                for (i in 18..31)
                {
                    objects.add(ObjectBlock(Vector3(i.toFloat(), 39f, k.toFloat())))
                }
                for (i in 10..39)
                {
                    objects.add(ObjectBlock(Vector3(18f, i.toFloat(), k.toFloat())))
                }
                for (i in 10..39)
                {
                    objects.add(ObjectBlock(Vector3(31f, i.toFloat(), k.toFloat())))
                }
            }

            for (i in 11..21)
            {
                //objects.add(ObjectHalfArch(Vector3(19f,i.toFloat(), 5.toFloat()),Direction2d.Left))
                objects.add(ObjectBlock(Vector3(19f, i.toFloat(), 6.toFloat())))
                objects.add(ObjectBlock(Vector3(20f, i.toFloat(), 6.toFloat())))
                objects.add(ObjectBlock(Vector3(21f, i.toFloat(), 6.toFloat())))
                objects.add(ObjectBlock(Vector3(22f, i.toFloat(), 6.toFloat())))
                objects.add(ObjectBlock(Vector3(27f, i.toFloat(), 6.toFloat())))
                objects.add(ObjectBlock(Vector3(28f, i.toFloat(), 6.toFloat())))
                objects.add(ObjectBlock(Vector3(29f, i.toFloat(), 6.toFloat())))
                objects.add(ObjectBlock(Vector3(30f, i.toFloat(), 6.toFloat())))
                //objects.add(ObjectHalfArch(Vector3(30f,i.toFloat(),5.toFloat()),Direction2d.Right))

                objects.add(ObjectBlock(Vector3(19f, i.toFloat(), 7.toFloat())))
                objects.add(ObjectBlock(Vector3(19f, i.toFloat(), 8.toFloat())))
                objects.add(ObjectBlock(Vector3(20f, i.toFloat(), 7.toFloat())))
                objects.add(ObjectBlock(Vector3(20f, i.toFloat(), 8.toFloat())))
                objects.add(ObjectBlock(Vector3(29f, i.toFloat(), 7.toFloat())))
                objects.add(ObjectBlock(Vector3(29f, i.toFloat(), 8.toFloat())))
                objects.add(ObjectBlock(Vector3(30f, i.toFloat(), 7.toFloat())))
                objects.add(ObjectBlock(Vector3(30f, i.toFloat(), 8.toFloat())))
            }

            for (i in 22..27)
            {
                objects.add(ObjectBlock(Vector3(19f, i.toFloat(), 6.toFloat())))
                objects.add(ObjectBlock(Vector3(19f, i.toFloat(), 7.toFloat())))
                objects.add(ObjectBlock(Vector3(19f, i.toFloat(), 8.toFloat())))
                objects.add(ObjectBlock(Vector3(20f, i.toFloat(), 7.toFloat())))
                objects.add(ObjectBlock(Vector3(20f, i.toFloat(), 8.toFloat())))
                objects.add(ObjectBlock(Vector3(29f, i.toFloat(), 7.toFloat())))
                objects.add(ObjectBlock(Vector3(29f, i.toFloat(), 8.toFloat())))
                objects.add(ObjectBlock(Vector3(30f, i.toFloat(), 7.toFloat())))
                objects.add(ObjectBlock(Vector3(30f, i.toFloat(), 8.toFloat())))
                objects.add(ObjectBlock(Vector3(30f, i.toFloat(), 6.toFloat())))
            }

            objects.add(ObjectBlock(Vector3(20f, 28.toFloat(), 6.toFloat())))
            objects.add(ObjectBlock(Vector3(21f, 28.toFloat(), 6.toFloat())))
            objects.add(ObjectBlock(Vector3(22f, 28.toFloat(), 6.toFloat())))
            objects.add(ObjectBlock(Vector3(27f, 28.toFloat(), 6.toFloat())))
            objects.add(ObjectBlock(Vector3(28f, 28.toFloat(), 6.toFloat())))
            objects.add(ObjectBlock(Vector3(29f, 28.toFloat(), 6.toFloat())))

            for (i in 29..38)
            {
                objects.add(ObjectBlock(Vector3(19f, i.toFloat(), 6.toFloat())))
                objects.add(ObjectBlock(Vector3(20f, i.toFloat(), 6.toFloat())))
                objects.add(ObjectBlock(Vector3(21f, i.toFloat(), 6.toFloat())))
                objects.add(ObjectBlock(Vector3(22f, i.toFloat(), 6.toFloat())))
                objects.add(ObjectBlock(Vector3(27f, i.toFloat(), 6.toFloat())))
                objects.add(ObjectBlock(Vector3(28f, i.toFloat(), 6.toFloat())))
                objects.add(ObjectBlock(Vector3(29f, i.toFloat(), 6.toFloat())))
                objects.add(ObjectBlock(Vector3(30f, i.toFloat(), 6.toFloat())))

                objects.add(ObjectBlock(Vector3(19f, i.toFloat(), 7.toFloat())))
                objects.add(ObjectBlock(Vector3(19f, i.toFloat(), 8.toFloat())))
                objects.add(ObjectBlock(Vector3(20f, i.toFloat(), 7.toFloat())))
                objects.add(ObjectBlock(Vector3(20f, i.toFloat(), 8.toFloat())))
                objects.add(ObjectBlock(Vector3(29f, i.toFloat(), 7.toFloat())))
                objects.add(ObjectBlock(Vector3(29f, i.toFloat(), 8.toFloat())))
                objects.add(ObjectBlock(Vector3(30f, i.toFloat(), 7.toFloat())))
                objects.add(ObjectBlock(Vector3(30f, i.toFloat(), 8.toFloat())))
            }

            objects.add(ObjectBridge(Vector3(23f, 14f, 6f), Direction2d.Right))
            objects.add(ObjectBridge(Vector3(23f, 15f, 6f), Direction2d.Right))
            objects.add(ObjectBridge(Vector3(24f, 14f, 6f), Direction2d.Right))
            objects.add(ObjectBridge(Vector3(24f, 15f, 6f), Direction2d.Right))
            objects.add(ObjectBridge(Vector3(25f, 14f, 6f), Direction2d.Right))
            objects.add(ObjectBridge(Vector3(25f, 15f, 6f), Direction2d.Right))
            objects.add(ObjectBridge(Vector3(26f, 14f, 6f), Direction2d.Right))
            objects.add(ObjectBridge(Vector3(26f, 15f, 6f), Direction2d.Right))

            objects.add(ObjectBlock(Vector3(23f, 35f, 6f)))
            objects.add(ObjectBlock(Vector3(23f, 36f, 6f)))
            objects.add(ObjectBlock(Vector3(24f, 35f, 6f)))
            objects.add(ObjectBlock(Vector3(24f, 36f, 6f)))
            objects.add(ObjectBlock(Vector3(25f, 35f, 6f)))
            objects.add(ObjectBlock(Vector3(25f, 36f, 6f)))
            objects.add(ObjectBlock(Vector3(26f, 35f, 6f)))
            objects.add(ObjectBlock(Vector3(26f, 36f, 6f)))

            for (i in 0..4)
            {
                objects.add(ObjectHalfColumn(Vector3(22f, 12f, (1 + i).toFloat()), Direction2d.Left))
                objects.add(ObjectHalfColumn(Vector3(22f, 15f, (1 + i).toFloat()), Direction2d.Left))
                objects.add(ObjectHalfColumn(Vector3(22f, 18f, (1 + i).toFloat()), Direction2d.Left))
                objects.add(ObjectHalfColumn(Vector3(22f, 21f, (1 + i).toFloat()), Direction2d.Left))
                objects.add(ObjectHalfColumn(Vector3(22f, 28f, (1 + i).toFloat()), Direction2d.Left))
                objects.add(ObjectHalfColumn(Vector3(22f, 31f, (1 + i).toFloat()), Direction2d.Left))
                objects.add(ObjectHalfColumn(Vector3(22f, 34f, (1 + i).toFloat()), Direction2d.Left))
                objects.add(ObjectHalfColumn(Vector3(22f, 35f, (1 + i).toFloat()), Direction2d.Left))
                objects.add(ObjectHalfColumn(Vector3(22f, 36f, (1 + i).toFloat()), Direction2d.Left))
                objects.add(ObjectHalfColumn(Vector3(22f, 37f, (1 + i).toFloat()), Direction2d.Left))

                objects.add(ObjectHalfColumn(Vector3(27f, 12f, (1 + i).toFloat()), Direction2d.Right))
                objects.add(ObjectHalfColumn(Vector3(27f, 15f, (1 + i).toFloat()), Direction2d.Right))
                objects.add(ObjectHalfColumn(Vector3(27f, 18f, (1 + i).toFloat()), Direction2d.Right))
                objects.add(ObjectHalfColumn(Vector3(27f, 21f, (1 + i).toFloat()), Direction2d.Right))
                objects.add(ObjectHalfColumn(Vector3(27f, 28f, (1 + i).toFloat()), Direction2d.Right))
                objects.add(ObjectHalfColumn(Vector3(27f, 31f, (1 + i).toFloat()), Direction2d.Right))
                objects.add(ObjectHalfColumn(Vector3(27f, 34f, (1 + i).toFloat()), Direction2d.Right))
                objects.add(ObjectHalfColumn(Vector3(27f, 35f, (1 + i).toFloat()), Direction2d.Right))
                objects.add(ObjectHalfColumn(Vector3(27f, 36f, (1 + i).toFloat()), Direction2d.Right))
                objects.add(ObjectHalfColumn(Vector3(27f, 37f, (1 + i).toFloat()), Direction2d.Right))

                objects.add(ObjectHalfColumn(Vector3(22f, 34f, (1 + i).toFloat()), Direction2d.Down))
                objects.add(ObjectHalfColumn(Vector3(23f, 34f, (1 + i).toFloat()), Direction2d.Down))
                objects.add(ObjectHalfColumn(Vector3(24f, 34f, (1 + i).toFloat()), Direction2d.Down))
                objects.add(ObjectHalfColumn(Vector3(25f, 34f, (1 + i).toFloat()), Direction2d.Down))
                objects.add(ObjectHalfColumn(Vector3(26f, 34f, (1 + i).toFloat()), Direction2d.Down))
                objects.add(ObjectHalfColumn(Vector3(27f, 34f, (1 + i).toFloat()), Direction2d.Down))

                objects.add(ObjectHalfColumn(Vector3(22f, 37f, (1 + i).toFloat()), Direction2d.Up))
                objects.add(ObjectHalfColumn(Vector3(23f, 37f, (1 + i).toFloat()), Direction2d.Up))
                objects.add(ObjectHalfColumn(Vector3(24f, 37f, (1 + i).toFloat()), Direction2d.Up))
                objects.add(ObjectHalfColumn(Vector3(25f, 37f, (1 + i).toFloat()), Direction2d.Up))
                objects.add(ObjectHalfColumn(Vector3(26f, 37f, (1 + i).toFloat()), Direction2d.Up))
                objects.add(ObjectHalfColumn(Vector3(27f, 37f, (1 + i).toFloat()), Direction2d.Up))
            }

            objects.add(ObjectHalfColumn(Vector3(27f, 15f, 1f), Direction2d.Down))
            objects.add(ObjectHalfColumn(Vector3(27f, 15f, 2f), Direction2d.Down))
            objects.add(ObjectHalfColumn(Vector3(27f, 15f, 3f), Direction2d.Down))
            objects.add(ObjectHalfColumn(Vector3(27f, 15f, 4f), Direction2d.Down))
            objects.add(ObjectHalfColumn(Vector3(27f, 15f, 5f), Direction2d.Down))

            objects.add(ObjectHalfColumn(Vector3(27f, 18f, 1f), Direction2d.Up))
            objects.add(ObjectHalfColumn(Vector3(27f, 18f, 2f), Direction2d.Up))
            objects.add(ObjectHalfColumn(Vector3(27f, 18f, 3f), Direction2d.Up))
            objects.add(ObjectHalfColumn(Vector3(27f, 18f, 4f), Direction2d.Up))
            objects.add(ObjectHalfColumn(Vector3(27f, 18f, 5f), Direction2d.Up))

            objects.add(ObjectHalfArch(Vector3(27f, 16f, 1f), Direction2d.Down))
            objects.add(ObjectHalfArch(Vector3(27f, 17f, 1f), Direction2d.Up))
            objects.add(ObjectHalfArch(Vector3(27f, 16f, 3f), Direction2d.Down))
            objects.add(ObjectHalfArch(Vector3(27f, 17f, 3f), Direction2d.Up))
            objects.add(ObjectHalfArch(Vector3(27f, 16f, 5f), Direction2d.Down))
            objects.add(ObjectHalfArch(Vector3(27f, 17f, 5f), Direction2d.Up))

            // objects

            objects.add(ObjectLadder(Vector3(23f, 12f, 7f), Direction2d.Left, 7))
            objects.add(ObjectRope(Vector3(28f, 22f, 9f), Direction2d.Right, 9))
            objects.add(ObjectExtendableLadder(Vector3(30f, 28f, 9f), Direction2d.Right, 9))
            objects.add(ObjectExtendableBridge(Vector3(27f, 27f, 6f), Direction2d.Down, 6))

            val elevatorParts = listOf(
                    ElevatorPart(Vector3(19f, 28f, 8f), Direction2d.Right, ElevatorPartType.Screen),
                    ElevatorPart(Vector3(19f, 28f, 7f), Direction2d.Right, ElevatorPartType.Door),
                    ElevatorPart(Vector3(19f, 28f, 6f), Direction2d.Right, ElevatorPartType.Empty),
                    ElevatorPart(Vector3(19f, 28f, 5f), Direction2d.Right, ElevatorPartType.Empty),
                    ElevatorPart(Vector3(19f, 28f, 4f), Direction2d.Right, ElevatorPartType.Empty),
                    ElevatorPart(Vector3(19f, 28f, 3f), Direction2d.Right, ElevatorPartType.Empty),
                    ElevatorPart(Vector3(19f, 28f, 2f), Direction2d.Right, ElevatorPartType.Screen),
                    ElevatorPart(Vector3(19f, 28f, 1f), Direction2d.Right, ElevatorPartType.Door)
            )
            val elevatorFloors = listOf(
                    ElevatorFloor(elevatorParts[7], "bottom", 1),
                    ElevatorFloor(elevatorParts[1], "top", 2)
            )
            objects.add(ObjectElevator(elevatorParts, elevatorFloors))

            objects.add(ObjectBlock(Vector3(19f, 22f, 6f)))
            objects.add(ObjectBlock(Vector3(19f, 22f, 5f)))
            objects.add(ObjectBlock(Vector3(19f, 22f, 4f)))
            objects.add(ObjectBlock(Vector3(19f, 22f, 3f)))
            objects.add(ObjectBlock(Vector3(19f, 22f, 2f)))
            objects.add(ObjectBlock(Vector3(19f, 22f, 1f)))
            objects.add(ObjectBlock(Vector3(20f, 22f, 6f)))
            objects.add(ObjectBlock(Vector3(20f, 22f, 5f)))
            objects.add(ObjectBlock(Vector3(20f, 22f, 4f)))
            objects.add(ObjectBlock(Vector3(20f, 22f, 3f)))
            objects.add(ObjectBlock(Vector3(20f, 22f, 2f)))
            objects.add(ObjectBlock(Vector3(20f, 22f, 1f)))
            objects.add(ObjectBlock(Vector3(21f, 22f, 6f)))
            objects.add(ObjectStairs(Vector3(21f, 23f, 6f), Direction2d.Left))
            objects.add(ObjectBlock(Vector3(21f, 23f, 5f)))
            objects.add(ObjectBlock(Vector3(21f, 23f, 4f)))
            objects.add(ObjectBlock(Vector3(21f, 23f, 3f)))
            objects.add(ObjectBlock(Vector3(21f, 23f, 2f)))
            objects.add(ObjectBlock(Vector3(21f, 23f, 1f)))
            objects.add(ObjectStairs(Vector3(22f, 23f, 5f), Direction2d.Left))
            objects.add(ObjectBlock(Vector3(22f, 23f, 4f)))
            objects.add(ObjectBlock(Vector3(22f, 23f, 3f)))
            objects.add(ObjectBlock(Vector3(22f, 23f, 2f)))
            objects.add(ObjectBlock(Vector3(22f, 23f, 1f)))
            objects.add(ObjectStairs(Vector3(23f, 23f, 4f), Direction2d.Left))
            objects.add(ObjectBlock(Vector3(23f, 23f, 3f)))
            objects.add(ObjectBlock(Vector3(23f, 23f, 2f)))
            objects.add(ObjectBlock(Vector3(23f, 23f, 1f)))
            objects.add(ObjectStairs(Vector3(24f, 23f, 3f), Direction2d.Down))
            objects.add(ObjectBlock(Vector3(24f, 23f, 2f)))
            objects.add(ObjectBlock(Vector3(24f, 23f, 1f)))
            objects.add(ObjectStairs(Vector3(24f, 24f, 2f), Direction2d.Down))
            objects.add(ObjectBlock(Vector3(24f, 24f, 1f)))
            objects.add(ObjectStairs(Vector3(24f, 25f, 1f), Direction2d.Down))

            objects.add(ObjectVendingMachine(Vector3(22f, 18f, 7f), Direction2d.Down).apply {
                changeVendingMachineItem(PlayerItem.Cell, 4)
                changeVendingMachineItem(PlayerItem.Coin, 0)
                changeVendingMachineItem(PlayerItem.Pickaxe, 2)
                changeVendingMachineItem(PlayerItem.Gear, 3)
                changeVendingMachineItem(PlayerItem.RopeArrow, 1)
                changeVendingMachineItem(PlayerItem.PointingArrow, 1)
            })
            objects.add(ObjectChest(Vector3(27f, 18f, 7f), Direction2d.Up).apply {
                changeChestItem(PlayerItem.Cell, 5)
                changeChestItem(PlayerItem.Coin, 5)
                changeChestItem(PlayerItem.Pickaxe, 5)
                changeChestItem(PlayerItem.Gear, 5)
                changeChestItem(PlayerItem.RopeArrow, 5)
                changeChestItem(PlayerItem.PointingArrow, 5)
            })
            objects.add(ObjectStation(Vector3(21f, 38f, 6f), PlayerUpgrade.SpringLeg))
            val firstTeleporter = ObjectTeleporter(Vector3(21f, 11f, 6f))
            val secondTeleporter = ObjectTeleporter(Vector3(28f, 38f, 6f))
            firstTeleporter.otherTeleporter = secondTeleporter
            secondTeleporter.otherTeleporter = firstTeleporter
            objects.add(firstTeleporter)
            objects.add(secondTeleporter)
            objects.add(ObjectPoweredDoor(Vector3(27f, 35f, 1f), Direction2d.Right))
            objects.add(ObjectDoor(Vector3(27f, 36f, 1f), Direction2d.Left))

            // player

            entityPlayer.setPosition(25f, 25f, 1f)
            entityPlayer.changePlayerItem(PlayerItem.Cell, 1)
            entities.add(entityPlayer)
        }

        Gdx.app.log("level", "created level")
    }

    fun changePlayerPosition(newPosition : Vector3)
    {
        entityPlayer.playerPosition.set(newPosition)
    }

    fun updateEntities()
    {
        Gdx.app.log("level", "updating entities...")

        for (space in levelSpaces)
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

        for (space in levelSpaces)
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

        for (space in levelSpaces)
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

    fun updateSpacesAboveAndBelow()
    {
        Gdx.app.log("level", "updating spaces above and below...")

        for (space in levelSpaces)
        {
            var spacesAbove = 0
            var spacesBelow = 0

            var spaceAbove = getSpace(space.position, Direction3d.Above)
            var spaceBelow = getSpace(space.position, Direction3d.Below)

            while (spaceAbove != null && spaceAbove.objectOccupying == null)
            {
                spacesAbove++
                spaceAbove = getSpace(spaceAbove.position, Direction3d.Above)
            }

            while (spaceBelow != null && spaceBelow.objectOccupying == null)
            {
                spacesBelow++
                spaceBelow = getSpace(spaceBelow.position, Direction3d.Below)
            }

            space.spacesAbove = spacesAbove
            space.spacesBelow = spacesBelow
        }

        Gdx.app.log("level", "updated spaces above and below")
    }

    fun updateGround()
    {
        Gdx.app.log("level", "updating ground...")

        for (space in levelSpaces)
        {
            val objectOccupying = space.objectOccupying

            space.isGround = objectOccupying?.isGround(space.position) ?: false
        }

        for (space in levelSpaces)
        {
            val spaceBelow = getSpace(space.position, Direction3d.Below)

            space.isOnGround = spaceBelow?.isGround ?: true
        }

        Gdx.app.log("level", "updated ground")
    }

    fun clearPathfinding()
    {
        Gdx.app.log("level", "clearing pathfinding...")

        for (space in levelSpaces)
        {
            space.isOnPath = false
            space.move = null
            space.parentSpace = null
            space.parentMove = null
            space.globalCost = Int.MAX_VALUE
            space.localCost = Int.MAX_VALUE
        }

        Gdx.app.log("level", "cleared pathfinding")
    }

    fun clearBorder()
    {
        Gdx.app.log("level", "clearing border...")

        for (space in levelSpaces)
        {
            space.isOnBorder = false
            space.isWithinBorder = false
        }

        Gdx.app.log("level", "cleared border")
    }

    fun clearSideVisibility()
    {
        Gdx.app.log("level", "clearing sideVisibility...")

        for (space in levelSpaces)
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
