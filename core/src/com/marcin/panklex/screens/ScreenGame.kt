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
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.utils.viewport.FitViewport
import com.marcin.panklex.*
import kotlin.math.floor

class ScreenGame(name : String, val game : PanKlexGame) : BaseScreen(name, game), InputProcessor
{
    // main

    val inputMultiplexer = InputMultiplexer(stage, this)
    val gameCamera = OrthographicCamera()
    val gameViewport = FitViewport(screenResolution.x, screenResolution.y, gameCamera)
    val tiles = Tiles(game.assetManager)
    val level = Level()
    val room = Room(level)
    val map = Map(room, tiles)
    val pathfinding = Pathfinding(room)

    // dragging

    var isDragging = false
    var dragOrigin = Vector2()
    var dragDifference = Vector2()

    // mouse

    var isBeingTouched = false
    var isTouched = false
    var screenMousePosition = Vector2()
    var worldMousePosition = Vector2()
    var isMouseInMap = false
    var mapRelativeMousePosition = Vector3()
    var mapObjectiveMousePosition = Vector3()
    var levelMousePosition = Vector3()
    var newLevelMousePosition = Vector3()
    var wasLevelMousePositionChanged = false

    // other

    var currentFloor = 0
    var isMoving = false
    var isMoveAccessible = false

    // hud

    val currentDirectionLabel = Label("direction", Label.LabelStyle(BitmapFont(), Color.CYAN))
    val currentFloorLabel = Label("floor", Label.LabelStyle(BitmapFont(), Color.YELLOW))
    val isMovingLabel = Label("move", Label.LabelStyle(BitmapFont(), Color.GREEN))

    init
    {
        // hud

        val table = Table()
        table.top().setFillParent(true)
        table.defaults().pad(10f)
        table.add(currentDirectionLabel)
        table.add(currentFloorLabel)
        table.add(isMovingLabel)
        stage.addActor(table)

        // init

        level.createLevel()
        level.updateEntities()
        level.updateObjects()
        level.updateSideTransparency()
        level.updateGround()
        level.updateSpacesAboveAndBelow()
        level.clearSideVisibility()
        level.clearBorder()
        level.clearPathfinding()
        room.updateRoom(level.entityPlayer!!.playerPosition)
        room.updateEntityTiles(tiles, map.mapDirection)
        room.updateObjectTiles(tiles, map.mapDirection)
        room.updateLinesTiles(tiles, map.mapDirection)
        room.updateMoveTiles(tiles, map.mapDirection)
        room.updateSelectTiles(tiles, levelMousePosition, isMoving, pathfinding.isPathFound, isMoveAccessible)
        map.createMap()
        map.updateMap()
        changeDirection(map.mapDirection)
        changeFloor(0)
        changeIsMoving(false)

        //Gdx.graphics.setTitle("Level: \"${level.levelName}\"")

        gameCamera.position.set(
            (tileLengthHalf * map.mapWidth).toFloat(), (tileLengthQuarter * map.mapHeight).toFloat(), 0f)
        gameCamera.zoom = 0.5f
    }

    fun changeDirection(direction : Direction2d)
    {
        currentDirectionLabel.setText("$direction")
        map.changeMapDirection(direction)
        room.updateObjectTiles(tiles, map.mapDirection)
        room.updateLinesTiles(tiles, map.mapDirection)
        map.updateMap()
    }

    fun changeFloor(floor : Int)
    {
        if (floor in 0 until map.mapFloors)
        {
            currentFloorLabel.setText("floor: $floor")
            currentFloor = floor
        }
    }

    fun changeIsMoving(newIsMoving : Boolean)
    {
        when (newIsMoving)
        {
            true  -> isMovingLabel.setText("moving player")
            false -> isMovingLabel.setText("not moving")
        }

        isMoving = newIsMoving
    }

    fun updateIsMoveAccessible()
    {
        isMoveAccessible = false

        if (isMoving && pathfinding.isPathFound)
        {
            val levelMouseSpace = room.getSpace(levelMousePosition)

            if (levelMouseSpace != null && levelMouseSpace.isOnGround)
            {
                if (levelMouseSpace.isObjectOccupyingNullOrCanStoreEntity(levelMousePosition))
                {
                    isMoveAccessible = true
                }
            }
        }
    }

    fun updateMouse(screenX : Int, screenY : Int)
    {
        screenMousePosition.set(screenX.toFloat(), screenY.toFloat())
        worldMousePosition.set(screenMousePosition)
        gameViewport.unproject(worldMousePosition)

        updateMouse()
    }

    fun updateMouse()
    {
        mapRelativeMousePosition.x =
            floor(
                (0.5f * worldMousePosition.x - worldMousePosition.y + tileLengthQuarter) / tileLengthHalf) + (currentFloor + 1)
        mapRelativeMousePosition.y =
            floor(
                (0.5f * worldMousePosition.x + worldMousePosition.y - tileLengthQuarter) / tileLengthHalf) - (currentFloor + 1)
        mapRelativeMousePosition.z = currentFloor.toFloat()
        isMouseInMap =
            (mapRelativeMousePosition.x.toInt() in 0 until map.mapWidth && mapRelativeMousePosition.y.toInt() in 0 until map.mapHeight && mapRelativeMousePosition.z.toInt() in 0 until map.mapFloors)
        map.getObjectiveMapPosition(mapRelativeMousePosition, mapObjectiveMousePosition)
        map.getLevelPosition(mapObjectiveMousePosition, newLevelMousePosition)
        wasLevelMousePositionChanged = (!levelMousePosition.epsilonEquals(newLevelMousePosition))
        levelMousePosition.set(newLevelMousePosition)
    }

    fun screenLoop()
    {
        if (wasLevelMousePositionChanged)
        {
            if (isMoving)
            {
                level.clearPathfinding()
                pathfinding.findPath(level.entityPlayer!!.playerPosition, levelMousePosition)
                updateIsMoveAccessible()
                room.updateMoveTiles(tiles, map.mapDirection)
            }


            room.updateSelectTiles(tiles, levelMousePosition, isMoving, pathfinding.isPathFound, isMoveAccessible)
            map.updateMap()

            wasLevelMousePositionChanged = false
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
        for (k in 0 until map.mapFloors)
        {
            map.renderers[k].setView(gameCamera)
            map.renderers[k].renderAllLayers()
        }
        for (k in 0 until map.mapFloors)
        {
            map.renderers[k].setView(gameCamera)
            map.renderers[k].renderOutlineLayers()
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
            // direction
            Input.Keys.Q     ->
            {
                changeDirection(direction2dToLeft(map.mapDirection))
                updateMouse()
            }
            Input.Keys.E     ->
            {
                changeDirection(direction2dToRight(map.mapDirection))
                updateMouse()
            }
            // floor
            Input.Keys.W     ->
            {
                changeFloor(currentFloor + 1)
                updateMouse()
            }
            Input.Keys.S     ->
            {
                changeFloor(currentFloor - 1)
                updateMouse()
            }
            Input.Keys.A     ->
            {
                changeFloor(room.roomFloorStart)
                updateMouse()
            }
            Input.Keys.D     ->
            {
                changeFloor(room.roomFloorEnd)
                updateMouse()
            }
            Input.Keys.X     ->
            {
                changeIsMoving(!isMoving)
                when (isMoving)
                {
                    true  ->
                    {
                        level.clearPathfinding()
                        pathfinding.findPath(level.entityPlayer!!.playerPosition, levelMousePosition)
                        updateIsMoveAccessible()
                        room.updateMoveTiles(tiles, map.mapDirection)
                    }
                    false ->
                    {
                        level.clearPathfinding()
                        room.updateMoveTiles(tiles, map.mapDirection)
                    }
                }
                room.updateSelectTiles(tiles, levelMousePosition, isMoving, pathfinding.isPathFound, isMoveAccessible)
                map.updateMap()
            }
            // numbers
            Input.Keys.NUM_1 ->
            {
                changeFloor(1)
                updateMouse()
            }
            Input.Keys.NUM_2 ->
            {
                changeFloor(2)
                updateMouse()
            }
            Input.Keys.NUM_3 ->
            {
                changeFloor(3)
                updateMouse()
            }
            Input.Keys.NUM_4 ->
            {
                changeFloor(4)
                updateMouse()
            }
            Input.Keys.NUM_5 ->
            {
                changeFloor(5)
                updateMouse()
            }
            Input.Keys.NUM_6 ->
            {
                changeFloor(6)
                updateMouse()
            }
            Input.Keys.NUM_7 ->
            {
                changeFloor(7)
                updateMouse()
            }
            Input.Keys.NUM_8 ->
            {
                changeFloor(8)
                updateMouse()
            }
            Input.Keys.NUM_9 ->
            {
                changeFloor(9)
                updateMouse()
            }
            Input.Keys.NUM_0 ->
            {
                changeFloor(0)
                updateMouse()
            }
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
        isBeingTouched = true

        if (button == Input.Buttons.RIGHT)
        {
            updateMouse(screenX, screenY)

            updateIsMoveAccessible()

            if (isMoveAccessible)
            {
                level.changePlayerPosition(levelMousePosition)
                level.updateEntities()
                level.clearPathfinding()
                room.updateEntityTiles(tiles, map.mapDirection)
                room.updateMoveTiles(tiles, map.mapDirection)
                map.updateMap()
            }
        }

        return true
    }

    override fun touchUp(screenX : Int, screenY : Int, pointer : Int, button : Int) : Boolean
    {
        if (isBeingTouched)
        {
            isTouched = true
            isBeingTouched = false
        }

        isDragging = false

        return true
    }

    override fun touchDragged(screenX : Int, screenY : Int, pointer : Int) : Boolean
    {
        isBeingTouched = true

        updateMouse(screenX, screenY)

        if (isDragging)
        {
            dragDifference.x = worldMousePosition.x - gameCamera.position.x
            dragDifference.y = worldMousePosition.y - gameCamera.position.y
            gameCamera.position.x = dragOrigin.x - dragDifference.x
            gameCamera.position.y = dragOrigin.y - dragDifference.y
        }
        else
        {
            dragOrigin.set(worldMousePosition)
            isDragging = true
        }

        return true
    }

    override fun mouseMoved(screenX : Int, screenY : Int) : Boolean
    {
        updateMouse(screenX, screenY)

        return true
    }

    override fun scrolled(amountX : Float, amountY : Float) : Boolean
    {
        gameCamera.zoom += amountY * 0.1f

        return true
    }
}
