package com.marcin.panklex

import com.badlogic.gdx.math.Vector3

class KlexRoom(val level : KlexLevel)
{
    var xStart = 0
    var width = 0
    var yStart = 0
    var height = 0
    var zStart = 0
    var floors = 0
    var blocks = mutableListOf<List<List<KlexBlock?>>>()

    fun updateRoom(levelPosition : Vector3)
    {
        xStart = 1
        width = 8
        yStart = 2
        height = 7
        zStart = 1
        floors = 5

        blocks.clear()

        for (k in zStart..2)
        {
            val floor = mutableListOf<List<KlexBlock?>>()

            for (j in yStart until yStart + height)
            {
                val row = mutableListOf<KlexBlock?>()

                for (i in xStart..4)
                {
                    row.add(null)
                }

                for (i in 5 until xStart + width)
                {
                    row.add(level.getBlock(i, j, k))
                }

                floor.add(row)
            }

            blocks.add(floor)
        }

        for (k in 3 until zStart + floors)
        {
            val floor = mutableListOf<List<KlexBlock?>>()

            for (j in yStart until yStart + height)
            {
                val row = mutableListOf<KlexBlock?>()

                for (i in xStart until xStart + width)
                {
                    row.add(level.getBlock(i, j, k))
                }

                floor.add(row)
            }

            blocks.add(floor)
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
