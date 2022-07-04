package com.marcin.panklex

import com.badlogic.gdx.math.Vector3

class KlexBlock(val position : Vector3)
{
    var type = BlockType.Empty

    var isBorder = false

    var isRightBorder = false
    var isRightBorderAbove = false
    var isRightBorderUp = false
    var isRightBorderBelow = false
    var isRightBorderDown = false

    var isDownBorder = false
    var isDownBorderAbove = false
    var isDownBorderRight = false
    var isDownBorderBelow = false
    var isDownBorderLeft = false

    var isAboveBorder = false
    var isAboveBorderUp = false
    var isAboveBorderRight = false
    var isAboveBorderDown = false
    var isAboveBorderLeft = false

    var isLeftBorder = false
    var isLeftBorderAbove = false
    var isLeftBorderUp = false
    var isLeftBorderBelow = false
    var isLeftBorderDown = false

    var isUpBorder = false
    var isUpBorderAbove = false
    var isUpBorderRight = false
    var isUpBorderBelow = false
    var isUpBorderLeft = false

    var isBelowBorder = false
    var isBelowBorderUp = false
    var isBelowBorderRight = false
    var isBelowBorderDown = false
    var isBelowBorderLeft = false

    fun isEmpty() = type == BlockType.Empty
    fun isNotEmpty() = type != BlockType.Empty

    fun clearBorders()
    {
        isBorder = false

        isRightBorder = false
        isRightBorderAbove = false
        isRightBorderUp = false
        isRightBorderBelow = false
        isRightBorderDown = false

        isDownBorder = false
        isDownBorderAbove = false
        isDownBorderRight = false
        isDownBorderBelow = false
        isDownBorderLeft = false

        isAboveBorder = false
        isAboveBorderUp = false
        isAboveBorderRight = false
        isAboveBorderDown = false
        isAboveBorderLeft = false

        isLeftBorder = false
        isLeftBorderAbove = false
        isLeftBorderUp = false
        isLeftBorderBelow = false
        isLeftBorderDown = false

        isUpBorder = false
        isUpBorderAbove = false
        isUpBorderRight = false
        isUpBorderBelow = false
        isUpBorderLeft = false

        isBelowBorder = false
        isBelowBorderUp = false
        isBelowBorderRight = false
        isBelowBorderDown = false
        isBelowBorderLeft = false
    }
}
