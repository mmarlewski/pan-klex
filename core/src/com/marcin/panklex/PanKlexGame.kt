package com.marcin.panklex

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.marcin.panklex.screens.*

class PanKlexGame : Game()
{
    lateinit var batch : SpriteBatch
    val assetManager = AssetManager()

    lateinit var screenGame : ScreenGame

    fun exit()
    {
        Gdx.app.exit()
    }

    fun log(tag : String, message : String)
    {
        Gdx.app.log(tag, message)
    }

    fun changeInputProcessor(inputProcessor : InputProcessor)
    {
        Gdx.input.inputProcessor = inputProcessor
    }

    fun changeScreen(screen : BaseScreen)
    {
        setScreen(screen)
    }

    fun loadAssets()
    {
        assetManager.load("iso.png", Texture::class.java)

        assetManager.finishLoading()
    }

    override fun create()
    {
        loadAssets()
        batch = SpriteBatch()
        screenGame = ScreenGame("screen Game", this)
        changeScreen(screenGame)
    }

    override fun dispose()
    {
        batch.dispose()
        assetManager.dispose()
        screenGame.dispose()
    }
}