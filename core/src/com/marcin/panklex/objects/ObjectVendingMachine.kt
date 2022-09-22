package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class ObjectVendingMachine(var vendingMachinePosition : Vector3, var vendingMachineDirection : Direction2d) :
    Object("vending machine")
{
    var isVendingMachineBroken = false

    val vendingMachineItems = mutableMapOf<PlayerItem, Int>().apply {
        for (item in PlayerItem.values())
            this[item] = 0
    }

    val actionSwitchToVendingMachineScreen = Action(
        "buy items",
        "( change screen )")
    {
        val objectVendingMachine = it.mouseObject as ObjectVendingMachine
        val entityPlayer = it.level.entityPlayer

        it.game.screenVendingMachine.setVendingMachineAndPlayer(objectVendingMachine, entityPlayer)
        it.game.screenVendingMachine.updateWidgets()
        it.game.changeScreen(it.game.screenVendingMachine)
    }

    fun getVendingMachineItem(item : PlayerItem) : Int
    {
        return vendingMachineItems[item] ?: 0
    }

    fun changeVendingMachineItem(item : PlayerItem, by : Int)
    {
        var count = vendingMachineItems[item]!!
        count += by
        vendingMachineItems[item] = count
    }

    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        positions.add(vendingMachinePosition)
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        positions.add(vendingMachinePosition)
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : MutableMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
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

    override fun getSideTransparency(spaceSideTransparency : MutableMap<Direction3d, Boolean>, spacePosition : Vector3)
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

    override fun getActions(actionArray : Array<Action?>, spacePosition : Vector3)
    {
        actionArray[0] = actionSwitchToVendingMachineScreen.apply { isActionPossible = true }
    }
}
