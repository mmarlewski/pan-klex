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
        assetManager.load("images/actions.png", Texture::class.java)

        assetManager.load("tiles/entities/player.png", Texture::class.java)
        assetManager.load("tiles/entities/container.png", Texture::class.java)
        assetManager.load("tiles/entities/vendingMachine.png", Texture::class.java)
        assetManager.load("tiles/entities/teleporter.png", Texture::class.java)
        assetManager.load("tiles/entities/elevator.png", Texture::class.java)
        assetManager.load("tiles/entities/flag.png", Texture::class.java)
        assetManager.load("tiles/entities/stairs.png", Texture::class.java)
        assetManager.load("tiles/entities/poweredGate.png", Texture::class.java)

        assetManager.load("tiles/special.png", Texture::class.java)
        assetManager.load("tiles/cover.png", Texture::class.java)

        assetManager.load("tiles/example/hole.png", Texture::class.java)
        assetManager.load("tiles/example/base.png", Texture::class.java)
        assetManager.load("tiles/example/wall.png", Texture::class.java)

        assetManager.load("tiles/undamagedStone/hole.png", Texture::class.java)
        assetManager.load("tiles/undamagedStone/base.png", Texture::class.java)
        assetManager.load("tiles/undamagedStone/wall.png", Texture::class.java)

        assetManager.load("tiles/damagedStone/hole.png", Texture::class.java)
        assetManager.load("tiles/damagedStone/base.png", Texture::class.java)
        assetManager.load("tiles/damagedStone/wall.png", Texture::class.java)

        assetManager.load("tiles/brick/hole.png", Texture::class.java)
        assetManager.load("tiles/brick/base.png", Texture::class.java)
        assetManager.load("tiles/brick/wall.png", Texture::class.java)

        assetManager.load("tiles/metal/hole.png", Texture::class.java)
        assetManager.load("tiles/metal/base.png", Texture::class.java)
        assetManager.load("tiles/metal/wall.png", Texture::class.java)

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