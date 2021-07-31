package me.arasple.mc.trmenu

import me.arasple.mc.trmenu.module.conf.Loader
import me.arasple.mc.trmenu.module.display.MenuSession
import me.arasple.mc.trmenu.module.internal.hook.HookPlugin
import me.arasple.mc.trmenu.module.internal.service.RegisterCommands
import me.arasple.mc.trmenu.module.internal.service.Shortcuts
import org.bukkit.Bukkit
import taboolib.common.env.RuntimeDependency
import taboolib.common.platform.Plugin
import taboolib.common.platform.console
import taboolib.common5.FileWatcher
import taboolib.module.configuration.Config
import taboolib.module.configuration.SecuredFile
import taboolib.module.lang.registerLanguage
import taboolib.module.lang.sendLang
import taboolib.platform.BukkitPlugin

/**
 * @author Arasple
 * @date 2021/1/24 9:51
 */
@RuntimeDependency(
    value = "!org.apache.commons:commons-lang3:3.12.0",
    test = "!org.apache.commons.lang3.ArrayUtils",
    relocate = ["!org.apache.commons.lang3", "!me.arasple.mc.trmenu.shade.org.apache.commons.lang3"]
)
object TrMenu : Plugin() {

//    @TInject("settings.yml", locale = "Options.Language", migrate = true)
    @Config("settings.yml",true)
    lateinit var SETTINGS: SecuredFile
        private set

    val plugin = BukkitPlugin.getInstance()
    
    override fun onLoad() {
        console().sendLang("Plugin.Loading", Bukkit.getBukkitVersion())
    }

    override fun onEnable() {
        FileWatcher.INSTANCE.addSimpleListener(SETTINGS.file) { onSettingsReload() }.also { onSettingsReload() }
        console().sendLang("Plugin.Enabled", plugin.description.version)
        HookPlugin.printInfo()
        Loader.loadMenus()
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