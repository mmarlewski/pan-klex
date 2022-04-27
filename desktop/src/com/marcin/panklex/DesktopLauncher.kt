package com.marcin.panklex

import kotlin.jvm.JvmStatic
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application
import com.marcin.panklex.PanKlexGame

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
object DesktopLauncher
{
    @JvmStatic
    fun main(arg: Array<String>)
    {
        val config = Lwjgl3ApplicationConfiguration()
        config.setTitle("Pan Klex")
        config.setWindowedMode(1080, 720)
        config.setForegroundFPS(60)
        Lwjgl3Application(PanKlexGame(), config)
    }
}