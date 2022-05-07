package com.marcin.panklex

import com.badlogic.gdx.math.Vector3
import com.marcin.panklex.entities.EntityPlayer

class LevelBlock
{
    var position = Vector3()
    var entity: BaseEntity? = null
    var rock = Block.Empty
    var depth = 0

    fun isEntity() = entity != null
    fun isRock() = rock != Block.Empty
    fun isGap() = depth > 0
}

class JsonLevel
{
    var version = ""
    var name = ""
    var levels = 0
    var width = 0
    var height = 0
    var map = emptyArray<Array<Array<Int>>>()
    var entities = emptyList<BaseEntity>()
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