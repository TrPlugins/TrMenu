package me.arasple.mc.trmenu.modules.display.menu

import io.izzel.taboolib.util.Variables
import me.arasple.mc.trmenu.api.nms.NMS
import me.arasple.mc.trmenu.modules.display.Icon
import me.arasple.mc.trmenu.modules.display.animation.Animated
import me.arasple.mc.trmenu.modules.display.position.Position
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import java.util.regex.Pattern
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

        icons.filter { it.defIcon.display.position.isEmpty() }.forEach {
            val key = it.id
            pages.forEachIndexed { index, page ->
                page[key]?.let { slots ->
                    it.defIcon.display.position.computeIfAbsent(index) { Animated(arrayOf()) }.addElement(Position(slots))
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
            rows = if (type == InventoryType.CHEST) max(rows, layout.size) else if (type.defaultSize % 9 == 0) type.defaultSize / 9 else 1
            if (type == InventoryType.CHEST) {
                rows = max(rows, layout.size)
                while (layout.size < rows) layout.add(BLANK_LINE)
                while (layoutInventory.size < 4) layoutInventory.add(BLANK_LINE)
            }
        }

        /**
         * 比较布局规格
         */
        fun isSimilar(layout: Layout): Boolean {
            return type == layout.type && rows == layout.rows
        }

        /**
         * 显示虚拟容器
         */
        fun displayInventory(player: Player, title: String) = NMS.sendOpenWindow(player, type, size(type, rows), title)

        /**
         * 关闭虚拟容器
         */
        fun close(player: Player, closeInventory: Boolean) {
            if (closeInventory) NMS.sendCloseWindow(player)
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

        fun setRows(rows: Int): Layout {
            this.rows = rows
            return this
        }

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

        fun blankLayout(rows: Int) = mutableListOf<String>().let {
            for (i in 0..rows) it.add(BLANK_LINE)
            it
        }

        fun width(type: InventoryType) = if (type.defaultSize == 27) 9 else 3

        fun size(type: InventoryType, rows: Int) = if (type == InventoryType.CHEST) rows * 9 else type.defaultSize

        const val BLANK_LINE = "         "
        const val BLANK_CHAR = " "
        val ICON_KEY_MATCHER: Pattern = Pattern.compile("`.*?`")

    }

}