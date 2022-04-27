package com.marcin.panklex.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.marcin.panklex.*
import kotlin.math.truncate

class ScreenGame(val name: String, val game: PanKlexGame) : BaseScreen(name, game)
{
    val mapCamera = OrthographicCamera()
    val mapViewport = ScreenViewport(mapCamera)

    val level = KlexLevel()
    val map = KlexMap(100, 100, 32, 32)

    val testTileset = game.assetManager.get<Texture>("tilesets/testTileset.png")
    val blueTile = StaticTiledMapTile(TextureRegion(testTileset, 0 * 32, 0 * 32, 32, 32))
    val greenTile = StaticTiledMapTile(TextureRegion(testTileset, 1 * 32, 0 * 32, 32, 32))
    val redTile = StaticTiledMapTile(TextureRegion(testTileset, 2 * 32, 0 * 32, 32, 32))
    val yellowTile = StaticTiledMapTile(TextureRegion(testTileset, 3 * 32, 0 * 32, 32, 32))
    val purpleTile = StaticTiledMapTile(TextureRegion(testTileset, 0 * 32, 1 * 32, 32, 32))
    val selectTile = StaticTiledMapTile(TextureRegion(testTileset, 1 * 32, 1 * 32, 32, 32))
    val objectTile = StaticTiledMapTile(TextureRegion(testTileset, 2 * 32, 1 * 32, 32, 32))
    val emptyTile = StaticTiledMapTile(TextureRegion(testTileset, 3 * 32, 1 * 32, 32, 32))

    val holeTileset = game.assetManager.get<Texture>("tilesets/holeTileset.png")
    val hole_l_m_r = StaticTiledMapTile(TextureRegion(holeTileset, 0 * 32, 0 * 32, 32, 32))
    val hole_l_m = StaticTiledMapTile(TextureRegion(holeTileset, 1 * 32, 0 * 32, 32, 32))
    val hole_m = StaticTiledMapTile(TextureRegion(holeTileset, 2 * 32, 0 * 32, 32, 32))
    val hole_m_r = StaticTiledMapTile(TextureRegion(holeTileset, 3 * 32, 0 * 32, 32, 32))
    val hole_none = StaticTiledMapTile(TextureRegion(holeTileset, 4 * 32, 0 * 32, 32, 32))
    val hole_empty = StaticTiledMapTile(TextureRegion(holeTileset, 5 * 32, 0 * 32, 32, 32))

    val baseTileset = game.assetManager.get<Texture>("tilesets/baseTileset.png")
    val base_ul = StaticTiledMapTile(TextureRegion(baseTileset, 0 * 32, 0 * 32, 32, 32))
    val base_ur = StaticTiledMapTile(TextureRegion(baseTileset, 1 * 32, 0 * 32, 32, 32))
    val base_ul_ur = StaticTiledMapTile(TextureRegion(baseTileset, 2 * 32, 0 * 32, 32, 32))
    val base_ur_dr = StaticTiledMapTile(TextureRegion(baseTileset, 3 * 32, 0 * 32, 32, 32))
    val base_ul_ur_dr = StaticTiledMapTile(TextureRegion(baseTileset, 4 * 32, 0 * 32, 32, 32))
    val base_ur_dr_dl = StaticTiledMapTile(TextureRegion(baseTileset, 5 * 32, 0 * 32, 32, 32))
    val base_ul_u_ur_r_dr = StaticTiledMapTile(TextureRegion(baseTileset, 6 * 32, 0 * 32, 32, 32))
    val base_ur_r_dr_d_dl = StaticTiledMapTile(TextureRegion(baseTileset, 7 * 32, 0 * 32, 32, 32))
    val base_ul_u_ur_r_dr_dl = StaticTiledMapTile(TextureRegion(baseTileset, 8 * 32, 0 * 32, 32, 32))
    val base_ul_ur_r_dr_d_dl = StaticTiledMapTile(TextureRegion(baseTileset, 9 * 32, 0 * 32, 32, 32))
    val base_ul_u_ur_r_dr_d_dl = StaticTiledMapTile(TextureRegion(baseTileset, 10 * 32, 0 * 32, 32, 32))
    val base_ul_ur_r_dr_d_dl_l = StaticTiledMapTile(TextureRegion(baseTileset, 11 * 32, 0 * 32, 32, 32))
    val base_dl = StaticTiledMapTile(TextureRegion(baseTileset, 0 * 32, 1 * 32, 32, 32))
    val base_dr = StaticTiledMapTile(TextureRegion(baseTileset, 1 * 32, 1 * 32, 32, 32))
    val base_ul_dl = StaticTiledMapTile(TextureRegion(baseTileset, 2 * 32, 1 * 32, 32, 32))
    val base_dr_dl = StaticTiledMapTile(TextureRegion(baseTileset, 3 * 32, 1 * 32, 32, 32))
    val base_ul_ur_dl = StaticTiledMapTile(TextureRegion(baseTileset, 4 * 32, 1 * 32, 32, 32))
    val base_ul_dr_dl = StaticTiledMapTile(TextureRegion(baseTileset, 5 * 32, 1 * 32, 32, 32))
    val base_ul_u_ur_dl_l = StaticTiledMapTile(TextureRegion(baseTileset, 6 * 32, 1 * 32, 32, 32))
    val base_ul_dr_d_dl_l = StaticTiledMapTile(TextureRegion(baseTileset, 7 * 32, 1 * 32, 32, 32))
    val base_ul_u_ur_dr_dl_l = StaticTiledMapTile(TextureRegion(baseTileset, 8 * 32, 1 * 32, 32, 32))
    val base_ul_ur_dr_d_dl_l = StaticTiledMapTile(TextureRegion(baseTileset, 9 * 32, 1 * 32, 32, 32))
    val base_ul_u_ur_r_dr_dl_l = StaticTiledMapTile(TextureRegion(baseTileset, 10 * 32, 1 * 32, 32, 32))
    val base_ul_u_ur_dr_d_dl_l = StaticTiledMapTile(TextureRegion(baseTileset, 11 * 32, 1 * 32, 32, 32))
    val base_ul_u_ur = StaticTiledMapTile(TextureRegion(baseTileset, 0 * 32, 2 * 32, 32, 32))
    val base_ur_r_dr = StaticTiledMapTile(TextureRegion(baseTileset, 1 * 32, 2 * 32, 32, 32))
    val base_ul_u_ur_dl = StaticTiledMapTile(TextureRegion(baseTileset, 2 * 32, 2 * 32, 32, 32))
    val base_ul_ur_r_dr = StaticTiledMapTile(TextureRegion(baseTileset, 3 * 32, 2 * 32, 32, 32))
    val base_ul_u_ur_dr = StaticTiledMapTile(TextureRegion(baseTileset, 4 * 32, 2 * 32, 32, 32))
    val base_ur_r_dr_dl = StaticTiledMapTile(TextureRegion(baseTileset, 5 * 32, 2 * 32, 32, 32))
    val base_ul_u_ur_dr_dl = StaticTiledMapTile(TextureRegion(baseTileset, 6 * 32, 2 * 32, 32, 32))
    val base_ul_ur_r_dr_dl = StaticTiledMapTile(TextureRegion(baseTileset, 7 * 32, 2 * 32, 32, 32))
    val base_ul_u_ur_dr_d_dl = StaticTiledMapTile(TextureRegion(baseTileset, 8 * 32, 2 * 32, 32, 32))
    val base_ul_dr = StaticTiledMapTile(TextureRegion(baseTileset, 9 * 32, 2 * 32, 32, 32))
    val base_ul_ur_dr_dl = StaticTiledMapTile(TextureRegion(baseTileset, 10 * 32, 2 * 32, 32, 32))
    val base_none = StaticTiledMapTile(TextureRegion(baseTileset, 11 * 32, 2 * 32, 32, 32))
    val base_ul_dl_l = StaticTiledMapTile(TextureRegion(baseTileset, 0 * 32, 3 * 32, 32, 32))
    val base_dr_d_dl = StaticTiledMapTile(TextureRegion(baseTileset, 1 * 32, 3 * 32, 32, 32))
    val base_ul_dr_dl_l = StaticTiledMapTile(TextureRegion(baseTileset, 2 * 32, 3 * 32, 32, 32))
    val base_ur_dr_d_dl = StaticTiledMapTile(TextureRegion(baseTileset, 3 * 32, 3 * 32, 32, 32))
    val base_ul_ur_dl_l = StaticTiledMapTile(TextureRegion(baseTileset, 4 * 32, 3 * 32, 32, 32))
    val base_ul_dr_d_dl = StaticTiledMapTile(TextureRegion(baseTileset, 5 * 32, 3 * 32, 32, 32))
    val base_ul_ur_dr_dl_l = StaticTiledMapTile(TextureRegion(baseTileset, 6 * 32, 3 * 32, 32, 32))
    val base_ul_ur_dr_d_dl = StaticTiledMapTile(TextureRegion(baseTileset, 7 * 32, 3 * 32, 32, 32))
    val base_ul_ur_r_dr_dl_l = StaticTiledMapTile(TextureRegion(baseTileset, 8 * 32, 3 * 32, 32, 32))
    val base_ur_dl = StaticTiledMapTile(TextureRegion(baseTileset, 9 * 32, 3 * 32, 32, 32))
    val base_ul_u_ur_r_dr_d_dl_l = StaticTiledMapTile(TextureRegion(baseTileset, 10 * 32, 3 * 32, 32, 32))
    val base_empty = StaticTiledMapTile(TextureRegion(baseTileset, 11 * 32, 3 * 32, 32, 32))

    val wallTileset = game.assetManager.get<Texture>("tilesets/wallTileset.png")
    val wall_l_m_r = StaticTiledMapTile(TextureRegion(wallTileset, 0 * 32, 0 * 32, 32, 32))
    val wall_l_m = StaticTiledMapTile(TextureRegion(wallTileset, 1 * 32, 0 * 32, 32, 32))
    val wall_m = StaticTiledMapTile(TextureRegion(wallTileset, 2 * 32, 0 * 32, 32, 32))
    val wall_m_r = StaticTiledMapTile(TextureRegion(wallTileset, 3 * 32, 0 * 32, 32, 32))
    val wall_empty = StaticTiledMapTile(TextureRegion(wallTileset, 4 * 32, 0 * 32, 32, 32))

    val coverTileset = game.assetManager.get<Texture>("tilesets/coverTileset.png")
    val cover_half = StaticTiledMapTile(TextureRegion(coverTileset, 0 * 32, 0 * 32, 32, 32))
    val cover_all = StaticTiledMapTile(TextureRegion(coverTileset, 1 * 32, 0 * 32, 32, 32))
    val cover_empty = StaticTiledMapTile(TextureRegion(coverTileset, 2 * 32, 0 * 32, 32, 32))

    var currentLevel = 0

    var selectPosition = Vector2()
    var isMouseInMap = true
    var isSelectFixed = false

    var isDragging = false
    var dragOrigin = Vector2()
    var dragDifference = Vector2()

    init
    { // stage

        val levelLabel = Label("Level: 0", Label.LabelStyle(BitmapFont(), Color.GREEN))
        val upButton = TextButton("Up", TextButton.TextButtonStyle(null, null, null, BitmapFont()))
        val downButton = TextButton("Down", TextButton.TextButtonStyle(null, null, null, BitmapFont()))
        val endGameButton = TextButton("End Game", TextButton.TextButtonStyle(null, null, null, BitmapFont()))

        upButton.addListener(object : ClickListener()
        {
            override fun clicked(event: InputEvent?, x: Float, y: Float)
            {
                if (currentLevel < level.levels - 1)
                {
                    currentLevel++
                    levelLabel.setText("Level: $currentLevel")
                    loadLevelToMap(currentLevel)
                }
            }
        })

        downButton.addListener(object : ClickListener()
        {
            override fun clicked(event: InputEvent?, x: Float, y: Float)
            {
                if (currentLevel > 0)
                {
                    currentLevel--
                    levelLabel.setText("Level: $currentLevel")
                    loadLevelToMap(currentLevel)
                }
            }
        })

        endGameButton.addListener(object : ClickListener()
        {
            override fun clicked(event: InputEvent?, x: Float, y: Float)
            {
                game.changeScreen(game.screenEndGame)
            }
        })

        val table = Table()
        table.top().setFillParent(true)
        table.add(levelLabel).pad(10f)
        table.add(upButton).pad(10f)
        table.add(downButton).pad(10f)
        table.add(endGameButton).pad(10f)
        stage.addActor(table)

        // init

        loadMapFromJson("levels/3.json")
        loadLevelToMap(0)
        mapCamera.zoom = 0.5f
    }

    fun loadMapFromJson(jsonPath: String)
    {
        val jsonFile = Gdx.files.internal(jsonPath)
        val jsonString = jsonFile.readString()
        val jsonLevel = Json().fromJson(JsonLevel::class.java, jsonString)

        level.version = jsonLevel.version
        level.name = jsonLevel.name
        level.levels = jsonLevel.levels
        level.width = jsonLevel.width
        level.height = jsonLevel.height

        val levelList = mutableListOf<List<List<LevelBlock>>>()

        for (i in 0..level.levels - 1)
        {
            val columnList = mutableListOf<List<LevelBlock>>()

            for (j in 0..level.height - 1)
            {
                val rowList = mutableListOf<LevelBlock>()

                for (k in 0..level.width - 1)
                {
                    val levelBlock = LevelBlock()
                    levelBlock.position.x = k.toFloat()
                    levelBlock.position.y = j.toFloat()
                    levelBlock.position.z = i.toFloat()
                    levelBlock.rock = when (jsonLevel.map[i][j][k])
                    {
                        0 -> Rock.Empty
                        1 -> Rock.Weak
                        2 -> Rock.Strong
                        3 -> Rock.Indestructible
                        else -> Rock.Empty
                    }

                    rowList.add(levelBlock)
                }

                columnList.add(rowList)
            }

            levelList.add(columnList)
        }

        level.map = levelList
    }

    fun loadLevelToMap(levelNr: Int)
    {
        for (i in 0..level.width - 1)
        {
            for (j in 0..level.height - 1)
            {
                val isRock = level.map[levelNr][j][i].rock != Rock.Empty
                val isUpLeftRock =
                    !(j < level.height - 1 && i > 0 && level.map[levelNr][j + 1][i - 1].rock == Rock.Empty)
                val isUpRock = !(j < level.height - 1 && level.map[levelNr][j + 1][i].rock == Rock.Empty)
                val isUpRightRock =
                    !(j < level.height - 1 && i < level.width - 1 && level.map[levelNr][j + 1][i + 1].rock == Rock.Empty)
                val isRightRock = !(i < level.width - 1 && level.map[levelNr][j][i + 1].rock == Rock.Empty)
                val isDownRightRock =
                    !(j > 0 && i < level.width - 1 && level.map[levelNr][j - 1][i + 1].rock == Rock.Empty)
                val isDownRock = !(j > 0 && level.map[levelNr][j - 1][i].rock == Rock.Empty)
                val isDownLeftRock = !(j > 0 && i > 0 && level.map[levelNr][j - 1][i - 1].rock == Rock.Empty)
                val isLeftRock = !(i > 0 && level.map[levelNr][j][i - 1].rock == Rock.Empty)

                val isAboveRock = !(levelNr < level.levels - 1 && level.map[levelNr + 1][j][i].rock == Rock.Empty)
                val isAboveUpLeftRock =
                    !(levelNr < level.levels - 1 && j < level.height - 1 && i > 0 && level.map[levelNr + 1][j + 1][i - 1].rock == Rock.Empty)
                val isAboveUpRock =
                    !(levelNr < level.levels - 1 && j < level.height - 1 && level.map[levelNr + 1][j + 1][i].rock == Rock.Empty)
                val isAboveUpRightRock =
                    !(levelNr < level.levels - 1 && j < level.height - 1 && i < level.width - 1 && level.map[levelNr + 1][j + 1][i + 1].rock == Rock.Empty)
                val isAboveRightRock =
                    !(levelNr < level.levels - 1 && i < level.width - 1 && level.map[levelNr + 1][j][i + 1].rock == Rock.Empty)
                val isAboveDownRightRock =
                    !(levelNr < level.levels - 1 && j > 0 && i < level.width - 1 && level.map[levelNr + 1][j - 1][i + 1].rock == Rock.Empty)
                val isAboveDownRock =
                    !(levelNr < level.levels - 1 && j > 0 && level.map[levelNr + 1][j - 1][i].rock == Rock.Empty)
                val isAboveDownLeftRock =
                    !(levelNr < level.levels - 1 && j > 0 && i > 0 && level.map[levelNr + 1][j - 1][i - 1].rock == Rock.Empty)
                val isAboveLeftRock =
                    !(levelNr < level.levels - 1 && i > 0 && level.map[levelNr + 1][j][i - 1].rock == Rock.Empty)

                val holeTile = when
                {
                    isRock -> hole_empty
                    !isUpRock -> hole_none
                    !isUpLeftRock && !isUpRightRock -> hole_l_m_r
                    !isUpLeftRock && isUpRightRock -> hole_l_m
                    isUpLeftRock && isUpRightRock -> hole_m
                    isUpLeftRock && !isUpRightRock -> hole_m_r
                    else -> hole_empty
                }

                val ul = !isUpLeftRock || (isUpLeftRock && isAboveUpLeftRock)
                val u = !isUpRock || (isUpRock && isAboveUpRock)
                val ur = !isUpRightRock || (isUpRightRock && isAboveUpRightRock)
                val r = !isRightRock || (isRightRock && isAboveRightRock)
                val dr = !isDownRightRock || (isDownRightRock && isAboveDownRightRock)
                val d = !isDownRock || (isDownRock && isAboveDownRock)
                val dl = !isDownLeftRock || (isDownLeftRock && isAboveDownLeftRock)
                val l = !isLeftRock || (isLeftRock && isAboveLeftRock)

                val baseTile = when
                {
                    !isRock -> base_empty
                    ul && !u && !ur && !r && !dr && !d && !dl && !l -> base_ul
                    !ul && !u && ur && !r && !dr && !d && !dl && !l -> base_ur
                    !ul && !u && !ur && !r && dr && !d && !dl && !l -> base_dr
                    !ul && !u && !ur && !r && !dr && !d && dl && !l -> base_dl
                    ul && !u && ur && !r && !dr && !d && !dl && !l -> base_ul_ur
                    !ul && !u && ur && !r && dr && !d && !dl && !l -> base_ur_dr
                    !ul && !u && !ur && !r && dr && !d && dl && !l -> base_dr_dl
                    ul && !u && !ur && !r && !dr && !d && dl && !l -> base_ul_dl
                    ul && !u && ur && !r && dr && !d && !dl && !l -> base_ul_ur_dr
                    !ul && !u && ur && !r && dr && !d && dl && !l -> base_ur_dr_dl
                    ul && !u && !ur && !r && dr && !d && dl && !l -> base_ul_dr_dl
                    ul && !u && ur && !r && !dr && !d && dl && !l -> base_ul_ur_dl
                    u && r && !d && !dl && !l -> base_ul_u_ur_r_dr
                    !ul && !u && r && d && !l -> base_ur_r_dr_d_dl
                    !u && !ur && !r && d && l -> base_ul_dr_d_dl_l
                    u && !r && !dr && !d && l -> base_ul_u_ur_dl_l
                    u && r && !d && dl && !l -> base_ul_u_ur_r_dr_dl
                    ul && !u && r && d && !l -> base_ul_ur_r_dr_d_dl
                    !u && ur && !r && d && l -> base_ul_ur_dr_d_dl_l
                    u && !r && dr && !d && l -> base_ul_u_ur_dr_dl_l
                    u && r && d && !l -> base_ul_u_ur_r_dr_d_dl
                    !u && r && d && l -> base_ul_ur_r_dr_d_dl_l
                    u && !r && d && l -> base_ul_u_ur_dr_d_dl_l
                    u && r && !d && l -> base_ul_u_ur_r_dr_dl_l
                    u && !r && !dr && !d && !dl && !l -> base_ul_u_ur
                    !ul && !u && r && !d && !dl && !l -> base_ur_r_dr
                    !ul && !u && !ur && !r && d && !l -> base_dr_d_dl
                    !u && !ur && !r && !d && !dr && l -> base_ul_dl_l
                    u && !r && !dr && !d && dl && !l -> base_ul_u_ur_dl
                    ul && !u && r && !dl && !d && !l -> base_ul_ur_r_dr
                    !ul && !u && ur && !r && d && !l -> base_ur_dr_d_dl
                    !u && !ur && !r && !d && dr && l -> base_ul_dr_dl_l
                    u && !r && dr && !d && !dl && !l -> base_ul_u_ur_dr
                    !ul && !u && r && !d && dl && !l -> base_ur_r_dr_dl
                    ul && !u && !ur && !r && d && !l -> base_ul_dr_d_dl
                    !u && ur && !r && !d && !dr && l -> base_ul_ur_dl_l
                    u && !r && dr && !d && dl && !l -> base_ul_u_ur_dr_dl
                    ul && !u && r && !d && dl && !l -> base_ul_ur_r_dr_dl
                    ul && !u && ur && !r && d && !l -> base_ul_ur_dr_d_dl
                    !u && ur && !r && !d && dl && l -> base_ul_ur_dr_dl_l
                    u && !r && d && !l -> base_ul_u_ur_dr_d_dl
                    !u && r && !d && l -> base_ul_ur_r_dr_dl_l
                    ul && !u && !ur && !r && dr && !d && !dl && !l -> base_ul_dr
                    !ul && !u && ur && !r && !dr && !d && dl && !l -> base_ur_dl
                    ul && !u && ur && !r && dr && !d && dl && !l -> base_ul_ur_dr_dl
                    u && r && d && l -> base_ul_u_ur_r_dr_d_dl_l
                    !u && !r && !d && !l -> base_none
                    else -> base_empty
                }

                val coverTile = when
                {
                    !isAboveRock && (isAboveDownRock || j == 0) -> cover_half
                    isAboveRock -> cover_all
                    else -> cover_empty
                }

                val wallTile = when
                {
                    !isAboveRock || isAboveDownRock -> wall_empty
                    isAboveLeftRock && isAboveRightRock -> wall_m
                    !isAboveLeftRock && isAboveRightRock -> wall_l_m
                    isAboveLeftRock && !isAboveRightRock -> wall_m_r
                    !isAboveLeftRock && !isAboveRightRock -> wall_l_m_r
                    else -> wall_empty
                }

                map.setTile("hole", i, j, holeTile)
                map.setTile("base", i, j, baseTile)
                map.setTile("cover", i, j, coverTile)
                map.setTile("wall", i, j, wallTile)
            }
        }

        map.setDimensions(level.width, level.height)
    }

    override fun show()
    {
        super.show()

        mapCamera.position.x = map.width() / 2f
        mapCamera.position.y = map.height() / 2f
    }

    override fun render(delta: Float)
    {
        super.render(delta)

        // input

        val isLeftButtonPressed = Gdx.input.isButtonPressed(0)
        val isRightButtonPressed = Gdx.input.isButtonPressed(1)
        val isLeftButtonJustPressed = Gdx.input.isButtonJustPressed(0)
        val isRightButtonJustPressed = Gdx.input.isButtonJustPressed(1)

        val screenMousePos = Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
        val worldMousePos = mapViewport.unproject(screenMousePos)

        // select

        isMouseInMap =
            (worldMousePos.x > 0 && worldMousePos.x < map.width() && worldMousePos.y > 0 && worldMousePos.y < map.height())


        if (isSelectFixed)
        {
            if (isLeftButtonJustPressed)
            {
                if (isMouseInMap)
                {
                    map.setTile("select", selectPosition.x.toInt(), selectPosition.y.toInt(), emptyTile)

                    selectPosition.x = truncate(worldMousePos.x / map.tileWidth)
                    selectPosition.y = truncate(worldMousePos.y / map.tileHeight)

                    map.setTile("select", selectPosition.x.toInt(), selectPosition.y.toInt(), selectTile)
                }
                else
                {
                    isSelectFixed = false
                }
            }
        }
        else
        {
            map.setTile("select", selectPosition.x.toInt(), selectPosition.y.toInt(), emptyTile)

            if (isMouseInMap)
            {
                selectPosition.x = truncate(worldMousePos.x / map.tileWidth)
                selectPosition.y = truncate(worldMousePos.y / map.tileHeight)

                map.setTile("select", selectPosition.x.toInt(), selectPosition.y.toInt(), selectTile)
            }

            if (isLeftButtonJustPressed)
            {
                //isSelectFixed = true
            }
        }

        // drag

        if (isDragging)
        {
            dragDifference.x = worldMousePos.x - mapCamera.position.x
            dragDifference.y = worldMousePos.y - mapCamera.position.y
            mapCamera.position.x = dragOrigin.x - dragDifference.x
            mapCamera.position.y = dragOrigin.y - dragDifference.y

            if (!isRightButtonPressed)
            {
                isDragging = false
            }
        }
        else
        {
            if (isRightButtonPressed)
            {
                //dragOrigin = worldMousePos
                //isDragging = true
            }
        }

        // block

        if(isLeftButtonJustPressed && isMouseInMap)
        {
            level.map[currentLevel][selectPosition.y.toInt()][selectPosition.x.toInt()].rock=Rock.Empty
            loadLevelToMap(currentLevel)
        }

        if(isRightButtonJustPressed && isMouseInMap)
        {
            level.map[currentLevel][selectPosition.y.toInt()][selectPosition.x.toInt()].rock=Rock.Weak
            loadLevelToMap(currentLevel)
        }

        // draw

        ScreenUtils.clear(0f, 0f, 0f, 1f)
        camera.update()
        mapCamera.update()
        map.renderer.setView(mapCamera)
        map.renderer.render()
        stage.draw()
    }

    override fun resize(width: Int, height: Int)
    {
        super.resize(width, height)
        mapViewport.update(width, height)
    }
}