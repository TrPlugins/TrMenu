package me.arasple.mc.trmenu.display.menu

import io.izzel.taboolib.util.Variables
import me.arasple.mc.trmenu.data.MenuSession.Companion.TRMENU_WINDOW_ID
import me.arasple.mc.trmenu.display.Icon
import me.arasple.mc.trmenu.display.animation.Animated
import me.arasple.mc.trmenu.display.icon.IconDisplay
import me.arasple.mc.trmenu.modules.packets.PacketsHandler
import me.arasple.mc.trmenu.utils.Patterns.ICON_KEY_MATCHER
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.inventory.InventoryType.*
import kotlin.math.max

/**
 * @author Arasple
 * @date 2020/7/6 14:26
 */
class MenuLayout(val layouts: List<Layout>) {

    /**
     * 写入图标默认位置到布局字符串中
     */
    fun writeIcons(icons: Set<Icon>) {
        icons.forEach {
            val key = it.id
            layouts.forEachIndexed { index, layout ->
                it.defIcon.display.position[index]?.elements?.firstOrNull()?.staticSlots?.let { slots ->
                    layout.reversePositionize(key, slots)
                }
            }
        }
    }

    /**
     * 从布局中为图标定位默认位置
     */
    fun locateIcons(icons: Set<Icon>) {
        val pages: MutableList<Map<String, Set<Int>>> = mutableListOf()
        layouts.forEach { pages.add(it.positionize()) }

        icons.forEach {
            val key = it.id
            pages.forEachIndexed { index, page ->
                page[key]?.let { slots ->
                    it.defIcon.display.position.computeIfAbsent(index) { Animated(arrayOf()) }.addElement(IconDisplay.Position(slots))
                }
            }
        }
    }

    /**
     * 布局
     */
    class Layout(val type: InventoryType, var rows: Int, val layout: MutableList<String>, val layoutInventory: MutableList<String>) {

        constructor(type: InventoryType, layout: List<String>?, layoutInventory: List<String>?) : this(type, -1, layout?.toMutableList() ?: mutableListOf(), layoutInventory?.toMutableList() ?: mutableListOf())

        /**
         * 初始化修正
         */
        init {
            rows = if (type == CHEST) max(rows, layout.size) else if (type.defaultSize % 9 == 0) type.defaultSize / 9 else 1
            if (type == CHEST) {
                rows = max(rows, layout.size)
                while (layout.size < rows) layout.add(BLANK_LINE)
                while (layoutInventory.size < 4) layoutInventory.add(BLANK_LINE)
            }
        }

        /**
         * 显示虚拟容器
         */
        fun displayInventory(player: Player, title: String) = PacketsHandler.sendOpenWindow(player, TRMENU_WINDOW_ID, type, size(type, rows), title)

        /**
         * 关闭虚拟容器
         */
        fun close(player: Player, closeInventory: Boolean) {
            if (closeInventory) PacketsHandler.sendCloseWindow(player, TRMENU_WINDOW_ID)
        }

        /**
         * 写入图标标识到布局的指定位置
         */
        fun reversePositionize(key: String, slots: Set<Int>) {
            val width = width(type)
            val size = size()

            slots.forEach {
                val inv = it > size
                var index = it - (if (inv) size else 0)
                var line = 0
                while (index > width) {
                    index -= width
                    line += 1
                }
                val string = (if (inv) layoutInventory else layout)[line]
                (if (inv) layoutInventory else layout)[line] = rebuildLine(string, width, index, key)
            }
        }

        /**
         * 读取布局
         */
        fun positionize(): Map<String, Set<Int>> = positionize(width(type), size(), layout, layoutInventory)

        fun size() = size(type, rows)

    }

    companion object {

        /**
         * 列出某行的所有图标标识
         */
        private fun listIconsKeys(line: String, size: Int): Array<String> {
            val list = mutableListOf<String>()
            Variables(line, ICON_KEY_MATCHER).find().variableList.forEach { it ->
                if (it.isVariable) list.add(it.text)
                else it.text.toCharArray().forEach { list.add(it.toString()) }
            }
            while (list.size < size) list.add(BLANK_CHAR)
            return list.subList(0, size).toTypedArray()
        }

        /**
         * 重构图标标识写入某行的字符串
         */
        private fun rebuildLine(line: String, width: Int, index: Int, key: String) =
            buildString {
                listIconsKeys(line, width).let {
                    it[index] = key
                    return@let it
                }.forEach {
                    if (it.length > 1) append("`$it`") else append(it[0])
                }
                return@buildString
            }


        fun positionize(width: Int, size: Int, layout: List<String>, layoutInventory: List<String>): Map<String, Set<Int>> {
            return mutableMapOf<String, MutableSet<Int>>().let {
                layout.forEachIndexed { y, line ->
                    listIconsKeys(line, width).forEachIndexed { x, key ->
                        if (key.isNotBlank()) it.computeIfAbsent(key) { mutableSetOf() }.add(y * width + x)
                    }
                }
                layoutInventory.forEachIndexed { y, line ->
                    listIconsKeys(line, width).forEachIndexed { x, key ->
                        if (key.isNotBlank()) it.computeIfAbsent(key) { mutableSetOf() }.add(size + y * width + x)
                    }
                }
                return@let it
            }
        }

        fun width(type: InventoryType) = if (type == CHEST || type == ENDER_CHEST || type == SHULKER_BOX || type == BARREL) 9 else 3

        fun size(type: InventoryType, rows: Int) = if (type == CHEST) rows * 9 else type.defaultSize

        const val BLANK_LINE = "         "
        const val BLANK_CHAR = " "

    }

}