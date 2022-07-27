package com.marcin.panklex

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.graphics.glutils.FrameBuffer
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.badlogic.gdx.utils.ScreenUtils

class KlexTiles(assetManager : AssetManager)
{
    // important

    val tileset = TextureRegion(assetManager.get<Texture>("iso.png")).split(32, 32)
    val frameBuffer = FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.width, Gdx.graphics.height, false)
    val spriteBatch = SpriteBatch()

    // select

    val downSelect = tileset[1][1]
    val rightSelect = tileset[0][0]
    val aboveSelect = tileset[2][0]
    val leftSelect = tileset[0][1]
    val upSelect = tileset[1][0]
    val belowSelect = tileset[2][1]

    // select tiles

    val frontSelectTile = glue(downSelect, rightSelect, aboveSelect)
    val leftSelectTile = glue(leftSelect)
    val upSelectTile = glue(upSelect)
    val belowSelectTile = glue(belowSelect)

    // border

    val frontBordersDown = tileset[1][2]
    val frontBordersRight = tileset[0][2]
    val frontBordersAbove = tileset[2][2]

    val leftBorderAbove = tileset[0][3]
    val leftBorderUp = tileset[0][4]
    val leftBorderBelow = tileset[0][5]
    val leftBorderDown = tileset[0][6]

    val upBorderAbove = tileset[1][3]
    val upBorderRight = tileset[1][4]
    val upBorderBelow = tileset[1][5]
    val upBorderLeft = tileset[1][6]

    val belowBorderUp = tileset[2][3]
    val belowBorderRight = tileset[2][4]
    val belowBorderDown = tileset[2][5]
    val belowBorderLeft = tileset[2][6]

    // border tiles

    val frontBordersDownRightAboveTile = glue(frontBordersDown, frontBordersRight, frontBordersAbove)
    val frontBordersDownRightTile = glue(frontBordersDown, frontBordersRight)
    val frontBordersDownAboveTile = glue(frontBordersDown, frontBordersAbove)
    val frontBordersDownTile = glue(frontBordersDown)
    val frontBordersRightAboveTile = glue(frontBordersRight, frontBordersAbove)
    val frontBordersRightTile = glue(frontBordersRight)
    val frontBordersAboveTile = glue(frontBordersAbove)
    val frontBordersTile = glue()

    val leftBorderAboveUpBelowDownTile = glue(leftBorderAbove, leftBorderUp, leftBorderBelow, leftBorderDown)
    val leftBorderAboveUpBelowTile = glue(leftBorderAbove, leftBorderUp, leftBorderBelow)
    val leftBorderAboveUpDownTile = glue(leftBorderAbove, leftBorderUp, leftBorderDown)
    val leftBorderAboveUpTile = glue(leftBorderAbove, leftBorderUp)
    val leftBorderAboveBelowDownTile = glue(leftBorderAbove, leftBorderBelow, leftBorderDown)
    val leftBorderAboveBelowTile = glue(leftBorderAbove, leftBorderBelow)
    val leftBorderAboveDownTile = glue(leftBorderAbove, leftBorderDown)
    val leftBorderAboveTile = glue(leftBorderAbove)
    val leftBorderUpBelowDownTile = glue(leftBorderUp, leftBorderBelow, leftBorderDown)
    val leftBorderUpBelowTile = glue(leftBorderUp, leftBorderBelow)
    val leftBorderUpDownTile = glue(leftBorderUp, leftBorderDown)
    val leftBorderUpTile = glue(leftBorderUp)
    val leftBorderBelowDownTile = glue(leftBorderBelow, leftBorderDown)
    val leftBorderBelowTile = glue(leftBorderBelow)
    val leftBorderDownTile = glue(leftBorderDown)
    val leftBorderTile = glue()

    val upBorderAboveRightBelowLeftTile = glue(upBorderAbove, upBorderRight, upBorderBelow, upBorderLeft)
    val upBorderAboveRightBelowTile = glue(upBorderAbove, upBorderRight, upBorderBelow)
    val upBorderAboveRightLeftTile = glue(upBorderAbove, upBorderRight, upBorderLeft)
    val upBorderAboveRightTile = glue(upBorderAbove, upBorderRight)
    val upBorderAboveBelowLeftTile = glue(upBorderAbove, upBorderBelow, upBorderLeft)
    val upBorderAboveBelowTile = glue(upBorderAbove, upBorderBelow)
    val upBorderAboveLeftTile = glue(upBorderAbove, upBorderLeft)
    val upBorderAboveTile = glue(upBorderAbove)
    val upBorderRightBelowLeftTile = glue(upBorderRight, upBorderBelow, upBorderLeft)
    val upBorderRightBelowTile = glue(upBorderRight, upBorderBelow)
    val upBorderRightLeftTile = glue(upBorderRight, upBorderLeft)
    val upBorderRightTile = glue(upBorderRight)
    val upBorderBelowLeftTile = glue(upBorderBelow, upBorderLeft)
    val upBorderBelowTile = glue(upBorderBelow)
    val upBorderLeftTile = glue(upBorderLeft)
    val upBorderTile = glue()

    val belowBorderUpRightDownLeftTile = glue(belowBorderUp, belowBorderRight, belowBorderDown, belowBorderLeft)
    val belowBorderUpRightDownTile = glue(belowBorderUp, belowBorderRight, belowBorderDown)
    val belowBorderUpRightLeftTile = glue(belowBorderUp, belowBorderRight, belowBorderLeft)
    val belowBorderUpRightTile = glue(belowBorderUp, belowBorderRight)
    val belowBorderUpDownLeftTile = glue(belowBorderUp, belowBorderDown, belowBorderLeft)
    val belowBorderUpDownTile = glue(belowBorderUp, belowBorderDown)
    val belowBorderUpLeftTile = glue(belowBorderUp, belowBorderLeft)
    val belowBorderUpTile = glue(belowBorderUp)
    val belowBorderRightDownLeftTile = glue(belowBorderRight, belowBorderDown, belowBorderLeft)
    val belowBorderRightDownTile = glue(belowBorderRight, belowBorderDown)
    val belowBorderRightLeftTile = glue(belowBorderRight, belowBorderLeft)
    val belowBorderRightTile = glue(belowBorderRight)
    val belowBorderDownLeftTile = glue(belowBorderDown, belowBorderLeft)
    val belowBorderDownTile = glue(belowBorderDown)
    val belowBorderLeftTile = glue(belowBorderLeft)
    val belowBorderTile = glue()

    // functions

    fun front(down : Boolean, right : Boolean, above : Boolean) : StaticTiledMapTile
    {
        return when
        {
            down && right && above    -> frontBordersDownRightAboveTile
            down && right && !above   -> frontBordersDownRightTile
            down && !right && above   -> frontBordersDownAboveTile
            down && !right && !above  -> frontBordersDownTile
            !down && right && above   -> frontBordersRightAboveTile
            !down && right && !above  -> frontBordersRightTile
            !down && !right && above  -> frontBordersAboveTile
            !down && !right && !above -> frontBordersTile
            else                      -> frontBordersTile
        }
    }

    fun left(above : Boolean, up : Boolean, below : Boolean, down : Boolean) : StaticTiledMapTile
    {
        return when
        {
            above && up && below && down     -> leftBorderAboveUpBelowDownTile
            above && up && below && !down    -> leftBorderAboveUpBelowTile
            above && up && !below && down    -> leftBorderAboveUpDownTile
            above && up && !below && !down   -> leftBorderAboveUpTile
            above && !up && below && down    -> leftBorderAboveBelowDownTile
            above && !up && below && !down   -> leftBorderAboveBelowTile
            above && !up && !below && down   -> leftBorderAboveDownTile
            above && !up && !below && !down  -> leftBorderAboveTile
            !above && up && below && down    -> leftBorderUpBelowDownTile
            !above && up && below && !down   -> leftBorderUpBelowTile
            !above && up && !below && down   -> leftBorderUpDownTile
            !above && up && !below && !down  -> leftBorderUpTile
            !above && !up && below && down   -> leftBorderBelowDownTile
            !above && !up && below && !down  -> leftBorderBelowTile
            !above && !up && !below && down  -> leftBorderDownTile
            !above && !up && !below && !down -> leftBorderTile
            else                             -> leftBorderTile
        }
    }

    fun up(above : Boolean, right : Boolean, below : Boolean, left : Boolean) : StaticTiledMapTile
    {
        return when
        {
            above && right && below && left     -> upBorderAboveRightBelowLeftTile
            above && right && below && !left    -> upBorderAboveRightBelowTile
            above && right && !below && left    -> upBorderAboveRightLeftTile
            above && right && !below && !left   -> upBorderAboveRightTile
            above && !right && below && left    -> upBorderAboveBelowLeftTile
            above && !right && below && !left   -> upBorderAboveBelowTile
            above && !right && !below && left   -> upBorderAboveLeftTile
            above && !right && !below && !left  -> upBorderAboveTile
            !above && right && below && left    -> upBorderRightBelowLeftTile
            !above && right && below && !left   -> upBorderRightBelowTile
            !above && right && !below && left   -> upBorderRightLeftTile
            !above && right && !below && !left  -> upBorderRightTile
            !above && !right && below && left   -> upBorderBelowLeftTile
            !above && !right && below && !left  -> upBorderBelowTile
            !above && !right && !below && left  -> upBorderLeftTile
            !above && !right && !below && !left -> upBorderTile
            else                                -> upBorderTile
        }
    }

    fun below(up : Boolean, right : Boolean, down : Boolean, left : Boolean) : StaticTiledMapTile
    {
        return when
        {
            up && right && down && left     -> belowBorderUpRightDownLeftTile
            up && right && down && !left    -> belowBorderUpRightDownTile
            up && right && !down && left    -> belowBorderUpRightLeftTile
            up && right && !down && !left   -> belowBorderUpRightTile
            up && !right && down && left    -> belowBorderUpDownLeftTile
            up && !right && down && !left   -> belowBorderUpDownTile
            up && !right && !down && left   -> belowBorderUpLeftTile
            up && !right && !down && !left  -> belowBorderUpTile
            !up && right && down && left    -> belowBorderRightDownLeftTile
            !up && right && down && !left   -> belowBorderRightDownTile
            !up && right && !down && left   -> belowBorderRightLeftTile
            !up && right && !down && !left  -> belowBorderRightTile
            !up && !right && down && left   -> belowBorderDownLeftTile
            !up && !right && down && !left  -> belowBorderDownTile
            !up && !right && !down && left  -> belowBorderLeftTile
            !up && !right && !down && !left -> belowBorderTile
            else                            -> belowBorderTile
        }
    }

    fun glue(vararg regions : TextureRegion) : StaticTiledMapTile
    {
        frameBuffer.begin()
        //spriteBatch.enableBlending()
        //Gdx.gl.glBlendFuncSeparate(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA, GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA)
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        spriteBatch.begin()

        for (region in regions)
        {
            spriteBatch.draw(region, 0f, 0f)
        }

        spriteBatch.end()
        val textureRegion = ScreenUtils.getFrameBufferTexture()
        frameBuffer.end()
        return StaticTiledMapTile(textureRegion)
    }
}
