package me.arasple.mc.trmenu.modules.display.icon

import me.arasple.mc.trmenu.modules.display.Menu
import me.arasple.mc.trmenu.modules.display.animation.Animated
import me.arasple.mc.trmenu.modules.display.item.DynamicItem
import me.arasple.mc.trmenu.modules.display.item.property.Lore
import me.arasple.mc.trmenu.modules.display.position.Position
import me.arasple.mc.trmenu.modules.service.mirror.Mirror
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

    fun isAnimatedPosition(pageIndex: Int): Boolean {
        return position[pageIndex]?.let { it.animatable || it.elements.any { it.dynamicSlots.isNotEmpty() } } ?: false
    }

    fun getPosition(player: Player, pageIndex: Int) = position[pageIndex]?.currentElement(player)?.getSlots(player)

    fun getCurrentPosition(player: Player, pageIndex: Int) = position[pageIndex]?.currentElement(player)?.getOccupiedSlots(player)

    fun nextPosition(player: Player, session: Menu.Session) {
        val pageIndex = session.page
        val current = getPosition(player, pageIndex) ?: return
        position[pageIndex]?.nextIndex(player)

        if (isAnimatedPosition(pageIndex)) {
            Mirror.async("Icon:restoreOverrides(async)") {
                // 1, 2(current) -> 1(updated)
                // target: 1,2,3,4,5,6,7
                // toRestore: 2
                val updated = getPosition(player, pageIndex) ?: return@async
                current.removeAll(updated)

                session.menu?.getIcons(player, pageIndex) { icon ->
                    val display = icon.getIconProperty(player).display
                    if (display.isAnimatedPosition(pageIndex)) {
                        return@getIcons false
                    } else {
                        return@getIcons display.getPosition(player, pageIndex)?.any { updated.contains(it) }!!
                    }
                }?.forEach {
                    it.setItemStack(player, session, it.getIconProperty(player), current)
                }
            }
        }
    }

    fun nextItem(player: Player) = item.nextItem(player)

    fun nextName(player: Player) = item.displayName(player, name.nextElement(player))

    fun nextLore(player: Player) = item.displayLore(player, lore.nextElement(player))

    fun nextFrame(player: Player, type: Set<Int>, session: Menu.Session) {
        type.forEach {
            when (it) {
                0 -> nextItem(player)
                1 -> nextName(player)
                2 -> nextLore(player)
                3 -> nextPosition(player, session)
                else -> throw Exception()
            }
        }
    }

}