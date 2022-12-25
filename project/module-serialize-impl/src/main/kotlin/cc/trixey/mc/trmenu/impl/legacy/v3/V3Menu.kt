package cc.trixey.mc.trmenu.impl.legacy.v3

import cc.trixey.mc.invero.Panel
import cc.trixey.mc.invero.Window
import cc.trixey.mc.invero.WindowType
import cc.trixey.mc.trmenu.api.menu.Menu
import org.bukkit.Bukkit
import org.bukkit.event.inventory.InventoryType
import taboolib.common.platform.ProxyPlayer
import taboolib.common.platform.function.adaptPlayer
import taboolib.common.platform.function.submit
import taboolib.common.util.VariableReader
import javax.lang.model.element.VariableElement
import kotlin.math.max

/**
 * TrMenu
 * cc.trixey.mc.trmenu.serialize.legacy.v3.V3Menu
 *
 * @author Score2
 * @since 2022/12/10 23:42
 */
class V3Menu(
    id: String,
    val settings: Settings,
    val layouts: List<Layout>,

) : Menu(id) {

    override val panels: List<Panel>
        get() = TODO("Not yet implemented")

    override fun windowed(viewer: ProxyPlayer): Window {
        TODO("Not yet implemented")
    }

    class Layout(
        rows: Int = 0,
        val type: InventoryType,
        layout: List<String>,
        playerInventory: List<String>
    ) {

        private val width = width(type)

        val rows = when {
            type == InventoryType.CHEST -> max(rows, layout.size)
            type.defaultSize % 9 == 0 -> type.defaultSize / 9
            else -> 1
        }

        private val size = WindowType.fromBukkitType(type).containerSize

        private val layout = mutableListOf<String>().also { while (it.size < rows) it.add(BLANK_LINE) }

        private val playerInventory = mutableListOf<String>().also { while (it.size < 4) it.add(BLANK_LINE) }

        val keys: Map<String, Set<Int>> by lazy {
            val keys = mutableMapOf<String, MutableSet<Int>>()

            layout.forEachIndexed { y, line ->
                listKeys(line).forEachIndexed { x, element ->
                    val index = y * width + x
                    keys.computeIfAbsent(element.toString()) { mutableSetOf() }.add(index)
                }

            }

            playerInventory.forEachIndexed { y, line ->
                listKeys(line).forEachIndexed { x, element ->
                    val index = size + y * width + x - 1
                    keys.computeIfAbsent(element.toString()) { mutableSetOf() }.add(index)
                }
            }

            keys
        }

        fun isSimilar(layout: Layout): Boolean {
            return layout.rows == rows && layout.type == type
        }

        companion object {

            private const val BLANK_CHAR = " "
            private val BLANK_LINE = BLANK_CHAR.repeat(9)

            @Suppress("DEPRECATION")
            fun listKeys(line: String): List<Any> {

                return VariableReader(start = "`", end = "`").readToFlatten(line).flatMap {
                    if (it.isVariable) listOf(it.text)
                    else it.text.toList()
                }
            }

            fun search(layouts: List<Layout>, iconKey: String, pages: List<Int>): Map<Int, Set<Int>> {
                val ps = mutableMapOf<Int, MutableSet<Int>>()

                layouts.forEachIndexed { index, layout ->
                    layout.keys[iconKey]?.let {
                        ps.computeIfAbsent(index) { mutableSetOf() }
                            .addAll(it)
                    }
                }

                pages.forEach { ps.computeIfAbsent(it) { mutableSetOf() } }

                return ps
            }

            private fun width(type: InventoryType): Int {
                return when (type.defaultSize) {
                    27 -> 9
                    5 -> 5
                    else -> 3
                }
            }

        }

    }


    class Settings(
        val title: List<String>,
        val titleUpdate: Int,
        val enableArguments: Boolean = true,
        val defaultArguments: Array<String> = arrayOf(),
        val freeSlots: Set<Int> = setOf(),
        val defaultLayout: Int,
//        expansions: Array<String>,
        val minClickDelay: Int,
        val hidePlayerInventory: Boolean,
//        val boundCommands: List<Regex>,
//        val boundItems: Array<ItemMatcher>,
//        val openEvent: Reactions,
//        val closeEvent: Reactions,
//        val clickEvent: Reactions,
//        val tasks: Map<Long, Reactions>,
//        val internalFunctions: Set<ScriptFunction>
    ) {

        /*val dependExpansions: Array<String> = expansions
            get() {
                return if (Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
                    val registered = PlaceholderAPI.getRegisteredIdentifiers()
                    field.filter { ex -> registered.none { it.equals(ex, true) } }.toTypedArray()
                } else {
                    arrayOf("PLUGINCORE")
                }
            }*/


        /**
         * 匹配菜单绑定的命令
         *
         * @return 参数,
         *         -> 为空: 命令匹配该菜单，支持打开
         *         -> 不为空：命令匹配该菜单，支持打开，且携带传递参数
         *         -> Null： 命令与该菜单不匹配
         */
        /*fun matchCommand(menu: Menu, command: String): List<String>? = command.split(" ").toMutableList().let { it ->
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
        }*/

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
}

