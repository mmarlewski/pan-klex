package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class ObjectExtendableLadder(val originPosition : Vector3, var ladderDirection : Direction2d, var ladderLength : Int) :
    Object("extendableLadder")
{
    val extensionPositions = mutableListOf<Vector3>()

    init
    {
        for (i in 1 until ladderLength)
        {
            extensionPositions.add(Vector3(originPosition.x, originPosition.y, originPosition.z - i))
        }
    }

    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        positions.add(originPosition)
        positions.addAll(extensionPositions)
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        positions.add(originPosition)
        positions.addAll(extensionPositions)
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : HashMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val relativeLadderDirection = objectiveToRelativeDirection2d(ladderDirection, mapDirection)

        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = null
        spaceLayerTiles[SpaceLayer.SideUp] = null
        spaceLayerTiles[SpaceLayer.Behind] = when (spacePosition)
        {
            originPosition        -> when (relativeLadderDirection)
            {
                Direction2d.Up    -> tiles.extendableLadderUpBehind
                Direction2d.Right -> tiles.extendableLadderRightBehind
                Direction2d.Down  -> tiles.extendableLadderDownBehind
                Direction2d.Left  -> tiles.extendableLadderLeftBehind
            }
            in extensionPositions -> when (relativeLadderDirection)
            {
                Direction2d.Left -> tiles.extendableLadderLadderLeft
                Direction2d.Up   -> tiles.extendableLadderLadderUp
                else             -> null
            }
            else                  -> null
        }
        spaceLayerTiles[SpaceLayer.Before] = when (spacePosition)
        {
            originPosition        -> when (relativeLadderDirection)
            {
                Direction2d.Up    -> tiles.extendableLadderUpBefore
                Direction2d.Right -> tiles.extendableLadderRightBefore
                Direction2d.Down  -> tiles.extendableLadderDownBefore
                Direction2d.Left  -> tiles.extendableLadderLeftBefore
            }
            in extensionPositions -> when (relativeLadderDirection)
            {
                Direction2d.Down  -> tiles.extendableLadderLadderDown
                Direction2d.Right -> tiles.extendableLadderLadderRight
                else              -> null
            }
            else                  -> null
        }
        spaceLayerTiles[SpaceLayer.SideDown] = when (spacePosition)
        {
            originPosition -> when (relativeLadderDirection)
            {
                Direction2d.Up    -> tiles.extendableLadderUpDown
                Direction2d.Right -> tiles.extendableLadderRightDown
                Direction2d.Down  -> tiles.extendableLadderDownDown
                Direction2d.Left  -> tiles.extendableLadderLeftDown
            }
            else           -> null
        }
        spaceLayerTiles[SpaceLayer.SideRight] = when (spacePosition)
        {
            originPosition -> when (relativeLadderDirection)
            {
                Direction2d.Up    -> tiles.extendableLadderUpRight
                Direction2d.Right -> tiles.extendableLadderRightRight
                Direction2d.Down  -> tiles.extendableLadderDownRight
                Direction2d.Left  -> tiles.extendableLadderLeftRight
            }
            else           -> null
        }
        spaceLayerTiles[SpaceLayer.SideAbove] = when (spacePosition)
        {
            originPosition -> when (relativeLadderDirection)
            {
                Direction2d.Up    -> tiles.extendableLadderUpAbove
                Direction2d.Right -> tiles.extendableLadderRightAbove
                Direction2d.Down  -> tiles.extendableLadderDownAbove
                Direction2d.Left  -> tiles.extendableLadderLeftAbove
            }
            else           -> null
        }
    }

    override fun getSideTransparency(spaceSideTransparency : HashMap<Direction3d, Boolean>, spacePosition : Vector3)
    {
        when (spacePosition)
        {
            originPosition        ->
            {
                spaceSideTransparency[Direction3d.Below] = true
                spaceSideTransparency[Direction3d.Left] = true
                spaceSideTransparency[Direction3d.Up] = true
                spaceSideTransparency[Direction3d.Down] = true
                spaceSideTransparency[Direction3d.Right] = true
                spaceSideTransparency[Direction3d.Above] = true

                spaceSideTransparency[direction2dToDirection3d(ladderDirection)] = false
            }
            in extensionPositions ->
            {
                spaceSideTransparency[Direction3d.Below] = true
                spaceSideTransparency[Direction3d.Left] = true
                spaceSideTransparency[Direction3d.Up] = true
                spaceSideTransparency[Direction3d.Down] = true
                spaceSideTransparency[Direction3d.Right] = true
                spaceSideTransparency[Direction3d.Above] = true
            }
        }
    }
}