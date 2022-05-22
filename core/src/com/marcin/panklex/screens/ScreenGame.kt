package com.marcin.panklex.screens

import com.badlogic.gdx.InputMultiplexer
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.utils.ScreenUtils
import com.badlogic.gdx.utils.viewport.FitViewport
import com.marcin.panklex.*

class ScreenGame(val name : String, val game : PanKlexGame) : BaseScreen(name, game), InputProcessor
{
    val inputMultiplexer = InputMultiplexer(stage, this)
    val mapCamera = OrthographicCamera()
    val mapViewport = FitViewport(1080f, 720f, mapCamera)

    override fun show()
    {
        super.show()
        game.changeInputProcessor(inputMultiplexer)
    }

    override fun render(delta : Float)
    {
        super.render(delta)

        ScreenUtils.clear(0f, 0f, 0f, 1f)
        camera.update()
        mapCamera.update()
        //map.renderer.setView(mapCamera)
        //map.renderer.render()
        stage.draw()
    }

    override fun resize(width : Int, height : Int)
    {
        super.resize(width, height)
        mapViewport.update(width, height)
    }

    override fun dispose()
    {
        super.dispose()
    }

    override fun keyDown(keycode : Int) : Boolean
    {
        return false
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
        return false
    }

    override fun touchUp(screenX : Int, screenY : Int, pointer : Int, button : Int) : Boolean
    {
        return false
    }

    override fun touchDragged(screenX : Int, screenY : Int, pointer : Int) : Boolean
    {
        return false
    }

    override fun mouseMoved(screenX : Int, screenY : Int) : Boolean
    {
        return false
    }

    override fun scrolled(amountX : Float, amountY : Float) : Boolean
    {
        return false
    }
}
