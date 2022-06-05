package com.marcin.panklex

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.utils.Json

class JsonLevel
{
    var name = ""
    var floors = 0
    var width = 0
    var height = 0
    var positions = arrayOf<Vector3>()
    var blocks = arrayOf<Array<Array<Int>>>()
}

class KlexLevel
{
    var name = ""
    var floors = 0
    var height = 0
    var width = 0
    var positions = arrayOf<Vector3>()
    var blocks = mutableListOf<List<List<KlexBlock>>>()

    fun createLevel(jsonPath : String)
    {
        val json = Json()
        val jsonFile = Gdx.files.internal(jsonPath)
        val jsonString = jsonFile.readString()
        val jsonLevel = json.fromJson(JsonLevel::class.java, jsonString)

        name = jsonLevel.name
        floors = jsonLevel.floors
        height = jsonLevel.height
        width = jsonLevel.width
        positions = jsonLevel.positions

        for (k in 0 until floors)
        {
            val floor = mutableListOf<List<KlexBlock>>()

            for (j in jsonLevel.height - 1 downTo 0)
            {
                val row = mutableListOf<KlexBlock>()

                for (i in 0 until width)
                {
                    val block = KlexBlock(i, j, k)

                    block.type = if (jsonLevel.blocks[k][j][i] == 1) BlockType.Brick
                    else BlockType.Empty

                    row.add(block)
                }

                floor.add(row)
            }

            blocks.add(floor)
        }
    }

    fun getBlock(xLevel : Int, yLevel : Int, zLevel : Int) : KlexBlock?
    {
        return if (xLevel in 0 until width && yLevel in 0 until height && zLevel in 0 until floors)
            blocks[zLevel][yLevel][xLevel]
        else null
    }
}
