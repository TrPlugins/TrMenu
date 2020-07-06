package me.arasple.mc.trmenu.display.menu

import io.izzel.taboolib.util.Variables
import me.arasple.mc.trmenu.display.Icon
import me.arasple.mc.trmenu.display.icon.IconDisplay
import org.bukkit.event.inventory.InventoryType
import org.bukkit.event.inventory.InventoryType.*
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

        icons.forEach {
            val key = it.id
            pages.forEachIndexed { index, page ->
                page[key]?.let { slots ->
                    it.defIcon.display.position[index]?.addElement(IconDisplay.Position(slots))
                }
            }
        }
    }

    /**
     * 布局
     */
    class Layout(val type: InventoryType, var rows: Int, val layout: MutableList<String>, val layoutInventory: MutableList<String>) {

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
         * 写入图标标识到布局的指定位置
         */
        fun reversePositionize(key: String, slots: Set<Int>) {
            val width = width()
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
        fun positionize(): Map<String, Set<Int>> {
            val width = width()
            val size = size()
            return mutableMapOf<String, MutableSet<Int>>().let {
                layout.forEachIndexed { y, line ->
                    listIconsKeys(line, width).forEachIndexed { x, key ->
                        it.computeIfAbsent(key) { mutableSetOf() }.add(y * width + x)
                    }
                }
                layoutInventory.forEachIndexed { y, line ->
                    listIconsKeys(line, width).forEachIndexed { x, key ->
                        it.computeIfAbsent(key) { mutableSetOf() }.add(size + y * width + x)
                    }
                }
                return@let it
            }
        }

        fun width() = if (type == CHEST || type == ENDER_CHEST || type == SHULKER_BOX || type == BARREL) 9 else 3

        fun size() = if (type == CHEST) rows * 9 else type.defaultSize

    }

    companion object {

        /**
         * 列出某行的所有图标标识
         */
        private fun listIconsKeys(line: String, maxSize: Int): Array<String> {
            val list = mutableListOf<String>()
            Variables(line, ICON_KEY).find().variableList.forEach { it ->
                if (it.isVariable) list.add(it.text)
                else it.text.toCharArray().forEach { list.add(it.toString()) }
            }
            return list.subList(0, maxSize).toTypedArray()
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

        const val BLANK_LINE = "         "
        val ICON_KEY: Pattern = Pattern.compile("`.*?`")

    }

}