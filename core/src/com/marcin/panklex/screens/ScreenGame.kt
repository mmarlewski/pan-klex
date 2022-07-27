package com.marcin.panklex.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.math.Vector3
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import com.marcin.panklex.*
import kotlin.math.floor

class ScreenGame(name : String, val game : PanKlexGame) : BaseScreen(name, game), InputProcessor
{
    // main

    val inputMultiplexer = InputMultiplexer(stage, this)
    val gameCamera = OrthographicCamera()
    val gameViewport = FitViewport(1080f, 720f, gameCamera)
    val tiles = KlexTiles(game.assetManager)
    val level = KlexLevel()
    val room = KlexRoom(level)
    val map = KlexMap(room, tiles)

    // dragging

    var isDragging = false
    var dragOrigin = Vector2()
    var dragDifference = Vector2()

    // touch

    var isTouch = false
    var isTouched = false
    var screenTouchPosition = Vector2()
    var worldTouchPosition = Vector2()
    var mapTouchPosition = Vector3()
    var roomTouchPosition = Vector3()
    var isTouchInMap = false

    // other

    var currentFloor = 0
    var currentPosition = 0

    // hud

    val leftArrowDirectionButton = TextButton("<---", TextButton.TextButtonStyle(null, null, null, BitmapFont()))
    val currentDirectionLabel = Label("direction", Label.LabelStyle(BitmapFont(), Color.CYAN))
    val rightArrowDirectionButton = TextButton("--->", TextButton.TextButtonStyle(null, null, null, BitmapFont()))
    val currentFloorLabel = Label("floor", Label.LabelStyle(BitmapFont(), Color.GREEN))
    val leftArrowPositionButton = TextButton("<---", TextButton.TextButtonStyle(null, null, null, BitmapFont()))
    val currentPositionLabel = Label("position", Label.LabelStyle(BitmapFont(), Color.ORANGE))
    val rightArrowPositionButton = TextButton("--->", TextButton.TextButtonStyle(null, null, null, BitmapFont()))

    init
    {
        // hud

        leftArrowDirectionButton.addListener(object : ClickListener()
                                             {
                                                 override fun clicked(event : InputEvent?, x : Float, y : Float)
                                                 {
                                                     val direction = when (map.direction)
                                                     {
                                                         MapDirection.Up    -> MapDirection.Left
                                                         MapDirection.Left  -> MapDirection.Down
                                                         MapDirection.Down  -> MapDirection.Right
                                                         MapDirection.Right -> MapDirection.Up
                                                     }
                                                     changeDirection(direction)
                                                 }
                                             })
        rightArrowDirectionButton.addListener(object : ClickListener()
                                              {
                                                  override fun clicked(event : InputEvent?, x : Float, y : Float)
                                                  {
                                                      val direction = when (map.direction)
                                                      {
                                                          MapDirection.Up    -> MapDirection.Right
                                                          MapDirection.Right -> MapDirection.Down
                                                          MapDirection.Down  -> MapDirection.Left
                                                          MapDirection.Left  -> MapDirection.Up
                                                      }
                                                      changeDirection(direction)
                                                  }
                                              })

        leftArrowPositionButton.addListener(object : ClickListener()
                                            {
                                                override fun clicked(event : InputEvent?, x : Float, y : Float)
                                                {
                                                    changePosition(currentPosition - 1)
                                                }
                                            })
        rightArrowPositionButton.addListener(object : ClickListener()
                                             {
                                                 override fun clicked(event : InputEvent?, x : Float, y : Float)
                                                 {
                                                     changePosition(currentPosition + 1)
                                                 }
                                             })

        val table = Table()
        table.top().setFillParent(true)
        table.defaults().pad(10f)
        table.add(leftArrowDirectionButton)
        table.add(currentDirectionLabel)
        table.add(rightArrowDirectionButton)
        table.add(currentFloorLabel)
        table.add(leftArrowPositionButton)
        table.add(currentPositionLabel)
        table.add(rightArrowPositionButton)
        stage.addActor(table)

        // init

        level.createLevel("level1.json")
        map.createMap()
        changeDirection(map.direction)
        changeFloor(0)
        changePosition(0)

        gameCamera.position.set(
                (map.tileLengthHalf * map.width).toFloat(),
                (map.tileLengthQuarter * map.height).toFloat(),
                0f
                               )
        gameCamera.zoom = 0.25f
    }

    fun changeDirection(direction : MapDirection)
    {
        currentDirectionLabel.setText("$direction")
        map.changeMapDirection(direction)
        map.updateMap()
    }

    fun changeFloor(floor : Int)
    {
        if (floor in 0 until level.floors)
        {
            currentFloorLabel.setText("floor: $floor")
            currentFloor = floor
        }
    }

    fun changePosition(position : Int)
    {
        if (position in 0 until level.positions.size)
        {
            currentPositionLabel.setText("position: $position")
            currentPosition = position
            room.updateRoom(level.positions[position])
            map.updateMap()
        }
    }

    fun updateTouch(screenX : Int, screenY : Int)
    {
        screenTouchPosition.set(screenX.toFloat(), screenY.toFloat())
        worldTouchPosition = gameViewport.unproject(screenTouchPosition)
        mapTouchPosition.x =
                floor((0.5f * worldTouchPosition.x - worldTouchPosition.y + map.tileLengthQuarter) / map.tileLengthHalf) + (currentFloor + 1)
        mapTouchPosition.y =
                floor((0.5f * worldTouchPosition.x + worldTouchPosition.y - map.tileLengthQuarter) / map.tileLengthHalf) - (currentFloor + 1)
        mapTouchPosition.z = currentFloor.toFloat()
        isTouchInMap =
                (mapTouchPosition.x.toInt() in 0 until level.width && mapTouchPosition.y.toInt() in 0 until level.height)
    }

    fun screenLoop()
    {
        if (isTouchInMap)
        {
            map.getRoomPosition(mapTouchPosition, roomTouchPosition)
            map.updateMap()
            map.changeSelection(mapTouchPosition)
            //game.log("map pos", "x ${mapTouchPosition.x} y ${mapTouchPosition.y} z ${mapTouchPosition.z}")
        }
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

        Gdx.gl.glClearColor(0f, 0f, 0f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.update()
        gameCamera.update()
        for (k in 0 until map.maxFloors)
        {
            map.renderers[k].setView(gameCamera)
            map.renderers[k].render()
        }
        stage.draw()
    }

    override fun resize(width : Int, height : Int)
    {
        super.resize(width, height)
        gameViewport.update(width, height)
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

        updateTouch(screenX, screenY)

        map.info(mapTouchPosition)

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
            val dragPosition = gameViewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
            dragDifference.x = dragPosition.x - gameCamera.position.x
            dragDifference.y = dragPosition.y - gameCamera.position.y
            gameCamera.position.x = dragOrigin.x - dragDifference.x
            gameCamera.position.y = dragOrigin.y - dragDifference.y
        }
        else
        {
            dragOrigin = gameViewport.unproject(Vector2(screenX.toFloat(), screenY.toFloat()))
            isDragging = true
        }

        return true
    }

    override fun mouseMoved(screenX : Int, screenY : Int) : Boolean
    {
        updateTouch(screenX, screenY)

        if (isTouchInMap) map.changeSelection(mapTouchPosition)

        return true
    }

    override fun scrolled(amountX : Float, amountY : Float) : Boolean
    {
        //gameCamera.zoom += 0.1f * amountY

        changeFloor(currentFloor + amountY.toInt() * (-1))

        return true
    }
}
