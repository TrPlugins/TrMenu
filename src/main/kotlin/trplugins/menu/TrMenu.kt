package trplugins.menu

import trplugins.menu.module.conf.Loader
import trplugins.menu.module.conf.prop.RunningPerformance
import trplugins.menu.module.display.MenuSession
import trplugins.menu.module.internal.data.Metadata
import trplugins.menu.module.internal.hook.HookPlugin
import trplugins.menu.module.internal.inputer.Inputer.Companion.cancelWords
import trplugins.menu.module.internal.listener.ListenerItemInteract.interactCooldown
import trplugins.menu.module.internal.service.RegisterCommands
import trplugins.menu.module.internal.service.Shortcuts
import org.bukkit.Bukkit
import taboolib.common.platform.*
import taboolib.common.platform.function.console
import taboolib.module.configuration.Config
import taboolib.module.configuration.Configuration
import taboolib.module.kether.Kether
import taboolib.module.lang.Language
import taboolib.module.lang.sendLang
import taboolib.platform.BukkitPlugin
import trplugins.menu.api.action.ActionHandle
import trplugins.menu.module.display.session
import trplugins.menu.module.internal.script.evalScript

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

    lateinit var actionHandle: ActionHandle
        private set
    
    override fun onLoad() {
        Language.default = "en_US"
        actionHandle = ActionHandle({ t, u -> t.evalScript(u) }, { t, u -> t.session().parse(u) }, "kether")
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
        Kether.isAllowToleranceParser = SETTINGS.getBoolean("Action.Kether.Allow-Tolerance-Parser",false)
    }

}