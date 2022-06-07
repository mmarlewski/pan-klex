package com.marcin.panklex.screens

import com.badlogic.gdx.Input
import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.Color
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

class ScreenGame(val name : String, val game : PanKlexGame) : BaseScreen(name, game), InputProcessor
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

    // hud

    val leftArrowButton = TextButton("<----", TextButton.TextButtonStyle(null, null, null, BitmapFont()))
    val currentDirectionLabel = Label("Up", Label.LabelStyle(BitmapFont(), Color.CYAN))
    val currentFloorLabel = Label("floor", Label.LabelStyle(BitmapFont(), Color.GREEN))
    val rightArrowButton = TextButton("---->", TextButton.TextButtonStyle(null, null, null, BitmapFont()))

    init
    {
        // hud

        leftArrowButton.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float)
            {
                map.changeMapDirectionLeft()
                currentDirectionLabel.setText(map.direction.toString())
            }
        })
        rightArrowButton.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float)
            {
                map.changeMapDirectionRight()
                currentDirectionLabel.setText(map.direction.toString())
            }
        })

        val table = Table()
        table.top().setFillParent(true)
        table.defaults().pad(10f)
        table.add(leftArrowButton)
        table.add(currentDirectionLabel)
        table.add(currentFloorLabel)
        table.add(rightArrowButton)
        stage.addActor(table)

        // init

        level.createLevel("level.json")
        room.updateRoom(level.positions[1])
        map.createMap()
        map.updateMap()
        changeFloor(9)

        gameCamera.position.set(160f, 80f, 0f)
        gameCamera.zoom = 0.5f
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
        gameCamera.update()
        map.renderer.setView(gameCamera)
        map.renderer.render()
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
        screenTouchPosition.set(screenX.toFloat(), screenY.toFloat())
        worldTouchPosition = gameViewport.unproject(screenTouchPosition)
        mapTouchPosition.x =
            floor((0.5f * worldTouchPosition.x - worldTouchPosition.y + 8) / 16) + (currentFloor + 1)
        mapTouchPosition.y =
            floor((0.5f * worldTouchPosition.x + worldTouchPosition.y - 8) / 16) - (currentFloor + 1)
        mapTouchPosition.z = currentFloor.toFloat()
        isTouchInMap =
            (mapTouchPosition.x >= 0 && mapTouchPosition.y >= 0 && mapTouchPosition.x < level.width && mapTouchPosition.y < level.height)

        if (isTouchInMap)
        {
            map.getRoomPosition(mapTouchPosition, roomTouchPosition)
            map.clearMap()
            map.updateMap()
            map.changeSelection(mapTouchPosition)
        }

        return true
    }

    override fun scrolled(amountX : Float, amountY : Float) : Boolean
    {
        gameCamera.zoom += 0.1f * amountY

        return true
    }
}
