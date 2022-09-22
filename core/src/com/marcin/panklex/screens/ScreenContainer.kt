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
import com.marcin.panklex.BaseScreen
import com.marcin.panklex.PanKlexGame
import com.marcin.panklex.PlayerItem
import com.marcin.panklex.Tiles
import com.marcin.panklex.entities.EntityPlayer
import com.marcin.panklex.objects.ObjectChest

class ScreenContainer(val name : String, val game : PanKlexGame) : BaseScreen(name, game)
{
    val tiles = game.screenGame.tiles

    var objectChest : ObjectChest? = null
    var entityPlayer : EntityPlayer? = null

    val textChestLabel = Label("chest", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val textPlayerLabel = Label("player", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val textReturnTextButton = TextButton("RETURN", TextButton.TextButtonStyle(null, null, null, BitmapFont())).apply {
        this.addListener(object : ClickListener()
                         {
                             override fun clicked(event : InputEvent?, x : Float, y : Float)
                             {
                                 setChestAndPlayer(null, null)
                                 game.changeScreen(game.screenGame)
                             }
                         })
    }
    val textTakeAllTextButton = TextButton("TAKE ALL", TextButton.TextButtonStyle(null, null, null, BitmapFont())).apply {
        this.addListener(object : ClickListener()
                         {
                             override fun clicked(event : InputEvent?, x : Float, y : Float)
                             {
                                 val chest = objectChest
                                 val player = entityPlayer

                                 if (chest != null && player != null)
                                 {
                                     for (item in PlayerItem.values())
                                     {
                                         player.changePlayerItem(item, chest.getChestItem(item))
                                         chest.changeChestItem(item, (-1) * chest.getChestItem(item))
                                     }

                                     updateWidgets()
                                     game.screenGame.updateItemLabels()
                                 }
                             }
                         })
    }

    val itemCellImage = Image(tiles.itemCell)
    val itemCellChestLabel = Label("-", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val itemCellPlayerLabel = Label("-", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val itemCoinImage = Image(tiles.itemCoin)
    val itemCoinChestLabel = Label("-", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val itemCoinPlayerLabel = Label("-", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val itemPickaxeImage = Image(tiles.itemPickaxe)
    val itemPickaxeChestLabel = Label("-", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val itemPickaxePlayerLabel = Label("-", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val itemGearImage = Image(tiles.itemGear)
    val itemGearChestLabel = Label("-", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val itemGearPlayerLabel = Label("-", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val itemRopeArrowImage = Image(tiles.itemRopeArrow)
    val itemRopeArrowChestLabel = Label("-", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val itemRopeArrowPlayerLabel = Label("-", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val itemPointingArrowImage = Image(tiles.itemPointingArrow)
    val itemPointingArrowChestLabel = Label("-", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val itemPointingArrowPlayerLabel = Label("-", Label.LabelStyle(BitmapFont(), Color.PURPLE))

    val table = Table().apply {
        this.center().setFillParent(true)
        this.defaults().pad(10f)
        this.add()
        this.add(textChestLabel)
        this.add(textPlayerLabel)
        this.row()
        this.add(itemCellImage)
        this.add(itemCellChestLabel)
        this.add(itemCellPlayerLabel)
        this.row()
        this.add(itemCoinImage)
        this.add(itemCoinChestLabel)
        this.add(itemCoinPlayerLabel)
        this.row()
        this.add(itemPickaxeImage)
        this.add(itemPickaxeChestLabel)
        this.add(itemPickaxePlayerLabel)
        this.row()
        this.add(itemGearImage)
        this.add(itemGearChestLabel)
        this.add(itemGearPlayerLabel)
        this.row()
        this.add(itemRopeArrowImage)
        this.add(itemRopeArrowChestLabel)
        this.add(itemRopeArrowPlayerLabel)
        this.row()
        this.add(itemPointingArrowImage)
        this.add(itemPointingArrowChestLabel)
        this.add(itemPointingArrowPlayerLabel)
        this.row()
        this.add(textReturnTextButton)
        this.add()
        this.add(textTakeAllTextButton)
        this.row()
    }

    init
    {
        stage.addActor(table)
    }

    fun setChestAndPlayer(chest : ObjectChest?, player : EntityPlayer?)
    {
        objectChest = chest
        entityPlayer = player
    }

    fun updateWidgets()
    {
        val chest = objectChest
        val player = entityPlayer

        if (chest != null && player != null)
        {
            itemCellChestLabel.setText(chest.getChestItem(PlayerItem.Cell))
            itemCellPlayerLabel.setText(player.getPlayerItem(PlayerItem.Cell))
            itemCoinChestLabel.setText(chest.getChestItem(PlayerItem.Coin))
            itemCoinPlayerLabel.setText(player.getPlayerItem(PlayerItem.Coin))
            itemPickaxeChestLabel.setText(chest.getChestItem(PlayerItem.Pickaxe))
            itemPickaxePlayerLabel.setText(player.getPlayerItem(PlayerItem.Pickaxe))
            itemGearChestLabel.setText(chest.getChestItem(PlayerItem.Gear))
            itemGearPlayerLabel.setText(player.getPlayerItem(PlayerItem.Gear))
            itemRopeArrowChestLabel.setText(chest.getChestItem(PlayerItem.RopeArrow))
            itemRopeArrowPlayerLabel.setText(player.getPlayerItem(PlayerItem.RopeArrow))
            itemPointingArrowChestLabel.setText(chest.getChestItem(PlayerItem.PointingArrow))
            itemPointingArrowPlayerLabel.setText(player.getPlayerItem(PlayerItem.PointingArrow))
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
