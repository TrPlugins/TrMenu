package trplugins.menu.module.display

import trplugins.menu.TrMenu
import trplugins.menu.api.reaction.Reactions
import trplugins.menu.module.internal.script.js.ScriptFunction
import trplugins.menu.util.bukkit.ItemMatcher
import trplugins.menu.util.collections.CycleList
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.Bukkit
import taboolib.common.platform.function.pluginId
import taboolib.common5.Baffle
import taboolib.module.chat.colored
import trplugins.menu.util.Cooldown
import java.util.concurrent.TimeUnit

/**
 * @author Arasple
 * @date 2021/1/24 20:54
 */
class MenuSettings(
    val title: CycleList<String>,
    val titleUpdate: Int,
    val enableArguments: Boolean = true,
    val defaultArguments: Array<String> = arrayOf(),
    val freeSlots: Set<Int> = setOf(),
    val defaultLayout: Int,
    expansions: Array<String>,
    val minClickDelay: Int,
    val hidePlayerInventory: Boolean,
    val boundCommands: List<Regex>,
    val boundItems: Array<ItemMatcher>,
    val openEvent: Reactions,
    val closeEvent: Reactions,
    val clickEvent: Reactions,
    val tasks: Map<Long, Reactions>,
    val internalFunctions: Set<ScriptFunction>
) {

    companion object {

        val PRE_COLOR get() = TrMenu.SETTINGS.getBoolean("Menu.Icon.Item.Pre-Color")
        val DEFAULT_NAME_COLOR get() = (TrMenu.SETTINGS.getString("Menu.Icon.Item.Default-Name-Color") ?: "&7").colored()
        val DEFAULT_LORE_COLOR get() = (TrMenu.SETTINGS.getString("Menu.Icon.Item.Default-Lore-Color") ?: "&7").colored()

    }

    val clickDelay = Cooldown("CLICK_DELAY", minClickDelay).also { it.plugin = pluginId }

    val dependExpansions: Array<String> = expansions
        get() {
            return if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                val registered = PlaceholderAPI.getRegisteredIdentifiers()
                field.filter { ex -> registered.none { it.equals(ex, true) } }.toTypedArray()
            } else {
                arrayOf("PLUGINCORE")
            }
        }

    /**
     * 匹配菜单绑定的命令
     *
     * @return 参数,
     *         -> 为空: 命令匹配该菜单，支持打开
     *         -> 不为空：命令匹配该菜单，支持打开，且携带传递参数
     *         -> Null： 命令与该菜单不匹配
     */
    fun matchCommand(menu: Menu, command: String): List<String>? = command.split(" ").toMutableList().let { it ->
        if (it.isNotEmpty()) {
            for (i in it.indices) {
                val read = read(it, i)
                val c = read[0]
                val args = read.toMutableList().also { it.removeAt(0) }
                if (boundCommands.any { it.matches(c) } && !(!menu.settings.enableArguments && args.isNotEmpty())) {
                    return@let args
                }
            }
        }
        return@let null
    }

    /**
     * 更好的兼容带参打开命令的同时支持菜单传递参数
     * 例如:
     * - 'is upgrade' 作为打开命令
     * - 'is upgrade 233' 将只会从 233 开始作为第一个参数
     */
    private fun read(cmds: List<String>, index: Int): List<String> {
        var commands = cmds
        val command = if (index == 0) commands[index]
        else {
            val cmd = StringBuilder()
            for (i in 0..index) cmd.append(commands[i]).append(" ")
            cmd.substring(0, cmd.length - 1)
        }
        for (i in 0..index) commands = commands.toMutableList().also { it.removeAt(0) }

        return commands.toMutableList().also { it.add(0, command ?: return@also) }
    }

}