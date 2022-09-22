package com.marcin.panklex.moves

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*
import com.marcin.panklex.objects.LadderLikePart
import com.marcin.panklex.objects.ObjectLadder
import com.marcin.panklex.objects.ObjectLadderLike
import kotlin.math.abs

abstract class MoveLadderLike(
    val startLadderLikeMove : Vector3, val endLadderLikeMove : Vector3, objectLadderLike : ObjectLadderLike) :
    Move(startLadderLikeMove, endLadderLikeMove)
{
    val moveStartZ = startLadderLikeMove.z
    val moveEndZ = endLadderLikeMove.z
    val moveDistance = abs(moveStartZ - moveEndZ + 1).toInt()
    val moveDirection = getDirectionZ(startLadderLikeMove, endLadderLikeMove)
    val centerPositions = mutableListOf<Vector3>()

    init
    {
        val range = when (moveDirection)
        {
            DirectionZ.Above -> moveStartZ..moveEndZ
            DirectionZ.Below -> moveEndZ..moveStartZ
            null             -> moveEndZ..moveEndZ
        }

        for (ladderLikePart in objectLadderLike.ladderLikeParts)
        {
            if (ladderLikePart.centerPosition.z in range)
            {
                centerPositions.add(ladderLikePart.centerPosition)
            }
        }
    }

    val endCenterPosition = when (moveDirection)
    {
        DirectionZ.Above -> centerPositions.maxBy { it.z }
        DirectionZ.Below -> centerPositions.minBy { it.z }
        null             -> startLadderLikeMove
    }
    val isEndInCenter = (endLadderLikeMove in centerPositions)

    override fun getCost() : Int
    {
        var cost = 1 * moveDistance
        if (!isEndInCenter) cost += 1
        return cost
    }

    override fun getPositions(positionList : MutableList<Vector3>)
    {
        positionList.addAll(centerPositions)
    }
}
