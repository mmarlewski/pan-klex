package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class ObjectVendingMachine(var vendingMachinePosition : Vector3, var vendingMachineDirection : Direction2d) :
    Object("vending machine")
{
    var isVendingMachineBroken = false

    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        positions.add(vendingMachinePosition)
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        positions.add(vendingMachinePosition)
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : HashMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val vendingMachineRelativeDirection = objectiveToRelativeDirection2d(vendingMachineDirection, mapDirection)

        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = null
        spaceLayerTiles[SpaceLayer.SideUp] = null
        spaceLayerTiles[SpaceLayer.SideDown] = when
        {
            vendingMachineRelativeDirection == Direction2d.Down && isVendingMachineBroken  -> tiles.vendingMachineScreenBrokenDown
            vendingMachineRelativeDirection == Direction2d.Down && !isVendingMachineBroken -> tiles.vendingMachineScreenDown
            vendingMachineRelativeDirection != Direction2d.Down && isVendingMachineBroken  -> tiles.vendingMachineBlankBrokenDown
            vendingMachineRelativeDirection != Direction2d.Down && !isVendingMachineBroken -> tiles.vendingMachineBlankDown
            else                                                                           -> null
        }
        spaceLayerTiles[SpaceLayer.SideRight] = when
        {
            vendingMachineRelativeDirection == Direction2d.Right && isVendingMachineBroken  -> tiles.vendingMachineScreenBrokenRight
            vendingMachineRelativeDirection == Direction2d.Right && !isVendingMachineBroken -> tiles.vendingMachineScreenRight
            vendingMachineRelativeDirection != Direction2d.Right && isVendingMachineBroken  -> tiles.vendingMachineBlankBrokenRight
            vendingMachineRelativeDirection != Direction2d.Right && !isVendingMachineBroken -> tiles.vendingMachineBlankRight
            else                                                                            -> null
        }
        spaceLayerTiles[SpaceLayer.SideAbove] =
            if (isVendingMachineBroken) tiles.vendingMachineBlankBrokenAbove else tiles.vendingMachineBlankAbove
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

    override fun canStoreEntity(spacePosition : Vector3) : Boolean
    {
        return false
    }

    override fun isGround(spacePosition : Vector3) : Boolean
    {
        return true
    }

    override fun getMoves(moveList : MutableList<Move>, spacePosition : Vector3, room : Room)
    {
        //
    }
}
