package com.marcin.panklex

import com.badlogic.gdx.math.Vector3

class KlexBlock(val position : Vector3)
{
    var type = BlockType.Empty

    var isBorder = false
    var isBorderRight = false
    var isBorderLeft = false
    var isBorderUp = false
    var isBorderDown = false
    var isBorderAbove = false
    var isBorderBelow = false

    fun isEmpty() = type == BlockType.Empty
    fun isNotEmpty() = type != BlockType.Empty

    fun clearBorders()
    {
        isBorder = false
        isBorderRight = false
        isBorderLeft = false
        isBorderUp = false
        isBorderDown = false
        isBorderAbove = false
        isBorderBelow = false
    }
}
