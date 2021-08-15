package me.arasple.mc.trmenu

import me.arasple.mc.trmenu.module.conf.Loader
import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.internal.data.Metadata
import me.arasple.mc.trmenu.module.internal.hook.HookPlugin
import me.arasple.mc.trmenu.module.internal.service.RegisterCommands
import me.arasple.mc.trmenu.module.internal.service.Shortcuts
import org.bukkit.Bukkit
import taboolib.common.platform.*
import taboolib.common.platform.function.console
import taboolib.common.platform.function.submit
import taboolib.common5.FileWatcher
import taboolib.module.configuration.Config
import taboolib.module.configuration.SecuredFile
import taboolib.module.lang.sendLang
import taboolib.platform.BukkitPlugin

/**
 * @author Arasple
 * @date 2021/1/24 9:51
 */
object TrMenu : Plugin() {

    @Config("settings.yml",true)
    lateinit var SETTINGS: SecuredFile
        private set

    val plugin by lazy { BukkitPlugin.getInstance() }
    
    override fun onLoad() {
        console().sendLang("Plugin-Loading", Bukkit.getVersion())
    }

    override fun onEnable() {
        Metadata.localDatabase
        console().sendLang("Plugin-Enabled", plugin.description.version)
        HookPlugin.printInfo()
        submit {
            FileWatcher.INSTANCE.addSimpleListener(SETTINGS.file) { onSettingsReload() }.also { onSettingsReload() }
            Loader.loadMenus()
        }
    }

    override fun onDisable() {
        MenuSession.SESSIONS.entries.removeIf {
            it.value.close(closePacket = true, updateInventory = true)
            true
        }
    }

    private fun onSettingsReload() {
        Shortcuts.Type.load()
        RegisterCommands.load()
    }

}