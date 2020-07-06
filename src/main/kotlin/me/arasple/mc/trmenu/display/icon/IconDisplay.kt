package me.arasple.mc.trmenu.display.icon

import me.arasple.mc.trmenu.display.animation.Animated
import me.arasple.mc.trmenu.display.item.BaseLore
import me.arasple.mc.trmenu.display.item.BaseMaterial
import me.arasple.mc.trmenu.display.item.Item
import me.arasple.mc.trmenu.utils.Msger
import org.bukkit.entity.Player

/**
 * @author Arasple
 * @date 2020/5/30 14:06
 */
class IconDisplay(val position: MutableMap<Int, MutableSet<Slot>>, val name: Animated<String>, val material: Animated<BaseMaterial>, val lore: Animated<BaseLore>) {

    fun createDisplayItem(player: Player) {

    }

    fun getLore(player: Player) = lore.nextElement(player)?.formatedLore(player)

    fun getName(player: Player) = name.nextElement(player)?.let {
        return@let Item.colorizeName(Msger.replace(player, it))
    }

    class Slot(val slots: Set<Int>)

}