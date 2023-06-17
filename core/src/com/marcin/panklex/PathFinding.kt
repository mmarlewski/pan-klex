package com.marcin.panklex

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3
import kotlin.math.pow
import kotlin.math.sqrt

class PathFinding(val room : Room)
{
    val startPosition = Vector3()
    val endPosition = Vector3()
    val path = mutableListOf<Space>()
    var isPathFound = false

    fun getDistance(position1 : Vector3, position2 : Vector3) : Int
    {
        var sum = (position2.x - position1.x).pow(2)
        sum += (position2.y - position1.y).pow(2)
        sum += (position2.z - position1.z).pow(2)

        return (sqrt(sum)).toInt()
    }

    fun findPath(start : Vector3, end : Vector3)
    {
        Gdx.app.log("pathFinding", "finding path...")

        // init

        startPosition.set(start)
        endPosition.set(end)
        path.clear()
        isPathFound = false

        val queue = mutableListOf<Space>()
        val checked = mutableListOf<Space>()
        val startSpace = room.getSpace(startPosition)
        val endSpace = room.getSpace(endPosition)
        if (startSpace != null)
        {
            queue.add(startSpace)
        }

        while (queue.isNotEmpty())
        {
            // space with lowest full cost

            var current = queue.first()
            var lowestFullCost = current.globalCost + current.localCost

            for (space in queue)
            {
                val spaceFullCost = space.globalCost + space.localCost
                if (spaceFullCost < lowestFullCost)
                {
                    lowestFullCost = spaceFullCost
                    current = space
                }
            }

            // current checked

            queue.remove(current)
            checked.add(current)

            // path found

            if (current.position == endPosition)
            {
                isPathFound = true
                break
            }

            // new spaces

            for (move in current.getMoves(room))
            {
                val moveEndSpace = room.getSpace(move.moveEnd)

                if (moveEndSpace in checked || moveEndSpace == null)
                {
                    continue
                }

                val moveEndSpaceCost = moveEndSpace.globalCost + moveEndSpace.localCost
                val newMoveEndSpaceGlobalCost = current.globalCost + move.getCost()
                val newMoveEndSpaceLocalCost = getDistance(moveEndSpace.position, endPosition)

                if (newMoveEndSpaceGlobalCost + newMoveEndSpaceLocalCost < moveEndSpaceCost || moveEndSpace !in queue)
                {
                    moveEndSpace.globalCost = newMoveEndSpaceGlobalCost
                    moveEndSpace.localCost = newMoveEndSpaceLocalCost
                    moveEndSpace.parentSpace = current
                    moveEndSpace.parentMove = move

                    if (moveEndSpace !in queue)
                    {
                        queue.add(moveEndSpace)
                    }
                }
            }
        }

        // recreating path

        if (isPathFound)
        {
            var pathSpace = endSpace
            var move = pathSpace?.move
            val positionList = mutableListOf<Vector3>()
            var positionSpace : Space?

            while (pathSpace != null)
            {
                positionList.clear()
                move?.getPositions(positionList)
                for (position in positionList)
                {
                    positionSpace = room.getSpace(position)
                    positionSpace?.isOnPath = true
                    positionSpace?.move = move
                }
                path.add(pathSpace)
                move = pathSpace.parentMove
                pathSpace = pathSpace.parentSpace
            }

            path.reverse()
        }

        when (isPathFound)
        {
            true  -> Gdx.app.log("pathFinding", "path found")
            false -> Gdx.app.log("pathFinding", "path not found")
        }
    }
}
