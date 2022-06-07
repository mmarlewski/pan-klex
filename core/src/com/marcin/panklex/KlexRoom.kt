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

            level.clearBorders()

            // gather all the blocks

            val queue = mutableListOf<KlexBlock>()
            val checked = mutableSetOf<KlexBlock>()

            queue.add(firstBlock)

            while (queue.isNotEmpty())
            {
                val current = queue.last()
                queue.remove(current)

                if (current.position.x < xStart) xStart = current.position.x.toInt()
                if (current.position.x > xEnd) xEnd = current.position.x.toInt()
                if (current.position.y < yStart) yStart = current.position.y.toInt()
                if (current.position.y > yEnd) yEnd = current.position.y.toInt()
                if (current.position.z < zStart) zStart = current.position.z.toInt()
                if (current.position.z > zEnd) zEnd = current.position.z.toInt()

                if (current.isNotEmpty()) current.isBorder = true
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

                    if (blockRight == null) current.isBorderRight = true
                    if (blockLeft == null) current.isBorderLeft = true
                    if (blockUp == null) current.isBorderUp = true
                    if (blockDown == null) current.isBorderDown = true
                    if (blockAbove == null) current.isBorderAbove = true
                    if (blockDown == null) current.isBorderDown = true

                    if (blockRight != null)
                    {
                        if (blockRight.isNotEmpty()) blockRight.isBorderLeft = true

                        if (blockRight !in queue && blockRight !in checked) queue.add(blockRight)
                    }

                    if (blockLeft != null)
                    {
                        if (blockLeft.isNotEmpty()) blockLeft.isBorderRight = true

                        if (blockLeft !in queue && blockLeft !in checked) queue.add(blockLeft)
                    }

                    if (blockUp != null)
                    {
                        if (blockUp.isNotEmpty()) blockUp.isBorderDown = true

                        if (blockUp !in queue && blockUp !in checked) queue.add(blockUp)
                    }

                    if (blockDown != null)
                    {
                        if (blockDown.isNotEmpty()) blockDown.isBorderUp = true

                        if (blockDown !in queue && blockDown !in checked) queue.add(blockDown)
                    }

                    if (blockAbove != null)
                    {
                        if (blockAbove.isNotEmpty()) blockAbove.isBorderBelow = true

                        if (blockAbove !in queue && blockAbove !in checked) queue.add(blockAbove)
                    }

                    if (blockBelow != null)
                    {
                        if (blockBelow.isNotEmpty()) blockBelow.isBorderAbove = true

                        if (blockBelow !in queue && blockBelow !in checked) queue.add(blockBelow)
                    }
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
