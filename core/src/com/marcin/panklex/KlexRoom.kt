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
                    /////

                    val rightBlock = level.getBlock(current.position, BlockSide.Right)
                    val downBlock = level.getBlock(current.position, BlockSide.Down)
                    val aboveBlock = level.getBlock(current.position, BlockSide.Above)
                    val leftBlock = level.getBlock(current.position, BlockSide.Left)
                    val upBlock = level.getBlock(current.position, BlockSide.Up)
                    val belowBlock = level.getBlock(current.position, BlockSide.Below)

                    val rightBlockAbove =
                        if (rightBlock != null) level.getBlock(rightBlock.position, BlockSide.Above) else null
                    val rightBlockUp =
                        if (rightBlock != null) level.getBlock(rightBlock.position, BlockSide.Up) else null
                    val rightBlockBelow =
                        if (rightBlock != null) level.getBlock(rightBlock.position, BlockSide.Below) else null
                    val rightBlockDown =
                        if (rightBlock != null) level.getBlock(rightBlock.position, BlockSide.Down) else null

                    val leftBlockAbove =
                        if (leftBlock != null) level.getBlock(leftBlock.position, BlockSide.Above) else null
                    val leftBlockUp =
                        if (leftBlock != null) level.getBlock(leftBlock.position, BlockSide.Up) else null
                    val leftBlockBelow =
                        if (leftBlock != null) level.getBlock(leftBlock.position, BlockSide.Below) else null
                    val leftBlockDown =
                        if (leftBlock != null) level.getBlock(leftBlock.position, BlockSide.Down) else null

                    val upBlockAbove = if (upBlock != null) level.getBlock(upBlock.position, BlockSide.Above) else null
                    val upBlockRight = if (upBlock != null) level.getBlock(upBlock.position, BlockSide.Right) else null
                    val upBlockBelow = if (upBlock != null) level.getBlock(upBlock.position, BlockSide.Below) else null
                    val upBlockLeft = if (upBlock != null) level.getBlock(upBlock.position, BlockSide.Left) else null

                    val downBlockAbove =
                        if (downBlock != null) level.getBlock(downBlock.position, BlockSide.Above) else null
                    val downBlockRight =
                        if (downBlock != null) level.getBlock(downBlock.position, BlockSide.Right) else null
                    val downBlockBelow =
                        if (downBlock != null) level.getBlock(downBlock.position, BlockSide.Below) else null
                    val downBlockLeft =
                        if (downBlock != null) level.getBlock(downBlock.position, BlockSide.Left) else null

                    val aboveBlockUp =
                        if (aboveBlock != null) level.getBlock(aboveBlock.position, BlockSide.Up) else null
                    val aboveBlockRight =
                        if (aboveBlock != null) level.getBlock(aboveBlock.position, BlockSide.Right) else null
                    val aboveBlockDown =
                        if (aboveBlock != null) level.getBlock(aboveBlock.position, BlockSide.Down) else null
                    val aboveBlockLeft =
                        if (aboveBlock != null) level.getBlock(aboveBlock.position, BlockSide.Left) else null

                    val belowBlockUp =
                        if (belowBlock != null) level.getBlock(belowBlock.position, BlockSide.Up) else null
                    val belowBlockRight =
                        if (belowBlock != null) level.getBlock(belowBlock.position, BlockSide.Right) else null
                    val belowBlockDown =
                        if (belowBlock != null) level.getBlock(belowBlock.position, BlockSide.Down) else null
                    val belowBlockLeft =
                        if (belowBlock != null) level.getBlock(belowBlock.position, BlockSide.Left) else null

                    /////

                    if (rightBlock == null || leftBlock == null || upBlock == null || downBlock == null || aboveBlock == null || belowBlock == null)
                    {
                        current.isBorder = true
                    }

                    if (rightBlock == null) current.isRightBorder = true
                    if (leftBlock == null) current.isLeftBorder = true
                    if (upBlock == null) current.isUpBorder = true
                    if (downBlock == null) current.isDownBorder = true
                    if (aboveBlock == null) current.isAboveBorder = true
                    if (belowBlock == null) current.isBelowBorder = true

                    /////

                    if (rightBlock != null)
                    {
                        rightBlock.isLeftBorder = rightBlock.isNotEmpty()

                        rightBlock.isLeftBorderAbove =
                            aboveBlock?.isNotEmpty() ?: true || (aboveBlock?.isEmpty() ?: true && aboveBlockRight?.isEmpty() ?: true)
                        rightBlock.isLeftBorderUp =
                            upBlock?.isNotEmpty() ?: true || (upBlock?.isEmpty() ?: true && upBlockRight?.isEmpty() ?: true)
                        rightBlock.isLeftBorderBelow =
                            belowBlock?.isNotEmpty() ?: true || (belowBlock?.isEmpty() ?: true && belowBlockRight?.isEmpty() ?: true)
                        rightBlock.isLeftBorderDown =
                            downBlock?.isNotEmpty() ?: true || (downBlock?.isEmpty() ?: true && downBlockRight?.isEmpty() ?: true)

                        if (rightBlock !in queue && rightBlock !in checked) queue.add(rightBlock)
                    }

                    if (leftBlock != null)
                    {
                        leftBlock.isRightBorder = leftBlock.isNotEmpty()

                        leftBlock.isRightBorderAbove =
                            aboveBlock?.isNotEmpty() ?: true || (aboveBlock?.isEmpty() ?: true && aboveBlockLeft?.isEmpty() ?: true)
                        leftBlock.isRightBorderUp =
                            upBlock?.isNotEmpty() ?: true || (upBlock?.isEmpty() ?: true && upBlockLeft?.isEmpty() ?: true)
                        leftBlock.isRightBorderBelow =
                            belowBlock?.isNotEmpty() ?: true || (belowBlock?.isEmpty() ?: true && belowBlockLeft?.isEmpty() ?: true)
                        leftBlock.isRightBorderDown =
                            downBlock?.isNotEmpty() ?: true || (downBlock?.isEmpty() ?: true && downBlockLeft?.isEmpty() ?: true)


                        if (leftBlock !in queue && leftBlock !in checked) queue.add(leftBlock)
                    }

                    if (upBlock != null)
                    {
                        upBlock.isDownBorder = upBlock.isNotEmpty()

                        upBlock.isDownBorderAbove =
                            aboveBlock?.isNotEmpty() ?: true || (aboveBlock?.isEmpty() ?: true && aboveBlockUp?.isEmpty() ?: true)
                        upBlock.isDownBorderRight =
                            rightBlock?.isNotEmpty() ?: true || (rightBlock?.isEmpty() ?: true && rightBlockUp?.isEmpty() ?: true)
                        upBlock.isDownBorderBelow =
                            belowBlock?.isNotEmpty() ?: true || (belowBlock?.isEmpty() ?: true && belowBlockUp?.isEmpty() ?: true)
                        upBlock.isDownBorderLeft =
                            leftBlock?.isNotEmpty() ?: true || (leftBlock?.isEmpty() ?: true && leftBlockUp?.isEmpty() ?: true)

                        if (upBlock !in queue && upBlock !in checked) queue.add(upBlock)
                    }

                    if (downBlock != null)
                    {
                        downBlock.isUpBorder = downBlock.isNotEmpty()

                        downBlock.isUpBorderAbove =
                            aboveBlock?.isNotEmpty() ?: true || (aboveBlock?.isEmpty() ?: true && aboveBlockDown?.isEmpty() ?: true)
                        downBlock.isUpBorderRight =
                            rightBlock?.isNotEmpty() ?: true || (rightBlock?.isEmpty() ?: true && rightBlockDown?.isEmpty() ?: true)
                        downBlock.isUpBorderBelow =
                            belowBlock?.isNotEmpty() ?: true || (belowBlock?.isEmpty() ?: true && belowBlockDown?.isEmpty() ?: true)
                        downBlock.isUpBorderLeft =
                            leftBlock?.isNotEmpty() ?: true || (leftBlock?.isEmpty() ?: true && leftBlockDown?.isEmpty() ?: true)

                        if (downBlock !in queue && downBlock !in checked) queue.add(downBlock)
                    }

                    if (aboveBlock != null)
                    {
                        aboveBlock.isBelowBorder = aboveBlock.isNotEmpty()

                        aboveBlock.isBelowBorderUp =
                            upBlock?.isNotEmpty() ?: true || (upBlock?.isEmpty() ?: true && upBlockAbove?.isEmpty() ?: true)
                        aboveBlock.isBelowBorderRight =
                            rightBlock?.isNotEmpty() ?: true || (rightBlock?.isEmpty() ?: true && rightBlockAbove?.isEmpty() ?: true)
                        aboveBlock.isBelowBorderDown =
                            downBlock?.isNotEmpty() ?: true || (downBlock?.isEmpty() ?: true && downBlockAbove?.isEmpty() ?: true)
                        aboveBlock.isBelowBorderLeft =
                            leftBlock?.isNotEmpty() ?: true || (leftBlock?.isEmpty() ?: true && leftBlockAbove?.isEmpty() ?: true)

                        if (aboveBlock !in queue && aboveBlock !in checked) queue.add(aboveBlock)
                    }

                    if (belowBlock != null)
                    {
                        belowBlock.isAboveBorder = belowBlock.isNotEmpty()

                        belowBlock.isAboveBorderUp =
                            upBlock?.isNotEmpty() ?: true || (upBlock?.isEmpty() ?: true && upBlockBelow?.isEmpty() ?: true)
                        belowBlock.isAboveBorderRight =
                            rightBlock?.isNotEmpty() ?: true || (rightBlock?.isEmpty() ?: true && rightBlockBelow?.isEmpty() ?: true)
                        belowBlock.isAboveBorderDown =
                            downBlock?.isNotEmpty() ?: true || (downBlock?.isEmpty() ?: true && downBlockBelow?.isEmpty() ?: true)
                        belowBlock.isAboveBorderLeft =
                            leftBlock?.isNotEmpty() ?: true || (leftBlock?.isEmpty() ?: true && leftBlockBelow?.isEmpty() ?: true)

                        if (belowBlock !in queue && belowBlock !in checked) queue.add(belowBlock)
                    }

                    /////
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

    fun getBlock(roomPosition : Vector3, direction : BlockSide) : KlexBlock?
    {
        return when (direction)
        {
            BlockSide.Right -> getBlock(roomPosition.x.toInt() + 1, roomPosition.y.toInt(), roomPosition.z.toInt())
            BlockSide.Left  -> getBlock(roomPosition.x.toInt() - 1, roomPosition.y.toInt(), roomPosition.z.toInt())
            BlockSide.Up    -> getBlock(roomPosition.x.toInt(), roomPosition.y.toInt() + 1, roomPosition.z.toInt())
            BlockSide.Down  -> getBlock(roomPosition.x.toInt(), roomPosition.y.toInt() - 1, roomPosition.z.toInt())
            BlockSide.Above -> getBlock(roomPosition.x.toInt(), roomPosition.y.toInt(), roomPosition.z.toInt() + 1)
            BlockSide.Below -> getBlock(roomPosition.x.toInt(), roomPosition.y.toInt(), roomPosition.z.toInt() - 1)
        }
    }
}
