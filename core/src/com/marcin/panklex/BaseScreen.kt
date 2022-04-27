package com.marcin.panklex

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.ScreenViewport

abstract class BaseScreen(val baseName: String, val baseGame: PanKlexGame) : Screen
{
    val camera = OrthographicCamera()
    val viewport = ScreenViewport(camera)
    val stage by lazy { Stage(viewport, baseGame.batch) }

    override fun show()
    {
        Gdx.app.log("show", baseName)
    }

    override fun render(delta: Float)
    {
    }

    override fun resize(width: Int, height: Int)
    {
        Gdx.app.log("resize", baseName)
        viewport.update(width, height,true)
    }

    override fun pause()
    {
        Gdx.app.log("pause", baseName)
    }

    override fun resume()
    {
        Gdx.app.log("resume", baseName)
    }

    override fun hide()
    {
        Gdx.app.log("hide", baseName)
    }

    override fun dispose()
    {
        Gdx.app.log("dispose", baseName)
        stage.dispose()
    }
}