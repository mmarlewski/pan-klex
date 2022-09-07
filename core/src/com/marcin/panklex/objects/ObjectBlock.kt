package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class ObjectBlock(val blockPosition : Vector3) : Object("block")
{
    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        positions.add(blockPosition)
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        positions.add(blockPosition)
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : HashMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = null
        spaceLayerTiles[SpaceLayer.SideUp] = null
        spaceLayerTiles[SpaceLayer.Behind] = null
        spaceLayerTiles[SpaceLayer.Before] = null
        spaceLayerTiles[SpaceLayer.SideDown] = tiles.blockDown
        spaceLayerTiles[SpaceLayer.SideRight] = tiles.blockRight
        spaceLayerTiles[SpaceLayer.SideAbove] = tiles.blockAbove
    }

    override fun getSideTransparency(spaceSideTransparency : HashMap<Direction3d, Boolean>, spacePosition : Vector3)
    {
        spaceSideTransparency[Direction3d.Below] = false
        spaceSideTransparency[Direction3d.Left] = false
        spaceSideTransparency[Direction3d.Up] = false
        spaceSideTransparency[Direction3d.Down] = false
        spaceSideTransparency[Direction3d.Right] = false
        spaceSideTransparency[Direction3d.Above] = false
    }
}
