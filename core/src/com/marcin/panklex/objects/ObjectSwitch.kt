package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class ObjectSwitch(var switchPosition : Vector3, var switchDirection : Direction2d) : Object("switch")
{
    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        positions.add(switchPosition)
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        positions.add(switchPosition)
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : HashMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val vendingMachineRelativeDirection = objectiveToRelativeDirection2d(switchDirection, mapDirection)

        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = when (vendingMachineRelativeDirection)
        {
            Direction2d.Left -> tiles.switchButtonLeft
            else             -> null
        }
        spaceLayerTiles[SpaceLayer.SideUp] = when (vendingMachineRelativeDirection)
        {
            Direction2d.Up -> tiles.switchButtonUp
            else           -> null
        }
        spaceLayerTiles[SpaceLayer.SideDown] = when (vendingMachineRelativeDirection)
        {
            Direction2d.Down -> tiles.switchButtonDown
            else             -> tiles.switchBlankDown
        }
        spaceLayerTiles[SpaceLayer.SideRight] = when (vendingMachineRelativeDirection)
        {
            Direction2d.Right -> tiles.switchButtonRight
            else              -> tiles.switchBlankRight
        }
        spaceLayerTiles[SpaceLayer.SideAbove] = tiles.switchBlankAbove
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
