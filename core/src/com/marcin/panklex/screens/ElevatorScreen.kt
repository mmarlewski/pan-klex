package com.marcin.panklex.screens

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.ScreenUtils
import com.marcin.panklex.BaseScreen
import com.marcin.panklex.PanKlexGame
import com.marcin.panklex.entities.EntityElevator

class ElevatorScreen(val name : String, val game : PanKlexGame) : BaseScreen(name, game)
{
    val elevatorLabel = Label("Elevator", Label.LabelStyle(BitmapFont(), Color.YELLOW))
    val cancelButton = ImageButton(TextureRegionDrawable(game.assetManager.get<Texture>("graphics/actions/cancel.png")))
    val floor1Label = Label("floor", Label.LabelStyle(BitmapFont(), Color.YELLOW))
    val floor1button = TextButton("go", TextButton.TextButtonStyle(null, null, null, BitmapFont()))
    val floor2Label = Label("floor", Label.LabelStyle(BitmapFont(), Color.YELLOW))
    val floor2button = TextButton("go", TextButton.TextButtonStyle(null, null, null, BitmapFont()))
    val floor3Label = Label("floor", Label.LabelStyle(BitmapFont(), Color.YELLOW))
    val floor3button = TextButton("go", TextButton.TextButtonStyle(null, null, null, BitmapFont()))
    val floor4Label = Label("floor", Label.LabelStyle(BitmapFont(), Color.YELLOW))
    val floor4button = TextButton("go", TextButton.TextButtonStyle(null, null, null, BitmapFont()))
    val floor5Label = Label("floor", Label.LabelStyle(BitmapFont(), Color.YELLOW))
    val floor5button = TextButton("go", TextButton.TextButtonStyle(null, null, null, BitmapFont()))

    var elevatorEntity : EntityElevator? = null

    init
    {
        cancelButton.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float) = game.changeScreen(game.screenGame)
        })

        floor1button.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float)
            {
                game.screenGame.playerPosition = elevatorEntity!!.exitPositions[0]
                game.screenGame.changeLevel(game.screenGame.playerPosition.z.toInt())
                game.screenGame.refreshMap()
                game.changeScreen(game.screenGame)
            }
        })

        floor2button.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float)
            {
                game.screenGame.playerPosition = elevatorEntity!!.exitPositions[1]
                game.screenGame.changeLevel(game.screenGame.playerPosition.z.toInt())
                game.screenGame.refreshMap()
                game.changeScreen(game.screenGame)
            }
        })

        floor3button.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float)
            {
                game.screenGame.playerPosition = elevatorEntity!!.exitPositions[2]
                game.screenGame.changeLevel(game.screenGame.playerPosition.z.toInt())
                game.screenGame.refreshMap()
                game.changeScreen(game.screenGame)
            }
        })

        floor4button.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float)
            {
                game.screenGame.playerPosition = elevatorEntity!!.exitPositions[3]
                game.screenGame.changeLevel(game.screenGame.playerPosition.z.toInt())
                game.screenGame.refreshMap()
                game.changeScreen(game.screenGame)
            }
        })

        floor5button.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float)
            {
                game.screenGame.playerPosition = elevatorEntity!!.exitPositions[4]
                game.screenGame.changeLevel(game.screenGame.playerPosition.z.toInt())
                game.screenGame.refreshMap()
                game.changeScreen(game.screenGame)
            }
        })

        val table = Table()
        table.center().setFillParent(true)
        table.defaults().pad(10f)
        table.add(elevatorLabel)
        table.add(cancelButton)
        table.row()
        table.add(floor1Label)
        table.add(floor1button)
        table.row()
        table.add(floor2Label)
        table.add(floor2button)
        table.row()
        table.add(floor3Label)
        table.add(floor3button)
        table.row()
        table.add(floor4Label)
        table.add(floor4button)
        table.row()
        table.add(floor5Label)
        table.add(floor5button)
        stage.addActor(table)
    }

    override fun show()
    {
        super.show()

        game.changeInputProcessor(stage)

        floor1Label.isVisible = false
        floor1button.isVisible = false
        floor2Label.isVisible = false
        floor2button.isVisible = false
        floor3Label.isVisible = false
        floor3button.isVisible = false
        floor4Label.isVisible = false
        floor4button.isVisible = false
        floor5Label.isVisible = false
        floor5button.isVisible = false

        if (elevatorEntity!!.elevatorPositions.size > 0)
        {
            floor1Label.isVisible = true
            floor1button.isVisible = true
            floor1Label.setText(elevatorEntity!!.floorNames[0])
            floor1button.setText("go")

            if (elevatorEntity!!.elevatorPositions.size > 1)
            {
                floor2Label.isVisible = true
                floor2button.isVisible = true
                floor2Label.setText(elevatorEntity!!.floorNames[1])
                floor2button.setText("go")

                if (elevatorEntity!!.elevatorPositions.size > 2)
                {
                    floor3Label.isVisible = true
                    floor3button.isVisible = true
                    floor3Label.setText(elevatorEntity!!.floorNames[2])
                    floor3button.setText("go")

                    if (elevatorEntity!!.elevatorPositions.size > 3)
                    {
                        floor4Label.isVisible = true
                        floor4button.isVisible = true
                        floor4Label.setText(elevatorEntity!!.floorNames[3])
                        floor4button.setText("go")

                        if (elevatorEntity!!.elevatorPositions.size > 4)
                        {
                            floor5Label.isVisible = true
                            floor5button.isVisible = true
                            floor5Label.setText(elevatorEntity!!.floorNames[4])
                            floor5button.setText("go")
                        }
                    }
                }
            }
        }
    }

    override fun render(delta : Float)
    {
        super.render(delta)

        ScreenUtils.clear(0f, 0f, 0f, 1f)
        camera.update()
        stage.draw()
    }
}
