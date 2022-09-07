package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class ObjectColumn(val columnPosition : Vector3) : Object("column")
{
    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        positions.add(columnPosition)
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        positions.add(columnPosition)
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : HashMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = null
        spaceLayerTiles[SpaceLayer.SideUp] = null
        spaceLayerTiles[SpaceLayer.Behind] = tiles.columnBehind
        spaceLayerTiles[SpaceLayer.Before] = tiles.columnBefore
        spaceLayerTiles[SpaceLayer.SideDown] = tiles.columnDown
        spaceLayerTiles[SpaceLayer.SideRight] = tiles.columnRight
        spaceLayerTiles[SpaceLayer.SideAbove] = tiles.columnAbove
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
