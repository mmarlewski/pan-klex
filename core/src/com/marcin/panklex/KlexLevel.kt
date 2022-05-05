package com.marcin.panklex

import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.entities.EntityPlayer

class LevelBlock
{
    var position = Vector3()
    var entity: BaseEntity? = null
    var rock = Block.Empty
    var depth = 0

    fun isEntity(): Boolean = entity != null
    fun isRock(): Boolean = rock != Block.Empty
    fun isGap(): Boolean = depth > 0
}

class JsonLevel
{
    var version: String = ""
    var name: String = ""
    var levels: Int = 0
    var width: Int = 0
    var height: Int = 0
    var map: Array<Array<Array<Int>>> = emptyArray<Array<Array<Int>>>()
}

class KlexLevel
{
    var version = "0.0.0"
    var name = "empty"
    var levels = 0
    var width = 0
    var height = 0
    var map = emptyList<List<List<LevelBlock>>>()
    var entities = emptyList<BaseEntity>()
    var playerEntity = EntityPlayer()
}