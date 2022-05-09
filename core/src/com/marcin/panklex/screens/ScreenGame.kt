package com.marcin.panklex.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
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
    val mapTilesLogic = KlexMapTilesLogic(tiles, map, level)

    // hud

    val blockTypeLabel = Label("undam stone", Label.LabelStyle(BitmapFont(), Color.CYAN))
    val levelLabel = Label("level: 0", Label.LabelStyle(BitmapFont(), Color.GREEN))
    val upButton = TextButton("Up", TextButton.TextButtonStyle(null, null, null, BitmapFont()))
    val downButton = TextButton("Down", TextButton.TextButtonStyle(null, null, null, BitmapFont()))
    val endGameButton = TextButton("End Game", TextButton.TextButtonStyle(null, null, null, BitmapFont()))

    val pickaxeUnselected = TextureRegion(game.assetManager.get<Texture>("tiles/actions/pickaxe.png"), 0, 0, 32, 32)
    val pickaxeSelected = TextureRegion(game.assetManager.get<Texture>("tiles/actions/pickaxe.png"), 32, 0, 32, 32)
    val bombUnselected = TextureRegion(game.assetManager.get<Texture>("tiles/actions/bomb.png"), 0, 0, 32, 32)
    val bombSelected = TextureRegion(game.assetManager.get<Texture>("tiles/actions/bomb.png"), 32, 0, 32, 32)
    val coinUnselected = TextureRegion(game.assetManager.get<Texture>("tiles/actions/coin.png"), 0, 0, 32, 32)
    val coinSelected = TextureRegion(game.assetManager.get<Texture>("tiles/actions/coin.png"), 32, 0, 32, 32)
    val cellUnselected = TextureRegion(game.assetManager.get<Texture>("tiles/actions/cell.png"), 0, 0, 32, 32)
    val cellSelected = TextureRegion(game.assetManager.get<Texture>("tiles/actions/cell.png"), 32, 0, 32, 32)
    val walkUnselected = TextureRegion(game.assetManager.get<Texture>("tiles/actions/walk.png"), 0, 0, 32, 32)
    val walkSelected = TextureRegion(game.assetManager.get<Texture>("tiles/actions/walk.png"), 32, 0, 32, 32)
    val cancelImage = game.assetManager.get<Texture>("tiles/actions/cancel.png")

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
    val walkButton = ImageButton(
            TextureRegionDrawable(walkUnselected),
            TextureRegionDrawable(walkUnselected),
            TextureRegionDrawable(walkSelected)
                                )
    val cancellButton = ImageButton(TextureRegionDrawable(cancelImage))

    val pickaxeCountLabel = Label("0", Label.LabelStyle(BitmapFont(), Color.YELLOW))
    val bombCountLabel = Label("0", Label.LabelStyle(BitmapFont(), Color.YELLOW))
    val coinCountLabel = Label("0", Label.LabelStyle(BitmapFont(), Color.YELLOW))
    val cellCountLabel = Label("0", Label.LabelStyle(BitmapFont(), Color.YELLOW))

    // other

    var currentLevel = 0
    var currentBlockType = Block.UndamagedStone
    var currentAction = Action.None

    var pickaxeCount = 3
    var bombCount = 3
    var coinCount = 3
    var cellCount = 3

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

        pickaxeButton.isChecked = (currentAction == Action.Pickaxe)
        bombButton.isChecked = (currentAction == Action.Bomb)
        coinButton.isChecked = (currentAction == Action.Coin)
        cellButton.isChecked = (currentAction == Action.Cell)
        walkButton.isChecked = (currentAction == Action.Walk)
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
                is EntityPlayer         ->
                {
                    positions.add(e.position)
                }
                is EntityContainer      ->
                {
                    positions.add(e.position)
                }
                is EntityVendingMachine ->
                {
                    positions.add(e.position)
                }
                is EntityElevator       ->
                {
                    positions.addAll(e.positions)
                    positions.addAll(e.exitPositions)
                }
                is EntityTeleporter     ->
                {
                    positions.add(e.firstTelePosition)
                    positions.add(e.secondTelePosition)
                }
                is EntityFlag           -> positions.add(e.position)
                is EntityStairs         ->
                {
                    positions.add(e.position)
                    positions.add(e.lowerEnd)
                    positions.add(e.upperEnd)
                }
                is EntityPoweredGate    ->
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
        mapTilesLogic.setUpMap(currentLevel)
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
        val isLeftButtonJustPressed = Gdx.input.isButtonJustPressed(0)
        val isRightButtonPressed = Gdx.input.isButtonPressed(1)
        val isRightButtonJustPressed = Gdx.input.isButtonJustPressed(1)
        val isMiddleButtonPressed = Gdx.input.isButtonPressed(2)
        val isMiddleButtonJustPressed = Gdx.input.isButtonJustPressed(2)

        val screenMousePosition = Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
        val worldMousePosition = mapViewport.unproject(screenMousePosition)
        val mapMousePosition =
                Vector2(truncate(worldMousePosition.x / map.tileWidth), truncate(worldMousePosition.y / map.tileHeight))
        val isMouseInMap =
                (worldMousePosition.x > 0 && worldMousePosition.x < map.width() && worldMousePosition.y > 0 && worldMousePosition.y < map.height())
        val mapMouseBlock =
                if (isMouseInMap) level.map[currentLevel][mapMousePosition.y.toInt()][mapMousePosition.x.toInt()] else LevelBlock()

        // block

        if (is1KeyJustPressed) currentBlockType = Block.UndamagedStone
        if (is2KeyJustPressed) currentBlockType = Block.DamagedStone
        if (is3KeyJustPressed) currentBlockType = Block.Brick
        if (is4KeyJustPressed) currentBlockType = Block.Metal

        blockTypeLabel.setText(
                when (currentBlockType)
                {
                    Block.UndamagedStone -> "undam stone"
                    Block.DamagedStone   -> "dam stone"
                    Block.Brick          -> "brick"
                    Block.Metal          -> "metal"
                    else                 -> "?"
                }
                              )

        if (isMiddleButtonPressed && isMouseInMap)
        {
            mapMouseBlock.rock = currentBlockType
            refreshMap()
        }

        // action

        map.clearLayer(MapLayer.Action.name)

        if (isMouseInMap)
        {
            val isActionPossible = when (currentAction)
            {
                Action.Pickaxe ->
                {
                    when (mapMouseBlock.rock)
                    {
                        Block.UndamagedStone, Block.DamagedStone, Block.Brick -> true
                        else                                                  -> false
                    }
                }
                Action.Bomb    ->
                {
                    mapMouseBlock.rock != Block.Empty
                }
                Action.Coin    ->
                {
                    mapMouseBlock.entity is EntityVendingMachine
                }
                Action.Cell    ->
                {
                    mapMouseBlock.entity is EntityPoweredGate
                }
                Action.Walk    ->
                {
                    mapMouseBlock.rock != Block.Empty && mapMouseBlock.entity == null
                }
                else           -> false
            }

            val actionTile = if (isActionPossible)
            {
                when (currentAction)
                {
                    Action.Pickaxe -> tiles.pickaxe_tile
                    Action.Bomb    -> tiles.bomb_tile
                    Action.Coin    -> tiles.coin_tile
                    Action.Cell    -> tiles.cell_tile
                    Action.Walk    -> tiles.walk_tile
                    else           -> tiles.emptyTile
                }
            }
            else tiles.emptyTile

            refreshMap()

            map.setTile(MapLayer.Action.name, mapMousePosition.x.toInt(), mapMousePosition.y.toInt(), actionTile)

            if (isLeftButtonJustPressed && isActionPossible)
            {
                when (currentAction)
                {
                    Action.Pickaxe ->
                    {
                        mapMouseBlock.rock = when (mapMouseBlock.rock)
                        {
                            Block.UndamagedStone            -> Block.DamagedStone
                            Block.DamagedStone, Block.Brick -> Block.Empty
                            else                            -> Block.Metal
                        }

                        pickaxeCount--
                        if (pickaxeCount <= 0)
                        {
                            changeAction(Action.None)
                        }
                        refreshEquipmentCountLabels()
                    }
                    Action.Bomb    ->
                    {
                        val entity = mapMouseBlock.entity
                        val entityPosition = Vector3(mapMousePosition.x, mapMousePosition.y, currentLevel.toFloat())

                        when (entity)
                        {
                            is EntityContainer      -> level.entities.remove(entity)
                            is EntityVendingMachine -> level.entities.remove(entity)
                            is EntityFlag           -> level.entities.remove(entity)
                            is EntityTeleporter     ->
                            {
                                if (entityPosition.equals(entity.firstTelePosition) || entityPosition.equals(entity.secondTelePosition))
                                {
                                    level.entities.remove(entity)
                                }
                            }
                            is EntityStairs         ->
                            {
                                if (entityPosition.equals(entity.position))
                                {
                                    level.entities.remove(entity)
                                }
                            }
                            is EntityElevator       ->
                            {
                                if (entity.positions.contains(entityPosition))
                                {
                                    level.entities.remove(entity)
                                }
                            }
                            is EntityPoweredGate    ->
                            {
                                if (entityPosition.equals(entity.gatePosition))
                                {
                                    entity.isFirstPartPowered = false
                                    entity.isSecondPartPowered = false
                                }
                                else if (entityPosition.equals(entity.firstPartPosition) || entityPosition.equals(entity.secondPartPosition))
                                {
                                    level.entities.remove(entity)
                                }
                            }
                            else                    ->
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
                    Action.Coin    ->
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
                    Action.Cell    ->
                    {
                        val entityPosition = Vector3(mapMousePosition.x, mapMousePosition.y, currentLevel.toFloat())
                        val poweredGate = (mapMouseBlock.entity as EntityPoweredGate)

                        if (entityPosition.equals(poweredGate.firstPartPosition))
                        {
                            poweredGate.isFirstPartPowered = true
                        }

                        if (entityPosition.equals(poweredGate.secondPartPosition))
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
                    Action.Walk    ->
                    {
                        level.player.position.x = mapMousePosition.x
                        level.player.position.y = mapMousePosition.y
                        level.player.position.z = currentLevel.toFloat()

                        refreshLevel()
                    }
                    else           ->
                    {
                    }
                }

            }
        }

        // drag

        if (isDragging)
        {
            dragDifference.x = worldMousePosition.x - mapCamera.position.x
            dragDifference.y = worldMousePosition.y - mapCamera.position.y
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
                dragOrigin = worldMousePosition
                isDragging = true
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
