package me.arasple.mc.trmenu.display.menu

import io.izzel.taboolib.internal.apache.lang3.ArrayUtils
import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.data.Sessions
import me.arasple.mc.trmenu.data.Sessions.getMenuSession
import me.arasple.mc.trmenu.display.Menu
import me.arasple.mc.trmenu.display.animation.Animated
import me.arasple.mc.trmenu.display.function.InternalFunction
import me.arasple.mc.trmenu.display.function.Reactions
import me.arasple.mc.trmenu.modules.item.ItemIdentifier
import me.arasple.mc.trmenu.modules.packets.PacketsHandler
import me.arasple.mc.trmenu.utils.Msger
import me.clip.placeholderapi.PlaceholderAPI
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.scheduler.BukkitRunnable

/**
 * @author Arasple
 * @date 2020/5/30 14:21
 */
class MenuSettings(val title: Titles, val options: Options, val bindings: Bindings, val events: Events, val tasks: ScheduledTasks, val functions: Funs) {

    fun load(player: Player, menu: Menu, layout: MenuLayout.Layout) {
        title.load(player, menu, layout)
        options.run(player, layout)
        tasks.run(player, menu)
    }

    class Titles(val titles: Animated<String>, val update: Int) {

        fun currentTitle(player: Player): String {
            return Msger.replace(
                player,
                if (titles.elements.size == 1 || update <= 0) titles.elements.firstOrNull() ?: "TrMenu"
                else titles.currentElement(player, "TrMenu")
            )
        }

        fun getTitle(player: Player): String {
            return Msger.replace(
                player,
                if (titles.elements.size == 1 || update <= 0) titles.elements.firstOrNull() ?: "TrMenu"
                else titles.nextElement(player, "TrMenu")
            )
        }

        fun load(player: Player, menu: Menu, layout: MenuLayout.Layout) {
            val session = player.getMenuSession()
            val sessionId = session.id
            val page = session.page

            if (update > 0 && titles.isUpdatable()) {
                menu.tasking.task(
                    player,
                    object : BukkitRunnable() {
                        override fun run() {
                            if (session.isDifferent(sessionId)) {
                                cancel()
                                return
                            }
                            layout.displayInventory(player, getTitle(player))
                            menu.resetIcons(player, session)
                        }
                    }.runTaskTimerAsynchronously(TrMenu.plugin, update.toLong(), update.toLong())
                )
            }
        }

    }

    class Options(val enableArguments: Boolean, val defaultArguments: Array<String>, val defaultLayout: String, val hidePlayerInventory: Boolean, val minClickDelay: Long, val dependExpansions: Array<String>) {

        fun run(player: Player, layout: MenuLayout.Layout) {
            if (hidePlayerInventory)
                PacketsHandler.clearInventory(player, layout.size(), Sessions.TRMENU_WINDOW_ID)
        }

        fun getDefaultLayout(player: Player): Int = NumberUtils.toInt(Msger.replace(player, defaultLayout), 0)

        fun expansions(): List<String> {
            val registered = PlaceholderAPI.getRegisteredIdentifiers()
            return dependExpansions.filter { ex -> registered.none { it.equals(ex, true) } }
        }

    }

    class Bindings(val boundCommands: Array<Regex>, val boundItems: Array<ItemIdentifier>) {

        /**
         * 匹配菜单绑定的命令
         *
         * @return 参数,
         *         -> 为空: 命令匹配该菜单，支持打开
         *         -> 不为空：命令匹配该菜单，支持打开，且携带传递参数
         *         -> Null： 命令与该菜单不匹配
         */
        fun matchCommand(menu: Menu, command: String): Array<String>? = command.split(" ").toTypedArray().let { it ->
            if (it.isNotEmpty()) {
                for (i in it.indices) {
                    val read = read(it, i)
                    val c = read[0]
                    val args = ArrayUtils.remove(read, 0)
                    if (boundCommands.any { it.matches(c) } && !(!menu.settings.options.enableArguments && args.isNotEmpty())) {
                        return@let args
                    }
                }
            }
            return@let null
        }

        /**
         * 匹配物品是否符合打开本菜单的条件
         */
        fun matchItem(player: Player, itemStack: ItemStack): Boolean =
            boundItems.any { it.isMatch(player, itemStack) }

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

    class Events(val openEvent: Reactions, val closeEvent: Reactions, val clickEvent: Reactions)

    class ScheduledTasks(val tasks: Map<Long, Reactions>) {

        fun run(player: Player, menu: Menu) {
            val session = player.getMenuSession()
            val sessionId = session.id
            val page = session.page

            tasks.forEach {
                menu.tasking.task(
                    player,
                    object : BukkitRunnable() {
                        override fun run() {
                            if (session.isDifferent(sessionId)) cancel()
                            else it.value.eval(player)
                        }
                    }.runTaskTimerAsynchronously(TrMenu.plugin, it.key, it.key)
                )
            }

        }

    }

    class Funs(val internalFunctions: Set<InternalFunction>)

}