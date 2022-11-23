package cc.trixey.mc.invero.util.dsl

import cc.trixey.mc.invero.common.Elemap
import cc.trixey.mc.invero.common.Panel
import cc.trixey.mc.invero.common.base.BasePanel
import cc.trixey.mc.invero.element.BasicItem
import cc.trixey.mc.invero.util.buildItem
import org.bukkit.Material
import org.bukkit.inventory.ItemStack
import taboolib.platform.util.ItemBuilder

/**
 * @author Arasple
 * @since 2022/11/23 11:00
 */
fun Panel.firstSlot() = slots.first()

fun Panel.lastSlot() = slots.last()

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

inline fun Elemap.item(
    slot: Int,
    material: Material,
    noinline builder: ItemBuilder.() -> Unit = {},
    block: BasicItem.() -> Unit = {}
) = item(material, builder, block).set(slot)

inline fun Elemap.item(
    material: Material,
    noinline builder: ItemBuilder.() -> Unit = {},
    block: BasicItem.() -> Unit = {}
): BasicItem {
    panel.apply { return item(material, builder, block) }
}