package me.arasple.mc.trmenu.display.icon

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import me.arasple.mc.trmenu.display.animation.Animated
import me.arasple.mc.trmenu.display.item.DynamicItem
import me.arasple.mc.trmenu.display.item.Item
import me.arasple.mc.trmenu.display.item.Lore
import me.arasple.mc.trmenu.utils.Msger
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/5/30 14:06
 */
data class IconDisplay(var position: MutableMap<Int, Animated<Position>>, val item: DynamicItem, var name: Animated<String>, var lore: Animated<Lore>) {

    fun createDisplayItem(player: Player) = item.releaseItem(player, getName(player), getLore(player)) ?: ItemStack(Material.BARRIER)

    fun isAnimatedPosition(pageIndex: Int) = position[pageIndex]?.elements?.let { return@let it.size > 1 } ?: false

    fun getPosition(player: Player, pageIndex: Int) = position[pageIndex]?.currentElement(player)?.getSlots(player)

    fun nextPosition(player: Player, pageIndex: Int) = position[pageIndex]?.nextIndex(player)

    fun nextItem(player: Player) = item.nextItem(player)

    fun getName(player: Player) = name.currentElement(player)?.let { return@let Item.colorizeName(Msger.replace(player, it)) }

    fun nextName(player: Player) = name.nextIndex(player)

    fun getLore(player: Player) = lore.currentElement(player)?.formatedLore(player)

    fun nextLore(player: Player) = lore.nextIndex(player)

    fun nextFrame(player: Player, type: Set<Int>, page: Int) {
        type.forEach {
            when (it) {
                0 -> nextItem(player)
                1 -> nextName(player)
                2 -> nextLore(player)
                3 -> nextPosition(player, page)
                else -> throw Exception()
            }
        }
    }

    data class Position(val staticSlots: Set<Int>, val dynamicSlots: Set<String>) {

        constructor(slots: Set<Int>) : this(slots, setOf())

        fun getSlots(player: Player) = mutableSetOf<Int>().let { set ->
            set.addAll(staticSlots)
            dynamicSlots.forEach {
                val slot = NumberUtils.toInt(Msger.replace(player, it), -1)
                if (slot >= 0) set.add(slot)
            }
            return@let set
        }

        companion object {

            fun createPosition(list: List<Any>): Position {
                val staticSlots = mutableSetOf<Int>()
                val dynamicSlots = mutableSetOf<String>()
                list.forEach {
                    val num = it.toString()
                    if (NumberUtils.isCreatable(num)) staticSlots.add(NumberUtils.toInt(num)) else dynamicSlots.add(num)
                }
                return Position(staticSlots, dynamicSlots)
            }

        }

    }

}