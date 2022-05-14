package com.marcin.panklex.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Input
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.math.Vector2
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.utils.ScreenUtils
import com.marcin.panklex.BaseScreen
import com.marcin.panklex.PanKlexGame

class ScreenMainMenu(val name : String, val game : PanKlexGame) : BaseScreen(name, game)
{
    init
    {
        val titleLabel = Label("Pan Klex", Label.LabelStyle(BitmapFont(), Color.RED))
        val versionLabel = Label("0.0.0", Label.LabelStyle(BitmapFont(), Color.GREEN))
        val startButton = TextButton("Start", TextButton.TextButtonStyle(null, null, null, BitmapFont()))
        val exitButton = TextButton("Exit", TextButton.TextButtonStyle(null, null, null, BitmapFont()))

        startButton.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float) = game.changeScreen(game.screenGame)
        })

        exitButton.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float) = game.exit()
        })

        val table = Table()
        table.center().setFillParent(true)
        table.add(titleLabel).pad(10f).row()
        table.add(versionLabel).pad(10f).row()
        table.add(startButton).pad(10f).row()
        table.add(exitButton).pad(10f).row()
        stage.addActor(table)
    }

    override fun render(delta : Float)
    {
        super.render(delta)

        // input

        val screenMousePos = Vector2(Gdx.input.x.toFloat(), Gdx.input.y.toFloat())
        val isLeftButtonPressed = Gdx.input.isButtonPressed(0)
        val isRightButtonPressed = Gdx.input.isButtonPressed(1)
        val isLeftButtonJustPressed = Gdx.input.isButtonJustPressed(0)
        val isRightButtonJustPressed = Gdx.input.isButtonJustPressed(1)
        // drawing

        ScreenUtils.clear(0f, 0f, 0f, 1f)
        camera.update()
        stage.draw()
    }
}