package com.marcin.panklex.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.maps.tiled.TiledMap
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.Json
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import com.marcin.panklex.*
import kotlin.math.floor

class ScreenGame(val name : String, val game : PanKlexGame) : BaseScreen(name, game), InputProcessor
{
    // main

    val inputMultiplexer = InputMultiplexer(stage, this)
    val mapCamera = OrthographicCamera()
    val mapViewport = FitViewport(1080f, 720f, mapCamera)
    val level = KlexLevel()
    val map = TiledMap()
    val renderer = KlexIsometricTiledMapRenderer(map, 2f)
    val tileset = TextureRegion(game.assetManager.get<Texture>("iso.png")).split(32, 32)
    val blockUnselected = StaticTiledMapTile(tileset[0][0])
    val blockSelected = StaticTiledMapTile(tileset[1][0])
    val emptyUnselected = StaticTiledMapTile(tileset[2][0])
    val emptySelected = StaticTiledMapTile(tileset[3][0])

    // dragging

    var isDragging = false
    var dragOrigin = Vector2()
    var dragDifference = Vector2()

    // touch

    var isTouch = false
    var isTouched = false
    var screenTouchPosition = Vector2()
    var worldTouchPosition = Vector2()
    var mapTouchPosition = Vector2()
    var levelTouchPosition = Vector2()
    var isTouchInMap = false

    // other

    var currentMapDirection = MapDirection.Up
    var currentFloor = 0

    // hud

    val leftArrowButton = TextButton("<--", TextButton.TextButtonStyle(null, null, null, BitmapFont()))
    val currentFloorLabel = Label("floor", Label.LabelStyle(BitmapFont(), Color.GREEN))
    val rightArrowButton = TextButton("-->", TextButton.TextButtonStyle(null, null, null, BitmapFont()))

    init
    {
        // hud

        leftArrowButton.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float)
            {
                when (currentMapDirection)
                {
                    MapDirection.Up    -> changeMapDirection(MapDirection.Left)
                    MapDirection.Left  -> changeMapDirection(MapDirection.Down)
                    MapDirection.Down  -> changeMapDirection(MapDirection.Right)
                    MapDirection.Right -> changeMapDirection(MapDirection.Up)
                }

                updateMap()
            }
        })
        rightArrowButton.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float)
            {
                when (currentMapDirection)
                {
                    MapDirection.Up    -> changeMapDirection(MapDirection.Right)
                    MapDirection.Right -> changeMapDirection(MapDirection.Down)
                    MapDirection.Down  -> changeMapDirection(MapDirection.Left)
                    MapDirection.Left  -> changeMapDirection(MapDirection.Up)
                }

                updateMap()
            }
        })

        val table = Table()
        table.top().setFillParent(true)
        table.defaults().pad(10f)
        table.add(leftArrowButton)
        table.add(currentFloorLabel)
        table.add(rightArrowButton)
        stage.addActor(table)

        // init

        createLevel()
        createMap()
        updateMap()
        changeFloor(9)

        mapCamera.position.set(160f, 160f, 0f)
        mapCamera.zoom = 1f
    }

    fun changeMapDirection(mapDirection : MapDirection)
    {
        currentMapDirection = mapDirection
    }

    fun createLevel()
    {
        val json = Json()
        val jsonFile = Gdx.files.internal("level.json")
        val jsonString = jsonFile.readString()
        val jsonLevel = json.fromJson(JsonLevel::class.java, jsonString)

        level.name = jsonLevel.name
        level.floors = jsonLevel.floors
        level.height = jsonLevel.height
        level.width = jsonLevel.width

        for (k in 0 until level.floors)
        {
            val floor = mutableListOf<List<Int>>()

            for (j in 0 until level.height)
            {
                val row = mutableListOf<Int>()

                for (i in 0 until level.width)
                {
                    row.add(jsonLevel.blocks[k][jsonLevel.height - 1 - j][i])
                }

                floor.add(row)
            }

            level.blocks.add(floor)
        }
    }

    fun createMap()
    {
        val layers = mutableListOf<TiledMapTileLayer>()

        for (k in 0 until 10)
        {
            layers.add(TiledMapTileLayer(100, 100, 32, 16).apply { name = k.toString() })
        }

        for (k in 0 until 10)
        {
            for (j in 0 until 100)
            {
                for (i in 0 until 100)
                {
                    val cell = TiledMapTileLayer.Cell()
                    layers[k].setCell(i, j, cell)
                }
            }
        }

        for (l in layers)
        {
            map.layers.add(l)
        }
    }

    fun updateMap()
    {
        for (k in 0 until level.floors)
        {
            for (j in 0 until level.height)
            {
                for (i in 0 until level.width)
                {
                    val cell = when (currentMapDirection)
                    {
                        MapDirection.Up    -> (map.layers[k] as TiledMapTileLayer).getCell(i, j)
                        MapDirection.Right -> (map.layers[k] as TiledMapTileLayer).getCell(j, level.width - 1 - i)
                        MapDirection.Down  -> (map.layers[k] as TiledMapTileLayer).getCell(
                            level.width - 1 - i,
                            level.height - 1 - j
                        )
                        MapDirection.Left  -> (map.layers[k] as TiledMapTileLayer).getCell(level.height - 1 - j, i)
                    }

                    cell.tile = if (level.blocks[k][j][i] == 1) blockUnselected else null
                }
            }
        }
    }

    fun changeFloor(floor : Int)
    {
        if (floor >= 0 && floor < level.floors)
        {
            currentFloor = floor
            currentFloorLabel.setText("floor: $floor")
        }
    }

    fun screenLoop()
    {
        //
    }

    override fun show()
    {
        super.show()
        game.changeInputProcessor(inputMultiplexer)
    }

    override fun render(delta : Float)
    {
        super.render(delta)

        screenLoop()

        ScreenUtils.clear(0f, 0f, 0f, 1f)
        camera.update()
        mapCamera.update()
        renderer.setView(mapCamera)
        renderer.render()
        stage.draw()
    }

    override fun resize(width : Int, height : Int)
    {
        super.resize(width, height)
        mapViewport.update(width, height)
    }

    override fun dispose()
    {
        super.dispose()
    }

    override fun keyDown(keycode : Int) : Boolean
    {
        when (keycode)
        {
            Input.Keys.NUM_0 -> changeFloor(0)
            Input.Keys.NUM_1 -> changeFloor(1)
            Input.Keys.NUM_2 -> changeFloor(2)
            Input.Keys.NUM_3 -> changeFloor(3)
            Input.Keys.NUM_4 -> changeFloor(4)
            Input.Keys.NUM_5 -> changeFloor(5)
            Input.Keys.NUM_6 -> changeFloor(6)
            Input.Keys.NUM_7 -> changeFloor(7)
            Input.Keys.NUM_8 -> changeFloor(8)
            Input.Keys.NUM_9 -> changeFloor(9)
        }

        return true
    }

    override fun keyUp(keycode : Int) : Boolean
    {
        return false
    }

    override fun keyTyped(character : Char) : Boolean
    {
        return false
    }

    override fun touchDown(screenX : Int, screenY : Int, pointer : Int, button : Int) : Boolean
    {
        isTouch = true

        return true
    }

    override fun touchUp(screenX : Int, screenY : Int, pointer : Int, button : Int) : Boolean
    {
        if (isTouch)
        {
            isTouched = true
            isTouch = false
        }
        isDragging = false

        return true
    }

    override fun touchDragged(screenX : Int, screenY : Int, pointer : Int) : Boolean
    {
        if (isDragging)
        {
            val dragPosition = mapViewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
            dragDifference.x = dragPosition.x - mapCamera.position.x
            dragDifference.y = dragPosition.y - mapCamera.position.y
            mapCamera.position.x = dragOrigin.x - dragDifference.x
            mapCamera.position.y = dragOrigin.y - dragDifference.y
        }
        else
        {
            dragOrigin = mapViewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
            isDragging = true
        }

        return true
    }

    override fun mouseMoved(screenX : Int, screenY : Int) : Boolean
    {
        screenTouchPosition.set(screenX.toFloat(), screenY.toFloat())
        worldTouchPosition = mapViewport.unproject(screenTouchPosition)
        mapTouchPosition.x =
            floor((0.5f * worldTouchPosition.x - worldTouchPosition.y + 16) / 32) + (currentFloor + 1)
        mapTouchPosition.y =
            floor((0.5f * worldTouchPosition.x + worldTouchPosition.y - 16) / 32) - (currentFloor + 1)
        isTouchInMap =
            (mapTouchPosition.x >= 0 && mapTouchPosition.y >= 0 && mapTouchPosition.x < level.width && mapTouchPosition.y < level.height)

        when (currentMapDirection)
        {
            MapDirection.Up    -> levelTouchPosition.set(mapTouchPosition.x, mapTouchPosition.y)
            MapDirection.Right -> levelTouchPosition.set(level.height - mapTouchPosition.y - 1, mapTouchPosition.x)
            MapDirection.Down  -> levelTouchPosition.set(
                level.width - mapTouchPosition.x - 1,
                level.height - mapTouchPosition.y - 1
            )
            MapDirection.Left  -> levelTouchPosition.set(mapTouchPosition.y, level.width - mapTouchPosition.x - 1)
        }

        updateMap()

        if (isTouchInMap)
        {
            val cell =
                (map.layers[currentFloor.toString()] as TiledMapTileLayer).getCell(
                    mapTouchPosition.x.toInt(),
                    mapTouchPosition.y.toInt()
                )
            val block = level.blocks[currentFloor][levelTouchPosition.y.toInt()][levelTouchPosition.x.toInt()]

            if (block == 1) cell.tile = blockSelected
            else cell.tile = emptySelected
        }

        return true
    }

    override fun scrolled(amountX : Float, amountY : Float) : Boolean
    {
        if (amountY > 0) mapCamera.zoom += 0.1f
        else mapCamera.zoom -= 0.1f

        return true
    }
}
