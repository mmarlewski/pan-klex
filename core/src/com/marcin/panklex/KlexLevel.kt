package com.marcin.panklex

import com.badlogic.gdx.math.Vector3

class LevelBlock
{
    var position = Vector3()
    var entity : BaseEntity? = null
    var rock = Block.Empty
    var depth = 0

    fun isEntity() = entity != null
    fun isRock() = rock != Block.Empty
    fun isDepth() = depth > 0
}

class JsonLevel
{
    var version = ""
    var name = ""
    var levels = 0
    var width = 0
    var height = 0
    var map = arrayOf<Array<Array<Int>>>()
    var playerPosition = Vector3()
    var entities = listOf<BaseEntity>()
}

class KlexLevel
{
    var version = "0.0.0"
    var name = "empty"
    var levels = 0
    var width = 0
    var height = 0
    var map = listOf<List<List<LevelBlock>>>()
    var entities = mutableListOf<BaseEntity>()
}
