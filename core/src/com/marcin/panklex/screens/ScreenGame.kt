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
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.viewport.FitViewport
import com.marcin.panklex.*
import kotlin.math.floor

class ScreenGame(val name : String, val game : PanKlexGame) : BaseScreen(name, game), InputProcessor
{
    // technical

    val inputMultiplexer = InputMultiplexer(stage, this)
    val gameCamera = OrthographicCamera()
    val gameViewport = FitViewport(screenResolution.x, screenResolution.y, gameCamera)

    // main

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
    var isLevelMousePositionFixed = false
    var newLevelMousePosition = Vector3()
    var wasLevelMousePositionChanged = false
    var mouseObject : Object? = null

    // other

    var currentFloor = 0
    var isMoving = false
    var isMoveAccessible = false
    val actionArray = Array<Action?>(5) { null }

    // hud

    val currentDirectionLabel = Label("direction", Label.LabelStyle(BitmapFont(), Color.CYAN))
    val currentFloorLabel = Label("floor", Label.LabelStyle(BitmapFont(), Color.YELLOW))
    val isMovingLabel = Label("move", Label.LabelStyle(BitmapFont(), Color.GREEN))

    val itemCellImage = Image(tiles.itemCell)
    val itemCellLabel = Label("-", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val itemCoinImage = Image(tiles.itemCoin)
    val itemCoinLabel = Label("-", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val itemPickaxeImage = Image(tiles.itemPickaxe)
    val itemPickaxeLabel = Label("-", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val itemGearImage = Image(tiles.itemGear)
    val itemGearLabel = Label("-", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val itemRopeArrowImage = Image(tiles.itemRopeArrow)
    val itemRopeArrowLabel = Label("-", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val itemPointingArrowImage = Image(tiles.itemPointingArrow)
    val itemPointingArrowLabel = Label("-", Label.LabelStyle(BitmapFont(), Color.PURPLE))

    val mouseObjectTypeLabel = Label("type", Label.LabelStyle(BitmapFont(), Color.CYAN))
    val levelMousePositionLabel = Label("position", Label.LabelStyle(BitmapFont(), Color.BLUE))
    val actionTitleTextButtons = Array(5) { TextButton("title", TextButton.TextButtonStyle(null, null, null, BitmapFont())) }
    val actionDescriptionLabels = Array(5) { Label("description", Label.LabelStyle(BitmapFont(), Color.RED)) }

    val table = Table().apply {
        this.top().left().setFillParent(true)
        this.defaults().pad(5f)
        this.add(currentDirectionLabel)
        this.row()
        this.add(currentFloorLabel)
        this.row()
        this.add(isMovingLabel)
        this.row()
        this.add(itemCellImage)
        this.add(itemCellLabel)
        this.row()
        this.add(itemCoinImage)
        this.add(itemCoinLabel)
        this.row()
        this.add(itemPickaxeImage)
        this.add(itemPickaxeLabel)
        this.row()
        this.add(itemGearImage)
        this.add(itemGearLabel)
        this.row()
        this.add(itemRopeArrowImage)
        this.add(itemRopeArrowLabel)
        this.row()
        this.add(itemPointingArrowImage)
        this.add(itemPointingArrowLabel)
        this.row()
        this.add(levelMousePositionLabel)
        this.row()
        this.add(mouseObjectTypeLabel)
        this.row()
        this.add(actionTitleTextButtons[0])
        this.row()
        this.add(actionDescriptionLabels[0])
        this.row()
        this.add(actionTitleTextButtons[1])
        this.row()
        this.add(actionDescriptionLabels[1])
        this.row()
        this.add(actionTitleTextButtons[2])
        this.row()
        this.add(actionDescriptionLabels[2])
        this.row()
        this.add(actionTitleTextButtons[3])
        this.row()
        this.add(actionDescriptionLabels[3])
        this.row()
        this.add(actionTitleTextButtons[4])
        this.row()
        this.add(actionDescriptionLabels[4])
    }

    init
    {
        stage.addActor(table)

        // main

        level.createLevel()
        level.updateEntities()
        level.updateObjects()
        level.updateSideTransparency()
        level.updateGround()
        level.updateSpacesAboveAndBelow()
        level.clearSideVisibility()
        level.clearBorder()
        level.clearPathfinding()
        room.updateRoom(level.entityPlayer.playerPosition)
        room.updateEntityTiles(tiles)
        room.updateObjectTiles(tiles, map.mapDirection)
        room.updateLinesTiles(tiles, map.mapDirection)
        room.updateMoveTiles(tiles, map.mapDirection)
        room.updateSelectTiles(tiles, levelMousePosition, isMoving, pathfinding.isPathFound, isMoveAccessible)
        map.createMap()
        map.updateMap()

        changeDirection(map.mapDirection)
        changeFloor(0)
        changeIsMoving(false)
        updateItemLabels()
        updateLevelMousePositionLabel()
        updateMouseObjectLabel()
        updateActionLabels()

        gameCamera.position.set(
            (tileLengthHalf * map.mapWidth).toFloat(), (tileLengthQuarter * map.mapHeight).toFloat(), 0f)
        gameCamera.zoom = 0.5f
    }

    fun changeDirection(direction : Direction2d)
    {
        currentDirectionLabel.setText("$direction")
        for (i in actionArray.indices)
        {
            actionArray[i] = null
        }
        updateActionLabels()
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

    fun updateItemLabels()
    {
        itemCellLabel.setText(level.entityPlayer.playerItems[PlayerItem.Cell].toString())
        itemCoinLabel.setText(level.entityPlayer.playerItems[PlayerItem.Coin].toString())
        itemPickaxeLabel.setText(level.entityPlayer.playerItems[PlayerItem.Pickaxe].toString())
        itemGearLabel.setText(level.entityPlayer.playerItems[PlayerItem.Gear].toString())
        itemRopeArrowLabel.setText(level.entityPlayer.playerItems[PlayerItem.RopeArrow].toString())
        itemPointingArrowLabel.setText(level.entityPlayer.playerItems[PlayerItem.PointingArrow].toString())
    }

    fun updateLevelMousePositionLabel()
    {
        levelMousePositionLabel.setText(levelMousePosition.toString())
    }

    fun updateMouseObjectLabel()
    {
        val mouseObj = mouseObject

        if (mouseObj != null)
        {
            mouseObjectTypeLabel.setText(mouseObj.objectType)
        }
        else
        {
            mouseObjectTypeLabel.setText("no object")
        }
    }

    fun updateActionLabels()
    {
        for (i in actionArray.indices)
        {
            actionTitleTextButtons[i].listeners.clear()

            val action = actionArray[i]

            if (action != null)
            {
                val screenGame = this

                actionTitleTextButtons[i].isVisible = true
                actionTitleTextButtons[i].setText(action.actionTitle)
                if (action.isActionPossible)
                {
                    actionTitleTextButtons[i].addListener(object : ClickListener()
                                                          {
                                                              override fun clicked(event : InputEvent?, x : Float, y : Float)
                                                              {
                                                                  action.performAction(screenGame)
                                                                  for (j in actionArray.indices)
                                                                  {
                                                                      actionArray[j] = null
                                                                  }
                                                                  updateActionLabels()
                                                                  isLevelMousePositionFixed = false
                                                              }
                                                          })
                }
                actionDescriptionLabels[i].isVisible = true
                actionDescriptionLabels[i].setText(action.actionDescription)
                actionDescriptionLabels[i].style.fontColor = if (action.isActionPossible) Color.GREEN else Color.RED
            }
            else
            {
                actionTitleTextButtons[i].isVisible = false
                actionDescriptionLabels[i].isVisible = false
            }
        }
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
        // mapRelativeMousePosition

        mapRelativeMousePosition.x =
            floor(
                (0.5f * worldMousePosition.x - worldMousePosition.y + tileLengthQuarter) / tileLengthHalf) + (currentFloor + 1)
        mapRelativeMousePosition.y =
            floor(
                (0.5f * worldMousePosition.x + worldMousePosition.y - tileLengthQuarter) / tileLengthHalf) - (currentFloor + 1)
        mapRelativeMousePosition.z = currentFloor.toFloat()

        // the rest

        isMouseInMap =
            (mapRelativeMousePosition.x.toInt() in 0 until map.mapWidth && mapRelativeMousePosition.y.toInt() in 0 until map.mapHeight && mapRelativeMousePosition.z.toInt() in 0 until map.mapFloors)
        map.getObjectiveMapPosition(mapRelativeMousePosition, mapObjectiveMousePosition)
        map.getLevelPosition(mapObjectiveMousePosition, newLevelMousePosition)
        wasLevelMousePositionChanged = levelMousePosition != newLevelMousePosition
        levelMousePosition.set(newLevelMousePosition)
        mouseObject = room.getSpace(levelMousePosition)?.objectOccupying
    }

    fun screenLoop()
    {
        if (wasLevelMousePositionChanged)
        {
            updateLevelMousePositionLabel()
            updateMouseObjectLabel()

            if (isMoving)
            {
                level.clearPathfinding()
                pathfinding.findPath(level.entityPlayer.playerPosition, levelMousePosition)
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
            Input.Keys.Q     -> changeDirection(direction2dToLeft(map.mapDirection))
            Input.Keys.E     -> changeDirection(direction2dToRight(map.mapDirection))

            // floor
            Input.Keys.W     -> changeFloor(currentFloor + 1)
            Input.Keys.S     -> changeFloor(currentFloor - 1)
            Input.Keys.A     -> changeFloor(room.roomFloorStart)
            Input.Keys.D     -> changeFloor(room.roomFloorEnd)

            // move
            Input.Keys.X     ->
            {
                isLevelMousePositionFixed = false
                changeIsMoving(!isMoving)
                updateMouse()
                level.clearPathfinding()
                if (isMoving)
                {
                    pathfinding.findPath(level.entityPlayer.playerPosition, levelMousePosition)
                    updateIsMoveAccessible()
                }
                room.updateMoveTiles(tiles, map.mapDirection)
                room.updateSelectTiles(tiles, levelMousePosition, isMoving, pathfinding.isPathFound, isMoveAccessible)
                map.updateMap()
            }

            // numbers
            Input.Keys.NUM_1 -> changeFloor(1)
            Input.Keys.NUM_2 -> changeFloor(2)
            Input.Keys.NUM_3 -> changeFloor(3)
            Input.Keys.NUM_4 -> changeFloor(4)
            Input.Keys.NUM_5 -> changeFloor(5)
            Input.Keys.NUM_6 -> changeFloor(6)
            Input.Keys.NUM_7 -> changeFloor(7)
            Input.Keys.NUM_8 -> changeFloor(8)
            Input.Keys.NUM_9 -> changeFloor(9)
            Input.Keys.NUM_0 -> changeFloor(0)
        }

        updateMouse()

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

        when (button)
        {
            Input.Buttons.LEFT  ->
            {
                when (isMoving)
                {
                    false ->
                    {
                        updateMouse(screenX, screenY)
                        isLevelMousePositionFixed = true
                        for (i in actionArray.indices)
                        {
                            actionArray[i] = null
                        }
                        mouseObject?.getActions(actionArray, levelMousePosition)
                        updateActionLabels()
                    }
                }
            }
            Input.Buttons.RIGHT ->
            {
                when (isMoving)
                {
                    true  ->
                    {
                        updateMouse(screenX, screenY)
                        updateIsMoveAccessible()

                        if (isMoveAccessible)
                        {
                            level.changePlayerPosition(levelMousePosition)
                            level.updateEntities()
                            level.clearPathfinding()
                            room.updateEntityTiles(tiles)
                            room.updateMoveTiles(tiles, map.mapDirection)
                            map.updateMap()
                        }
                    }
                    false ->
                    {
                        if (isLevelMousePositionFixed)
                        {
                            isLevelMousePositionFixed = false
                            for (i in actionArray.indices)
                            {
                                actionArray[i] = null
                            }
                            updateActionLabels()
                            updateMouse()
                        }
                    }
                }
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
        if (isMoving || !isLevelMousePositionFixed)
        {
            updateMouse(screenX, screenY)
        }

        return true
    }

    override fun scrolled(amountX : Float, amountY : Float) : Boolean
    {
        gameCamera.zoom += amountY * 0.1f

        return true
    }
}
