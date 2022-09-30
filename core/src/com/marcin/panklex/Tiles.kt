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
import com.badlogic.gdx.scenes.scene2d.utils.Drawable
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.ScreenUtils

class Tiles(assetManager : AssetManager)
{
    // important

    val frameBuffer = FrameBuffer(Pixmap.Format.RGBA8888, Gdx.graphics.width, Gdx.graphics.height, false)
    val spriteBatch = SpriteBatch()

    fun glueIntoTile(vararg regions : TextureRegion) : StaticTiledMapTile
    {
        frameBuffer.begin()
        Gdx.gl.glClearColor(0f, 0f, 0f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        spriteBatch.begin()
        for (region in regions) spriteBatch.draw(region, 0f, 0f)
        spriteBatch.end()
        val textureRegion = ScreenUtils.getFrameBufferTexture()
        frameBuffer.end()
        return StaticTiledMapTile(textureRegion)
    }

    // other

    val select = TextureRegion(assetManager.get<Texture>("graphics/other/select.png")).split(tileLength, tileLength)
    val selectNotMovingBack = StaticTiledMapTile(select[0][0])
    val selectNotMovingFront = StaticTiledMapTile(select[1][0])
    val selectPathFoundBack = StaticTiledMapTile(select[0][1])
    val selectPathFoundFront = StaticTiledMapTile(select[1][1])
    val selectInaccessibleBack = StaticTiledMapTile(select[1][2])
    val selectInaccessibleFront = StaticTiledMapTile(select[1][2])
    val selectPathNotFoundBack = StaticTiledMapTile(select[0][3])
    val selectPathNotFoundFront = StaticTiledMapTile(select[1][3])

    val groundMove = TextureRegion(assetManager.get<Texture>("graphics/other/groundMove.png")).split(tileLength, tileLength)
    val groundMoveUpWhole = StaticTiledMapTile(groundMove[0][0])
    val groundMoveUpRightWhole = StaticTiledMapTile(groundMove[1][0])
    val groundMoveRightWhole = StaticTiledMapTile(groundMove[2][0])
    val groundMoveDownRightWhole = StaticTiledMapTile(groundMove[3][0])
    val groundMoveDownWhole = StaticTiledMapTile(groundMove[4][0])
    val groundMoveDownLeftWhole = StaticTiledMapTile(groundMove[5][0])
    val groundMoveLeftWhole = StaticTiledMapTile(groundMove[6][0])
    val groundMoveUpLeftWhole = StaticTiledMapTile(groundMove[7][0])
    val groundMoveUpOutline = StaticTiledMapTile(groundMove[0][1])
    val groundMoveUpRightOutline = StaticTiledMapTile(groundMove[1][1])
    val groundMoveRightOutline = StaticTiledMapTile(groundMove[2][1])
    val groundMoveDownRightOutline = StaticTiledMapTile(groundMove[3][1])
    val groundMoveDownOutline = StaticTiledMapTile(groundMove[4][1])
    val groundMoveDownLeftOutline = StaticTiledMapTile(groundMove[5][1])
    val groundMoveLeftOutline = StaticTiledMapTile(groundMove[6][1])
    val groundMoveUpLeftOutline = StaticTiledMapTile(groundMove[7][1])

    val fallMove = TextureRegion(assetManager.get<Texture>("graphics/other/fallMove.png")).split(tileLength, tileLength)
    val fallMoveWhole = StaticTiledMapTile(fallMove[0][0])
    val fallMoveOutline = StaticTiledMapTile(fallMove[0][1])

    val dist2JumpMove =
        TextureRegion(assetManager.get<Texture>("graphics/other/dist2JumpMove.png")).split(tileLength, tileLength)
    val dist2JumpMoveUpWhole = StaticTiledMapTile(dist2JumpMove[0][0])
    val dist2JumpMoveUpRightWhole = StaticTiledMapTile(dist2JumpMove[1][0])
    val dist2JumpMoveRightWhole = StaticTiledMapTile(dist2JumpMove[2][0])
    val dist2JumpMoveDownRightWhole = StaticTiledMapTile(dist2JumpMove[3][0])
    val dist2JumpMoveDownWhole = StaticTiledMapTile(dist2JumpMove[4][0])
    val dist2JumpMoveDownLeftWhole = StaticTiledMapTile(dist2JumpMove[5][0])
    val dist2JumpMoveLeftWhole = StaticTiledMapTile(dist2JumpMove[6][0])
    val dist2JumpMoveUpLeftWhole = StaticTiledMapTile(dist2JumpMove[7][0])
    val dist2JumpMoveUpOutline = StaticTiledMapTile(dist2JumpMove[0][1])
    val dist2JumpMoveUpRightOutline = StaticTiledMapTile(dist2JumpMove[1][1])
    val dist2JumpMoveRightOutline = StaticTiledMapTile(dist2JumpMove[2][1])
    val dist2JumpMoveDownRightOutline = StaticTiledMapTile(dist2JumpMove[3][1])
    val dist2JumpMoveDownOutline = StaticTiledMapTile(dist2JumpMove[4][1])
    val dist2JumpMoveDownLeftOutline = StaticTiledMapTile(dist2JumpMove[5][1])
    val dist2JumpMoveLeftOutline = StaticTiledMapTile(dist2JumpMove[6][1])
    val dist2JumpMoveUpLeftOutline = StaticTiledMapTile(dist2JumpMove[7][1])

    val dist3JumpMove =
        TextureRegion(assetManager.get<Texture>("graphics/other/dist3JumpMove.png")).split(tileLength, tileLength)
    val dist3JumpMoveUpWhole = StaticTiledMapTile(dist3JumpMove[0][0])
    val dist3JumpMoveUpRightWhole = StaticTiledMapTile(dist3JumpMove[1][0])
    val dist3JumpMoveRightWhole = StaticTiledMapTile(dist3JumpMove[2][0])
    val dist3JumpMoveDownRightWhole = StaticTiledMapTile(dist3JumpMove[3][0])
    val dist3JumpMoveDownWhole = StaticTiledMapTile(dist3JumpMove[4][0])
    val dist3JumpMoveDownLeftWhole = StaticTiledMapTile(dist3JumpMove[5][0])
    val dist3JumpMoveLeftWhole = StaticTiledMapTile(dist3JumpMove[6][0])
    val dist3JumpMoveUpLeftWhole = StaticTiledMapTile(dist3JumpMove[7][0])
    val dist3JumpMoveUpOutline = StaticTiledMapTile(dist3JumpMove[0][1])
    val dist3JumpMoveUpRightOutline = StaticTiledMapTile(dist3JumpMove[1][1])
    val dist3JumpMoveRightOutline = StaticTiledMapTile(dist3JumpMove[2][1])
    val dist3JumpMoveDownRightOutline = StaticTiledMapTile(dist3JumpMove[3][1])
    val dist3JumpMoveDownOutline = StaticTiledMapTile(dist3JumpMove[4][1])
    val dist3JumpMoveDownLeftOutline = StaticTiledMapTile(dist3JumpMove[5][1])
    val dist3JumpMoveLeftOutline = StaticTiledMapTile(dist3JumpMove[6][1])
    val dist3JumpMoveUpLeftOutline = StaticTiledMapTile(dist3JumpMove[7][1])

    val items = TextureRegion(assetManager.get<Texture>("graphics/other/items.png")).split(tileLengthHalf, tileLengthHalf)
    val itemCell = items[0][0]
    val itemCoin = items[1][0]
    val itemPickaxe = items[0][1]
    val itemGear = items[1][1]
    val itemRopeArrow = items[0][2]
    val itemPointingArrow = items[1][2]

    val upgrades =
        TextureRegion(assetManager.get<Texture>("graphics/other/upgrades.png")).split(tileLengthHalf, tileLengthHalf)
    val upgradeLongFallLegUnlocked = upgrades[0][0]
    val upgradeLongFallLegLocked = upgrades[0][1]
    val upgradeSpringLegUnlocked = upgrades[1][0]
    val upgradeSpringLegLocked = upgrades[1][1]
    val upgradeDrillArmUnlocked = upgrades[2][0]
    val upgradeDrillArmLocked = upgrades[2][1]
    val upgradeExtendedArmUnlocked = upgrades[3][0]
    val upgradeExtendedArmLocked = upgrades[3][1]

    val upgradeLongFallLegUnlockedDrawable = TextureRegionDrawable(upgrades[0][0])
    val upgradeLongFallLegLockedDrawable = TextureRegionDrawable(upgrades[0][1])
    val upgradeSpringLegUnlockedDrawable = TextureRegionDrawable(upgrades[1][0])
    val upgradeSpringLegLockedDrawable = TextureRegionDrawable(upgrades[1][1])
    val upgradeDrillArmUnlockedDrawable = TextureRegionDrawable(upgrades[2][0])
    val upgradeDrillArmLockedDrawable = TextureRegionDrawable(upgrades[2][1])
    val upgradeExtendedArmUnlockedDrawable = TextureRegionDrawable(upgrades[3][0])
    val upgradeExtendedArmLockedDrawable = TextureRegionDrawable(upgrades[3][1])

    val lines = TextureRegion(assetManager.get<Texture>("graphics/other/lines.png")).split(tileLength, tileLength)
    val belowLinesUp = lines[0][1]
    val belowLinesRight = lines[0][2]
    val belowLinesDown = lines[0][3]
    val belowLinesLeft = lines[0][4]

    val belowUpRightDownLeft = glueIntoTile(belowLinesUp, belowLinesRight, belowLinesDown, belowLinesLeft)
    val belowUpRightDown = glueIntoTile(belowLinesUp, belowLinesRight, belowLinesDown)
    val belowUpRightLeft = glueIntoTile(belowLinesUp, belowLinesRight, belowLinesLeft)
    val belowUpRight = glueIntoTile(belowLinesUp, belowLinesRight)
    val belowUpDownLeft = glueIntoTile(belowLinesUp, belowLinesDown, belowLinesLeft)
    val belowUpDown = glueIntoTile(belowLinesUp, belowLinesDown)
    val belowUpLeft = glueIntoTile(belowLinesUp, belowLinesLeft)
    val belowUp = glueIntoTile(belowLinesUp)
    val belowRightDownLeft = glueIntoTile(belowLinesRight, belowLinesDown, belowLinesLeft)
    val belowRightDown = glueIntoTile(belowLinesRight, belowLinesDown)
    val belowRightLeft = glueIntoTile(belowLinesRight, belowLinesLeft)
    val belowRight = glueIntoTile(belowLinesRight)
    val belowDownLeft = glueIntoTile(belowLinesDown, belowLinesLeft)
    val belowDown = glueIntoTile(belowLinesDown)
    val belowLeft = glueIntoTile(belowLinesLeft)
    val below = glueIntoTile()

    fun getBelow(up : Boolean, right : Boolean, down : Boolean, left : Boolean) : StaticTiledMapTile?
    {
        return when
        {
            up && right && down && left     -> belowUpRightDownLeft
            up && right && down && !left    -> belowUpRightDown
            up && right && !down && left    -> belowUpRightLeft
            up && right && !down && !left   -> belowUpRight
            up && !right && down && left    -> belowUpDownLeft
            up && !right && down && !left   -> belowUpDown
            up && !right && !down && left   -> belowUpLeft
            up && !right && !down && !left  -> belowUp
            !up && right && down && left    -> belowRightDownLeft
            !up && right && down && !left   -> belowRightDown
            !up && right && !down && left   -> belowRightLeft
            !up && right && !down && !left  -> belowRight
            !up && !right && down && left   -> belowDownLeft
            !up && !right && down && !left  -> belowDown
            !up && !right && !down && left  -> belowLeft
            !up && !right && !down && !left -> below
            else                            -> null
        }
    }

    val leftLinesAbove = lines[1][1]
    val leftLinesUp = lines[1][2]
    val leftLinesBelow = lines[1][3]
    val leftLinesDown = lines[1][4]

    val leftAboveUpBelowDown = glueIntoTile(leftLinesAbove, leftLinesUp, leftLinesBelow, leftLinesDown)
    val leftAboveUpBelow = glueIntoTile(leftLinesAbove, leftLinesUp, leftLinesBelow)
    val leftAboveUpDown = glueIntoTile(leftLinesAbove, leftLinesUp, leftLinesDown)
    val leftAboveUp = glueIntoTile(leftLinesAbove, leftLinesUp)
    val leftAboveBelowDown = glueIntoTile(leftLinesAbove, leftLinesBelow, leftLinesDown)
    val leftAboveBelow = glueIntoTile(leftLinesAbove, leftLinesBelow)
    val leftAboveDown = glueIntoTile(leftLinesAbove, leftLinesDown)
    val leftAbove = glueIntoTile(leftLinesAbove)
    val leftUpBelowDown = glueIntoTile(leftLinesUp, leftLinesBelow, leftLinesDown)
    val leftUpBelow = glueIntoTile(leftLinesUp, leftLinesBelow)
    val leftUpDown = glueIntoTile(leftLinesUp, leftLinesDown)
    val leftUp = glueIntoTile(leftLinesUp)
    val leftBelowDown = glueIntoTile(leftLinesBelow, leftLinesDown)
    val leftBelow = glueIntoTile(leftLinesBelow)
    val leftDown = glueIntoTile(leftLinesDown)
    val left = glueIntoTile()

    fun getLeft(above : Boolean, up : Boolean, below : Boolean, down : Boolean) : StaticTiledMapTile?
    {
        return when
        {
            above && up && below && down     -> leftAboveUpBelowDown
            above && up && below && !down    -> leftAboveUpBelow
            above && up && !below && down    -> leftAboveUpDown
            above && up && !below && !down   -> leftAboveUp
            above && !up && below && down    -> leftAboveBelowDown
            above && !up && below && !down   -> leftAboveBelow
            above && !up && !below && down   -> leftAboveDown
            above && !up && !below && !down  -> leftAbove
            !above && up && below && down    -> leftUpBelowDown
            !above && up && below && !down   -> leftUpBelow
            !above && up && !below && down   -> leftUpDown
            !above && up && !below && !down  -> leftUp
            !above && !up && below && down   -> leftBelowDown
            !above && !up && below && !down  -> leftBelow
            !above && !up && !below && down  -> leftDown
            !above && !up && !below && !down -> left
            else                             -> null
        }
    }

    val upLinesAbove = lines[2][1]
    val upLinesRight = lines[2][2]
    val upLinesBelow = lines[2][3]
    val upLinesLeft = lines[2][4]

    val upAboveRightBelowLeft = glueIntoTile(upLinesAbove, upLinesRight, upLinesBelow, upLinesLeft)
    val upAboveRightBelow = glueIntoTile(upLinesAbove, upLinesRight, upLinesBelow)
    val upAboveRightLeft = glueIntoTile(upLinesAbove, upLinesRight, upLinesLeft)
    val upAboveRight = glueIntoTile(upLinesAbove, upLinesRight)
    val upAboveBelowLeft = glueIntoTile(upLinesAbove, upLinesBelow, upLinesLeft)
    val upAboveBelow = glueIntoTile(upLinesAbove, upLinesBelow)
    val upAboveLeft = glueIntoTile(upLinesAbove, upLinesLeft)
    val upAbove = glueIntoTile(upLinesAbove)
    val upRightBelowLeft = glueIntoTile(upLinesRight, upLinesBelow, upLinesLeft)
    val upRightBelow = glueIntoTile(upLinesRight, upLinesBelow)
    val upRightLeft = glueIntoTile(upLinesRight, upLinesLeft)
    val upRight = glueIntoTile(upLinesRight)
    val upBelowLeft = glueIntoTile(upLinesBelow, upLinesLeft)
    val upBelow = glueIntoTile(upLinesBelow)
    val upLeft = glueIntoTile(upLinesLeft)
    val up = glueIntoTile()

    fun getUp(above : Boolean, right : Boolean, below : Boolean, left : Boolean) : StaticTiledMapTile?
    {
        return when
        {
            above && right && below && left     -> upAboveRightBelowLeft
            above && right && below && !left    -> upAboveRightBelow
            above && right && !below && left    -> upAboveRightLeft
            above && right && !below && !left   -> upAboveRight
            above && !right && below && left    -> upAboveBelowLeft
            above && !right && below && !left   -> upAboveBelow
            above && !right && !below && left   -> upAboveLeft
            above && !right && !below && !left  -> upAbove
            !above && right && below && left    -> upRightBelowLeft
            !above && right && below && !left   -> upRightBelow
            !above && right && !below && left   -> upRightLeft
            !above && right && !below && !left  -> upRight
            !above && !right && below && left   -> upBelowLeft
            !above && !right && below && !left  -> upBelow
            !above && !right && !below && left  -> upLeft
            !above && !right && !below && !left -> up
            else                                -> null
        }
    }

    // entities

    val player = TextureRegion(assetManager.get<Texture>("graphics/entities/player.png")).split(tileLength, tileLength)
    val playerWhole = StaticTiledMapTile(player[0][0])
    val playerOutline = StaticTiledMapTile(player[1][0])

    // objects

    val block = TextureRegion(assetManager.get<Texture>("graphics/objects/block.png")).split(tileLength, tileLength)
    val blockBelow = StaticTiledMapTile(block[0][0])
    val blockLeft = StaticTiledMapTile(block[1][0])
    val blockUp = StaticTiledMapTile(block[2][0])
    val blockBehind = StaticTiledMapTile(block[3][0])
    val blockBefore = StaticTiledMapTile(block[4][0])
    val blockDown = StaticTiledMapTile(block[5][0])
    val blockRight = StaticTiledMapTile(block[6][0])
    val blockAbove = StaticTiledMapTile(block[7][0])

    val column = TextureRegion(assetManager.get<Texture>("graphics/objects/column.png")).split(tileLength, tileLength)
    val columnBelow = StaticTiledMapTile(column[0][0])
    val columnLeft = StaticTiledMapTile(column[1][0])
    val columnUp = StaticTiledMapTile(column[2][0])
    val columnBehind = StaticTiledMapTile(column[3][0])
    val columnBefore = StaticTiledMapTile(column[4][0])
    val columnDown = StaticTiledMapTile(column[5][0])
    val columnRight = StaticTiledMapTile(column[6][0])
    val columnAbove = StaticTiledMapTile(column[7][0])

    val halfColumn =
        TextureRegion(assetManager.get<Texture>("graphics/objects/halfColumn.png")).split(tileLength, tileLength)
    val halfColumnUpBelow = StaticTiledMapTile(halfColumn[0][0])
    val halfColumnUpLeft = StaticTiledMapTile(halfColumn[1][0])
    val halfColumnUpUp = StaticTiledMapTile(halfColumn[2][0])
    val halfColumnUpBehind = StaticTiledMapTile(halfColumn[3][0])
    val halfColumnUpBefore = StaticTiledMapTile(halfColumn[4][0])
    val halfColumnUpDown = StaticTiledMapTile(halfColumn[5][0])
    val halfColumnUpRight = StaticTiledMapTile(halfColumn[6][0])
    val halfColumnUpAbove = StaticTiledMapTile(halfColumn[7][0])
    val halfColumnRightBelow = StaticTiledMapTile(halfColumn[0][1])
    val halfColumnRightLeft = StaticTiledMapTile(halfColumn[1][1])
    val halfColumnRightUp = StaticTiledMapTile(halfColumn[2][1])
    val halfColumnRightBehind = StaticTiledMapTile(halfColumn[3][1])
    val halfColumnRightBefore = StaticTiledMapTile(halfColumn[4][1])
    val halfColumnRightDown = StaticTiledMapTile(halfColumn[5][1])
    val halfColumnRightRight = StaticTiledMapTile(halfColumn[6][1])
    val halfColumnRightAbove = StaticTiledMapTile(halfColumn[7][1])
    val halfColumnDownBelow = StaticTiledMapTile(halfColumn[0][2])
    val halfColumnDownLeft = StaticTiledMapTile(halfColumn[1][2])
    val halfColumnDownUp = StaticTiledMapTile(halfColumn[2][2])
    val halfColumnDownBehind = StaticTiledMapTile(halfColumn[3][2])
    val halfColumnDownBefore = StaticTiledMapTile(halfColumn[4][2])
    val halfColumnDownDown = StaticTiledMapTile(halfColumn[5][2])
    val halfColumnDownRight = StaticTiledMapTile(halfColumn[6][2])
    val halfColumnDownAbove = StaticTiledMapTile(halfColumn[7][2])
    val halfColumnLeftBelow = StaticTiledMapTile(halfColumn[0][3])
    val halfColumnLeftLeft = StaticTiledMapTile(halfColumn[1][3])
    val halfColumnLeftUp = StaticTiledMapTile(halfColumn[2][3])
    val halfColumnLeftBehind = StaticTiledMapTile(halfColumn[3][3])
    val halfColumnLeftBefore = StaticTiledMapTile(halfColumn[4][3])
    val halfColumnLeftDown = StaticTiledMapTile(halfColumn[5][3])
    val halfColumnLeftRight = StaticTiledMapTile(halfColumn[6][3])
    val halfColumnLeftAbove = StaticTiledMapTile(halfColumn[7][3])

    val arch = TextureRegion(assetManager.get<Texture>("graphics/objects/arch.png")).split(tileLength, tileLength)
    val archUpBelow = StaticTiledMapTile(arch[0][0])
    val archUpLeft = StaticTiledMapTile(arch[1][0])
    val archUpUp = StaticTiledMapTile(arch[2][0])
    val archUpBehind = StaticTiledMapTile(arch[3][0])
    val archUpBefore = StaticTiledMapTile(arch[4][0])
    val archUpDown = StaticTiledMapTile(arch[5][0])
    val archUpRight = StaticTiledMapTile(arch[6][0])
    val archUpAbove = StaticTiledMapTile(arch[7][0])
    val archRightBelow = StaticTiledMapTile(arch[0][1])
    val archRightLeft = StaticTiledMapTile(arch[1][1])
    val archRightUp = StaticTiledMapTile(arch[2][1])
    val archRightBehind = StaticTiledMapTile(arch[3][1])
    val archRightBefore = StaticTiledMapTile(arch[4][1])
    val archRightDown = StaticTiledMapTile(arch[5][1])
    val archRightRight = StaticTiledMapTile(arch[6][1])
    val archRightAbove = StaticTiledMapTile(arch[7][1])

    val halfArch = TextureRegion(assetManager.get<Texture>("graphics/objects/halfArch.png")).split(tileLength, tileLength)
    val halfArchUpBelow = StaticTiledMapTile(halfArch[0][0])
    val halfArchUpLeft = StaticTiledMapTile(halfArch[1][0])
    val halfArchUpUp = StaticTiledMapTile(halfArch[2][0])
    val halfArchUpBehind = StaticTiledMapTile(halfArch[3][0])
    val halfArchUpBefore = StaticTiledMapTile(halfArch[4][0])
    val halfArchUpDown = StaticTiledMapTile(halfArch[5][0])
    val halfArchUpRight = StaticTiledMapTile(halfArch[6][0])
    val halfArchUpAbove = StaticTiledMapTile(halfArch[7][0])
    val halfArchRightBelow = StaticTiledMapTile(halfArch[0][1])
    val halfArchRightLeft = StaticTiledMapTile(halfArch[1][1])
    val halfArchRightUp = StaticTiledMapTile(halfArch[2][1])
    val halfArchRightBehind = StaticTiledMapTile(halfArch[3][1])
    val halfArchRightBefore = StaticTiledMapTile(halfArch[4][1])
    val halfArchRightDown = StaticTiledMapTile(halfArch[5][1])
    val halfArchRightRight = StaticTiledMapTile(halfArch[6][1])
    val halfArchRightAbove = StaticTiledMapTile(halfArch[7][1])
    val halfArchDownBelow = StaticTiledMapTile(halfArch[0][2])
    val halfArchDownLeft = StaticTiledMapTile(halfArch[1][2])
    val halfArchDownUp = StaticTiledMapTile(halfArch[2][2])
    val halfArchDownBehind = StaticTiledMapTile(halfArch[3][2])
    val halfArchDownBefore = StaticTiledMapTile(halfArch[4][2])
    val halfArchDownDown = StaticTiledMapTile(halfArch[5][2])
    val halfArchDownRight = StaticTiledMapTile(halfArch[6][2])
    val halfArchDownAbove = StaticTiledMapTile(halfArch[7][2])
    val halfArchLeftBelow = StaticTiledMapTile(halfArch[0][3])
    val halfArchLeftLeft = StaticTiledMapTile(halfArch[1][3])
    val halfArchLeftUp = StaticTiledMapTile(halfArch[2][3])
    val halfArchLeftBehind = StaticTiledMapTile(halfArch[3][3])
    val halfArchLeftBefore = StaticTiledMapTile(halfArch[4][3])
    val halfArchLeftDown = StaticTiledMapTile(halfArch[5][3])
    val halfArchLeftRight = StaticTiledMapTile(halfArch[6][3])
    val halfArchLeftAbove = StaticTiledMapTile(halfArch[7][3])

    val stairs = TextureRegion(assetManager.get<Texture>("graphics/objects/stairs.png")).split(tileLength, tileLength)
    val stairsUpBelow = StaticTiledMapTile(stairs[0][0])
    val stairsUpLeft = StaticTiledMapTile(stairs[1][0])
    val stairsUpUp = StaticTiledMapTile(stairs[2][0])
    val stairsUpBehind = StaticTiledMapTile(stairs[3][0])
    val stairsUpBefore = StaticTiledMapTile(stairs[4][0])
    val stairsUpDown = StaticTiledMapTile(stairs[5][0])
    val stairsUpRight = StaticTiledMapTile(stairs[6][0])
    val stairsUpAbove = StaticTiledMapTile(stairs[7][0])
    val stairsRightBelow = StaticTiledMapTile(stairs[0][1])
    val stairsRightLeft = StaticTiledMapTile(stairs[1][1])
    val stairsRightUp = StaticTiledMapTile(stairs[2][1])
    val stairsRightBehind = StaticTiledMapTile(stairs[3][1])
    val stairsRightBefore = StaticTiledMapTile(stairs[4][1])
    val stairsRightDown = StaticTiledMapTile(stairs[5][1])
    val stairsRightRight = StaticTiledMapTile(stairs[6][1])
    val stairsRightAbove = StaticTiledMapTile(stairs[7][1])
    val stairsDownBelow = StaticTiledMapTile(stairs[0][2])
    val stairsDownLeft = StaticTiledMapTile(stairs[1][2])
    val stairsDownUp = StaticTiledMapTile(stairs[2][2])
    val stairsDownBehind = StaticTiledMapTile(stairs[3][2])
    val stairsDownBefore = StaticTiledMapTile(stairs[4][2])
    val stairsDownDown = StaticTiledMapTile(stairs[5][2])
    val stairsDownRight = StaticTiledMapTile(stairs[6][2])
    val stairsDownAbove = StaticTiledMapTile(stairs[7][2])
    val stairsLeftBelow = StaticTiledMapTile(stairs[0][3])
    val stairsLeftLeft = StaticTiledMapTile(stairs[1][3])
    val stairsLeftUp = StaticTiledMapTile(stairs[2][3])
    val stairsLeftBehind = StaticTiledMapTile(stairs[3][3])
    val stairsLeftBefore = StaticTiledMapTile(stairs[4][3])
    val stairsLeftDown = StaticTiledMapTile(stairs[5][3])
    val stairsLeftRight = StaticTiledMapTile(stairs[6][3])
    val stairsLeftAbove = StaticTiledMapTile(stairs[7][3])
    val stairsMoveUpAboveWhole = StaticTiledMapTile(stairs[0][4])
    val stairsMoveUpBelowWhole = StaticTiledMapTile(stairs[1][4])
    val stairsMoveRightAboveWhole = StaticTiledMapTile(stairs[2][4])
    val stairsMoveRightBelowWhole = StaticTiledMapTile(stairs[3][4])
    val stairsMoveDownAboveWhole = StaticTiledMapTile(stairs[4][4])
    val stairsMoveDownBelowWhole = StaticTiledMapTile(stairs[5][4])
    val stairsMoveLeftAboveWhole = StaticTiledMapTile(stairs[6][4])
    val stairsMoveLeftBelowWhole = StaticTiledMapTile(stairs[7][4])
    val stairsMoveUpAboveOutline = StaticTiledMapTile(stairs[0][5])
    val stairsMoveUpBelowOutline = StaticTiledMapTile(stairs[1][5])
    val stairsMoveRightAboveOutline = StaticTiledMapTile(stairs[2][5])
    val stairsMoveRightBelowOutline = StaticTiledMapTile(stairs[3][5])
    val stairsMoveDownAboveOutline = StaticTiledMapTile(stairs[4][5])
    val stairsMoveDownBelowOutline = StaticTiledMapTile(stairs[5][5])
    val stairsMoveLeftAboveOutline = StaticTiledMapTile(stairs[6][5])
    val stairsMoveLeftBelowOutline = StaticTiledMapTile(stairs[7][5])

    val ladder = TextureRegion(assetManager.get<Texture>("graphics/objects/ladder.png")).split(tileLength, tileLength)
    val ladderUp = StaticTiledMapTile(ladder[4][0])
    val ladderRight = StaticTiledMapTile(ladder[5][0])
    val ladderDown = StaticTiledMapTile(ladder[6][0])
    val ladderLeft = StaticTiledMapTile(ladder[7][0])
    val ladderMoveUpAboveWhole = StaticTiledMapTile(ladder[4][1])
    val ladderMoveRightAboveWhole = StaticTiledMapTile(ladder[5][1])
    val ladderMoveDownAboveWhole = StaticTiledMapTile(ladder[6][1])
    val ladderMoveLeftAboveWhole = StaticTiledMapTile(ladder[7][1])
    val ladderMoveUpAboveOutline = StaticTiledMapTile(ladder[4][2])
    val ladderMoveRightAboveOutline = StaticTiledMapTile(ladder[5][2])
    val ladderMoveDownAboveOutline = StaticTiledMapTile(ladder[6][2])
    val ladderMoveLeftAboveOutline = StaticTiledMapTile(ladder[7][2])
    val ladderMoveUpBelowWhole = StaticTiledMapTile(ladder[4][3])
    val ladderMoveRightBelowWhole = StaticTiledMapTile(ladder[5][3])
    val ladderMoveDownBelowWhole = StaticTiledMapTile(ladder[6][3])
    val ladderMoveLeftBelowWhole = StaticTiledMapTile(ladder[7][3])
    val ladderMoveUpBelowOutline = StaticTiledMapTile(ladder[4][4])
    val ladderMoveRightBelowOutline = StaticTiledMapTile(ladder[5][4])
    val ladderMoveDownBelowOutline = StaticTiledMapTile(ladder[6][4])
    val ladderMoveLeftBelowOutline = StaticTiledMapTile(ladder[7][4])

    val extendableLadder =
        TextureRegion(assetManager.get<Texture>("graphics/objects/extendableLadder.png")).split(tileLength, tileLength)
    val extendableLadderUpBelow = StaticTiledMapTile(extendableLadder[0][0])
    val extendableLadderUpLeft = StaticTiledMapTile(extendableLadder[1][0])
    val extendableLadderUpUp = StaticTiledMapTile(extendableLadder[2][0])
    val extendableLadderUpBehind = StaticTiledMapTile(extendableLadder[3][0])
    val extendableLadderUpBefore = StaticTiledMapTile(extendableLadder[4][0])
    val extendableLadderUpDown = StaticTiledMapTile(extendableLadder[5][0])
    val extendableLadderUpRight = StaticTiledMapTile(extendableLadder[6][0])
    val extendableLadderUpAbove = StaticTiledMapTile(extendableLadder[7][0])
    val extendableLadderRightBelow = StaticTiledMapTile(extendableLadder[0][1])
    val extendableLadderRightLeft = StaticTiledMapTile(extendableLadder[1][1])
    val extendableLadderRightUp = StaticTiledMapTile(extendableLadder[2][1])
    val extendableLadderRightBehind = StaticTiledMapTile(extendableLadder[3][1])
    val extendableLadderRightBefore = StaticTiledMapTile(extendableLadder[4][1])
    val extendableLadderRightDown = StaticTiledMapTile(extendableLadder[5][1])
    val extendableLadderRightRight = StaticTiledMapTile(extendableLadder[6][1])
    val extendableLadderRightAbove = StaticTiledMapTile(extendableLadder[7][1])
    val extendableLadderDownBelow = StaticTiledMapTile(extendableLadder[0][2])
    val extendableLadderDownLeft = StaticTiledMapTile(extendableLadder[1][2])
    val extendableLadderDownUp = StaticTiledMapTile(extendableLadder[2][2])
    val extendableLadderDownBehind = StaticTiledMapTile(extendableLadder[3][2])
    val extendableLadderDownBefore = StaticTiledMapTile(extendableLadder[4][2])
    val extendableLadderDownDown = StaticTiledMapTile(extendableLadder[5][2])
    val extendableLadderDownRight = StaticTiledMapTile(extendableLadder[6][2])
    val extendableLadderDownAbove = StaticTiledMapTile(extendableLadder[7][2])
    val extendableLadderLeftBelow = StaticTiledMapTile(extendableLadder[0][3])
    val extendableLadderLeftLeft = StaticTiledMapTile(extendableLadder[1][3])
    val extendableLadderLeftUp = StaticTiledMapTile(extendableLadder[2][3])
    val extendableLadderLeftBehind = StaticTiledMapTile(extendableLadder[3][3])
    val extendableLadderLeftBefore = StaticTiledMapTile(extendableLadder[4][3])
    val extendableLadderLeftDown = StaticTiledMapTile(extendableLadder[5][3])
    val extendableLadderLeftRight = StaticTiledMapTile(extendableLadder[6][3])
    val extendableLadderLeftAbove = StaticTiledMapTile(extendableLadder[7][3])
    val extendableLadderLadderUp = StaticTiledMapTile(extendableLadder[4][4])
    val extendableLadderLadderRight = StaticTiledMapTile(extendableLadder[5][4])
    val extendableLadderLadderDown = StaticTiledMapTile(extendableLadder[6][4])
    val extendableLadderLadderLeft = StaticTiledMapTile(extendableLadder[7][4])
    val extendableLadderMoveOriginUpAboveWhole = StaticTiledMapTile(extendableLadder[0][5])
    val extendableLadderMoveOriginRightAboveWhole = StaticTiledMapTile(extendableLadder[1][5])
    val extendableLadderMoveOriginDownAboveWhole = StaticTiledMapTile(extendableLadder[2][5])
    val extendableLadderMoveOriginLeftAboveWhole = StaticTiledMapTile(extendableLadder[3][5])
    val extendableLadderMoveLadderUpAboveWhole = StaticTiledMapTile(extendableLadder[4][5])
    val extendableLadderMoveLadderRightAboveWhole = StaticTiledMapTile(extendableLadder[5][5])
    val extendableLadderMoveLadderDownAboveWhole = StaticTiledMapTile(extendableLadder[6][5])
    val extendableLadderMoveLadderLeftAboveWhole = StaticTiledMapTile(extendableLadder[7][5])
    val extendableLadderMoveOriginUpAboveOutline = StaticTiledMapTile(extendableLadder[0][6])
    val extendableLadderMoveOriginRightAboveOutline = StaticTiledMapTile(extendableLadder[1][6])
    val extendableLadderMoveOriginDownAboveOutline = StaticTiledMapTile(extendableLadder[2][6])
    val extendableLadderMoveOriginLeftAboveOutline = StaticTiledMapTile(extendableLadder[3][6])
    val extendableLadderMoveLadderUpAboveOutline = StaticTiledMapTile(extendableLadder[4][6])
    val extendableLadderMoveLadderRightAboveOutline = StaticTiledMapTile(extendableLadder[5][6])
    val extendableLadderMoveLadderDownAboveOutline = StaticTiledMapTile(extendableLadder[6][6])
    val extendableLadderMoveLadderLeftAboveOutline = StaticTiledMapTile(extendableLadder[7][6])
    val extendableLadderMoveOriginUpBelowWhole = StaticTiledMapTile(extendableLadder[0][7])
    val extendableLadderMoveOriginRightBelowWhole = StaticTiledMapTile(extendableLadder[1][7])
    val extendableLadderMoveOriginDownBelowWhole = StaticTiledMapTile(extendableLadder[2][7])
    val extendableLadderMoveOriginLeftBelowWhole = StaticTiledMapTile(extendableLadder[3][7])
    val extendableLadderMoveLadderUpBelowWhole = StaticTiledMapTile(extendableLadder[4][7])
    val extendableLadderMoveLadderRightBelowWhole = StaticTiledMapTile(extendableLadder[5][7])
    val extendableLadderMoveLadderDownBelowWhole = StaticTiledMapTile(extendableLadder[6][7])
    val extendableLadderMoveLadderLeftBelowWhole = StaticTiledMapTile(extendableLadder[7][7])
    val extendableLadderMoveOriginUpBelowOutline = StaticTiledMapTile(extendableLadder[0][8])
    val extendableLadderMoveOriginRightBelowOutline = StaticTiledMapTile(extendableLadder[1][8])
    val extendableLadderMoveOriginDownBelowOutline = StaticTiledMapTile(extendableLadder[2][8])
    val extendableLadderMoveOriginLeftBelowOutline = StaticTiledMapTile(extendableLadder[3][8])
    val extendableLadderMoveLadderUpBelowOutline = StaticTiledMapTile(extendableLadder[4][8])
    val extendableLadderMoveLadderRightBelowOutline = StaticTiledMapTile(extendableLadder[5][8])
    val extendableLadderMoveLadderDownBelowOutline = StaticTiledMapTile(extendableLadder[6][8])
    val extendableLadderMoveLadderLeftBelowOutline = StaticTiledMapTile(extendableLadder[7][8])

    val bridge = TextureRegion(assetManager.get<Texture>("graphics/objects/bridge.png")).split(tileLength, tileLength)
    val bridgeUpBelow = StaticTiledMapTile(bridge[0][0])
    val bridgeUpLeft = StaticTiledMapTile(bridge[1][0])
    val bridgeUpUp = StaticTiledMapTile(bridge[2][0])
    val bridgeUpBehind = StaticTiledMapTile(bridge[3][0])
    val bridgeUpBefore = StaticTiledMapTile(bridge[4][0])
    val bridgeUpDown = StaticTiledMapTile(bridge[5][0])
    val bridgeUpRight = StaticTiledMapTile(bridge[6][0])
    val bridgeUpAbove = StaticTiledMapTile(bridge[7][0])
    val bridgeRightBelow = StaticTiledMapTile(bridge[0][1])
    val bridgeRightLeft = StaticTiledMapTile(bridge[1][1])
    val bridgeRightUp = StaticTiledMapTile(bridge[2][1])
    val bridgeRightBehind = StaticTiledMapTile(bridge[3][1])
    val bridgeRightBefore = StaticTiledMapTile(bridge[4][1])
    val bridgeRightDown = StaticTiledMapTile(bridge[5][1])
    val bridgeRightRight = StaticTiledMapTile(bridge[6][1])
    val bridgeRightAbove = StaticTiledMapTile(bridge[7][1])

    val extendableBridge =
        TextureRegion(assetManager.get<Texture>("graphics/objects/extendableBridge.png")).split(tileLength, tileLength)
    val extendableBridgeOriginUpBelow = StaticTiledMapTile(extendableBridge[0][0])
    val extendableBridgeOriginUpLeft = StaticTiledMapTile(extendableBridge[1][0])
    val extendableBridgeOriginUpUp = StaticTiledMapTile(extendableBridge[2][0])
    val extendableBridgeOriginUpBehind = StaticTiledMapTile(extendableBridge[3][0])
    val extendableBridgeOriginUpBefore = StaticTiledMapTile(extendableBridge[4][0])
    val extendableBridgeOriginUpDown = StaticTiledMapTile(extendableBridge[5][0])
    val extendableBridgeOriginUpRight = StaticTiledMapTile(extendableBridge[6][0])
    val extendableBridgeOriginUpAbove = StaticTiledMapTile(extendableBridge[7][0])
    val extendableBridgeOriginRightBelow = StaticTiledMapTile(extendableBridge[0][1])
    val extendableBridgeOriginRightLeft = StaticTiledMapTile(extendableBridge[1][1])
    val extendableBridgeOriginRightUp = StaticTiledMapTile(extendableBridge[2][1])
    val extendableBridgeOriginRightBehind = StaticTiledMapTile(extendableBridge[3][1])
    val extendableBridgeOriginRightBefore = StaticTiledMapTile(extendableBridge[4][1])
    val extendableBridgeOriginRightDown = StaticTiledMapTile(extendableBridge[5][1])
    val extendableBridgeOriginRightRight = StaticTiledMapTile(extendableBridge[6][1])
    val extendableBridgeOriginRightAbove = StaticTiledMapTile(extendableBridge[7][1])
    val extendableBridgeExtensionUpBelow = StaticTiledMapTile(extendableBridge[0][2])
    val extendableBridgeExtensionUpLeft = StaticTiledMapTile(extendableBridge[1][2])
    val extendableBridgeExtensionUpUp = StaticTiledMapTile(extendableBridge[2][2])
    val extendableBridgeExtensionUpBehind = StaticTiledMapTile(extendableBridge[3][2])
    val extendableBridgeExtensionUpBefore = StaticTiledMapTile(extendableBridge[4][2])
    val extendableBridgeExtensionUpDown = StaticTiledMapTile(extendableBridge[5][2])
    val extendableBridgeExtensionUpRight = StaticTiledMapTile(extendableBridge[6][2])
    val extendableBridgeExtensionUpAbove = StaticTiledMapTile(extendableBridge[7][2])
    val extendableBridgeExtensionRightBelow = StaticTiledMapTile(extendableBridge[0][3])
    val extendableBridgeExtensionRightLeft = StaticTiledMapTile(extendableBridge[1][3])
    val extendableBridgeExtensionRightUp = StaticTiledMapTile(extendableBridge[2][3])
    val extendableBridgeExtensionRightBehind = StaticTiledMapTile(extendableBridge[3][3])
    val extendableBridgeExtensionRightBefore = StaticTiledMapTile(extendableBridge[4][3])
    val extendableBridgeExtensionRightDown = StaticTiledMapTile(extendableBridge[5][3])
    val extendableBridgeExtensionRightRight = StaticTiledMapTile(extendableBridge[6][3])
    val extendableBridgeExtensionRightAbove = StaticTiledMapTile(extendableBridge[7][3])

    val vendingMachine =
        TextureRegion(assetManager.get<Texture>("graphics/objects/vendingMachine.png")).split(tileLength, tileLength)
    val vendingMachineBlankBelow = StaticTiledMapTile(vendingMachine[0][0])
    val vendingMachineBlankLeft = StaticTiledMapTile(vendingMachine[1][0])
    val vendingMachineBlankUp = StaticTiledMapTile(vendingMachine[2][0])
    val vendingMachineBlankBehind = StaticTiledMapTile(vendingMachine[3][0])
    val vendingMachineBlankBefore = StaticTiledMapTile(vendingMachine[4][0])
    val vendingMachineBlankDown = StaticTiledMapTile(vendingMachine[5][0])
    val vendingMachineBlankRight = StaticTiledMapTile(vendingMachine[6][0])
    val vendingMachineBlankAbove = StaticTiledMapTile(vendingMachine[7][0])
    val vendingMachineScreenBelow = StaticTiledMapTile(vendingMachine[0][1])
    val vendingMachineScreenLeft = StaticTiledMapTile(vendingMachine[1][1])
    val vendingMachineScreenUp = StaticTiledMapTile(vendingMachine[2][1])
    val vendingMachineScreenBehind = StaticTiledMapTile(vendingMachine[3][1])
    val vendingMachineScreenBefore = StaticTiledMapTile(vendingMachine[4][1])
    val vendingMachineScreenDown = StaticTiledMapTile(vendingMachine[5][1])
    val vendingMachineScreenRight = StaticTiledMapTile(vendingMachine[6][1])
    val vendingMachineScreenAbove = StaticTiledMapTile(vendingMachine[7][1])
    val vendingMachineBlankBrokenBelow = StaticTiledMapTile(vendingMachine[0][2])
    val vendingMachineBlankBrokenLeft = StaticTiledMapTile(vendingMachine[1][2])
    val vendingMachineBlankBrokenUp = StaticTiledMapTile(vendingMachine[2][2])
    val vendingMachineBlankBrokenBehind = StaticTiledMapTile(vendingMachine[3][2])
    val vendingMachineBlankBrokenBefore = StaticTiledMapTile(vendingMachine[4][2])
    val vendingMachineBlankBrokenDown = StaticTiledMapTile(vendingMachine[5][2])
    val vendingMachineBlankBrokenRight = StaticTiledMapTile(vendingMachine[6][2])
    val vendingMachineBlankBrokenAbove = StaticTiledMapTile(vendingMachine[7][2])
    val vendingMachineScreenBrokenBelow = StaticTiledMapTile(vendingMachine[0][3])
    val vendingMachineScreenBrokenLeft = StaticTiledMapTile(vendingMachine[1][3])
    val vendingMachineScreenBrokenUp = StaticTiledMapTile(vendingMachine[2][3])
    val vendingMachineScreenBrokenBehind = StaticTiledMapTile(vendingMachine[3][3])
    val vendingMachineScreenBrokenBefore = StaticTiledMapTile(vendingMachine[4][3])
    val vendingMachineScreenBrokenDown = StaticTiledMapTile(vendingMachine[5][3])
    val vendingMachineScreenBrokenRight = StaticTiledMapTile(vendingMachine[6][3])
    val vendingMachineScreenBrokenAbove = StaticTiledMapTile(vendingMachine[7][3])

    val switch = TextureRegion(assetManager.get<Texture>("graphics/objects/switch.png")).split(tileLength, tileLength)
    val switchBlankBelow = StaticTiledMapTile(switch[0][0])
    val switchBlankLeft = StaticTiledMapTile(switch[1][0])
    val switchBlankUp = StaticTiledMapTile(switch[2][0])
    val switchBlankBehind = StaticTiledMapTile(switch[3][0])
    val switchBlankBefore = StaticTiledMapTile(switch[4][0])
    val switchBlankDown = StaticTiledMapTile(switch[5][0])
    val switchBlankRight = StaticTiledMapTile(switch[6][0])
    val switchBlankAbove = StaticTiledMapTile(switch[7][0])
    val switchButtonBelow = StaticTiledMapTile(switch[0][1])
    val switchButtonLeft = StaticTiledMapTile(switch[1][1])
    val switchButtonUp = StaticTiledMapTile(switch[2][1])
    val switchButtonBehind = StaticTiledMapTile(switch[3][1])
    val switchButtonBefore = StaticTiledMapTile(switch[4][1])
    val switchButtonDown = StaticTiledMapTile(switch[5][1])
    val switchButtonRight = StaticTiledMapTile(switch[6][1])
    val switchButtonAbove = StaticTiledMapTile(switch[7][1])

    val station = TextureRegion(assetManager.get<Texture>("graphics/objects/station.png")).split(tileLength, tileLength)
    val stationBelow = StaticTiledMapTile(station[0][0])
    val stationLeft = StaticTiledMapTile(station[1][0])
    val stationUp = StaticTiledMapTile(station[2][0])
    val stationBehind = StaticTiledMapTile(station[3][0])
    val stationBefore = StaticTiledMapTile(station[4][0])
    val stationDown = StaticTiledMapTile(station[5][0])
    val stationRight = StaticTiledMapTile(station[6][0])
    val stationAbove = StaticTiledMapTile(station[7][0])

    val chest = TextureRegion(assetManager.get<Texture>("graphics/objects/chest.png")).split(tileLength, tileLength)
    val chestBackBelow = StaticTiledMapTile(chest[0][0])
    val chestBackLeft = StaticTiledMapTile(chest[1][0])
    val chestBackUp = StaticTiledMapTile(chest[2][0])
    val chestBackBehind = StaticTiledMapTile(chest[3][0])
    val chestBackBefore = StaticTiledMapTile(chest[4][0])
    val chestBackDown = StaticTiledMapTile(chest[5][0])
    val chestBackRight = StaticTiledMapTile(chest[6][0])
    val chestBackAbove = StaticTiledMapTile(chest[7][0])
    val chestSideBelow = StaticTiledMapTile(chest[0][1])
    val chestSideLeft = StaticTiledMapTile(chest[1][1])
    val chestSideUp = StaticTiledMapTile(chest[2][1])
    val chestSideBehind = StaticTiledMapTile(chest[3][1])
    val chestSideBefore = StaticTiledMapTile(chest[4][1])
    val chestSideDown = StaticTiledMapTile(chest[5][1])
    val chestSideRight = StaticTiledMapTile(chest[6][1])
    val chestSideAbove = StaticTiledMapTile(chest[7][1])
    val chestFrontBelow = StaticTiledMapTile(chest[0][2])
    val chestFrontLeft = StaticTiledMapTile(chest[1][2])
    val chestFrontUp = StaticTiledMapTile(chest[2][2])
    val chestFrontBehind = StaticTiledMapTile(chest[3][2])
    val chestFrontBefore = StaticTiledMapTile(chest[4][2])
    val chestFrontDown = StaticTiledMapTile(chest[5][2])
    val chestFrontRight = StaticTiledMapTile(chest[6][2])
    val chestFrontAbove = StaticTiledMapTile(chest[7][2])

    val teleporter =
        TextureRegion(assetManager.get<Texture>("graphics/objects/teleporter.png")).split(tileLength, tileLength)
    val teleporterUnpoweredBelow = StaticTiledMapTile(teleporter[0][0])
    val teleporterUnpoweredLeft = StaticTiledMapTile(teleporter[1][0])
    val teleporterUnpoweredUp = StaticTiledMapTile(teleporter[2][0])
    val teleporterUnpoweredBehind = StaticTiledMapTile(teleporter[3][0])
    val teleporterUnpoweredBefore = StaticTiledMapTile(teleporter[4][0])
    val teleporterUnpoweredDown = StaticTiledMapTile(teleporter[5][0])
    val teleporterUnpoweredRight = StaticTiledMapTile(teleporter[6][0])
    val teleporterUnpoweredAbove = StaticTiledMapTile(teleporter[7][0])
    val teleporterPoweredBelow = StaticTiledMapTile(teleporter[0][1])
    val teleporterPoweredLeft = StaticTiledMapTile(teleporter[1][1])
    val teleporterPoweredUp = StaticTiledMapTile(teleporter[2][1])
    val teleporterPoweredBehind = StaticTiledMapTile(teleporter[3][1])
    val teleporterPoweredBefore = StaticTiledMapTile(teleporter[4][1])
    val teleporterPoweredDown = StaticTiledMapTile(teleporter[5][1])
    val teleporterPoweredRight = StaticTiledMapTile(teleporter[6][1])
    val teleporterPoweredAbove = StaticTiledMapTile(teleporter[7][1])
    val teleporterFieldBehind = StaticTiledMapTile(teleporter[3][2])
    val teleporterFieldBefore = StaticTiledMapTile(teleporter[4][2])
    val teleporterFieldMoveUp = StaticTiledMapTile(teleporter[0][3])
    val teleporterFieldMoveUpRight = StaticTiledMapTile(teleporter[1][3])
    val teleporterFieldMoveRight = StaticTiledMapTile(teleporter[2][3])
    val teleporterFieldMoveDownRight = StaticTiledMapTile(teleporter[3][3])
    val teleporterFieldMoveDown = StaticTiledMapTile(teleporter[4][3])
    val teleporterFieldMoveDownLeft = StaticTiledMapTile(teleporter[5][3])
    val teleporterFieldMoveLeft = StaticTiledMapTile(teleporter[6][3])
    val teleporterFieldMoveUpLeft = StaticTiledMapTile(teleporter[7][3])

    val door = TextureRegion(assetManager.get<Texture>("graphics/objects/door.png")).split(tileLength, tileLength)
    val doorBlankBelow = StaticTiledMapTile(door[0][0])
    val doorBlankLeft = StaticTiledMapTile(door[1][0])
    val doorBlankUp = StaticTiledMapTile(door[2][0])
    val doorBlankBehind = StaticTiledMapTile(door[3][0])
    val doorBlankBefore = StaticTiledMapTile(door[4][0])
    val doorBlankDown = StaticTiledMapTile(door[5][0])
    val doorBlankRight = StaticTiledMapTile(door[6][0])
    val doorBlankAbove = StaticTiledMapTile(door[7][0])
    val doorDoorBelow = StaticTiledMapTile(door[0][1])
    val doorDoorLeft = StaticTiledMapTile(door[1][1])
    val doorDoorUp = StaticTiledMapTile(door[2][1])
    val doorDoorBehind = StaticTiledMapTile(door[3][1])
    val doorDoorBefore = StaticTiledMapTile(door[4][1])
    val doorDoorDown = StaticTiledMapTile(door[5][1])
    val doorDoorRight = StaticTiledMapTile(door[6][1])
    val doorDoorAbove = StaticTiledMapTile(door[7][1])

    val poweredDoor =
        TextureRegion(assetManager.get<Texture>("graphics/objects/poweredDoor.png")).split(tileLength, tileLength)
    val poweredDoorBlankBelow = StaticTiledMapTile(poweredDoor[0][0])
    val poweredDoorBlankLeft = StaticTiledMapTile(poweredDoor[1][0])
    val poweredDoorBlankUp = StaticTiledMapTile(poweredDoor[2][0])
    val poweredDoorBlankBehind = StaticTiledMapTile(poweredDoor[3][0])
    val poweredDoorBlankBefore = StaticTiledMapTile(poweredDoor[4][0])
    val poweredDoorBlankDown = StaticTiledMapTile(poweredDoor[5][0])
    val poweredDoorBlankRight = StaticTiledMapTile(poweredDoor[6][0])
    val poweredDoorBlankAbove = StaticTiledMapTile(poweredDoor[7][0])
    val poweredDoorUnpoweredBelow = StaticTiledMapTile(poweredDoor[0][1])
    val poweredDoorUnpoweredLeft = StaticTiledMapTile(poweredDoor[1][1])
    val poweredDoorUnpoweredUp = StaticTiledMapTile(poweredDoor[2][1])
    val poweredDoorUnpoweredBehind = StaticTiledMapTile(poweredDoor[3][1])
    val poweredDoorUnpoweredBefore = StaticTiledMapTile(poweredDoor[4][1])
    val poweredDoorUnpoweredDown = StaticTiledMapTile(poweredDoor[5][1])
    val poweredDoorUnpoweredRight = StaticTiledMapTile(poweredDoor[6][1])
    val poweredDoorUnpoweredAbove = StaticTiledMapTile(poweredDoor[7][1])
    val poweredDoorPoweredBelow = StaticTiledMapTile(poweredDoor[0][2])
    val poweredDoorPoweredLeft = StaticTiledMapTile(poweredDoor[1][2])
    val poweredDoorPoweredUp = StaticTiledMapTile(poweredDoor[2][2])
    val poweredDoorPoweredBehind = StaticTiledMapTile(poweredDoor[3][2])
    val poweredDoorPoweredBefore = StaticTiledMapTile(poweredDoor[4][2])
    val poweredDoorPoweredDown = StaticTiledMapTile(poweredDoor[5][2])
    val poweredDoorPoweredRight = StaticTiledMapTile(poweredDoor[6][2])
    val poweredDoorPoweredAbove = StaticTiledMapTile(poweredDoor[7][2])

    val elevator = TextureRegion(assetManager.get<Texture>("graphics/objects/elevator.png")).split(tileLength, tileLength)
    val elevatorLeft = StaticTiledMapTile(elevator[0][0])
    val elevatorUp = StaticTiledMapTile(elevator[0][1])
    val elevatorEmptyDown = StaticTiledMapTile(elevator[1][0])
    val elevatorEmptyRight = StaticTiledMapTile(elevator[1][1])
    val elevatorEmptyBrokenDown = StaticTiledMapTile(elevator[2][0])
    val elevatorEmptyBrokenRight = StaticTiledMapTile(elevator[2][1])
    val elevatorDoorDown = StaticTiledMapTile(elevator[3][0])
    val elevatorDoorRight = StaticTiledMapTile(elevator[3][1])
    val elevatorDoorBrokenDown = StaticTiledMapTile(elevator[4][0])
    val elevatorDoorBrokenRight = StaticTiledMapTile(elevator[4][1])
    val elevatorScreenBrokenDown = StaticTiledMapTile(elevator[5][0])
    val elevatorScreenBrokenRight = StaticTiledMapTile(elevator[5][1])
    val elevatorScreenBlankDown = StaticTiledMapTile(elevator[6][0])
    val elevatorScreenBlankRight = StaticTiledMapTile(elevator[6][1])
    val elevatorScreen1Down = StaticTiledMapTile(elevator[7][0])
    val elevatorScreen1Right = StaticTiledMapTile(elevator[7][1])
    val elevatorScreen2Down = StaticTiledMapTile(elevator[8][0])
    val elevatorScreen2Right = StaticTiledMapTile(elevator[8][1])
    val elevatorScreen3Down = StaticTiledMapTile(elevator[9][0])
    val elevatorScreen3Right = StaticTiledMapTile(elevator[9][1])
    val elevatorScreen4Down = StaticTiledMapTile(elevator[10][0])
    val elevatorScreen4Right = StaticTiledMapTile(elevator[10][1])
    val elevatorScreen5Down = StaticTiledMapTile(elevator[11][0])
    val elevatorScreen5Right = StaticTiledMapTile(elevator[11][1])
    val elevatorScreen6Down = StaticTiledMapTile(elevator[12][0])
    val elevatorScreen6Right = StaticTiledMapTile(elevator[12][1])
    val elevatorScreen7Down = StaticTiledMapTile(elevator[13][0])
    val elevatorScreen7Right = StaticTiledMapTile(elevator[13][1])
    val elevatorScreen8Down = StaticTiledMapTile(elevator[14][0])
    val elevatorScreen8Right = StaticTiledMapTile(elevator[14][1])
    val elevatorScreen9Down = StaticTiledMapTile(elevator[15][0])
    val elevatorScreen9Right = StaticTiledMapTile(elevator[15][1])

    val rope = TextureRegion(assetManager.get<Texture>("graphics/objects/rope.png")).split(tileLength, tileLength)
    val ropeMiddle = StaticTiledMapTile(rope[0][0])
    val ropeBottom = StaticTiledMapTile(rope[1][0])
    val ropeTopUp = StaticTiledMapTile(rope[0][2])
    val ropeTopRight = StaticTiledMapTile(rope[1][2])
    val ropeTopDown = StaticTiledMapTile(rope[1][1])
    val ropeTopLeft = StaticTiledMapTile(rope[0][1])
    val ropeMoveAboveWhole = StaticTiledMapTile(rope[0][3])
    val ropeMoveAboveOutline = StaticTiledMapTile(rope[0][4])
    val ropeMoveBelowWhole = StaticTiledMapTile(rope[1][3])
    val ropeMoveBelowOutline = StaticTiledMapTile(rope[1][4])

    /*
    val Below = StaticTiledMapTile(stairs[0][0])
    val Left = StaticTiledMapTile(stairs[1][0])
    val Up = StaticTiledMapTile(stairs[2][0])
    val Behind = StaticTiledMapTile(stairs[3][0])
    val Before = StaticTiledMapTile(stairs[4][0])
    val Down = StaticTiledMapTile(stairs[5][0])
    val Right = StaticTiledMapTile(stairs[6][0])
    val Above = StaticTiledMapTile(stairs[7][0])
     */
}
