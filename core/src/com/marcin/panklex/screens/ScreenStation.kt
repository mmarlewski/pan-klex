package com.marcin.panklex.screens

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.ui.*
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import com.marcin.panklex.BaseScreen
import com.marcin.panklex.PanKlexGame
import com.marcin.panklex.PlayerUpgrade
import com.marcin.panklex.entities.EntityPlayer
import com.marcin.panklex.objects.ObjectStation

class ScreenStation(val name : String, val game : PanKlexGame) : BaseScreen(name, game)
{
    val tiles = game.screenGame.tiles

    var objectStation : ObjectStation? = null
    var entityPlayer : EntityPlayer? = null

    val textNewUpgradeLabel = Label("new upgrade", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val stationUpgradeImage = Image()
    val textCurrentUpgradesLabel = Label("current upgrades", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val textAddTextButton = TextButton("-", TextButton.TextButtonStyle(null, null, null, BitmapFont())).apply {
        this.addListener(object : ClickListener()
                         {
                             override fun clicked(event : InputEvent?, x : Float, y : Float)
                             {
                                 val station = objectStation
                                 val player = entityPlayer

                                 if (station != null && player != null)
                                 {
                                     if (!station.isUpgradeTaken)
                                     {
                                         player.changePlayerUpgrade(station.upgrade, true)
                                         station.isUpgradeTaken = true
                                         updateWidgets()
                                         game.screenGame.updateUpgradeLabels()
                                     }
                                 }
                             }
                         })
    }
    val textReturnTextButton = TextButton("RETURN", TextButton.TextButtonStyle(null, null, null, BitmapFont())).apply {
        this.addListener(object : ClickListener()
                         {
                             override fun clicked(event : InputEvent?, x : Float, y : Float)
                             {
                                 setStationAndPlayer(null, null)
                                 game.changeScreen(game.screenGame)
                             }
                         })
    }

    val upgradeLongFallLegImage = Image()
    val upgradeLongFallLegLabel = Label("long fall leg", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val upgradeSpringLegImage = Image()
    val upgradeSpringLegLabel = Label("spring leg", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val upgradeDrillArmImage = Image()
    val upgradeDrillArmLabel = Label("drill arm", Label.LabelStyle(BitmapFont(), Color.PURPLE))
    val upgradeExtendedArmImage = Image()
    val upgradeExtendedArmLabel = Label("extended arm", Label.LabelStyle(BitmapFont(), Color.PURPLE))

    val table = Table().apply {
        this.center().setFillParent(true)
        this.defaults().pad(10f)
        this.add(textNewUpgradeLabel)
        this.row()
        this.add(stationUpgradeImage)
        this.add(textAddTextButton)
        this.row()
        this.add(textCurrentUpgradesLabel)
        this.row()
        this.add(upgradeLongFallLegImage)
        this.add(upgradeLongFallLegLabel)
        this.row()
        this.add(upgradeSpringLegImage)
        this.add(upgradeSpringLegLabel)
        this.row()
        this.add(upgradeDrillArmImage)
        this.add(upgradeDrillArmLabel)
        this.row()
        this.add(upgradeExtendedArmImage)
        this.add(upgradeExtendedArmLabel)
        this.row()
        this.add(textReturnTextButton)
    }

    init
    {
        stage.addActor(table)
    }

    fun setStationAndPlayer(station : ObjectStation?, player : EntityPlayer?)
    {
        objectStation = station
        entityPlayer = player
    }

    fun updateWidgets()
    {
        val station = objectStation
        val player = entityPlayer

        if (station != null && player != null)
        {
            stationUpgradeImage.drawable = when (station.upgrade)
            {
                PlayerUpgrade.LongFallLeg -> tiles.upgradeLongFallLegUnlockedDrawable
                PlayerUpgrade.SpringLeg   -> tiles.upgradeSpringLegUnlockedDrawable
                PlayerUpgrade.DrillArm    -> tiles.upgradeDrillArmUnlockedDrawable
                PlayerUpgrade.ExtendedArm -> tiles.upgradeExtendedArmUnlockedDrawable
            }

            textAddTextButton.setText(if (station.isUpgradeTaken) "TAKEN" else "ADD")

            upgradeLongFallLegImage.drawable = when (player.hasPlayerUpgrade(PlayerUpgrade.LongFallLeg))
            {
                true  -> tiles.upgradeLongFallLegUnlockedDrawable
                false -> tiles.upgradeLongFallLegLockedDrawable
            }
            upgradeSpringLegImage.drawable = when (player.hasPlayerUpgrade(PlayerUpgrade.SpringLeg))
            {
                true  -> tiles.upgradeSpringLegUnlockedDrawable
                false -> tiles.upgradeSpringLegLockedDrawable
            }
            upgradeDrillArmImage.drawable = when (player.hasPlayerUpgrade(PlayerUpgrade.DrillArm))
            {
                true  -> tiles.upgradeDrillArmUnlockedDrawable
                false -> tiles.upgradeDrillArmLockedDrawable
            }
            upgradeExtendedArmImage.drawable = when (player.hasPlayerUpgrade(PlayerUpgrade.ExtendedArm))
            {
                true  -> tiles.upgradeExtendedArmUnlockedDrawable
                false -> tiles.upgradeExtendedArmLockedDrawable
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
