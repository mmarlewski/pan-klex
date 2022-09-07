package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class ObjectLadder(val originPosition : Vector3, var ladderDirection : Direction2d, var ladderLength : Int) :
    Object("ladder")
{
    val ladderPositions = mutableListOf<Vector3>()

    init
    {
        for (i in 1 until ladderLength)
        {
            ladderPositions.add(Vector3(originPosition.x, originPosition.y, originPosition.z - i))
        }
    }

    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        positions.add(originPosition)
        positions.addAll(ladderPositions)
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        positions.add(originPosition)
        positions.addAll(ladderPositions)
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : HashMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val relativeLadderDirection = objectiveToRelativeDirection2d(ladderDirection, mapDirection)

        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = null
        spaceLayerTiles[SpaceLayer.SideUp] = null
        spaceLayerTiles[SpaceLayer.Behind] = when(relativeLadderDirection)
        {
            Direction2d.Left -> tiles.ladderLeft
            Direction2d.Up   -> tiles.ladderUp
            else             -> null
        }
        spaceLayerTiles[SpaceLayer.Before] = when (relativeLadderDirection)
        {
            Direction2d.Down  -> tiles.ladderDown
            Direction2d.Right -> tiles.ladderRight
            else              -> null
        }
        spaceLayerTiles[SpaceLayer.SideDown] = null
        spaceLayerTiles[SpaceLayer.SideRight] = null
        spaceLayerTiles[SpaceLayer.SideAbove] = null
    }

    override fun getSideTransparency(spaceSideTransparency : HashMap<Direction3d, Boolean>, spacePosition : Vector3)
    {
        spaceSideTransparency[Direction3d.Below] = true
        spaceSideTransparency[Direction3d.Left] = true
        spaceSideTransparency[Direction3d.Up] = true
        spaceSideTransparency[Direction3d.Down] = true
        spaceSideTransparency[Direction3d.Right] = true
        spaceSideTransparency[Direction3d.Above] = true
    }
}