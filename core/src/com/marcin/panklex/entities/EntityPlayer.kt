package com.marcin.panklex.entities

import com.badlogic.gdx.maps.tiled.TiledMapTile
import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.*

class EntityPlayer : Entity("player")
{
    var playerPosition = Vector3()

    val playerItems = mutableMapOf<PlayerItem, Int>().apply {
        for (item in PlayerItem.values())
            this[item] = 0
    }

    val playerUpgrades = mutableMapOf<PlayerUpgrade, Boolean>().apply {
        for (upgrade in PlayerUpgrade.values())
            this[upgrade] = false
    }

    fun setPosition(x : Float, y : Float, z : Float)
    {
        playerPosition.set(x, y, z)
    }

    fun setPosition(position : Vector3)
    {
        playerPosition.set(position)
    }

    fun getPlayerItem(item : PlayerItem) : Int
    {
        return playerItems[item] ?: 0
    }

    fun changePlayerItem(item : PlayerItem, by : Int)
    {

        var count = playerItems[item]!!
        count += by
        playerItems[item] = count
    }

    fun hasPlayerUpgrade(upgrade : PlayerUpgrade) : Boolean
    {
        return playerUpgrades[upgrade] ?: false
    }

    fun changePlayerUpgrade(upgrade : PlayerUpgrade, isUnlocked : Boolean)
    {
        playerUpgrades[upgrade] = isUnlocked
    }

    override fun getOccupiedPosition() : Vector3
    {
        return playerPosition
    }

    override fun getTiles(tiles : Tiles, spaceLayerTiles : MutableMap<SpaceLayer, TiledMapTile?>)
    {
        spaceLayerTiles[SpaceLayer.EntityWhole] = tiles.playerWhole
        spaceLayerTiles[SpaceLayer.EntityOutline] = tiles.playerOutline
    }
}
