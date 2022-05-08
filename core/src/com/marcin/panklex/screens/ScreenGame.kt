package com.marcin.panklex.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Pixmap
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.marcin.panklex.*
import com.marcin.panklex.entities.*
import kotlin.math.truncate

class ScreenGame(val name: String, val game: PanKlexGame) : BaseScreen(name, game)
{
    // main

    val mapCamera = OrthographicCamera()
    val mapViewport = ScreenViewport(mapCamera)

    val level = KlexLevel()
    val map = KlexMap(100, 100, 32, 32)
    val tiles = KlexTiles(game.assetManager)

    // hud

    val blockTypeLabel = Label("undam stone", Label.LabelStyle(BitmapFont(), Color.CYAN))
    val levelLabel = Label("level: 0", Label.LabelStyle(BitmapFont(), Color.GREEN))
    val upButton = TextButton("Up", TextButton.TextButtonStyle(null, null, null, BitmapFont()))
    val downButton = TextButton("Down", TextButton.TextButtonStyle(null, null, null, BitmapFont()))
    val endGameButton = TextButton("End Game", TextButton.TextButtonStyle(null, null, null, BitmapFont()))

    val pickaxeImage = game.assetManager.get<Texture>("images/pickaxe.png")
    val bombImage = game.assetManager.get<Texture>("images/bomb.png")
    val coinImage = game.assetManager.get<Texture>("images/coin.png")
    val cellImage = game.assetManager.get<Texture>("images/cell.png")
    val walkImage = game.assetManager.get<Texture>("images/walk.png")
    val cancelImage = game.assetManager.get<Texture>("images/cancel.png")

    val pickaxeButton = ImageButton(TextureRegionDrawable(pickaxeImage))
    val bombButton = ImageButton(TextureRegionDrawable(bombImage))
    val coinButton = ImageButton(TextureRegionDrawable(coinImage))
    val cellButton = ImageButton(TextureRegionDrawable(cellImage))
    val walkButton = ImageButton(TextureRegionDrawable(walkImage))
    val cancellButton = ImageButton(TextureRegionDrawable(cancelImage))

    val pickaxeCountLabel = Label("0", Label.LabelStyle(BitmapFont(), Color.YELLOW))
    val bombCountLabel = Label("0", Label.LabelStyle(BitmapFont(), Color.YELLOW))
    val coinCountLabel = Label("0", Label.LabelStyle(BitmapFont(), Color.YELLOW))
    val cellCountLabel = Label("0", Label.LabelStyle(BitmapFont(), Color.YELLOW))

    // other

    var currentLevel = 0
    var currentBlockType = Block.UndamagedStone
    var currentAction = Action.None
    var isActionPossible = false

    var pickaxeCount = 3
    var bombCount = 3
    var coinCount = 3
    var cellCount = 3

    var selectPosition = Vector2()
    var actionPosition = Vector2()
    var isMouseInMap = true
    var isSelectFixed = false

    var isDragging = false
    var dragOrigin = Vector2()
    var dragDifference = Vector2()

    init
    {
        // hud

        upButton.addListener(object : ClickListener()
                             {
                                 override fun clicked(event: InputEvent?, x: Float, y: Float)
                                 {
                                     if (currentLevel < level.levels - 1)
                                     {
                                         changeLevel(currentLevel + 1)
                                         refreshLevel()
                                         refreshMap()
                                         levelLabel.setText("level: $currentLevel")
                                     }
                                 }
                             })

        downButton.addListener(object : ClickListener()
                               {
                                   override fun clicked(event: InputEvent?, x: Float, y: Float)
                                   {
                                       if (currentLevel > 0)
                                       {
                                           changeLevel(currentLevel - 1)
                                           refreshLevel()
                                           refreshMap()
                                           levelLabel.setText("level: $currentLevel")
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

        pickaxeButton.addListener(object : ClickListener()
                                  {
                                      override fun clicked(event: InputEvent?, x: Float, y: Float)
                                      {
                                          if (pickaxeCount > 0) changeAction(Action.Pickaxe)
                                      }
                                  })

        bombButton.addListener(object : ClickListener()
                               {
                                   override fun clicked(event: InputEvent?, x: Float, y: Float)
                                   {
                                       if (bombCount > 0) changeAction(Action.Bomb)
                                   }
                               })

        coinButton.addListener(object : ClickListener()
                               {
                                   override fun clicked(event: InputEvent?, x: Float, y: Float)
                                   {
                                       if (coinCount > 0) changeAction(Action.Coin)
                                   }
                               })

        cellButton.addListener(object : ClickListener()
                               {
                                   override fun clicked(event: InputEvent?, x: Float, y: Float)
                                   {
                                       if (cellCount > 0) changeAction(Action.Cell)
                                   }
                               })

        walkButton.addListener(object : ClickListener()
                               {
                                   override fun clicked(event: InputEvent?, x: Float, y: Float)
                                   {
                                       changeAction(Action.Walk)
                                   }
                               })

        cancellButton.addListener(object : ClickListener()
                                  {
                                      override fun clicked(event: InputEvent?, x: Float, y: Float)
                                      {
                                          changeAction(Action.None)
                                      }
                                  })

        val table = Table()
        table.top()
        table.setFillParent(true)
        table.add(blockTypeLabel).pad(10f)
        table.add(levelLabel).pad(10f)
        table.add(upButton).pad(10f)
        table.add(downButton).pad(10f)
        table.add(endGameButton).pad(10f)
        table.row()
        table.add(pickaxeButton).pad(10f)
        table.add(bombButton).pad(10f)
        table.add(coinButton).pad(10f)
        table.add(cellButton).pad(10f)
        table.add(walkButton).pad(10f)
        table.add(cancellButton).pad(10f)
        table.row()
        table.add(pickaxeCountLabel).pad(10f)
        table.add(bombCountLabel).pad(10f)
        table.add(coinCountLabel).pad(10f)
        table.add(cellCountLabel).pad(10f)
        stage.addActor(table)

        // init

        loadLevelFromJson("levels/3.json")
        changeLevel(0)
        refreshLevel()
        refreshMap()
        refreshEquipmentCountLabels()
        mapCamera.zoom = 0.5f
    }

    fun changeAction(action: Action)
    {
        currentAction = action
    }

    fun loadLevelFromJson(jsonPath: String)
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
                        0 -> Block.Empty
                        1 -> Block.UndamagedStone
                        2 -> Block.DamagedStone
                        3 -> Block.Brick
                        4 -> Block.Metal
                        else -> Block.Empty
                    }

                    rowList.add(levelBlock)
                }

                columnList.add(rowList)
            }

            levelList.add(columnList)
        }

        level.map = levelList
        level.player = jsonLevel.player
        level.entities.add(level.player)
        level.entities.addAll(jsonLevel.entities)
    }

    fun changeLevel(levelNr: Int)
    {
        currentLevel = levelNr
    }

    fun refreshLevel()
    {
        for (i in 0..level.width - 1)
        {
            for (j in 0..level.height - 1)
            {
                level.map[currentLevel][j][i].entity = null
            }
        }

        for (e in level.entities)
        {
            val positions = mutableListOf<Vector3>()
            when (e)
            {
                is EntityPlayer -> positions.add(e.position)
                is EntityContainer -> positions.add(e.position)
                is EntityVendingMachine -> positions.add(e.position)
                is EntityElevator ->
                {
                    positions.addAll(e.positions)
                    positions.addAll(e.exitPositions)
                }
                is EntityTeleporter ->
                {
                    positions.add(e.firstTelePosition)
                    positions.add(e.secondTelePosition)
                }
                is EntityFlag -> positions.add(e.position)
                is EntityStairs ->
                {
                    positions.add(e.position)
                    positions.add(e.lowerEnd)
                    positions.add(e.upperEnd)
                }
                is EntityPoweredGate ->
                {
                    positions.add(e.firstPartPosition)
                    positions.add(e.secondPartPosition)
                    positions.add(e.gatePosition)
                }
            }

            for (p in positions)
            {
                level.map[p.z.toInt()][p.y.toInt()][p.x.toInt()].entity = e
            }
        }
    }

    fun refreshMap()
    {
        for (i in 0..level.width - 1)
        {
            for (j in 0..level.height - 1)
            {
                val blockType = if (level.map[currentLevel][j][i].rock != Block.Empty) level.map[currentLevel][j][i].rock else Block.Metal
                val UpBlockType = if (j < level.height - 1) level.map[currentLevel][j + 1][i].rock else Block.Metal
                val AboveBlockType = if (currentLevel < level.levels - 1) level.map[currentLevel + 1][j][i].rock else Block.Metal

                val isRock = level.map[currentLevel][j][i].rock != Block.Empty
                val isUpLeftRock = !(j < level.height - 1 && i > 0 && level.map[currentLevel][j + 1][i - 1].rock == Block.Empty)
                val isUpRock = !(j < level.height - 1 && level.map[currentLevel][j + 1][i].rock == Block.Empty)
                val isUpRightRock = !(j < level.height - 1 && i < level.width - 1 && level.map[currentLevel][j + 1][i + 1].rock == Block.Empty)
                val isRightRock = !(i < level.width - 1 && level.map[currentLevel][j][i + 1].rock == Block.Empty)
                val isDownRightRock = !(j > 0 && i < level.width - 1 && level.map[currentLevel][j - 1][i + 1].rock == Block.Empty)
                val isDownRock = !(j > 0 && level.map[currentLevel][j - 1][i].rock == Block.Empty)
                val isDownLeftRock = !(j > 0 && i > 0 && level.map[currentLevel][j - 1][i - 1].rock == Block.Empty)
                val isLeftRock = !(i > 0 && level.map[currentLevel][j][i - 1].rock == Block.Empty)

                val isAboveRock = !(currentLevel < level.levels - 1 && level.map[currentLevel + 1][j][i].rock == Block.Empty)
                val isAboveUpLeftRock =
                        !(currentLevel < level.levels - 1 && j < level.height - 1 && i > 0 && level.map[currentLevel + 1][j + 1][i - 1].rock == Block.Empty)
                val isAboveUpRock =
                        !(currentLevel < level.levels - 1 && j < level.height - 1 && level.map[currentLevel + 1][j + 1][i].rock == Block.Empty)
                val isAboveUpRightRock =
                        !(currentLevel < level.levels - 1 && j < level.height - 1 && i < level.width - 1 && level.map[currentLevel + 1][j + 1][i + 1].rock == Block.Empty)
                val isAboveRightRock =
                        !(currentLevel < level.levels - 1 && i < level.width - 1 && level.map[currentLevel + 1][j][i + 1].rock == Block.Empty)
                val isAboveDownRightRock =
                        !(currentLevel < level.levels - 1 && j > 0 && i < level.width - 1 && level.map[currentLevel + 1][j - 1][i + 1].rock == Block.Empty)
                val isAboveDownRock = !(currentLevel < level.levels - 1 && j > 0 && level.map[currentLevel + 1][j - 1][i].rock == Block.Empty)
                val isAboveDownLeftRock =
                        !(currentLevel < level.levels - 1 && j > 0 && i > 0 && level.map[currentLevel + 1][j - 1][i - 1].rock == Block.Empty)
                val isAboveLeftRock = !(currentLevel < level.levels - 1 && i > 0 && level.map[currentLevel + 1][j][i - 1].rock == Block.Empty)

                val holeTile = when
                {
                    isRock -> tiles.emptyTile
                    !isUpRock -> when (UpBlockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneHole_none
                        Block.DamagedStone -> tiles.damStoneHole_none
                        Block.Brick -> tiles.brickHole_none
                        Block.Metal -> tiles.metalHole_none
                        else -> tiles.exampleHole_none
                    }
                    !isUpLeftRock && !isUpRightRock -> when (UpBlockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneHole_l_m_r
                        Block.DamagedStone -> tiles.damStoneHole_l_m_r
                        Block.Brick -> tiles.brickHole_l_m_r
                        Block.Metal -> tiles.metalHole_l_m_r
                        else -> tiles.exampleHole_l_m_r
                    }
                    !isUpLeftRock && isUpRightRock -> when (UpBlockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneHole_l_m
                        Block.DamagedStone -> tiles.damStoneHole_l_m
                        Block.Brick -> tiles.brickHole_l_m
                        Block.Metal -> tiles.metalHole_l_m
                        else -> tiles.exampleHole_l_m
                    }
                    isUpLeftRock && isUpRightRock -> when (UpBlockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneHole_m
                        Block.DamagedStone -> tiles.damStoneHole_m
                        Block.Brick -> tiles.brickHole_m
                        Block.Metal -> tiles.metalHole_m
                        else -> tiles.exampleHole_m
                    }
                    isUpLeftRock && !isUpRightRock -> when (UpBlockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneHole_m_r
                        Block.DamagedStone -> tiles.damStoneHole_m_r
                        Block.Brick -> tiles.brickHole_m_r
                        Block.Metal -> tiles.metalHole_m_r
                        else -> tiles.exampleHole_m_r
                    }
                    else -> tiles.emptyTile
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
                    !isRock -> tiles.emptyTile
                    ul && !u && !ur && !r && !dr && !d && !dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul
                        Block.DamagedStone -> tiles.damStoneBase_ul
                        Block.Brick -> tiles.brickBase_ul
                        Block.Metal -> tiles.metalBase_ul
                        else -> tiles.exampleBase_ul
                    }
                    !ul && !u && ur && !r && !dr && !d && !dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ur
                        Block.DamagedStone -> tiles.damStoneBase_ur
                        Block.Brick -> tiles.brickBase_ur
                        Block.Metal -> tiles.metalBase_ur
                        else -> tiles.exampleBase_ur
                    }
                    !ul && !u && !ur && !r && dr && !d && !dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_dr
                        Block.DamagedStone -> tiles.damStoneBase_dr
                        Block.Brick -> tiles.brickBase_dr
                        Block.Metal -> tiles.metalBase_dr
                        else -> tiles.exampleBase_dr
                    }
                    !ul && !u && !ur && !r && !dr && !d && dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_dl
                        Block.DamagedStone -> tiles.damStoneBase_dl
                        Block.Brick -> tiles.brickBase_dl
                        Block.Metal -> tiles.metalBase_dl
                        else -> tiles.exampleBase_dl
                    }
                    ul && !u && ur && !r && !dr && !d && !dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_ur
                        Block.DamagedStone -> tiles.damStoneBase_ul_ur
                        Block.Brick -> tiles.brickBase_ul_ur
                        Block.Metal -> tiles.metalBase_ul_ur
                        else -> tiles.exampleBase_ul_ur
                    }
                    !ul && !u && ur && !r && dr && !d && !dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ur_dr
                        Block.DamagedStone -> tiles.damStoneBase_ur_dr
                        Block.Brick -> tiles.brickBase_ur_dr
                        Block.Metal -> tiles.metalBase_ur_dr
                        else -> tiles.exampleBase_ur_dr
                    }
                    !ul && !u && !ur && !r && dr && !d && dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_dr_dl
                        Block.DamagedStone -> tiles.damStoneBase_dr_dl
                        Block.Brick -> tiles.brickBase_dr_dl
                        Block.Metal -> tiles.metalBase_dr_dl
                        else -> tiles.exampleBase_dr_dl
                    }
                    ul && !u && !ur && !r && !dr && !d && dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_dl
                        Block.DamagedStone -> tiles.damStoneBase_ul_dl
                        Block.Brick -> tiles.brickBase_ul_dl
                        Block.Metal -> tiles.metalBase_ul_dl
                        else -> tiles.exampleBase_ul_dl
                    }
                    ul && !u && ur && !r && dr && !d && !dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_ur_dr
                        Block.DamagedStone -> tiles.damStoneBase_ul_ur_dr
                        Block.Brick -> tiles.brickBase_ul_ur_dr
                        Block.Metal -> tiles.metalBase_ul_ur_dr
                        else -> tiles.exampleBase_ul_ur_dr
                    }
                    !ul && !u && ur && !r && dr && !d && dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ur_dr_dl
                        Block.DamagedStone -> tiles.damStoneBase_ur_dr_dl
                        Block.Brick -> tiles.brickBase_ur_dr_dl
                        Block.Metal -> tiles.metalBase_ur_dr_dl
                        else -> tiles.exampleBase_ur_dr_dl
                    }
                    ul && !u && !ur && !r && dr && !d && dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_dr_dl
                        Block.DamagedStone -> tiles.damStoneBase_ul_dr_dl
                        Block.Brick -> tiles.brickBase_ul_dr_dl
                        Block.Metal -> tiles.metalBase_ul_dr_dl
                        else -> tiles.exampleBase_ul_dr_dl
                    }
                    ul && !u && ur && !r && !dr && !d && dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_ur_dl
                        Block.DamagedStone -> tiles.damStoneBase_ul_ur_dl
                        Block.Brick -> tiles.brickBase_ul_ur_dl
                        Block.Metal -> tiles.metalBase_ul_ur_dl
                        else -> tiles.exampleBase_ul_ur_dl
                    }
                    u && r && !d && !dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_u_ur_r_dr
                        Block.DamagedStone -> tiles.damStoneBase_ul_u_ur_r_dr
                        Block.Brick -> tiles.brickBase_ul_u_ur_r_dr
                        Block.Metal -> tiles.metalBase_ul_u_ur_r_dr
                        else -> tiles.exampleBase_ul_u_ur_r_dr
                    }
                    !ul && !u && r && d && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ur_r_dr_d_dl
                        Block.DamagedStone -> tiles.damStoneBase_ur_r_dr_d_dl
                        Block.Brick -> tiles.brickBase_ur_r_dr_d_dl
                        Block.Metal -> tiles.metalBase_ur_r_dr_d_dl
                        else -> tiles.exampleBase_ur_r_dr_d_dl
                    }
                    !u && !ur && !r && d && l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_dr_d_dl_l
                        Block.DamagedStone -> tiles.damStoneBase_ul_dr_d_dl_l
                        Block.Brick -> tiles.brickBase_ul_dr_d_dl_l
                        Block.Metal -> tiles.metalBase_ul_dr_d_dl_l
                        else -> tiles.exampleBase_ul_dr_d_dl_l
                    }
                    u && !r && !dr && !d && l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_u_ur_dl_l
                        Block.DamagedStone -> tiles.damStoneBase_ul_u_ur_dl_l
                        Block.Brick -> tiles.brickBase_ul_u_ur_dl_l
                        Block.Metal -> tiles.metalBase_ul_u_ur_dl_l
                        else -> tiles.exampleBase_ul_u_ur_dl_l
                    }
                    u && r && !d && dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_u_ur_r_dr_dl
                        Block.DamagedStone -> tiles.damStoneBase_ul_u_ur_r_dr_dl
                        Block.Brick -> tiles.brickBase_ul_u_ur_r_dr_dl
                        Block.Metal -> tiles.metalBase_ul_u_ur_r_dr_dl
                        else -> tiles.exampleBase_ul_u_ur_r_dr_dl
                    }
                    ul && !u && r && d && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_ur_r_dr_d_dl
                        Block.DamagedStone -> tiles.damStoneBase_ul_ur_r_dr_d_dl
                        Block.Brick -> tiles.brickBase_ul_ur_r_dr_d_dl
                        Block.Metal -> tiles.metalBase_ul_ur_r_dr_d_dl
                        else -> tiles.exampleBase_ul_ur_r_dr_d_dl
                    }
                    !u && ur && !r && d && l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_ur_dr_d_dl_l
                        Block.DamagedStone -> tiles.damStoneBase_ul_ur_dr_d_dl_l
                        Block.Brick -> tiles.brickBase_ul_ur_dr_d_dl_l
                        Block.Metal -> tiles.metalBase_ul_ur_dr_d_dl_l
                        else -> tiles.exampleBase_ul_ur_dr_d_dl_l
                    }
                    u && !r && dr && !d && l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_u_ur_dr_dl_l
                        Block.DamagedStone -> tiles.damStoneBase_ul_u_ur_dr_dl_l
                        Block.Brick -> tiles.brickBase_ul_u_ur_dr_dl_l
                        Block.Metal -> tiles.metalBase_ul_u_ur_dr_dl_l
                        else -> tiles.exampleBase_ul_u_ur_dr_dl_l
                    }
                    u && r && d && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_u_ur_r_dr_d_dl
                        Block.DamagedStone -> tiles.damStoneBase_ul_u_ur_r_dr_d_dl
                        Block.Brick -> tiles.brickBase_ul_u_ur_r_dr_d_dl
                        Block.Metal -> tiles.metalBase_ul_u_ur_r_dr_d_dl
                        else -> tiles.exampleBase_ul_u_ur_r_dr_d_dl
                    }
                    !u && r && d && l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_ur_r_dr_d_dl_l
                        Block.DamagedStone -> tiles.damStoneBase_ul_ur_r_dr_d_dl_l
                        Block.Brick -> tiles.brickBase_ul_ur_r_dr_d_dl_l
                        Block.Metal -> tiles.metalBase_ul_ur_r_dr_d_dl_l
                        else -> tiles.exampleBase_ul_ur_r_dr_d_dl_l
                    }
                    u && !r && d && l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_u_ur_dr_d_dl_l
                        Block.DamagedStone -> tiles.damStoneBase_ul_u_ur_dr_d_dl_l
                        Block.Brick -> tiles.brickBase_ul_u_ur_dr_d_dl_l
                        Block.Metal -> tiles.metalBase_ul_u_ur_dr_d_dl_l
                        else -> tiles.exampleBase_ul_u_ur_dr_d_dl_l
                    }
                    u && r && !d && l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_u_ur_r_dr_dl_l
                        Block.DamagedStone -> tiles.damStoneBase_ul_u_ur_r_dr_dl_l
                        Block.Brick -> tiles.brickBase_ul_u_ur_r_dr_dl_l
                        Block.Metal -> tiles.metalBase_ul_u_ur_r_dr_dl_l
                        else -> tiles.exampleBase_ul_u_ur_r_dr_dl_l
                    }
                    u && !r && !dr && !d && !dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_u_ur
                        Block.DamagedStone -> tiles.damStoneBase_ul_u_ur
                        Block.Brick -> tiles.brickBase_ul_u_ur
                        Block.Metal -> tiles.metalBase_ul_u_ur
                        else -> tiles.exampleBase_ul_u_ur
                    }
                    !ul && !u && r && !d && !dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ur_r_dr
                        Block.DamagedStone -> tiles.damStoneBase_ur_r_dr
                        Block.Brick -> tiles.brickBase_ur_r_dr
                        Block.Metal -> tiles.metalBase_ur_r_dr
                        else -> tiles.exampleBase_ur_r_dr
                    }
                    !ul && !u && !ur && !r && d && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_dr_d_dl
                        Block.DamagedStone -> tiles.damStoneBase_dr_d_dl
                        Block.Brick -> tiles.brickBase_dr_d_dl
                        Block.Metal -> tiles.metalBase_dr_d_dl
                        else -> tiles.exampleBase_dr_d_dl
                    }
                    !u && !ur && !r && !d && !dr && l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_dl_l
                        Block.DamagedStone -> tiles.damStoneBase_ul_dl_l
                        Block.Brick -> tiles.brickBase_ul_dl_l
                        Block.Metal -> tiles.metalBase_ul_dl_l
                        else -> tiles.exampleBase_ul_dl_l
                    }
                    u && !r && !dr && !d && dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_u_ur_dl
                        Block.DamagedStone -> tiles.damStoneBase_ul_u_ur_dl
                        Block.Brick -> tiles.brickBase_ul_u_ur_dl
                        Block.Metal -> tiles.metalBase_ul_u_ur_dl
                        else -> tiles.exampleBase_ul_u_ur_dl
                    }
                    ul && !u && r && !dl && !d && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_ur_r_dr
                        Block.DamagedStone -> tiles.damStoneBase_ul_ur_r_dr
                        Block.Brick -> tiles.brickBase_ul_ur_r_dr
                        Block.Metal -> tiles.metalBase_ul_ur_r_dr
                        else -> tiles.exampleBase_ul_ur_r_dr
                    }
                    !ul && !u && ur && !r && d && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ur_dr_d_dl
                        Block.DamagedStone -> tiles.damStoneBase_ur_dr_d_dl
                        Block.Brick -> tiles.brickBase_ur_dr_d_dl
                        Block.Metal -> tiles.metalBase_ur_dr_d_dl
                        else -> tiles.exampleBase_ur_dr_d_dl
                    }
                    !u && !ur && !r && !d && dr && l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_dr_dl_l
                        Block.DamagedStone -> tiles.damStoneBase_ul_dr_dl_l
                        Block.Brick -> tiles.brickBase_ul_dr_dl_l
                        Block.Metal -> tiles.metalBase_ul_dr_dl_l
                        else -> tiles.exampleBase_ul_dr_dl_l
                    }
                    u && !r && dr && !d && !dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_u_ur_dr
                        Block.DamagedStone -> tiles.damStoneBase_ul_u_ur_dr
                        Block.Brick -> tiles.brickBase_ul_u_ur_dr
                        Block.Metal -> tiles.metalBase_ul_u_ur_dr
                        else -> tiles.exampleBase_ul_u_ur_dr
                    }
                    !ul && !u && r && !d && dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ur_r_dr_dl
                        Block.DamagedStone -> tiles.damStoneBase_ur_r_dr_dl
                        Block.Brick -> tiles.brickBase_ur_r_dr_dl
                        Block.Metal -> tiles.metalBase_ur_r_dr_dl
                        else -> tiles.exampleBase_ur_r_dr_dl
                    }
                    ul && !u && !ur && !r && d && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_dr_d_dl
                        Block.DamagedStone -> tiles.damStoneBase_ul_dr_d_dl
                        Block.Brick -> tiles.brickBase_ul_dr_d_dl
                        Block.Metal -> tiles.metalBase_ul_dr_d_dl
                        else -> tiles.exampleBase_ul_dr_d_dl
                    }
                    !u && ur && !r && !d && !dr && l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_ur_dl_l
                        Block.DamagedStone -> tiles.damStoneBase_ul_ur_dl_l
                        Block.Brick -> tiles.brickBase_ul_ur_dl_l
                        Block.Metal -> tiles.metalBase_ul_ur_dl_l
                        else -> tiles.exampleBase_ul_ur_dl_l
                    }
                    u && !r && dr && !d && dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_u_ur_dr_dl
                        Block.DamagedStone -> tiles.damStoneBase_ul_u_ur_dr_dl
                        Block.Brick -> tiles.brickBase_ul_u_ur_dr_dl
                        Block.Metal -> tiles.metalBase_ul_u_ur_dr_dl
                        else -> tiles.exampleBase_ul_u_ur_dr_dl
                    }
                    ul && !u && r && !d && dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_ur_r_dr_dl
                        Block.DamagedStone -> tiles.damStoneBase_ul_ur_r_dr_dl
                        Block.Brick -> tiles.brickBase_ul_ur_r_dr_dl
                        Block.Metal -> tiles.metalBase_ul_ur_r_dr_dl
                        else -> tiles.exampleBase_ul_ur_r_dr_dl
                    }
                    ul && !u && ur && !r && d && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_ur_dr_d_dl
                        Block.DamagedStone -> tiles.damStoneBase_ul_ur_dr_d_dl
                        Block.Brick -> tiles.brickBase_ul_ur_dr_d_dl
                        Block.Metal -> tiles.metalBase_ul_ur_dr_d_dl
                        else -> tiles.exampleBase_ul_ur_dr_d_dl
                    }
                    !u && ur && !r && !d && dr && l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_ur_dr_dl_l
                        Block.DamagedStone -> tiles.damStoneBase_ul_ur_dr_dl_l
                        Block.Brick -> tiles.brickBase_ul_ur_dr_dl_l
                        Block.Metal -> tiles.metalBase_ul_ur_dr_dl_l
                        else -> tiles.exampleBase_ul_ur_dr_dl_l
                    }
                    u && !r && d && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_u_ur_dr_d_dl
                        Block.DamagedStone -> tiles.damStoneBase_ul_u_ur_dr_d_dl
                        Block.Brick -> tiles.brickBase_ul_u_ur_dr_d_dl
                        Block.Metal -> tiles.metalBase_ul_u_ur_dr_d_dl
                        else -> tiles.exampleBase_ul_u_ur_dr_d_dl
                    }
                    !u && r && !d && l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_ur_r_dr_dl_l
                        Block.DamagedStone -> tiles.damStoneBase_ul_ur_r_dr_dl_l
                        Block.Brick -> tiles.brickBase_ul_ur_r_dr_dl_l
                        Block.Metal -> tiles.metalBase_ul_ur_r_dr_dl_l
                        else -> tiles.exampleBase_ul_ur_r_dr_dl_l
                    }
                    ul && !u && !ur && !r && dr && !d && !dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_dr
                        Block.DamagedStone -> tiles.damStoneBase_ul_dr
                        Block.Brick -> tiles.brickBase_ul_dr
                        Block.Metal -> tiles.metalBase_ul_dr
                        else -> tiles.exampleBase_ul_dr
                    }
                    !ul && !u && ur && !r && !dr && !d && dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ur_dl
                        Block.DamagedStone -> tiles.damStoneBase_ur_dl
                        Block.Brick -> tiles.brickBase_ur_dl
                        Block.Metal -> tiles.metalBase_ur_dl
                        else -> tiles.exampleBase_ur_dl
                    }
                    ul && !u && ur && !r && dr && !d && dl && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_ur_dr_dl
                        Block.DamagedStone -> tiles.damStoneBase_ul_ur_dr_dl
                        Block.Brick -> tiles.brickBase_ul_ur_dr_dl
                        Block.Metal -> tiles.metalBase_ul_ur_dr_dl
                        else -> tiles.exampleBase_ul_ur_dr_dl
                    }
                    u && r && d && l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_ul_u_ur_r_dr_d_dl_l
                        Block.DamagedStone -> tiles.damStoneBase_ul_u_ur_r_dr_d_dl_l
                        Block.Brick -> tiles.brickBase_ul_u_ur_r_dr_d_dl_l
                        Block.Metal -> tiles.metalBase_ul_u_ur_r_dr_d_dl_l
                        else -> tiles.exampleBase_ul_u_ur_r_dr_d_dl_l
                    }
                    !u && !r && !d && !l -> when (blockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneBase_none
                        Block.DamagedStone -> tiles.damStoneBase_none
                        Block.Brick -> tiles.brickBase_none
                        Block.Metal -> tiles.metalBase_none
                        else -> tiles.exampleBase_none
                    }
                    else -> tiles.emptyTile
                }

                val blockTile = when
                {
                    isAboveRock -> tiles.cover_all
                    else -> tiles.emptyTile
                }

                val wallTile = when
                {
                    !isAboveRock || isAboveDownRock -> tiles.emptyTile
                    isAboveLeftRock && isAboveRightRock -> when (AboveBlockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneWall_m
                        Block.DamagedStone -> tiles.damStoneWall_m
                        Block.Brick -> tiles.brickWall_m
                        Block.Metal -> tiles.metalWall_m
                        else -> tiles.exampleWall_m
                    }
                    !isAboveLeftRock && isAboveRightRock -> when (AboveBlockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneWall_l_m
                        Block.DamagedStone -> tiles.damStoneWall_l_m
                        Block.Brick -> tiles.brickWall_l_m
                        Block.Metal -> tiles.metalWall_l_m
                        else -> tiles.exampleWall_l_m
                    }
                    isAboveLeftRock && !isAboveRightRock -> when (AboveBlockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneWall_m_r
                        Block.DamagedStone -> tiles.damStoneWall_m_r
                        Block.Brick -> tiles.brickWall_m_r
                        Block.Metal -> tiles.metalWall_m_r
                        else -> tiles.exampleWall_m_r
                    }
                    !isAboveLeftRock && !isAboveRightRock -> when (AboveBlockType)
                    {
                        Block.UndamagedStone -> tiles.undamStoneWall_l_m_r
                        Block.DamagedStone -> tiles.damStoneWall_l_m_r
                        Block.Brick -> tiles.brickWall_l_m_r
                        Block.Metal -> tiles.metalWall_l_m_r
                        else -> tiles.exampleWall_l_m_r
                    }
                    else -> tiles.emptyTile
                }

                val coverTile = when
                {
                    (isAboveDownRock || j == 0) -> tiles.cover_half
                    else -> tiles.emptyTile
                }

                val entity = level.map[currentLevel][j][i].entity
                val position = Vector3(i.toFloat(), j.toFloat(), currentLevel.toFloat())

                val entityTile = when (entity)
                {
                    null -> tiles.emptyTile
                    is EntityPlayer -> tiles.player
                    is EntityContainer -> tiles.container
                    is EntityVendingMachine -> tiles.vendingMachine
                    is EntityTeleporter -> tiles.teleporter
                    is EntityFlag -> tiles.flag
                    is EntityElevator ->
                    {
                        if (entity.positions.contains(position)) tiles.elevator
                        else tiles.emptyTile
                    }
                    is EntityStairs -> when
                    {
                        position.equals(entity.position) -> when (entity.direction)
                        {
                            Direction.Up -> tiles.stairs_a_u
                            Direction.Right -> tiles.stairs_a_r
                            Direction.Down -> tiles.stairs_a_d
                            Direction.Left -> tiles.stairs_a_l
                            else -> tiles.emptyTile
                        }

                        position.equals(entity.upperEnd) -> when (entity.direction)
                        {
                            Direction.Up -> tiles.stairs_b_u
                            Direction.Right -> tiles.stairs_b_r
                            Direction.Down -> tiles.stairs_b_d
                            Direction.Left -> tiles.stairs_b_l
                            else -> tiles.emptyTile
                        }
                        else -> tiles.emptyTile
                    }
                    is EntityPoweredGate -> when
                    {
                        position.equals(entity.gatePosition) -> if (entity.isGateVertical)
                        {
                            if (entity.firstPartDirection == Direction.Down && entity.secondPartDirection == Direction.Up)
                            {
                                when
                                {
                                    !entity.isFirstPartPowered && !entity.isSecondPartPowered -> tiles.emptyTile
                                    entity.isFirstPartPowered && !entity.isSecondPartPowered -> tiles.poweredGate_gate_ver_u
                                    !entity.isFirstPartPowered && entity.isSecondPartPowered -> tiles.poweredGate_gate_ver_d
                                    entity.isFirstPartPowered && entity.isSecondPartPowered -> tiles.poweredGate_gate_ver_u_d
                                    else -> tiles.emptyTile
                                }
                            }
                            else
                            {
                                when
                                {
                                    !entity.isFirstPartPowered && !entity.isSecondPartPowered -> tiles.emptyTile
                                    entity.isFirstPartPowered && !entity.isSecondPartPowered -> tiles.poweredGate_gate_ver_d
                                    !entity.isFirstPartPowered && entity.isSecondPartPowered -> tiles.poweredGate_gate_ver_u
                                    entity.isFirstPartPowered && entity.isSecondPartPowered -> tiles.poweredGate_gate_ver_u_d
                                    else -> tiles.emptyTile
                                }
                            }
                        }
                        else
                        {
                            if (entity.firstPartDirection == Direction.Right && entity.secondPartDirection == Direction.Left)
                            {
                                when
                                {
                                    !entity.isFirstPartPowered && !entity.isSecondPartPowered -> tiles.emptyTile
                                    entity.isFirstPartPowered && !entity.isSecondPartPowered -> tiles.poweredGate_gate_hor_l
                                    !entity.isFirstPartPowered && entity.isSecondPartPowered -> tiles.poweredGate_gate_hor_r
                                    entity.isFirstPartPowered && entity.isSecondPartPowered -> tiles.poweredGate_gate_hor_l_r
                                    else -> tiles.emptyTile
                                }
                            }
                            else
                            {
                                when
                                {
                                    !entity.isFirstPartPowered && !entity.isSecondPartPowered -> tiles.emptyTile
                                    entity.isFirstPartPowered && !entity.isSecondPartPowered -> tiles.poweredGate_gate_hor_r
                                    !entity.isFirstPartPowered && entity.isSecondPartPowered -> tiles.poweredGate_gate_hor_l
                                    entity.isFirstPartPowered && entity.isSecondPartPowered -> tiles.poweredGate_gate_hor_l_r
                                    else -> tiles.emptyTile
                                }
                            }
                        }

                        position.equals(entity.firstPartPosition) -> if (entity.isFirstPartPowered)
                        {
                            when (entity.firstPartDirection)
                            {
                                Direction.Up -> tiles.poweredGate_part_pow_l_r
                                Direction.Right -> tiles.poweredGate_part_pow_r
                                Direction.Down -> tiles.poweredGate_part_pow_l_r
                                Direction.Left -> tiles.poweredGate_part_pow_l
                                else -> tiles.emptyTile
                            }
                        }
                        else
                        {
                            when (entity.firstPartDirection)
                            {
                                Direction.Up -> tiles.poweredGate_part_unp_l_r
                                Direction.Right -> tiles.poweredGate_part_unp_r
                                Direction.Down -> tiles.poweredGate_part_unp_l_r
                                Direction.Left -> tiles.poweredGate_part_unp_l
                                else -> tiles.emptyTile
                            }
                        }
                        position.equals(entity.secondPartPosition) -> if (entity.isSecondPartPowered)
                        {
                            when (entity.secondPartDirection)
                            {
                                Direction.Up -> tiles.poweredGate_part_pow_l_r
                                Direction.Right -> tiles.poweredGate_part_pow_r
                                Direction.Down -> tiles.poweredGate_part_pow_l_r
                                Direction.Left -> tiles.poweredGate_part_pow_l
                                else -> tiles.emptyTile
                            }
                        }
                        else
                        {
                            when (entity.secondPartDirection)
                            {
                                Direction.Up -> tiles.poweredGate_part_unp_l_r
                                Direction.Right -> tiles.poweredGate_part_unp_r
                                Direction.Down -> tiles.poweredGate_part_unp_l_r
                                Direction.Left -> tiles.poweredGate_part_unp_l
                                else -> tiles.emptyTile
                            }
                        }
                        else -> tiles.emptyTile
                    }
                    else -> tiles.emptyTile
                }

                map.setTile("hole", i, j, holeTile)
                map.setTile("base", i, j, baseTile)
                map.setTile("block", i, j, blockTile)
                map.setTile("wall", i, j, wallTile)
                map.setTile("entity", i, j, entityTile)
                map.setTile("cover", i, j, coverTile)
            }
        }

        map.setDimensions(level.width, level.height)
    }

    fun refreshEquipmentCountLabels()
    {
        pickaxeCountLabel.setText(pickaxeCount)
        bombCountLabel.setText(bombCount)
        coinCountLabel.setText(coinCount)
        cellCountLabel.setText(cellCount)
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

        val is1KeyJustPressed = Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)
        val is2KeyJustPressed = Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)
        val is3KeyJustPressed = Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)
        val is4KeyJustPressed = Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)

        val isLeftButtonPressed = Gdx.input.isButtonPressed(0)
        val isRightButtonPressed = Gdx.input.isButtonPressed(1)
        val isLeftButtonJustPressed = Gdx.input.isButtonJustPressed(0)
        val isRightButtonJustPressed = Gdx.input.isButtonJustPressed(1)

        val screenMousePos = Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
        val worldMousePos = mapViewport.unproject(screenMousePos)
        val isMouseInMap = (worldMousePos.x > 0 && worldMousePos.x < map.width() && worldMousePos.y > 0 && worldMousePos.y < map.height())


        // block type

        if (is1KeyJustPressed) currentBlockType = Block.UndamagedStone
        if (is2KeyJustPressed) currentBlockType = Block.DamagedStone
        if (is3KeyJustPressed) currentBlockType = Block.Brick
        if (is4KeyJustPressed) currentBlockType = Block.Metal

        blockTypeLabel.setText(
                when (currentBlockType)
                {
                    Block.UndamagedStone -> "undam stone"
                    Block.DamagedStone -> "dam stone"
                    Block.Brick -> "brick"
                    Block.Metal -> "metal"
                    else -> "?"
                }
                              )

        // block

        if (isLeftButtonPressed && isMouseInMap)
        {
            //level.map[currentLevel][selectPosition.y.toInt()][selectPosition.x.toInt()].rock = Block.Empty
            refreshMap()
        }

        if (isRightButtonPressed && isMouseInMap)
        {
            level.map[currentLevel][selectPosition.y.toInt()][selectPosition.x.toInt()].rock = currentBlockType
            refreshMap()
        }

        // action

        map.setTile("action", actionPosition.x.toInt(), actionPosition.y.toInt(), tiles.emptyTile)

        if (isMouseInMap)
        {
            actionPosition.x = truncate(worldMousePos.x / map.tileWidth)
            actionPosition.y = truncate(worldMousePos.y / map.tileHeight)

            isActionPossible = when (currentAction)
            {
                Action.Pickaxe ->
                {
                    when (level.map[currentLevel][selectPosition.y.toInt()][selectPosition.x.toInt()].rock)
                    {
                        Block.UndamagedStone, Block.DamagedStone, Block.Brick -> true
                        else -> false
                    }
                }
                Action.Bomb ->
                {
                    level.map[currentLevel][selectPosition.y.toInt()][selectPosition.x.toInt()].rock != Block.Empty
                }
                Action.Coin ->
                {
                    level.map[currentLevel][selectPosition.y.toInt()][selectPosition.x.toInt()].entity is EntityVendingMachine
                }
                Action.Cell ->
                {
                    level.map[currentLevel][selectPosition.y.toInt()][selectPosition.x.toInt()].entity is EntityPoweredGate
                }
                Action.Walk ->
                {
                    level.map[currentLevel][selectPosition.y.toInt()][selectPosition.x.toInt()].rock != Block.Empty && level.map[currentLevel][selectPosition.y.toInt()][selectPosition.x.toInt()].entity == null
                }
                else -> false
            }

            val tile = if (isActionPossible)
            {
                when (currentAction)
                {
                    Action.Pickaxe -> tiles.pickaxeTile
                    Action.Bomb -> tiles.bombTile
                    Action.Coin -> tiles.coinTile
                    Action.Cell -> tiles.cellTile
                    Action.Walk -> tiles.walkTile
                    else -> tiles.emptyTile
                }
            }
            else tiles.emptyTile

            refreshMap()

            map.setTile("action", actionPosition.x.toInt(), actionPosition.y.toInt(), tile)
        }

        if (isLeftButtonJustPressed)
        {
            if (isMouseInMap && isActionPossible)
            {
                when (currentAction)
                {
                    Action.Pickaxe ->
                    {
                        level.map[currentLevel][selectPosition.y.toInt()][selectPosition.x.toInt()].rock =
                                when (level.map[currentLevel][selectPosition.y.toInt()][selectPosition.x.toInt()].rock)
                                {
                                    Block.UndamagedStone -> Block.DamagedStone
                                    Block.DamagedStone, Block.Brick -> Block.Empty
                                    else -> Block.Metal
                                }

                        pickaxeCount--
                        if (pickaxeCount <= 0)
                        {
                            changeAction(Action.None)
                        }
                        refreshEquipmentCountLabels()
                    }
                    Action.Bomb ->
                    {
                        val entity = level.map[currentLevel][selectPosition.y.toInt()][selectPosition.x.toInt()].entity
                        val position = Vector3(selectPosition.x, selectPosition.y, currentLevel.toFloat())

                        when (entity)
                        {
                            is EntityContainer -> level.entities.remove(entity)
                            is EntityVendingMachine -> level.entities.remove(entity)
                            is EntityFlag -> level.entities.remove(entity)
                            is EntityTeleporter ->
                            {
                                if (position.equals(entity.firstTelePosition) || position.equals(entity.secondTelePosition))
                                {
                                    level.entities.remove(entity)
                                }
                            }
                            is EntityStairs ->
                            {
                                if (position.equals(entity.position))
                                {
                                    level.entities.remove(entity)
                                }
                            }
                            is EntityElevator ->
                            {
                                if (entity.positions.contains(position))
                                {
                                    level.entities.remove(entity)
                                }
                            }
                            is EntityPoweredGate ->
                            {
                                if (position.equals(entity.gatePosition))
                                {
                                    entity.isFirstPartPowered = false
                                    entity.isSecondPartPowered = false
                                }
                                else if (position.equals(entity.firstPartPosition) || position.equals(entity.secondPartPosition))
                                {
                                    level.entities.remove(entity)
                                }
                            }
                            else ->
                            {
                            }
                        }

                        bombCount--
                        if (bombCount <= 0)
                        {
                            changeAction(Action.None)
                        }
                        refreshEquipmentCountLabels()

                        refreshLevel()
                    }
                    Action.Coin ->
                    {
                        pickaxeCount++
                        bombCount++
                        cellCount++

                        coinCount--
                        if (coinCount <= 0)
                        {
                            changeAction(Action.None)
                        }
                        refreshEquipmentCountLabels()
                    }
                    Action.Cell ->
                    {
                        val position = Vector3(selectPosition.x, selectPosition.y, currentLevel.toFloat())
                        val poweredGate = (level.map[currentLevel][selectPosition.y.toInt()][selectPosition.x.toInt()].entity as EntityPoweredGate)

                        if (position.equals(poweredGate.firstPartPosition))
                        {
                            poweredGate.isFirstPartPowered = true
                        }

                        if (position.equals(poweredGate.secondPartPosition))
                        {
                            poweredGate.isSecondPartPowered = true
                        }

                        cellCount--
                        if (cellCount <= 0)
                        {
                            changeAction(Action.None)
                        }
                        refreshEquipmentCountLabels()
                    }
                    Action.Walk ->
                    {
                        level.player.position.x = actionPosition.x
                        level.player.position.y = actionPosition.y
                        level.player.position.z = currentLevel.toFloat()

                        refreshLevel()
                    }
                    else ->
                    {
                    }
                }
            }
        }

        // select

        if (isSelectFixed)
        {
            if (isLeftButtonJustPressed)
            {
                if (isMouseInMap)
                {
                    map.setTile("select", selectPosition.x.toInt(), selectPosition.y.toInt(), tiles.emptyTile)

                    selectPosition.x = truncate(worldMousePos.x / map.tileWidth)
                    selectPosition.y = truncate(worldMousePos.y / map.tileHeight)

                    map.setTile("select", selectPosition.x.toInt(), selectPosition.y.toInt(), tiles.selectTile)
                }
                else
                {
                    isSelectFixed = false
                }
            }
        }
        else
        {
            map.setTile("select", selectPosition.x.toInt(), selectPosition.y.toInt(), tiles.emptyTile)

            if (isMouseInMap)
            {
                selectPosition.x = truncate(worldMousePos.x / map.tileWidth)
                selectPosition.y = truncate(worldMousePos.y / map.tileHeight)

                map.setTile("select", selectPosition.x.toInt(), selectPosition.y.toInt(), tiles.selectTile)
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
