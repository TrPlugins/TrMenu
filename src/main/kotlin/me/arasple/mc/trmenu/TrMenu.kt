package me.arasple.mc.trmenu

import io.izzel.taboolib.loader.Plugin
import io.izzel.taboolib.module.config.TConfig
import io.izzel.taboolib.module.inject.TInject
import io.izzel.taboolib.module.locale.TLocale
import me.arasple.mc.trmenu.module.conf.Loader
import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.display.MenuSettings
import me.arasple.mc.trmenu.module.internal.service.RegisterCommands
import me.arasple.mc.trmenu.module.internal.service.Shortcuts
import org.bukkit.Bukkit

/**
 * @author Arasple
 * @date 2021/1/24 9:51
 */
object TrMenu : Plugin() {

    @TInject("settings.yml", locale = "Options.Language", migrate = true)
    lateinit var SETTINGS: TConfig
        private set

    override fun onLoad() {
        TLocale.sendToConsole("Plugin.Loading", Bukkit.getBukkitVersion())
    }

    override fun onEnable() {
        SETTINGS.listener { onSettingsReload() }.also { onSettingsReload() }
        TLocale.sendToConsole("Plugin.Enabled", plugin.description.version)
        Loader.loadMenus()
    }

    override fun onDisable() {
        MenuSession.getSessions().let {
            it.values.forEach { it.close(closePacket = true, updateInventory = true) }
            it.clear()
        }
    }

    fun onSettingsReload() {
        MenuSettings.load()
        Shortcuts.Type.load()
        RegisterCommands.load()
    }

}