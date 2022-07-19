package com.marcin.panklex

import com.badlogic.gdx.Screen
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.scenes.scene2d.Stage
import com.badlogic.gdx.utils.viewport.FitViewport
import com.badlogic.gdx.utils.viewport.ScreenViewport

abstract class BaseScreen(val baseName : String, val baseGame : PanKlexGame) : Screen
{
    val camera = OrthographicCamera()
    val viewport = FitViewport(1080f, 720f, camera)
    val stage by lazy { Stage(viewport, baseGame.batch) }

    override fun show()
    {
        baseGame.log(baseName, "show")
    }

    override fun render(delta : Float)
    {
    }

    override fun resize(width : Int, height : Int)
    {
        baseGame.log(baseName, "resize")
        //viewport.update(width, height, true)
    }

    override fun pause()
    {
        baseGame.log(baseName, "pause")
    }

    override fun resume()
    {
        baseGame.log(baseName, "resume")
    }

    override fun hide()
    {
        baseGame.log(baseName, "hide")
    }

    override fun dispose()
    {
        baseGame.log(baseName, "dispose")
        stage.dispose()
    }
}
