package com.marcin.panklex.objects

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class ObjectStation(val stationPosition : Vector3, val upgrade : PlayerUpgrade) : Object("station")
{
    var isUpgradeTaken = false

    val actionSwitchToStationScreen = Action(
        "add upgrade",
        "( change screen )")
    {
        val entityPlayer = it.level.entityPlayer

        it.game.screenStation.setStationAndPlayer(this, entityPlayer)
        it.game.screenStation.updateWidgets()
        it.game.changeScreen(it.game.screenStation)
    }

    override fun getOccupiedPositions(positions : MutableList<Vector3>)
    {
        positions.add(stationPosition)
    }

    override fun getPresentPositions(positions : MutableList<Vector3>)
    {
        positions.add(stationPosition)
    }

    override fun getTiles(
        tiles : Tiles, spaceLayerTiles : MutableMap<SpaceLayer, TiledMapTile?>, spacePosition : Vector3,
        mapDirection : Direction2d)
    {
        spaceLayerTiles[SpaceLayer.SideBelow] = null
        spaceLayerTiles[SpaceLayer.SideLeft] = null
        spaceLayerTiles[SpaceLayer.SideUp] = null
        spaceLayerTiles[SpaceLayer.SideDown] = tiles.stationDown
        spaceLayerTiles[SpaceLayer.SideRight] = tiles.stationRight
        spaceLayerTiles[SpaceLayer.SideAbove] = tiles.stationAbove
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
        actionArray[0] = actionSwitchToStationScreen.apply { isActionPossible=true }
    }
}
