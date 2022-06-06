package com.marcin.panklex

import com.badlogic.gdx.math.Vector3

class KlexRoom(val level : KlexLevel)
{
    var isRoom = false
    var xStart = 0
    var xEnd = 0
    var width = 0
    var yStart = 0
    var yEnd = 0
    var height = 0
    var zStart = 0
    var zEnd = 0
    var floors = 0
    var blocks = mutableListOf<List<List<KlexBlock?>>>()

    fun checkCoordinates(position : Vector3)
    {
        if (position.x < xStart) xStart = position.x.toInt()
        if (position.x > xEnd) xEnd = position.x.toInt()
        if (position.y < yStart) yStart = position.y.toInt()
        if (position.y > yEnd) yEnd = position.y.toInt()
        if (position.z < zStart) zStart = position.z.toInt()
        if (position.z > zEnd) zEnd = position.z.toInt()
    }

    fun checkBlock(block : KlexBlock, queue : MutableList<KlexBlock>, checked : MutableSet<KlexBlock>)
    {
        if (block !in queue && block !in checked) queue.add(block)
    }

    fun updateRoom(levelPosition : Vector3)
    {
        xStart = 0
        xEnd = 0
        width = 0
        yStart = 0
        yEnd = 0
        height = 0
        zStart = 0
        zEnd = 0
        floors = 0
        blocks.clear()

        val firstBlock = level.getBlock(levelPosition)

        if (firstBlock == null || firstBlock.type != BlockType.Empty) isRoom = false
        else
        {
            isRoom = true

            xStart = firstBlock.position.x.toInt()
            xEnd = firstBlock.position.x.toInt()
            width = 0
            yStart = firstBlock.position.y.toInt()
            yEnd = firstBlock.position.y.toInt()
            height = 0
            zStart = firstBlock.position.z.toInt()
            zEnd = firstBlock.position.z.toInt()
            floors = 0

            // gather all the blocks

            val queue = mutableListOf<KlexBlock>()
            val checked = mutableSetOf<KlexBlock>()

            queue.add(firstBlock)

            while (queue.isNotEmpty())
            {
                val current = queue.last()
                queue.remove(current)
                checkCoordinates(current.position)

                if (current.type != BlockType.Empty) current.isBorder = true
                else
                {
                    val blockRight = level.getBlockRight(current.position)
                    val blockLeft = level.getBlockLeft(current.position)
                    val blockUp = level.getBlockUp(current.position)
                    val blockDown = level.getBlockDown(current.position)
                    val blockAbove = level.getBlockAbove(current.position)
                    val blockBelow = level.getBlockBelow(current.position)

                    current.isBorder =
                        blockRight == null || blockLeft == null || blockUp == null || blockDown == null || blockAbove == null || blockBelow == null

                    if (blockRight != null) checkBlock(blockRight, queue, checked)
                    if (blockLeft != null) checkBlock(blockLeft, queue, checked)
                    if (blockUp != null) checkBlock(blockUp, queue, checked)
                    if (blockDown != null) checkBlock(blockDown, queue, checked)
                    if (blockAbove != null) checkBlock(blockAbove, queue, checked)
                    if (blockBelow != null) checkBlock(blockBelow, queue, checked)
                }

                checked.add(current)
            }

            // build room from the blocks

            width = xEnd - xStart + 1
            height = yEnd - yStart + 1
            floors = zEnd - zStart + 1

            for (k in zStart..zEnd)
            {
                val floor = mutableListOf<List<KlexBlock?>>()

                for (j in yStart..yEnd)
                {
                    val row = mutableListOf<KlexBlock?>()

                    for (i in xStart..xEnd)
                    {
                        var block : KlexBlock? = null

                        for (b in checked)
                        {
                            if (b.position.x.toInt() == i && b.position.y.toInt() == j && b.position.z.toInt() == k)
                            {
                                block = b
                                checked.remove(b)
                                break
                            }
                        }

                        row.add(block)
                    }

                    floor.add(row)
                }

                blocks.add(floor)
            }
        }
    }

    fun getBlock(xRoom : Int, yRoom : Int, zRoom : Int) : KlexBlock?
    {
        return if (xRoom in 0 until width && yRoom in 0 until height && zRoom in 0 until floors)
            blocks[zRoom][yRoom][xRoom]
        else null
    }

    fun getBlock(roomPosition : Vector3) : KlexBlock?
    {
        return getBlock(roomPosition.x.toInt(), roomPosition.y.toInt(), roomPosition.z.toInt())
    }
}
