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
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.Align
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.ScreenViewport
import com.marcin.panklex.*
import kotlin.math.truncate

class ScreenGame(val name : String, val game : PanKlexGame) : BaseScreen(name, game)
{
    // main

    val mapCamera = OrthographicCamera()
    val mapViewport = ScreenViewport(mapCamera)

    val level = KlexLevel()
    val map = KlexMap(100, 100, 32, 32)
    val tiles = KlexTiles(game.assetManager)
    val mapTilesLogic = KlexMapTilesLogic(tiles, map, level)
    val pathFinding = KlexPathFinding(level)

    // hud

    val heartImage1 = Image(game.assetManager.get<Texture>("graphics/hud/heart.png"))
    val heartImage2 = Image(game.assetManager.get<Texture>("graphics/hud/heart.png"))
    val heartImage3 = Image(game.assetManager.get<Texture>("graphics/hud/heart.png"))
    val heartImage4 = Image(game.assetManager.get<Texture>("graphics/hud/heart.png"))

    val currentBlockLabel =
        Label("undam stone", Label.LabelStyle(BitmapFont(), Color.CYAN)).apply { setAlignment(Align.center) }
    val currentLevelLabel =
        Label("level: 0", Label.LabelStyle(BitmapFont(), Color.GREEN)).apply { setAlignment(Align.center) }
    val upButton = TextButton("Up", TextButton.TextButtonStyle(null, null, null, BitmapFont()))
    val downButton = TextButton("Down", TextButton.TextButtonStyle(null, null, null, BitmapFont()))

    val pickaxeUnselected = TextureRegion(game.assetManager.get<Texture>("graphics/actions/pickaxe.png"), 0, 0, 32, 32)
    val pickaxeSelected = TextureRegion(game.assetManager.get<Texture>("graphics/actions/pickaxe.png"), 32, 0, 32, 32)
    val bombUnselected = TextureRegion(game.assetManager.get<Texture>("graphics/actions/bomb.png"), 0, 0, 32, 32)
    val bombSelected = TextureRegion(game.assetManager.get<Texture>("graphics/actions/bomb.png"), 32, 0, 32, 32)
    val coinUnselected = TextureRegion(game.assetManager.get<Texture>("graphics/actions/coin.png"), 0, 0, 32, 32)
    val coinSelected = TextureRegion(game.assetManager.get<Texture>("graphics/actions/coin.png"), 32, 0, 32, 32)
    val cellUnselected = TextureRegion(game.assetManager.get<Texture>("graphics/actions/cell.png"), 0, 0, 32, 32)
    val cellSelected = TextureRegion(game.assetManager.get<Texture>("graphics/actions/cell.png"), 32, 0, 32, 32)
    val handUnselected = TextureRegion(game.assetManager.get<Texture>("graphics/actions/hand.png"), 0, 0, 32, 32)
    val handSelected = TextureRegion(game.assetManager.get<Texture>("graphics/actions/hand.png"), 32, 0, 32, 32)
    val walkUnselected = TextureRegion(game.assetManager.get<Texture>("graphics/actions/walk.png"), 0, 0, 32, 32)
    val walkSelected = TextureRegion(game.assetManager.get<Texture>("graphics/actions/walk.png"), 32, 0, 32, 32)
    val cancelImage = game.assetManager.get<Texture>("graphics/actions/cancel.png")

    val pickaxeButton = ImageButton(
        TextureRegionDrawable(pickaxeUnselected),
        TextureRegionDrawable(pickaxeUnselected),
        TextureRegionDrawable(pickaxeSelected)
    )
    val bombButton = ImageButton(
        TextureRegionDrawable(bombUnselected),
        TextureRegionDrawable(bombUnselected),
        TextureRegionDrawable(bombSelected)
    )
    val coinButton = ImageButton(
        TextureRegionDrawable(coinUnselected),
        TextureRegionDrawable(coinUnselected),
        TextureRegionDrawable(coinSelected)
    )
    val cellButton = ImageButton(
        TextureRegionDrawable(cellUnselected),
        TextureRegionDrawable(cellUnselected),
        TextureRegionDrawable(cellSelected)
    )
    val handButton = ImageButton(
        TextureRegionDrawable(handUnselected),
        TextureRegionDrawable(handUnselected),
        TextureRegionDrawable(handSelected)
    )
    val walkButton = ImageButton(
        TextureRegionDrawable(walkUnselected),
        TextureRegionDrawable(walkUnselected),
        TextureRegionDrawable(walkSelected)
    )
    val cancelButton = ImageButton(TextureRegionDrawable(cancelImage))

    val pickaxeLabel = Label("0", Label.LabelStyle(BitmapFont(), Color.YELLOW)).apply { setAlignment(Align.center) }
    val bombLabel = Label("0", Label.LabelStyle(BitmapFont(), Color.YELLOW)).apply { setAlignment(Align.center) }
    val coinLabel = Label("0", Label.LabelStyle(BitmapFont(), Color.YELLOW)).apply { setAlignment(Align.center) }
    val cellLabel = Label("0", Label.LabelStyle(BitmapFont(), Color.YELLOW)).apply { setAlignment(Align.center) }

    // other

    var currentHearts = 0
    var currentBlock = Block.UndamagedStone
    var currentLevel = 0
    var currentAction = Action.None

    var playerPosition = Vector3()

    var pickaxeCount = 0
    var bombCount = 0
    var coinCount = 0
    var cellCount = 0

    var isDragging = false
    var dragOrigin = Vector2()
    var dragDifference = Vector2()

    init
    {
        // hud

        upButton.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float)
            {
                changeLevel(currentLevel + 1)
                refreshCurrentLevel()
                refreshMap()
            }
        })
        downButton.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float)
            {
                changeLevel(currentLevel - 1)
                refreshCurrentLevel()
                refreshMap()
            }
        })
        pickaxeButton.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float)
            {
                changeAction(Action.Pickaxe)
                refreshActions()
            }
        })
        bombButton.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float)
            {
                changeAction(Action.Bomb)
                refreshActions()
            }
        })
        coinButton.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float)
            {
                changeAction(Action.Coin)
                refreshActions()
            }
        })
        cellButton.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float)
            {
                changeAction(Action.Cell)
                refreshActions()
            }
        })
        handButton.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float)
            {
                changeAction(Action.Hand)
                refreshActions()
            }
        })
        walkButton.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float)
            {
                changeAction(Action.Walk)
                refreshActions()
            }
        })
        cancelButton.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float)
            {
                changeAction(Action.None)
                refreshActions()
            }
        })

        val table = Table()
        table.top()
        table.setFillParent(true)
        table.defaults().width(64f).pad(10f)
        table.add(heartImage1).width(32f)
        table.add(heartImage2).width(32f)
        table.add(heartImage3).width(32f)
        table.add(heartImage4).width(32f)
        table.row()
        table.add(currentBlockLabel)
        table.add(currentLevelLabel)
        table.add(upButton)
        table.add(downButton)
        table.row()
        table.add(pickaxeButton)
        table.add(bombButton)
        table.add(coinButton)
        table.add(cellButton)
        table.row()
        table.add(pickaxeLabel)
        table.add(bombLabel)
        table.add(coinLabel)
        table.add(cellLabel)
        table.row()
        table.add(handButton)
        table.add(walkButton)
        table.add(cancelButton)
        stage.addActor(table)

        // other

        loadLevelFromJson("levels/2.json")
        pathFinding.setUpMap()
        changeHearts(4)
        changeBlock(Block.UndamagedStone)
        changeLevel(playerPosition.z.toInt())
        changeAction(Action.None)
        refreshEntities()
        refreshDepths()
        refreshMap()
        refreshHearts()
        refreshCurrentBlock()
        refreshCurrentLevel()
        refreshActions()
        refreshEquipment()
        showPlayerOnMap()
        mapCamera.zoom = 0.5f
        mapCamera.position.x = map.width / 2f
        mapCamera.position.y = map.height / 2f
    }

    fun gameLoop()
    {
        // input

        val is1KeyJustPressed = Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)
        val is2KeyJustPressed = Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)
        val is3KeyJustPressed = Gdx.input.isKeyJustPressed(Input.Keys.NUM_3)
        val is4KeyJustPressed = Gdx.input.isKeyJustPressed(Input.Keys.NUM_4)

        val isEqualsKeyJustPressed = Gdx.input.isKeyJustPressed(Input.Keys.EQUALS)
        val isMinusKeyJustPressed = Gdx.input.isKeyJustPressed(Input.Keys.MINUS)

        val isLeftButtonPressed = Gdx.input.isButtonPressed(0)
        val isLeftButtonJustPressed = Gdx.input.isButtonJustPressed(0)
        val isMiddleButtonPressed = Gdx.input.isButtonPressed(2)
        val isMiddleButtonJustPressed = Gdx.input.isButtonJustPressed(2)

        val screenMousePosition = Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
        val worldMousePosition = mapViewport.unproject(screenMousePosition)
        val mapMousePosition =
            Vector2(truncate(worldMousePosition.x / map.tileWidth), truncate(worldMousePosition.y / map.tileHeight))
        val isMouseInMap =
            (worldMousePosition.x > 0 && worldMousePosition.x < map.width && worldMousePosition.y > 0 && worldMousePosition.y < map.height)
        val levelMousePosition = Vector3(mapMousePosition.x, mapMousePosition.y, currentLevel.toFloat())
        val mapMouseBlock =
            if (isMouseInMap) level.map[currentLevel][mapMousePosition.y.toInt()][mapMousePosition.x.toInt()] else LevelBlock()
        val mapMouseBlockUp =
            if (isMouseInMap) level.map[currentLevel + 1][mapMousePosition.y.toInt()][mapMousePosition.x.toInt()] else null

        // hearts

        if (isEqualsKeyJustPressed) changeHearts(1)
        if (isMinusKeyJustPressed) changeHearts(-1)

        refreshHearts()

        // block

        if (is1KeyJustPressed) changeBlock(Block.UndamagedStone)
        if (is2KeyJustPressed) changeBlock(Block.DamagedStone)
        if (is3KeyJustPressed) changeBlock(Block.Brick)
        if (is4KeyJustPressed) changeBlock(Block.Metal)

        refreshCurrentBlock()

        if (isMiddleButtonPressed && isMouseInMap)
        {
            mapMouseBlock.rock = currentBlock
            refreshMap()
        }

        // action

        if (isMouseInMap)
        {
            // is action possible

            val isActionPossible = if (mapMouseBlock.isEntity())
            {
                mapMouseBlock.entity!!.isActionPossible(levelMousePosition, currentAction)
            }
            else
            {
                when (currentAction)
                {
                    Action.Pickaxe ->
                    {

                        if (mapMouseBlockUp!!.isRock())
                        {
                            when (mapMouseBlockUp.rock)
                            {
                                Block.UndamagedStone, Block.DamagedStone, Block.Brick -> true
                                else                                                  -> false
                            }
                        }
                        else
                        {
                            when (mapMouseBlock.rock)
                            {
                                Block.UndamagedStone, Block.DamagedStone, Block.Brick -> true
                                else                                                  -> false
                            }
                        }
                    }
                    Action.Walk    -> !mapMouseBlockUp!!.isRock()
                    else           -> false
                }
            }

            // action tile

            val actionTile = if (isActionPossible)
            {
                when (currentAction)
                {
                    Action.Pickaxe -> tiles.pickaxe_tile
                    Action.Bomb    -> tiles.bomb_tile
                    Action.Coin    -> tiles.coin_tile
                    Action.Cell    -> tiles.cell_tile
                    Action.Hand    -> tiles.hand_tile
                    Action.Walk    -> tiles.walk_tile
                    else           -> tiles.empty
                }
            }
            else tiles.empty

            showActionOnMap(mapMousePosition.x.toInt(), mapMousePosition.y.toInt(), actionTile)

            // executing action

            if (isLeftButtonJustPressed && isActionPossible && playerPosition.z.toInt() == currentLevel)
            {
                pathFinding.findPath(
                    playerPosition.x.toInt(),
                    playerPosition.y.toInt(),
                    mapMousePosition.x.toInt(),
                    mapMousePosition.y.toInt(),
                    currentLevel
                )

                if (pathFinding.isTraversable)
                {
                    if (mapMouseBlock.isEntity())
                    {
                        mapMouseBlock.entity?.onAction(levelMousePosition, currentAction, this)
                    }
                    else
                    {
                        when (currentAction)
                        {
                            Action.Pickaxe ->
                            {
                                if (mapMouseBlockUp!!.isRock())
                                {
                                    mapMouseBlockUp.rock = when (mapMouseBlockUp.rock)
                                    {
                                        Block.UndamagedStone            -> Block.DamagedStone
                                        Block.DamagedStone, Block.Brick -> Block.Empty
                                        else                            -> Block.Metal
                                    }
                                }
                                else
                                {
                                    mapMouseBlock.rock = when (mapMouseBlock.rock)
                                    {
                                        Block.UndamagedStone            -> Block.DamagedStone
                                        Block.DamagedStone, Block.Brick -> Block.Empty
                                        else                            -> Block.Metal
                                    }
                                }

                                pickaxeCount--
                                if (pickaxeCount <= 0)
                                {
                                    changeAction(Action.None)
                                    refreshActions()
                                }

                                refreshDepths()

                                if (mapMousePosition.x == playerPosition.x && mapMousePosition.y == playerPosition.y && !mapMouseBlock.isRock())
                                {
                                    changePlayerPosition(
                                        Vector3(
                                            mapMousePosition.x,
                                            mapMousePosition.y,
                                            (currentLevel - mapMouseBlock.depth).toFloat()
                                        )
                                    )

                                    changeLevel(currentLevel - mapMouseBlock.depth)
                                    changeHearts(-mapMouseBlock.depth)
                                }
                            }
                            Action.Walk    ->
                            {
                                if (mapMouseBlock.isRock())
                                {
                                    changePlayerPosition(
                                        Vector3(
                                            mapMousePosition.x,
                                            mapMousePosition.y,
                                            currentLevel.toFloat()
                                        )
                                    )
                                }
                                else
                                {
                                    changePlayerPosition(
                                        Vector3(
                                            mapMousePosition.x,
                                            mapMousePosition.y,
                                            (currentLevel - mapMouseBlock.depth).toFloat()
                                        )
                                    )

                                    changeLevel(currentLevel - mapMouseBlock.depth)
                                    changeHearts(-mapMouseBlock.depth)
                                }
                            }
                            else           ->
                            {
                            }
                        }
                    }
                }

                refreshEntities()
                refreshDepths()
                refreshMap()
                refreshHearts()
                refreshCurrentBlock()
                refreshCurrentLevel()
                refreshActions()
                refreshEquipment()
            }
        }

        // drag

        if (isDragging)
        {
            dragDifference.x = worldMousePosition.x - mapCamera.position.x
            dragDifference.y = worldMousePosition.y - mapCamera.position.y
            mapCamera.position.x = dragOrigin.x - dragDifference.x
            mapCamera.position.y = dragOrigin.y - dragDifference.y

            if (!isLeftButtonPressed)
            {
                isDragging = false
            }
        }
        else
        {
            if (isLeftButtonPressed && currentAction == Action.None)
            {
                dragOrigin = worldMousePosition
                isDragging = true
            }
        }
    }

    fun changePlayerPosition(position : Vector3)
    {
        playerPosition = position
    }

    fun showActionOnMap(x : Int, y : Int, tile : StaticTiledMapTile)
    {

        map.clearLayer(MapLayer.Action)
        map.setTile(MapLayer.Action, x, y, tile)
    }

    fun showPlayerOnMap()
    {
        map.clearLayer(MapLayer.Player)

        if (playerPosition.z.toInt() == currentLevel)
        {
            map.setTile(MapLayer.Player, playerPosition.x.toInt(), playerPosition.y.toInt(), tiles.player)
        }
    }

    fun loadLevelFromJson(jsonPath : String)
    {
        val jsonFile = Gdx.files.internal(jsonPath)
        val jsonString = jsonFile.readString()
        val jsonLevel = Json().fromJson(JsonLevel::class.java, jsonString)

        level.version = jsonLevel.version
        level.name = jsonLevel.name
        level.levels = jsonLevel.levels
        level.width = jsonLevel.width
        level.height = jsonLevel.height

        playerPosition = jsonLevel.playerPosition

        val levelList = mutableListOf<List<List<LevelBlock>>>()

        for (i in 0 until level.levels)
        {
            val columnList = mutableListOf<List<LevelBlock>>()

            for (j in 0 until level.height)
            {
                val rowList = mutableListOf<LevelBlock>()

                for (k in 0 until level.width)
                {
                    val levelBlock = LevelBlock()
                    levelBlock.position.x = k.toFloat()
                    levelBlock.position.y = j.toFloat()
                    levelBlock.position.z = i.toFloat()
                    levelBlock.rock = when (jsonLevel.map[i][level.height - j - 1][k])
                    {
                        0    -> Block.Empty
                        1    -> Block.UndamagedStone
                        2    -> Block.DamagedStone
                        3    -> Block.Brick
                        4    -> Block.Metal
                        else -> Block.Empty
                    }

                    rowList.add(levelBlock)
                }

                columnList.add(rowList)
            }

            levelList.add(columnList)
        }

        level.map = levelList
        level.entities.addAll(jsonLevel.entities)
    }

    fun refreshEntities()
    {
        for (i in 0 until level.width)
        {
            for (j in 0 until level.height)
            {
                level.map[currentLevel][j][i].entity = null
            }
        }

        for (e in level.entities)
        {
            for (p in e.getPositions())
            {
                level.map[p.z.toInt()][p.y.toInt()][p.x.toInt()].entity = e
            }
        }
    }

    fun refreshDepths()
    {
        for (i in 0 until level.levels)
        {
            for (j in 0 until level.height)
            {
                for (k in 0 until level.width)
                {
                    var depth = 0

                    for (l in i - 1 downTo 0)
                    {
                        if (!level.map[i-l][j][k].isRock()) depth++
                    }

                    level.map[i][j][k].depth = depth
                }
            }
        }
    }

    fun refreshMap()
    {
        mapTilesLogic.setUpMap(currentLevel)
        showPlayerOnMap()
    }

    fun changeHearts(by : Int)
    {
        currentHearts += by

        if (currentHearts < 1)
        {
            game.log("hearts", "Dead!")
            currentHearts = 1
        }
        if (currentHearts > 4)
        {
            game.log("hearts", "Healthy!")
            currentHearts = 4
        }
    }

    fun changeBlock(block : Block)
    {
        currentBlock = block
    }

    fun changeLevel(levelNr : Int)
    {
        currentLevel = levelNr

        if (currentLevel < 0)
        {
            game.log("level", "Too deep!")
            currentLevel = 0
        }
        if (currentLevel > level.levels - 1)
        {
            game.log("level", "Too high!")
            currentLevel = level.levels - 1
        }
    }

    fun changeAction(action : Action)
    {
        when (action)
        {
            Action.Pickaxe -> if (pickaxeCount > 0) currentAction = Action.Pickaxe
            Action.Bomb    -> if (bombCount > 0) currentAction = Action.Bomb
            Action.Coin    -> if (coinCount > 0) currentAction = Action.Coin
            Action.Cell    -> if (cellCount > 0) currentAction = Action.Cell
            Action.Hand    -> currentAction = Action.Hand
            Action.Walk    -> currentAction = Action.Walk
            Action.None    -> currentAction = Action.None
        }
    }

    fun refreshHearts()
    {
        heartImage1.isVisible = (currentHearts >= 1)
        heartImage2.isVisible = (currentHearts >= 2)
        heartImage3.isVisible = (currentHearts >= 3)
        heartImage4.isVisible = (currentHearts >= 4)
    }

    fun refreshCurrentBlock()
    {
        currentBlockLabel.setText(
            when (currentBlock)
            {
                Block.UndamagedStone -> "undam stone"
                Block.DamagedStone   -> "dam stone"
                Block.Brick          -> "brick"
                Block.Metal          -> "metal"
                else                 -> "?"
            }
        )
    }

    fun refreshCurrentLevel()
    {
        currentLevelLabel.setText("Level: $currentLevel")
    }

    fun refreshActions()
    {
        pickaxeButton.isChecked = (currentAction == Action.Pickaxe)
        bombButton.isChecked = (currentAction == Action.Bomb)
        coinButton.isChecked = (currentAction == Action.Coin)
        cellButton.isChecked = (currentAction == Action.Cell)
        handButton.isChecked = (currentAction == Action.Hand)
        walkButton.isChecked = (currentAction == Action.Walk)
    }

    fun refreshEquipment()
    {
        pickaxeLabel.setText(pickaxeCount)
        bombLabel.setText(bombCount)
        coinLabel.setText(coinCount)
        cellLabel.setText(cellCount)
    }

    override fun show()
    {
        super.show()
    }

    override fun render(delta : Float)
    {
        super.render(delta)

        gameLoop()

        ScreenUtils.clear(0f, 0f, 0f, 1f)
        camera.update()
        mapCamera.update()
        map.renderer.setView(mapCamera)
        map.renderer.render()
        stage.draw()
    }

    override fun resize(width : Int, height : Int)
    {
        super.resize(width, height)
        mapViewport.update(width, height)
    }
}
