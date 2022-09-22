package com.marcin.panklex.objects

import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*
import com.marcin.panklex.moves.MoveLadderLike

class LadderLikePart(val centerPosition : Vector3, val endPositionDirection : Dist1Conn = Dist1Conn.Center)
{
    val ladderPartEndPositions = mutableMapOf<Dist1Conn, Vector3>().apply {
        when (endPositionDirection)
        {
            Dist1Conn.Center ->
            {
                for (direction in Dist1Conn.values())
                    this[direction] = getPositionDist1Conn(centerPosition, direction)
            }
            else             ->
            {
                this[endPositionDirection] = getPositionDist1Conn(centerPosition, endPositionDirection)
            }
        }
    }

    val centerMoves = mutableListOf<MoveLadderLike>()
}

abstract class ObjectLadderLike(
    val ladderLikeOriginPosition : Vector3, val ladderLikeDirection : Direction2d, val ladderLikeLength : Int,
    val ladderLikeType : String) : Object(ladderLikeType)
{
    val topEndPositionX = when (ladderLikeDirection)
    {
        Direction2d.Up    -> ladderLikeOriginPosition.x
        Direction2d.Right -> ladderLikeOriginPosition.x + 1
        Direction2d.Down  -> ladderLikeOriginPosition.x
        Direction2d.Left  -> ladderLikeOriginPosition.x - 1
    }
    val topEndPositionY = when (ladderLikeDirection)
    {
        Direction2d.Up    -> ladderLikeOriginPosition.y + 1
        Direction2d.Right -> ladderLikeOriginPosition.y
        Direction2d.Down  -> ladderLikeOriginPosition.y - 1
        Direction2d.Left  -> ladderLikeOriginPosition.y
    }
    val topEndPositionZ = ladderLikeOriginPosition.z + 1

    //val topPosition=Vector3(ladderLikeOriginPosition.x,ladderLikeOriginPosition.y,ladderLikeOriginPosition.z+1)
    val ladderLikeParts = mutableListOf<LadderLikePart>()
    val centerPositions = mutableListOf<Vector3>()
    val endPositions = mutableListOf<Vector3>()

    init
    {
        val topLadderLikePart = LadderLikePart(ladderLikeOriginPosition, direction2dToDist1Conn(ladderLikeDirection))
        ladderLikeParts.add(topLadderLikePart)
        centerPositions.add(topLadderLikePart.centerPosition)
        endPositions.addAll(topLadderLikePart.ladderPartEndPositions.values)

        for (i in 1 until ladderLikeLength)
        {
            val partCenterPosition =
                Vector3(ladderLikeOriginPosition.x, ladderLikeOriginPosition.y, ladderLikeOriginPosition.z - i)
            val ladderLikePart = LadderLikePart(partCenterPosition)
            ladderLikeParts.add(ladderLikePart)
            centerPositions.add(partCenterPosition)
            endPositions.addAll(ladderLikePart.ladderPartEndPositions.values)
        }
    }

    abstract fun createMoves()

    fun getLadderLikePart(centerPosition : Vector3) : LadderLikePart?
    {
        var part : LadderLikePart? = null

        for (ladderLikePart in ladderLikeParts)
        {
            if (centerPosition == ladderLikePart.centerPosition)
            {
                part = ladderLikePart
            }
        }

        return part
    }
}
