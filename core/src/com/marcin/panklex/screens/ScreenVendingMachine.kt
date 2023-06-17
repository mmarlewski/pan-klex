package com.marcin.panklex.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.Image
import com.badlogic.gdx.scenes.scene2d.ui.Label
import com.badlogic.gdx.scenes.scene2d.ui.Table
import com.badlogic.gdx.scenes.scene2d.ui.TextButton
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.marcin.panklex.*
import com.marcin.panklex.entities.EntityPlayer
import com.marcin.panklex.objects.ObjectVendingMachine

class ScreenVendingMachinePart(val screenVendingMachine : ScreenVendingMachine, val playerItem : PlayerItem)
{
    val itemImage = Image(
        when (playerItem)
        {
            PlayerItem.Cell          -> screenVendingMachine.tiles.itemCell
            PlayerItem.Coin          -> screenVendingMachine.tiles.itemCoin
            PlayerItem.Pickaxe       -> screenVendingMachine.tiles.itemPickaxe
            PlayerItem.Gear          -> screenVendingMachine.tiles.itemGear
            PlayerItem.RopeArrow     -> screenVendingMachine.tiles.itemRopeArrow
            PlayerItem.PointingArrow -> screenVendingMachine.tiles.itemPointingArrow
        })
    val itemMachineLabel = Label("-", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val itemPlayerLabel = Label("-", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val itemTextBuyButton = TextButton("-", TextButton.TextButtonStyle(null, null, null, BitmapFont())).apply {
        this.addListener(object : ClickListener()
                         {
                             override fun clicked(event : InputEvent?, x : Float, y : Float)
                             {
                                 val machine = screenVendingMachine.objectVendingMachine
                                 val player = screenVendingMachine.entityPlayer

                                 if (machine != null && player != null)
                                 {
                                     when (machine.isVendingMachineBroken)
                                     {
                                         true  ->
                                         {
                                             if (machine.getVendingMachineItem(playerItem) > 0)
                                             {
                                                 machine.changeVendingMachineItem(playerItem, -1)
                                                 player.changePlayerItem(playerItem, 1)
                                             }
                                         }
                                         false ->
                                         {
                                             if (machine.getVendingMachineItem(playerItem) > 0)
                                             {
                                                 if (player.getPlayerItem(PlayerItem.Coin) > 0)
                                                 {
                                                     machine.changeVendingMachineItem(playerItem, -1)
                                                     player.changePlayerItem(playerItem, 1)
                                                     player.changePlayerItem(PlayerItem.Coin, -1)
                                                 }
                                             }
                                         }
                                     }

                                     screenVendingMachine.updateWidgets()
                                     screenVendingMachine.game.screenGame.updateItemLabels()
                                 }
                             }
                         })
    }
}

class ScreenVendingMachine(val name : String, val game : PanKlexGame) : BaseScreen(name, game)
{
    val tiles = game.screenGame.tiles

    var objectVendingMachine : ObjectVendingMachine? = null
    var entityPlayer : EntityPlayer? = null

    val textMachineLabel = Label("machine", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val textPlayerLabel = Label("player", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val coinImage = Image(tiles.itemCoin)
    val coinPlayerLabel = Label("-", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val textReturnTextButton = TextButton("RETURN", TextButton.TextButtonStyle(null, null, null, BitmapFont())).apply {
        this.addListener(object : ClickListener()
                         {
                             override fun clicked(event : InputEvent?, x : Float, y : Float)
                             {
                                 setVendingMachineAndPlayer(null, null)
                                 game.changeScreen(game.screenGame)
                             }
                         })
    }

    val screenVendingMachineParts = arrayOf(
        ScreenVendingMachinePart(this, PlayerItem.Cell),
        ScreenVendingMachinePart(this, PlayerItem.Pickaxe),
        ScreenVendingMachinePart(this, PlayerItem.Gear),
        ScreenVendingMachinePart(this, PlayerItem.RopeArrow),
        ScreenVendingMachinePart(this, PlayerItem.PointingArrow)
                                           )

    val table = Table().apply {
        this.center().setFillParent(true)
        this.defaults().pad(10f)
        this.add()
        this.add(textMachineLabel)
        this.add(textPlayerLabel)
        this.row()
        for (screenVendingMachinePart in screenVendingMachineParts)
        {
            this.add(screenVendingMachinePart.itemImage)
            this.add(screenVendingMachinePart.itemMachineLabel)
            this.add(screenVendingMachinePart.itemPlayerLabel)
            this.add(screenVendingMachinePart.itemTextBuyButton)
            this.row()
        }
        this.row()
        this.add(coinImage)
        this.add()
        this.add(coinPlayerLabel)
        this.row()
        this.add(textReturnTextButton)
    }

    init
    {
        stage.addActor(table)
    }

    fun setVendingMachineAndPlayer(machine : ObjectVendingMachine?, player : EntityPlayer?)
    {
        objectVendingMachine = machine
        entityPlayer = player
    }

    fun updateWidgets()
    {
        val machine = objectVendingMachine
        val player = entityPlayer

        if (machine != null && player != null)
        {
            coinPlayerLabel.setText(player.getPlayerItem(PlayerItem.Coin))

            for (screenVendingMachinePart in screenVendingMachineParts)
            {
                screenVendingMachinePart.itemMachineLabel.setText(
                    machine.getVendingMachineItem(screenVendingMachinePart.playerItem))
                screenVendingMachinePart.itemPlayerLabel.setText(player.getPlayerItem(screenVendingMachinePart.playerItem))
                screenVendingMachinePart.itemTextBuyButton.setText(
                    if (machine.isVendingMachineBroken) "TAKE"
                    else when (screenVendingMachinePart.playerItem)
                    {
                        PlayerItem.Cell          -> "BUY ( $itemCellPrice )"
                        PlayerItem.Coin          -> "BUY ( 0 )"
                        PlayerItem.Pickaxe       -> "BUY ( $itemPickaxePrice )"
                        PlayerItem.Gear          -> "BUY ( $itemGearPrice )"
                        PlayerItem.RopeArrow     -> "BUY ( $itemRopeArrowPrice )"
                        PlayerItem.PointingArrow -> "BUY ( $itemPointingArrowPrice )"
                    })
            }
        }
    }

    override fun show()
    {
        super.show()
        game.changeInputProcessor(stage)
    }

    override fun render(delta : Float)
    {
        super.render(delta)

        Gdx.gl.glClearColor(0f, 0f, 0f, 0f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        camera.update()
        stage.draw()
    }
}
