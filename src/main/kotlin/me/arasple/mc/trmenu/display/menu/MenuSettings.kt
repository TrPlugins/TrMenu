package me.arasple.mc.trmenu.display.menu

import io.izzel.taboolib.internal.apache.lang3.ArrayUtils
import me.arasple.mc.trmenu.display.function.InternalFunction
import me.arasple.mc.trmenu.display.function.Reaction
import me.arasple.mc.trmenu.display.animation.Animated
import me.arasple.mc.trmenu.modules.item.ItemIdentifier
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/5/30 14:21
 */
class MenuSettings(val title: Titles, val options: Options, val bindings: Bindings, val events: Events, val tasks: Tasks, val functions: Funs) {

    class Titles(val titles: Animated<String>, val update: Int) {

        fun getTitle(player: Player): String {
            return if (titles.elements.size == 1 || update <= 0) titles.elements.firstOrNull() ?: "TrMenu"
            else titles.nextElement(player, "TrMenu")
        }

    }

    class Options(val defaultArguments: Array<String>, val hidePlayerInventory: Boolean, val minClickDelay: Int, val dependExpansions: Array<String>)

    class Bindings(val boundCommands: Array<Regex>, val boundItems: Array<ItemIdentifier>)

    class Events(val openEvent: Reaction, val closeEvent: Reaction, val clickEvent: Reaction)

    class Tasks(val tasks: Map<Long, Reaction>)

    class Funs(val internalFunctions: Set<InternalFunction>)


    /**
     * 匹配菜单绑定的命令
     *
     * @return 参数,
     *         -> 为空: 命令匹配该菜单，支持打开
     *         -> 不为空：命令匹配该菜单，支持打开，且携带传递参数
     *         -> Null： 命令与该菜单不匹配
     */
    fun matchCommand(command: String): Array<String>? = command.split(" ").toTypedArray().let { it ->
        if (it.isNotEmpty()) {
            for (i in it.indices) {
                val read = read(it, i)
                val c = read[0]
                val args = ArrayUtils.remove(read, 0)
                if (bindings.boundCommands.any { c.matches(it) }) return@let args
            }
        }
        return@let null
    }

    /**
     * 匹配物品是否符合打开本菜单的条件
     */
    fun matchItem(player: Player, itemStack: ItemStack): Boolean =
        bindings.boundItems.any { it.isMatch(player, itemStack) }

    /**
     * 更好的兼容带参打开命令的同时支持菜单传递参数
     * 例如:
     * - 'is upgrade' 作为打开命令
     * - 'is upgrade 233' 将只会从 233 开始作为第一个参数
     */
    private fun read(cmds: Array<String>, index: Int): Array<String> {
        var commands = cmds
        val command: String
        command = if (index == 0) commands[index]
        else {
            val cmd = StringBuilder()
            for (i in 0..index) cmd.append(commands[i]).append(" ")
            cmd.substring(0, cmd.length - 1)
        }
        for (i in 0..index) commands = ArrayUtils.remove(commands, 0)
        return ArrayUtils.insert(0, commands, command)
    }

}