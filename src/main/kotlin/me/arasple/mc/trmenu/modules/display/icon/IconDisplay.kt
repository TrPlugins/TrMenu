package me.arasple.mc.trmenu.modules.display.icon

import me.arasple.mc.trmenu.modules.display.animation.Animated
import me.arasple.mc.trmenu.modules.display.item.DynamicItem
import me.arasple.mc.trmenu.modules.display.item.property.Lore
import me.arasple.mc.trmenu.modules.display.position.Position
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

/**
 * @author Arasple
 * @date 2020/5/30 14:06
 */
data class IconDisplay(var position: MutableMap<Int, Animated<Position>>, val item: DynamicItem, var name: Animated<String>, var lore: Animated<Lore>) {

    fun createDisplayItem(player: Player): ItemStack {
        if (!item.cache.containsKey(player.uniqueId)) {
            item.displayName(player, name.currentElement(player))
            item.displayLore(player, lore.currentElement(player))
        }

        return item.releaseItem(player) ?: ItemStack(Material.BARRIER)
    }

    fun isAnimatedPosition(pageIndex: Int) = position[pageIndex]?.elements?.let { return@let it.size > 1 } ?: false

    fun getPosition(player: Player, pageIndex: Int) = position[pageIndex]?.currentElement(player)?.getSlots(player)

    fun nextPosition(player: Player, pageIndex: Int) = position[pageIndex]?.nextIndex(player)

    fun nextItem(player: Player) = item.nextItem(player)

    fun nextName(player: Player) = item.displayName(player, name.nextElement(player))

    fun nextLore(player: Player) = item.displayLore(player, lore.nextElement(player))

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

}