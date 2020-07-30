package me.arasple.mc.trmenu.api.factory

import me.arasple.mc.trmenu.TrMenu
import me.arasple.mc.trmenu.api.factory.task.BuildTask
import me.arasple.mc.trmenu.api.factory.task.ClickTask
import me.arasple.mc.trmenu.api.factory.task.CloseTask
import me.arasple.mc.trmenu.data.MetaPlayer.updateInventoryContents
import me.arasple.mc.trmenu.data.Sessions.getMenuFactorySession
import me.arasple.mc.trmenu.display.menu.MenuLayout
import me.arasple.mc.trmenu.display.menu.MenuLayout.Companion.size
import me.arasple.mc.trmenu.display.menu.MenuLayout.Companion.width
import org.bukkit.entity.Player
import org.bukkit.event.inventory.InventoryType
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.Plugin

/**
 * @author Arasple
 * @date 2020/7/20 15:17
 * 皆在提供更简单的数据包 GUI 构建服务
 * 部分代码参考来自 io.izzel.taboolib.util.item.inventory
 * 目前仅适合做展示性 GUI,功能性容器 GUI 请使用 MenuBuilder
 */
class MenuFactory(
    val plugin: Plugin,
    private var title: String,
    private var type: InventoryType,
    private var size: Int,
    private var positions: Map<String, Set<Int>>,
    private val items: MutableMap<String, ItemStack>,
    var clickTask: ClickTask?,
    var buildTask: BuildTask?,
    var closeTask: CloseTask?
) {

    constructor() : this(TrMenu.plugin)

    constructor(plugin: Plugin) : this(plugin, "MenuFactory", InventoryType.CHEST, 3, mapOf(), mutableMapOf(), null, null, null)

    fun title(title: String): MenuFactory {
        this.title = title
        return this
    }

    fun type(type: InventoryType): MenuFactory {
        this.type = type
        return this
    }

    fun rows(rows: Int): MenuFactory {
        return layout(
            *mutableListOf<String>().let {
                for (i in 0..rows) it.add("         ")
                it
            }.toTypedArray()
        )
    }

    fun size() = size

    fun layout(vararg layout: String) = layout(layout.toList(), listOf())

    fun layout(layout: List<String>, layoutInventory: List<String>): MenuFactory {
        this.size = size(type, layout.size)
        this.positions = MenuLayout.positionize(
            width(type),
            size,
            layout,
            layoutInventory
        )
        return this
    }

    fun item(key: Char, item: ItemStack) = item(key.toString(), item)

    fun item(key: String, item: ItemStack): MenuFactory {
        items[key] = item
        return this
    }

    fun click(clickTask: ClickTask): MenuFactory {
        this.clickTask = clickTask
        return this
    }

    fun build(buildTask: BuildTask): MenuFactory {
        this.buildTask = buildTask
        return this
    }

    fun close(closeTask: CloseTask): MenuFactory {
        this.closeTask = closeTask
        return this
    }

    fun display(player: Player) = display(player) {}

    fun display(player: Player, runnable: Runnable) {
        val session = player.getMenuFactorySession()

        items.entries.forEach { entry ->
            val id = entry.key
            val item = entry.value
            positions[id]?.let { it -> session.def[id] = Pair(item, it) }
        }

        player.updateInventoryContents()
        session.menuFactory = this
        buildTask?.run(BuildTask.Event(player, session))
        runnable.run()

        session.display(type, size, title)
        session.displayItems()
    }

}