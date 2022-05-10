package com.marcin.panklex

import kotlin.math.abs

class Node(val x: Int, val y: Int)
{
    var parent: Node? = null
    val neighbours = mutableListOf<Node>()
    var gCost = 0
    var hCost = 0
    val fCost get() = gCost + hCost
}

class KlexPathFinding(val level: KlexLevel)
{
    val map = mutableListOf<List<Node>>()
    var isTraversable = false

    fun setUpMap()
    {
        // create

        for (i in 0..level.height - 1)
        {
            val list = mutableListOf<Node>()

            for (j in 0..level.width - 1)
            {
                list.add(Node(j, i))
            }

            map.add(list)
        }

        // neighbours

        for (i in 0..level.height - 1)
        {
            for (j in 0..level.width - 1)
            {
                val node = map[j][i]

                if (i > 0) node.neighbours.add(map[j][i - 1])
                if (i < level.height - 1) node.neighbours.add(map[j][i + 1])
                if (j > 0) node.neighbours.add(map[j - 1][i])
                if (j < level.width - 1) node.neighbours.add(map[j + 1][i])
            }
        }
    }

    fun isBlockTraversable(block: LevelBlock): Boolean
    {
        val z = block.position.z.toInt()
        val y = block.position.y.toInt()
        val x = block.position.x.toInt()

        val blockAbove: LevelBlock? = if (z < level.levels - 1) level.map[z + 1][y][x] else null

        return block.rock != Block.Empty && block.entity == null && if (blockAbove == null) false else blockAbove.rock == Block.Empty
    }

    fun distance(a: Node, b: Node): Int
    {
        var distance = 0

        val yDistance = abs(a.y - b.y)
        val xDistance = abs(a.x - b.x)

        distance =
                if (yDistance < xDistance) (14 * yDistance + 10 * (xDistance - yDistance)) else (14 * xDistance + 10 * (yDistance - xDistance))

        return distance
    }

    fun findPath(xStart: Int, yStart: Int, xEnd: Int, yEnd: Int, currentLevel: Int): List<Node>
    {
        isTraversable = false

        val start = map[yStart][xStart]
        val end = map[yEnd][xEnd]
        var current = start
        val open = mutableListOf<Node>()
        val closed = mutableListOf<Node>()
        val path = mutableListOf<Node>()

        open.add(start)

        while (open.isNotEmpty())
        {
            current = open.first()

            for (n in open)
            {
                if (current.fCost > n.fCost)
                {
                    current = n
                }
                else if (current.fCost == n.fCost)
                {
                    if (current.hCost > n.hCost)
                    {
                        current = n
                    }
                }
            }

            open.remove(current)
            closed.add(current)

            if (current == end)
            {
                isTraversable = true
                break
            }

            for (n in current.neighbours)
            {
                if (n !in closed && isBlockTraversable(level.map[currentLevel][n.y][n.x]))
                {
                    if (current.gCost + distance(current, n) < n.gCost || n !in open)
                    {
                        n.gCost = current.gCost + distance(current, n)
                        n.hCost = distance(end, n)
                        n.parent = current

                        if (n !in open)
                        {
                            open.add(n)
                        }
                    }
                }
            }
        }

        var node = current

        while (node != start)
        {
            path.add(node)
            node = node.parent!!
        }

        path.reverse()

        return path
    }
}
