package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class ObjectStairs(val stairsPosition : Vector3, var stairsDirection : Direction2d) : Object("stairs")
{
    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        positions.add(stairsPosition)
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        positions.add(stairsPosition)
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : HashMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val relativeStairsDirection2d = objectiveToRelativeDirection2d(stairsDirection, mapDirection)

        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = null
        spaceLayerTiles[SpaceLayer.SideUp] = null
        spaceLayerTiles[SpaceLayer.Behind] = when (relativeStairsDirection2d)
        {
            Direction2d.Up    -> tiles.stairsUpBehind
            Direction2d.Right -> tiles.stairsRightBehind
            Direction2d.Down  -> tiles.stairsDownBehind
            Direction2d.Left  -> tiles.stairsLeftBehind
        }
        spaceLayerTiles[SpaceLayer.Before] = when (relativeStairsDirection2d)
        {
            Direction2d.Up    -> tiles.stairsUpBefore
            Direction2d.Right -> tiles.stairsRightBefore
            Direction2d.Down  -> tiles.stairsDownBefore
            Direction2d.Left  -> tiles.stairsLeftBefore
        }
        spaceLayerTiles[SpaceLayer.SideDown] = when (relativeStairsDirection2d)
        {
            Direction2d.Up    -> tiles.stairsUpDown
            Direction2d.Right -> tiles.stairsRightDown
            Direction2d.Down  -> tiles.stairsDownDown
            Direction2d.Left  -> tiles.stairsLeftDown
        }
        spaceLayerTiles[SpaceLayer.SideRight] = when (relativeStairsDirection2d)
        {
            Direction2d.Up    -> tiles.stairsUpRight
            Direction2d.Right -> tiles.stairsRightRight
            Direction2d.Down  -> tiles.stairsDownRight
            Direction2d.Left  -> tiles.stairsLeftRight
        }
        spaceLayerTiles[SpaceLayer.SideAbove] = when (relativeStairsDirection2d)
        {
            Direction2d.Up    -> tiles.stairsUpAbove
            Direction2d.Right -> tiles.stairsRightAbove
            Direction2d.Down  -> tiles.stairsDownAbove
            Direction2d.Left  -> tiles.stairsLeftAbove
        }
    }

    override fun getSideTransparency(spaceSideTransparency : HashMap<Direction3d, Boolean>, spacePosition : Vector3)
    {
        spaceSideTransparency[Direction3d.Below] = false
        spaceSideTransparency[Direction3d.Left] = true
        spaceSideTransparency[Direction3d.Up] = true
        spaceSideTransparency[Direction3d.Down] = true
        spaceSideTransparency[Direction3d.Right] = true
        spaceSideTransparency[Direction3d.Above] = true

        spaceSideTransparency[direction2dToDirection3d(stairsDirection)] = false
    }
}
