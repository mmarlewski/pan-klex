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
    lateinit var spriteBatch : SpriteBatch
    lateinit var assetManager : AssetManager

    lateinit var screenGame : ScreenGame
    lateinit var screenContainer : ScreenContainer
    lateinit var screenVendingMachine : ScreenVendingMachine
    lateinit var screenElevator : ScreenElevator
    lateinit var screenStation : ScreenStation

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
        assetManager = AssetManager()

        assetManager.load("graphics/other/select.png", Texture::class.java)
        assetManager.load("graphics/other/lines.png", Texture::class.java)
        assetManager.load("graphics/other/groundMove.png", Texture::class.java)
        assetManager.load("graphics/other/fallMove.png", Texture::class.java)
        assetManager.load("graphics/other/dist2JumpMove.png", Texture::class.java)
        assetManager.load("graphics/other/dist3JumpMove.png", Texture::class.java)
        assetManager.load("graphics/other/items.png", Texture::class.java)
        assetManager.load("graphics/other/upgrades.png", Texture::class.java)

        assetManager.load("graphics/entities/player.png", Texture::class.java)

        assetManager.load("graphics/objects/block.png", Texture::class.java)
        assetManager.load("graphics/objects/column.png", Texture::class.java)
        assetManager.load("graphics/objects/halfColumn.png", Texture::class.java)
        assetManager.load("graphics/objects/arch.png", Texture::class.java)
        assetManager.load("graphics/objects/halfArch.png", Texture::class.java)
        assetManager.load("graphics/objects/stairs.png", Texture::class.java)
        assetManager.load("graphics/objects/vendingMachine.png", Texture::class.java)
        assetManager.load("graphics/objects/teleporter.png", Texture::class.java)
        assetManager.load("graphics/objects/switch.png", Texture::class.java)
        assetManager.load("graphics/objects/station.png", Texture::class.java)
        assetManager.load("graphics/objects/elevator.png", Texture::class.java)
        assetManager.load("graphics/objects/chest.png", Texture::class.java)
        assetManager.load("graphics/objects/door.png", Texture::class.java)
        assetManager.load("graphics/objects/poweredDoor.png", Texture::class.java)
        assetManager.load("graphics/objects/ladder.png", Texture::class.java)
        assetManager.load("graphics/objects/extendableLadder.png", Texture::class.java)
        assetManager.load("graphics/objects/bridge.png", Texture::class.java)
        assetManager.load("graphics/objects/extendableBridge.png", Texture::class.java)
        assetManager.load("graphics/objects/rope.png", Texture::class.java)

        assetManager.finishLoading()
    }

    override fun create()
    {
        loadAssets()
        spriteBatch = SpriteBatch()
        screenGame = ScreenGame("game", this)
        screenContainer = ScreenContainer("container", this)
        screenVendingMachine = ScreenVendingMachine("vending machine", this)
        screenElevator = ScreenElevator("elevator", this)
        screenStation = ScreenStation("station", this)
        changeScreen(screenGame)
    }

    override fun dispose()
    {
        spriteBatch.dispose()
        assetManager.dispose()

        screenGame.dispose()
        screenContainer.dispose()
        screenVendingMachine.dispose()
        screenElevator.dispose()
        screenStation.dispose()
    }
}
