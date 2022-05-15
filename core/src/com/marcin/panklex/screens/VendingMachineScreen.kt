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
import com.marcin.panklex.entities.EntityVendingMachine

class VendingMachineScreen(val name : String, val game : PanKlexGame) : BaseScreen(name, game)
{
    val containerLabel = Label("-working-", Label.LabelStyle(BitmapFont(), Color.YELLOW))
    val cancelButton =
        ImageButton(TextureRegionDrawable(game.assetManager.get<Texture>("graphics/actions/cancel.png")))
    val coinLabel = Label("coins: 0", Label.LabelStyle(BitmapFont(), Color.YELLOW))
    val pickaxeImage =
        Image(TextureRegion(game.assetManager.get<Texture>("graphics/actions/pickaxe.png"), 0, 0, 32, 32))
    val pickaxeLabel = Label("pickaxes: 0", Label.LabelStyle(BitmapFont(), Color.YELLOW))
    val pickaxeButton = TextButton("buy for 1", TextButton.TextButtonStyle(null, null, null, BitmapFont()))
    val bombImage = Image(TextureRegion(game.assetManager.get<Texture>("graphics/actions/bomb.png"), 0, 0, 32, 32))
    val bombLabel = Label("bombs: 0", Label.LabelStyle(BitmapFont(), Color.YELLOW))
    val bombButton = TextButton("buy for 2", TextButton.TextButtonStyle(null, null, null, BitmapFont()))
    val cellImage = Image(TextureRegion(game.assetManager.get<Texture>("graphics/actions/cell.png"), 0, 0, 32, 32))
    val cellLabel = Label("cells: 0", Label.LabelStyle(BitmapFont(), Color.YELLOW))
    val cellButton = TextButton("buy for 3", TextButton.TextButtonStyle(null, null, null, BitmapFont()))

    var vendingMachineEntity : EntityVendingMachine? = null

    init
    {
        cancelButton.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float)
            {
                game.screenGame.refreshEquipment()
                game.changeScreen(game.screenGame)
            }
        })

        pickaxeButton.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float)
            {
                if (!vendingMachineEntity!!.isBroken)
                {
                    if (game.screenGame.coinCount > 0 && vendingMachineEntity!!.pickaxes > 0)
                    {
                        game.screenGame.coinCount-=1
                        coinLabel.setText("coins: ${game.screenGame.coinCount}")
                        vendingMachineEntity!!.pickaxes--
                        pickaxeLabel.setText("pickaxes: ${vendingMachineEntity!!.pickaxes}")
                        game.screenGame.pickaxeCount++
                    }
                }
                else
                {
                    if (vendingMachineEntity!!.pickaxes > 0)
                    {
                        vendingMachineEntity!!.pickaxes--
                        pickaxeLabel.setText("pickaxes: ${vendingMachineEntity!!.pickaxes}")
                        game.screenGame.pickaxeCount++
                    }
                }
            }
        })

        bombButton.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float)
            {
                if (!vendingMachineEntity!!.isBroken)
                {
                    if (game.screenGame.coinCount > 1 && vendingMachineEntity!!.bombs > 0)
                    {
                        game.screenGame.coinCount-=2
                        coinLabel.setText("coins: ${game.screenGame.coinCount}")
                        vendingMachineEntity!!.bombs--
                        bombLabel.setText("bombs: ${vendingMachineEntity!!.bombs}")
                        game.screenGame.bombCount++
                    }
                }
                else
                {
                    if (vendingMachineEntity!!.bombs > 0)
                    {
                        vendingMachineEntity!!.bombs--
                        bombLabel.setText("bombs: ${vendingMachineEntity!!.bombs}")
                        game.screenGame.bombCount++
                    }
                }
            }
        })

        cellButton.addListener(object : ClickListener()
        {
            override fun clicked(event : InputEvent?, x : Float, y : Float)
            {
                if (!vendingMachineEntity!!.isBroken)
                {
                    if (game.screenGame.coinCount > 2 && vendingMachineEntity!!.cells > 0)
                    {
                        game.screenGame.coinCount-=3
                        coinLabel.setText("coins: ${game.screenGame.coinCount}")
                        vendingMachineEntity!!.cells--
                        cellLabel.setText("cells: ${vendingMachineEntity!!.cells}")
                        game.screenGame.cellCount++
                    }
                }
                else
                {
                    if (vendingMachineEntity!!.cells > 0)
                    {
                        vendingMachineEntity!!.cells--
                        cellLabel.setText("cells: ${vendingMachineEntity!!.cells}")
                        game.screenGame.cellCount++
                    }
                }
            }
        })

        val table = Table()
        table.center().setFillParent(true)
        table.add(containerLabel).pad(10f)
        table.add(coinLabel).pad(10f)
        table.add(cancelButton).pad(10f)
        table.row()
        table.add(pickaxeImage).pad(10f)
        table.add(pickaxeLabel).pad(10f)
        table.add(pickaxeButton).pad(10f)
        table.row()
        table.add(bombImage).pad(10f)
        table.add(bombLabel).pad(10f)
        table.add(bombButton).pad(10f)
        table.row()
        table.add(cellImage).pad(10f)
        table.add(cellLabel).pad(10f)
        table.add(cellButton).pad(10f)
        table.row()
        stage.addActor(table)
    }

    override fun show()
    {
        super.show()

        containerLabel.setText(
            if (vendingMachineEntity!!.isBroken)
                "-broken-"
            else
                "-working-"
        )

        coinLabel.setText(
            if (vendingMachineEntity!!.isBroken)
                "---"
            else
                "coins: ${game.screenGame.coinCount}"
        )

        pickaxeLabel.setText("pickaxes: ${vendingMachineEntity!!.pickaxes}")
        bombLabel.setText("pickaxes: ${vendingMachineEntity!!.bombs}")
        cellLabel.setText("pickaxes: ${vendingMachineEntity!!.cells}")

        pickaxeButton.setText(
            if (vendingMachineEntity!!.isBroken)
                "take"
            else
                "buy for 1"
        )

        bombButton.setText(
            if (vendingMachineEntity!!.isBroken)
                "take"
            else
                "buy for 2"
        )

        cellButton.setText(
            if (vendingMachineEntity!!.isBroken)
                "take"
            else
                "buy for 3"
        )
    }

    override fun render(delta : Float)
    {
        super.render(delta)

        ScreenUtils.clear(0f, 0f, 0f, 1f)
        camera.update()
        stage.draw()
    }
}
