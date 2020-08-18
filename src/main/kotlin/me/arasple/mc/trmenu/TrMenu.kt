package me.arasple.mc.trmenu

import io.izzel.taboolib.loader.Plugin
import io.izzel.taboolib.module.config.TConfig
import io.izzel.taboolib.module.inject.TInject

/**
 * @author Arasple
 * @date 2020/2/26 10:05
 */
object TrMenu : Plugin() {

    @TInject("settings.yml", locale = "Options.Locale", migrate = true)
    lateinit var SETTINGS: TConfig

    @TInject(state = TInject.State.STARTING, init = "init", active = "active", cancel = "cancel")
    lateinit var LOADER: TrMenuLoader

}