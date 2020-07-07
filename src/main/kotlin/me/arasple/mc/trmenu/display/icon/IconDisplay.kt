package me.arasple.mc.trmenu.display.icon

import io.izzel.taboolib.internal.apache.lang3.math.NumberUtils
import me.arasple.mc.trmenu.display.animation.Animated
import me.arasple.mc.trmenu.display.item.BaseItem
import me.arasple.mc.trmenu.display.item.BaseLore
import me.arasple.mc.trmenu.display.item.Item
import me.arasple.mc.trmenu.utils.Msger
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/5/30 14:06
 */
class IconDisplay(val position: MutableMap<Int, Animated<Position>>, val item: Animated<BaseItem>, val name: Animated<String>, val lore: Animated<BaseLore>) {

    fun createDisplayItem(player: Player) = getItem(player)?.releaseItem(player, getName(player), getLore(player)) ?: ItemStack(Material.BARRIER)

    fun getPosition(player: Player, pageIndex: Int) = position[pageIndex]?.currentElement(player)!!.getSlots(player)

    fun nextPosition(player: Player, pageIndex: Int) = position[pageIndex]?.nextIndex(player)

    fun getItem(player: Player) = item.currentElement(player)

    fun nextItem(player: Player) = item.nextIndex(player)

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

    class Position(val staticSlots: Set<Int>, val dynamicSlots: Set<String>) {

        constructor(slots: Set<Int>) : this(slots, setOf())

        fun getSlots(player: Player) = mutableSetOf<Int>().let { set ->
            set.addAll(staticSlots)
            dynamicSlots.forEach {
                val slot = NumberUtils.toInt(Msger.replace(player, it), -1)
                if (slot >= 0) set.add(slot)
            }
            return@let set
        }

    }

}