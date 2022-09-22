package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class ObjectChest(val chestPosition : Vector3, val chestDirection : Direction2d) : Object("chest")
{
    val chestItems = mutableMapOf<PlayerItem, Int>().apply {
        for (item in PlayerItem.values())
            this[item] = 0
    }

    val actionSwitchToContainerScreen = Action(
        "search chest",
        "( change screen )")
    {
        val objectChest = it.mouseObject as ObjectChest
        val entityPlayer = it.level.entityPlayer

        it.game.screenContainer.setChestAndPlayer(objectChest, entityPlayer)
        it.game.screenContainer.updateWidgets()
        it.game.changeScreen(it.game.screenContainer)
    }

    fun getChestItem(item : PlayerItem) : Int
    {
        return chestItems[item]!!
    }

    fun changeChestItem(item : PlayerItem, by : Int)
    {
        var count = chestItems[item]!!
        count += by
        chestItems[item] = count
    }

    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        positions.add(chestPosition)
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        positions.add(chestPosition)
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : MutableMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        val relativeChestDirection = objectiveToRelativeDirection2d(chestDirection, mapDirection)

        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = null
        spaceLayerTiles[SpaceLayer.SideUp] = null
        spaceLayerTiles[SpaceLayer.Behind] = tiles.chestSideBehind
        spaceLayerTiles[SpaceLayer.Before] = tiles.chestSideBefore
        spaceLayerTiles[SpaceLayer.SideDown] = when (relativeChestDirection)
        {
            Direction2d.Down -> tiles.chestFrontDown
            Direction2d.Up   -> tiles.chestBackDown
            else             -> tiles.chestSideDown
        }
        spaceLayerTiles[SpaceLayer.SideRight] = when (relativeChestDirection)
        {
            Direction2d.Right -> tiles.chestFrontRight
            Direction2d.Left  -> tiles.chestBackRight
            else              -> tiles.chestSideRight
        }
        spaceLayerTiles[SpaceLayer.SideAbove] = tiles.chestSideAbove
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
        actionArray[0] = actionSwitchToContainerScreen.apply { isActionPossible = true }
    }
}
