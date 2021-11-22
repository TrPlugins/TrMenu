package me.arasple.mc.trmenu

import me.arasple.mc.trmenu.module.conf.Loader
import me.arasple.mc.trmenu.module.conf.prop.RunningPerformance
import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.internal.data.Metadata
import me.arasple.mc.trmenu.module.internal.hook.HookPlugin
import me.arasple.mc.trmenu.module.internal.inputer.Inputer.Companion.cancelWords
import me.arasple.mc.trmenu.module.internal.listener.ListenerItemInteract.interactCooldown
import me.arasple.mc.trmenu.module.internal.service.RegisterCommands
import me.arasple.mc.trmenu.module.internal.service.Shortcuts
import org.bukkit.Bukkit
import taboolib.common.platform.*
import taboolib.common.platform.function.console
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

    var performance = RunningPerformance.NORMAL
        private set
    
    override fun onLoad() {
        Language.default = "en_US"
        console().sendLang("Plugin-Loading", Bukkit.getVersion())
    }

    override fun onEnable() {
        SETTINGS.onReload { onSettingsReload() }
        onSettingsReload()
        Loader.loadMenus()
        Metadata.database
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
        performance = kotlin.runCatching {
            RunningPerformance.valueOf(SETTINGS.getString("Options.Running-Performance")!!)
        }.getOrNull() ?: RunningPerformance.NORMAL

        cancelWords.reload()
        interactCooldown.reload()
        Shortcuts.Type.load()
        RegisterCommands.load()
    }

}