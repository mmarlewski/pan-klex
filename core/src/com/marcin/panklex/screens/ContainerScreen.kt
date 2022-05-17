package com.marcin.panklex.screens

import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.Texture
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.TextureRegion
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable
import com.badlogic.gdx.utils.ScreenUtils
import com.marcin.panklex.BaseScreen
import com.marcin.panklex.PanKlexGame
import com.marcin.panklex.entities.EntityContainer

class ContainerScreen(val name : String, val game : PanKlexGame) : BaseScreen(name, game)
{
    val containerLabel = Label("Container", Label.LabelStyle(BitmapFont(), Color.YELLOW))
    val cancelButton =
        ImageButton(TextureRegionDrawable(game.assetManager.get<Texture>("graphics/actions/cancel.png")))
    val pickaxeImage =
        Image(TextureRegion(game.assetManager.get<Texture>("graphics/actions/pickaxe.png"), 0, 0, 32, 32))
    val pickaxeLabel = Label("pickaxes: 0", Label.LabelStyle(BitmapFont(), Color.YELLOW))
    val bombImage = Image(TextureRegion(game.assetManager.get<Texture>("graphics/actions/bomb.png"), 0, 0, 32, 32))
    val bombLabel = Label("bombs: 0", Label.LabelStyle(BitmapFont(), Color.YELLOW))
    val coinImage = Image(TextureRegion(game.assetManager.get<Texture>("graphics/actions/coin.png"), 0, 0, 32, 32))
    val coinLabel = Label("coins: 0", Label.LabelStyle(BitmapFont(), Color.YELLOW))
    val cellImage = Image(TextureRegion(game.assetManager.get<Texture>("graphics/actions/cell.png"), 0, 0, 32, 32))
    val cellLabel = Label("cells: 0", Label.LabelStyle(BitmapFont(), Color.YELLOW))
    val takeAllButton = TextButton("Take all", TextButton.TextButtonStyle(null, null, null, BitmapFont()))

    var containerEntity : EntityContainer? = null

    init
    {
        cancelButton.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float) = game.changeScreen(game.screenGame)
        })

        takeAllButton.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float)
            {
                game.screenGame.pickaxeCount += containerEntity!!.pickaxes
                game.screenGame.bombCount += containerEntity!!.bombs
                game.screenGame.coinCount += containerEntity!!.coins
                game.screenGame.cellCount += containerEntity!!.cells

                containerEntity!!.pickaxes = 0
                containerEntity!!.bombs = 0
                containerEntity!!.coins = 0
                containerEntity!!.cells = 0

                game.screenGame.refreshEquipment()
                game.changeScreen(game.screenGame)
            }
        })

        val table = Table()
        table.center().setFillParent(true)
        table.defaults().pad(10f)
        table.add(containerLabel)
        table.add(cancelButton)
        table.row()
        table.add(pickaxeImage)
        table.add(pickaxeLabel)
        table.row()
        table.add(bombImage)
        table.add(bombLabel)
        table.row()
        table.add(coinImage)
        table.add(coinLabel)
        table.row()
        table.add(cellImage)
        table.add(cellLabel)
        table.row()
        table.add(takeAllButton).colspan(2)
        stage.addActor(table)
    }

    override fun show()
    {
        super.show()

        game.changeInputProcessor(stage)

        pickaxeLabel.setText("pickaxes: ${containerEntity!!.pickaxes}")
        bombLabel.setText("bombs:${containerEntity!!.bombs}")
        coinLabel.setText("coins: ${containerEntity!!.coins}")
        cellLabel.setText("cells: ${containerEntity!!.cells}")
    }

    override fun render(delta : Float)
    {
        super.render(delta)

        ScreenUtils.clear(0f, 0f, 0f, 1f)
        camera.update()
        stage.draw()
    }
}
