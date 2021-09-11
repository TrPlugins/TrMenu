package me.arasple.mc.trmenu

import me.arasple.mc.trmenu.module.conf.Loader
import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.internal.data.Metadata
import me.arasple.mc.trmenu.module.internal.hook.HookPlugin
import me.arasple.mc.trmenu.module.internal.listener.ListenerItemInteract
import me.arasple.mc.trmenu.module.internal.service.RegisterCommands
import me.arasple.mc.trmenu.module.internal.service.Shortcuts
import org.bukkit.Bukkit
import taboolib.common.platform.*
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submit
import taboolib.common5.FileWatcher
import taboolib.module.configuration.Config
import taboolib.module.configuration.SecuredFile
import taboolib.module.lang.Language
import taboolib.module.lang.sendLang
import taboolib.platform.BukkitPlugin

/**
 * @author Arasple
 * @date 2021/1/24 9:51
 */
object TrMenu : Plugin() {

    @Config("settings.yml",true, autoReload = true)
    lateinit var SETTINGS: SecuredFile
        private set

    val plugin by lazy { BukkitPlugin.getInstance() }
    
    override fun onLoad() {
        Language.default = "en_US"
        console().sendLang("Plugin-Loading", Bukkit.getVersion())
    }

    override fun onEnable() {
        SETTINGS.onReload { onSettingsReload() }
        onSettingsReload()
        Loader.loadMenus()
        Metadata.localDatabase
        console().sendLang("Plugin-Enabled", plugin.description.version)
        HookPlugin.printInfo()
    }

    override fun onDisable() {
        MenuSession.SESSIONS.entries.removeIf {
            it.value.close(closePacket = true, updateInventory = true)
            true
        }
    }

    private fun onSettingsReload() {
        ListenerItemInteract.reload()
        Shortcuts.Type.load()
        RegisterCommands.load()
    }

}