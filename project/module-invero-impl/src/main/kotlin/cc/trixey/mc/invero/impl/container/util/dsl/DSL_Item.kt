package cc.trixey.mc.invero.impl.container.util.dsl

import cc.trixey.mc.invero.Elemap
import cc.trixey.mc.invero.Panel
import cc.trixey.mc.invero.panel.BasePanel
import cc.trixey.mc.invero.impl.container.element.BasicItem
import cc.trixey.mc.invero.impl.container.util.buildItem
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.platform.util.ItemBuilder

/**
 * @author Arasple
 * @since 2022/11/23 11:00
 */
fun Panel.firstSlot() = scale.slots.first()

fun Panel.lastSlot() = scale.slots.last()

fun BasePanel.firstAvailableSlot() = getUnoccupiedSlots().minOf { it }

inline fun BasePanel.item(
    slot: Int = firstAvailableSlot(),
    material: Material,
    noinline builder: ItemBuilder.() -> Unit = {},
    block: BasicItem.() -> Unit = {},
) = item(material, builder, block).also { it.set(slot) }

inline fun Panel.item(
    material: Material,
    noinline builder: ItemBuilder.() -> Unit = {},
    block: BasicItem.() -> Unit = {}
) = buildItem(taboolib.platform.util.buildItem(material, builder), block)

inline fun Panel.item(
    itemStack: ItemStack,
    noinline builder: ItemBuilder.() -> Unit = {},
    block: BasicItem.() -> Unit = {}
) = buildItem(taboolib.platform.util.buildItem(itemStack, builder), block)

inline fun cc.trixey.mc.invero.Elemap.item(
    slot: Int,
    material: Material,
    noinline builder: ItemBuilder.() -> Unit = {},
    block: BasicItem.() -> Unit = {}
) = item(material, builder, block).set(slot)

inline fun cc.trixey.mc.invero.Elemap.item(
    material: Material,
    noinline builder: ItemBuilder.() -> Unit = {},
    block: BasicItem.() -> Unit = {}
): BasicItem {
    panel.apply { return item(material, builder, block) }
}