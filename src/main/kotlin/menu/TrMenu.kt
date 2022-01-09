package menu

import trmenu.module.conf.Loader
import trmenu.module.conf.prop.RunningPerformance
import trmenu.module.display.MenuSession
import trmenu.module.internal.data.Metadata
import trmenu.module.internal.hook.HookPlugin
import trmenu.module.internal.inputer.Inputer.Companion.cancelWords
import trmenu.module.internal.listener.ListenerItemInteract.interactCooldown
import trmenu.module.internal.service.RegisterCommands
import trmenu.module.internal.service.Shortcuts
import org.bukkit.Bukkit
import taboolib.common.platform.*
import taboolib.common.platform.function.console
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.lang.Language
import taboolib.module.lang.sendLang
import taboolib.platform.BukkitPlugin
import menu.api.action.Actions
import trmenu.module.internal.script.contentParser
import trmenu.module.internal.script.scriptParser

/**
 * @author Arasple
 * @date 2021/1/24 9:51
 */
object TrMenu : Plugin() {

    @Config("settings.yml",true, autoReload = true)
    lateinit var SETTINGS: Configuration
        private set

    val plugin by lazy { BukkitPlugin.getInstance() }

    var performance = RunningPerformance.NORMAL
        private set
    
    override fun onLoad() {
        Language.default = "en_US"
        menu.api.action.Actions.init(contentParser, scriptParser)
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
            RunningPerformance.valueOf(SETTINGS.getString("Options.Running-Performance") ?: "Normal")
        }.getOrNull() ?: RunningPerformance.NORMAL

        cancelWords.reload()
        interactCooldown.reload()
        Shortcuts.Type.load()
        RegisterCommands.load()
    }

}