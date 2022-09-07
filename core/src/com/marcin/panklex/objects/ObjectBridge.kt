package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class ObjectBridge(val bridgePosition : Vector3, var bridgeDirection : Direction2d) : Object("bridge")
{
    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        positions.add(bridgePosition)
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        positions.add(bridgePosition)
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : HashMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val relativeBridgeDirection2d = objectiveToRelativeDirection2d(bridgeDirection, mapDirection)

        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = null
        spaceLayerTiles[SpaceLayer.SideUp] = null
        spaceLayerTiles[SpaceLayer.Behind] = null
        spaceLayerTiles[SpaceLayer.Before] = tiles.bridgeUpBefore
        spaceLayerTiles[SpaceLayer.SideDown] = when (relativeBridgeDirection2d)
        {
            Direction2d.Left, Direction2d.Right -> tiles.bridgeRightDown
            Direction2d.Up, Direction2d.Down    -> tiles.bridgeUpDown
        }
        spaceLayerTiles[SpaceLayer.SideRight] = when (relativeBridgeDirection2d)
        {
            Direction2d.Left, Direction2d.Right -> tiles.bridgeRightRight
            Direction2d.Up, Direction2d.Down    -> tiles.bridgeUpRight
        }
        spaceLayerTiles[SpaceLayer.SideAbove] = when (relativeBridgeDirection2d)
        {
            Direction2d.Left, Direction2d.Right -> tiles.bridgeRightAbove
            Direction2d.Up, Direction2d.Down    -> tiles.bridgeUpAbove
        }
    }

    override fun getSideTransparency(spaceSideTransparency : HashMap<Direction3d, Boolean>, spacePosition : Vector3)
    {
        spaceSideTransparency[Direction3d.Below] = true
        spaceSideTransparency[Direction3d.Left] = true
        spaceSideTransparency[Direction3d.Up] = true
        spaceSideTransparency[Direction3d.Down] = true
        spaceSideTransparency[Direction3d.Right] = true
        spaceSideTransparency[Direction3d.Above] = false
    }
}
