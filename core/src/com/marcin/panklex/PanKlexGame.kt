package com.marcin.panklex

import com.badlogic.gdx.Game
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.assets.AssetManager
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.marcin.panklex.screens.*

class PanKlexGame : Game()
{
    lateinit var batch : SpriteBatch
    val assetManager = AssetManager()

    lateinit var screenMainMenu : ScreenMainMenu
    lateinit var screenGame : ScreenGame
    lateinit var containerScreen : ContainerScreen
    lateinit var vendingMachineScreen : VendingMachineScreen
    lateinit var elevatorScreen : ElevatorScreen
    lateinit var screenEndGame : ScreenEndGame

    fun exit()
    {
        Gdx.app.exit()
    }

    fun log(tag : String, message : String)
    {
        Gdx.app.log(tag, message)
    }

    fun changeScreen(screen : BaseScreen)
    {
        Gdx.input.inputProcessor = screen.stage
        setScreen(screen)
    }

    fun loadAssets()
    {
        assetManager.load("graphics/hud/heart.png", Texture::class.java)

        assetManager.load("graphics/actions/pickaxe.png", Texture::class.java)
        assetManager.load("graphics/actions/bomb.png", Texture::class.java)
        assetManager.load("graphics/actions/coin.png", Texture::class.java)
        assetManager.load("graphics/actions/cell.png", Texture::class.java)
        assetManager.load("graphics/actions/hand.png", Texture::class.java)
        assetManager.load("graphics/actions/walk.png", Texture::class.java)
        assetManager.load("graphics/actions/cancel.png", Texture::class.java)

        assetManager.load("graphics/entities/player.png", Texture::class.java)
        assetManager.load("graphics/entities/container.png", Texture::class.java)
        assetManager.load("graphics/entities/vendingMachine.png", Texture::class.java)
        assetManager.load("graphics/entities/teleporter.png", Texture::class.java)
        assetManager.load("graphics/entities/elevator.png", Texture::class.java)
        assetManager.load("graphics/entities/flag.png", Texture::class.java)
        assetManager.load("graphics/entities/stairs.png", Texture::class.java)
        assetManager.load("graphics/entities/poweredGate.png", Texture::class.java)

        assetManager.load("graphics/blocks/special.png", Texture::class.java)
        assetManager.load("graphics/blocks/example.png", Texture::class.java)
        assetManager.load("graphics/blocks/undamagedStone.png", Texture::class.java)
        assetManager.load("graphics/blocks/damagedStone.png", Texture::class.java)
        assetManager.load("graphics/blocks/brick.png", Texture::class.java)
        assetManager.load("graphics/blocks/metal.png", Texture::class.java)

        assetManager.finishLoading()
    }

    override fun create()
    {
        loadAssets()
        batch = SpriteBatch()
        screenMainMenu = ScreenMainMenu("screen MainMenu", this)
        screenGame = ScreenGame("screen Game", this)
        containerScreen = ContainerScreen("screen Container", this)
        vendingMachineScreen = VendingMachineScreen("screen VendingMachine", this)
        elevatorScreen = ElevatorScreen("screen Elevator", this)
        screenEndGame = ScreenEndGame("screen EndGame", this)
        changeScreen(screenMainMenu)
    }

    override fun dispose()
    {
        batch.dispose()
        assetManager.dispose()
        screenMainMenu.dispose()
        screenGame.dispose()
        containerScreen.dispose()
        vendingMachineScreen.dispose()
        elevatorScreen.dispose()
        screenEndGame.dispose()
    }
}