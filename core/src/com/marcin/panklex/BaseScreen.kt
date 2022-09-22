package com.marcin.panklex

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport

abstract class BaseScreen(val baseName : String, val baseGame : PanKlexGame) : Screen
{
    val camera = OrthographicCamera()
    val viewport = FitViewport(screenResolution.x, screenResolution.y, camera)
    val stage = Stage(viewport, baseGame.spriteBatch)

    override fun show()
    {
        Gdx.app.log(baseName, "show")
    }

    override fun render(delta : Float)
    {
    }

    override fun resize(width : Int, height : Int)
    {
        Gdx.app.log(baseName, "resize")
        viewport.update(width, height, true)
    }

    override fun pause()
    {
        Gdx.app.log(baseName, "pause")
    }

    override fun resume()
    {
        Gdx.app.log(baseName, "resume")
    }

    override fun hide()
    {
        Gdx.app.log(baseName, "hide")
    }

    override fun dispose()
    {
        Gdx.app.log(baseName, "dispose")
        stage.dispose()
    }
}
