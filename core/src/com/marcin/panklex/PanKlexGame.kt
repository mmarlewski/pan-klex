package com.marcin.panklex

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.marcin.panklex.screens.ScreenEndGame
import com.marcin.panklex.screens.ScreenGame
import com.marcin.panklex.screens.ScreenMainMenu

class PanKlexGame : Game()
{
    lateinit var batch: SpriteBatch
    val assetManager = AssetManager()

    lateinit var screenMainMenu: ScreenMainMenu
    lateinit var screenGame: ScreenGame
    lateinit var screenEndGame: ScreenEndGame

    fun exit()
    {
        Gdx.app.exit()
    }

    fun log(tag: String, message: String)
    {
        Gdx.app.log(tag, message)
    }

    fun changeScreen(screen: BaseScreen)
    {
        Gdx.input.inputProcessor = screen.stage
        setScreen(screen)
    }

    fun loadAssets()
    {
        assetManager.load("tilesets/testTileset.png", Texture::class.java)
        assetManager.load("tilesets/holeTileset.png", Texture::class.java)
        assetManager.load("tilesets/baseTileset.png", Texture::class.java)
        assetManager.load("tilesets/wallTileset.png", Texture::class.java)
        assetManager.load("tilesets/coverTileset.png", Texture::class.java)
        assetManager.finishLoading()
    }

    override fun create()
    {
        loadAssets()
        batch = SpriteBatch()
        screenMainMenu = ScreenMainMenu("screen MainMenu", this)
        screenGame = ScreenGame("screen Game", this)
        screenEndGame = ScreenEndGame("screen EndGame", this)
        changeScreen(screenMainMenu)
    }

    override fun dispose()
    {
        batch.dispose()
        assetManager.dispose()
        screenMainMenu.dispose()
        screenGame.dispose()
        screenEndGame.dispose()
    }
}