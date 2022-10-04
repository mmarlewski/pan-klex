package com.marcin.panklex

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.round

class LineFinding(val room : Room)
{
    val startPosition = Vector3()
    val endPosition = Vector3()
    val linePositions = mutableListOf<Vector3>()
    var isLineFound = false
    var isLineObstructed = true

    fun diagonalDistance(start : Vector3, end : Vector3) : Int
    {
        val dx = end.x - start.x
        val dy = end.y - start.y
        val dz = end.z - start.z
        return max(max(abs(dx), abs(dy)), abs(dz)).toInt()
    }

    fun lerpPosition(start : Vector3, end : Vector3, t : Float) : Vector3
    {
        return Vector3(round(lerp(start.x, end.x, t)), round(lerp(start.y, end.y, t)), round(lerp(start.z, end.z, t)))
    }

    fun lerp(start : Float, end : Float, t : Float) : Float
    {
        return start + t * (end - start)
    }

    fun findLine(start : Vector3, end : Vector3)
    {
        Gdx.app.log("lineFinding", "finding line...")

        startPosition.set(start)
        endPosition.set(end)
        linePositions.clear()
        isLineFound = false
        linePositions.add(start)
        linePositions.add(end)

        val startSpace = room.getSpace(startPosition)
        val endSpace = room.getSpace(endPosition)
        val distance = diagonalDistance(startPosition, endPosition)

        if (startSpace != null && endSpace != null && distance > 0)
        {
            for (i in 0..distance)
            {
                linePositions.add(lerpPosition(startPosition, endPosition, i.toFloat() / distance))
            }

            isLineFound = true
        }

        isLineObstructed = false

        for (position in linePositions)
        {
            if (!isLineObstructed)
            {
                if (position != startPosition && position != endPosition)
                {
                    val space = room.getSpace(position)
                    if (space != null)
                    {
                        isLineObstructed = space.objectOccupying != null
                    }
                    else
                    {
                        isLineObstructed = true
                    }
                }
            }
        }

        when (isLineFound)
        {
            true  -> Gdx.app.log("lineFinding", "line found")
            false -> Gdx.app.log("lineFinding", "line not found")
        }
    }
}
