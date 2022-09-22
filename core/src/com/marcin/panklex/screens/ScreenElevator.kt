package com.marcin.panklex.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.marcin.panklex.BaseScreen
import com.marcin.panklex.PanKlexGame
import com.marcin.panklex.entities.EntityPlayer
import com.marcin.panklex.objects.*

class ScreenElevatorPart(val screenElevator : ScreenElevator, var elevatorFloor : ElevatorFloor?, var isAccessible : Boolean)
{
    val accessibilityLabel = Label("-", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val floorNameLabel = Label("-", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val floorNumberLabel = Label("-", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val textGoTextButton = TextButton("RETURN", TextButton.TextButtonStyle(null, null, null, BitmapFont())).apply {
        this.addListener(object : ClickListener()
                         {
                             override fun clicked(event : InputEvent?, x : Float, y : Float)
                             {
                                 val floor = elevatorFloor
                                 val elevator = screenElevator.objectElevator
                                 val player = screenElevator.entityPlayer

                                 if (floor != null && elevator != null && player != null)
                                 {
                                     if (floor.isAccessible)
                                     {
                                         val screenGame = screenElevator.game.screenGame

                                         player.setPosition(floor.elevatorPart.partPosition)
                                         elevator.changeCurrentFloor(floor)

                                         screenGame.room.updateObjectTiles(screenGame.tiles, screenGame.map.mapDirection)
                                         screenGame.map.updateMap()
                                         screenGame.level.updateEntities()
                                         screenGame.room.updateEntityTiles(screenGame.tiles)
                                         screenGame.map.updateMap()
                                         screenGame.game.changeScreen(screenElevator.game.screenGame)
                                     }
                                 }
                             }
                         })
    }
}

class ScreenElevator(val name : String, val game : PanKlexGame) : BaseScreen(name, game)
{
    val screenElevatorParts = Array(10) { ScreenElevatorPart(this, null, false) }

    val textReturnTextButton = TextButton("RETURN", TextButton.TextButtonStyle(null, null, null, BitmapFont())).apply {
        this.addListener(object : ClickListener()
                         {
                             override fun clicked(event : InputEvent?, x : Float, y : Float)
                             {
                                 setElevatorAndPlayer(null, null)
                                 game.changeScreen(game.screenGame)
                             }
                         })
    }

    var objectElevator : ObjectElevator? = null
    var entityPlayer : EntityPlayer? = null

    val table = Table().apply {
        this.center().setFillParent(true)
        this.defaults().pad(10f)
        for (screenElevatorPart in screenElevatorParts.reversed())
        {
            this.add(screenElevatorPart.accessibilityLabel)
            this.add(screenElevatorPart.floorNameLabel)
            this.add(screenElevatorPart.floorNumberLabel)
            this.add(screenElevatorPart.textGoTextButton)
            this.row()
        }
        this.add(textReturnTextButton)
    }

    init
    {
        stage.addActor(table)
    }

    fun setElevatorAndPlayer(elevator : ObjectElevator?, player : EntityPlayer?)
    {
        objectElevator = elevator
        entityPlayer = player
    }

    fun updateWidgets()
    {
        // clear

        for (screenElevatorPart in screenElevatorParts)
        {
            screenElevatorPart.elevatorFloor = null
        }

        // set elevator parts

        val elevator = objectElevator

        if (elevator != null)
        {
            for (i in screenElevatorParts.indices)
            {
                val screenElevatorPart = screenElevatorParts[i]

                if (elevator.elevatorFloors.size > i)
                {
                    screenElevatorPart.elevatorFloor = elevator.elevatorFloors[i]
                }
                else
                {
                    screenElevatorPart.elevatorFloor = null
                }
            }
        }

        // set widgets

        for (screenElevatorPart in screenElevatorParts)
        {
            val elevatorFloor = screenElevatorPart.elevatorFloor

            if (elevatorFloor!=null)
            {
                screenElevatorPart.accessibilityLabel.setText(if (elevatorFloor.isAccessible) "V" else "X")
                screenElevatorPart.floorNameLabel.setText(elevatorFloor.floorName)
                screenElevatorPart.floorNumberLabel.setText(elevatorFloor.floorNumber)
                screenElevatorPart.textGoTextButton.setText(if (elevatorFloor.isAccessible) "GO" else "-")
            }
            else
            {
                screenElevatorPart.accessibilityLabel.setText("")
                screenElevatorPart.floorNameLabel.setText("")
                screenElevatorPart.floorNumberLabel.setText("")
                screenElevatorPart.textGoTextButton.setText("")
            }
        }
    }

    override fun show()
    {
        super.show()
        game.changeInputProcessor(stage)
    }

    override fun render(delta : Float)
    {
        super.render(delta)

        Gdx.gl.glClearColor(0f, 0f, 0f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.update()
        stage.draw()
    }
}
